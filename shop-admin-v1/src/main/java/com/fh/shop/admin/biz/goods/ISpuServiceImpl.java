package com.fh.shop.admin.biz.goods;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fh.shop.admin.mapper.brand.IBrandMapper;
import com.fh.shop.admin.mapper.cate.ICateMapper;
import com.fh.shop.admin.mapper.goods.ISkuImageMapper;
import com.fh.shop.admin.mapper.goods.ISkuMapper;
import com.fh.shop.admin.mapper.goods.ISpuMapper;
import com.fh.shop.admin.mapper.spec.ISpecMapper;
import com.fh.shop.admin.mapper.spec.ISpecValueMapper;
import com.fh.shop.admin.mapper.type.IAttrMapper;
import com.fh.shop.admin.mapper.type.IAttrValueMapper;
import com.fh.shop.admin.param.SpuParam;
import com.fh.shop.admin.param.SpuQueryParam;
import com.fh.shop.admin.param.SpuStatusParam;
import com.fh.shop.admin.po.brand.Brand;
import com.fh.shop.admin.po.cate.Cate;
import com.fh.shop.admin.po.goods.Sku;
import com.fh.shop.admin.po.goods.SkuImage;
import com.fh.shop.admin.po.goods.Spu;
import com.fh.shop.admin.po.spec.Spec;
import com.fh.shop.admin.po.spec.SpecValue;
import com.fh.shop.admin.po.type.Attr;
import com.fh.shop.admin.po.type.AttrValue;
import com.fh.shop.admin.vo.goods.*;
import com.fh.shop.common.DataTableResult;
import com.fh.shop.common.ServerResponse;
import com.fh.shop.util.OSSUtil;
import com.fh.shop.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service("spuService")
public class ISpuServiceImpl implements ISpuService {

    @Autowired
    private IBrandMapper brandMapper;

    @Autowired
    private IAttrMapper attrMapper;

    @Autowired
    private IAttrValueMapper attrValueMapper;

    @Autowired
    private ISpecMapper specMapper;

    @Autowired
    private ISpecValueMapper specValueMapper;

    @Autowired
    private ISpuMapper spuMapper;

    @Autowired
    private ISkuMapper skuMapper;

    @Autowired
    private ISkuImageMapper skuImageMapper;

    @Autowired
    private ICateMapper cateMapper;

    @Override
    public ServerResponse findSpuInfo(Long typeId) {
        //根据类型id获取对应的品牌列表
        List<Brand> brandList = brandMapper.findBrandListByTypeId(typeId);
        //===============根据类型id获取对应的SpuAttrVoList
        //根据类型id获取对应的属性列表
        QueryWrapper<Attr> attrQueryWrapper = new QueryWrapper<>();
        attrQueryWrapper.eq("typeId",typeId);
        List<Attr> attrList = attrMapper.selectList(attrQueryWrapper);
        List<Long> idList = attrList.stream().map(x -> x.getId()).collect(Collectors.toList());
        //获取查询出来的属性对应的所有属性值
        QueryWrapper<AttrValue> attrValueQueryWrapper = new QueryWrapper<>();
        attrValueQueryWrapper.in("attrId",idList);
        List<AttrValue> attrValueList = attrValueMapper.selectList(attrValueQueryWrapper);
        //将属性和属性值关联起来
        List<SpuAttrVo> spuAttrVoList = attrList.stream().map(x -> {
            SpuAttrVo spuAttrVo = new SpuAttrVo();
            String attrName = x.getAttrName();
            spuAttrVo.setAttrName(attrName);
            Long id = x.getId();
            List<AttrValue> attrValues = attrValueList.stream().filter(y -> y.getAttrId().longValue() == id.longValue()).collect(Collectors.toList());
            spuAttrVo.setAttrValueList(attrValues);
            spuAttrVo.setId(id);
            return spuAttrVo;
        }).collect(Collectors.toList());
        //====================根据类型id获取对应的spuSpecVoList
        //根据类型id获取对应的规格列表
        List<Spec> specList = specMapper.findListByTypeId(typeId);
        List<Long> specIdList = specList.stream().map(x -> x.getId()).collect(Collectors.toList());
        //根据规格id获取对应的规格值列表
        QueryWrapper<SpecValue> specValueQueryWrapper = new QueryWrapper<>();
        specValueQueryWrapper.in("specId",specIdList);
        List<SpecValue> specValueList = specValueMapper.selectList(specValueQueryWrapper);
        //组合
        List<SpuSpecVo> spuSpecVoList = specList.stream().map(z -> {
            SpuSpecVo spuSpecVo = new SpuSpecVo();
            spuSpecVo.setSpecName(z.getSpecName());
            spuSpecVo.setId(z.getId());
            List<SpecValue> specValues = specValueList.stream().filter(y -> y.getSpecId().longValue() == z.getId()).collect(Collectors.toList());
            spuSpecVo.setSpecValueList(specValues);
            return spuSpecVo;
        }).collect(Collectors.toList());
        //最终的组装
        SpuVo spuVo = new SpuVo();
        spuVo.setBrandList(brandList);
        spuVo.setSpuAttrVoList(spuAttrVoList);
        spuVo.setSpuSpecVoList(spuSpecVoList);
        return ServerResponse.success(spuVo);
    }

    @Override
    public ServerResponse addSpu(SpuParam spuParam) {
        //插入spu信息
        Spu spu = spuParam.getSpu();
        spu.setStatus(1);
        spu.setNews(1);
        spu.setRecommend(1);
        spuMapper.insert(spu);
        Long id = spu.getId();
        String spuName = spu.getSpuName();
        String[] priceArr = spuParam.getPrices().split(",");
        String[] stockArr = spuParam.getStocks().split(",");
        String[] specInfoArr = spuParam.getSpecInfos().split(";");
        //16:z,x,c;17:y,u,i
        String[] skuImagesArr = spuParam.getSkuImages().split(";");
        List<Sku> skuList = new ArrayList<>();
        for (int i = 0; i < priceArr.length; i++) {
            String specInfo = specInfoArr[i];
            Sku sku = new Sku();
            sku.setSpuId(id);
            sku.setPrice(new BigDecimal(priceArr[i]));
            sku.setStock(Integer.parseInt(stockArr[i]));
            sku.setSpecInfo(specInfo);
            Long colorId = Long.parseLong(specInfo.split(",")[0].split(":")[0]);
            sku.setColorId(colorId);
            String image = Arrays.stream(skuImagesArr).filter(x -> Long.parseLong(x.split("=")[0]) == colorId.longValue())
                    .map(y -> y.split("=")[1].split(",")[0])
                    .findFirst()
                    .get();
            sku.setImage(image);
            //在spuName的基础上加上规格信息
            List<String> specValueNameList = Arrays.stream(specInfo.split(",")).map(x -> x.split(":")[1]).collect(Collectors.toList());
            String skuName = spuName+" "+StringUtils.join(specValueNameList," ");
            sku.setSkuName(skuName);
            skuList.add(sku);
        }
        if(skuList.size() > 0){
            //批量插入sku
            skuMapper.addBatch(skuList);
        }
        //批量插入图片
        //16:z,x,c;17:q,w,e
        List<SkuImage> skuImageList = new ArrayList<>();
        Arrays.stream(skuImagesArr).forEach(x -> {
            Long colorId = Long.valueOf(x.split("=")[0]);
            Arrays.stream(x.split("=")[1].split(",")).forEach(y -> {
                SkuImage skuImage = new SkuImage();
                skuImage.setColorId(colorId);
                skuImage.setImage(y);
                skuImage.setSpuId(id);
                skuImageList.add(skuImage);
            });
        });

        //批量注入
        if(skuImageList.size()>0) {
            skuImageMapper.addBatch(skuImageList);
        }
        //删除缓存
        RedisUtil.delete("skuList");
        return ServerResponse.success();
    }

    @Override
    public DataTableResult findList(SpuQueryParam spuQueryParam) {
        //获取总条数
        Long count = spuMapper.findListCount(spuQueryParam);
        //获取分页列表
        List<Spu> spuList = spuMapper.findPageList(spuQueryParam);
        //获取spuIdList
        List<Long> idList = spuList.stream().map(Spu::getId).collect(Collectors.toList());
        if(idList.size() > 0){
            //获取对应的skuList
            QueryWrapper<Sku> skuQueryWrapper = new QueryWrapper<>();
            skuQueryWrapper.in("spuId",idList);
            List<Sku> skuList = skuMapper.selectList(skuQueryWrapper);
            //spuQueryVoList 组装数据
            List<SpuQueryVo> spuQueryVoList = spuList.stream().map(x -> {
                SpuQueryVo spuQueryVo = new SpuQueryVo();
                spuQueryVo.setSpu(x);
                spuQueryVo.setSkuList(skuList.stream().filter(y -> y.getSpuId().longValue() == x.getId().longValue()).collect(Collectors.toList()));
                return spuQueryVo;
            }).collect(Collectors.toList());
            //组装数据
            return new DataTableResult(spuQueryParam.getDraw(),count,count,spuQueryVoList);
        }
        return new DataTableResult(spuQueryParam.getDraw(),count,count,new ArrayList());
    }

    @Override
    public ServerResponse del(Long id,String rootPath) {
        //删除spu
        spuMapper.deleteById(id);
        //删除spu对应的sku
        QueryWrapper<Sku> skuQueryWrapper = new QueryWrapper<>();
        skuQueryWrapper.eq("spuId",id);
        skuMapper.delete(skuQueryWrapper);
        //查询spu对应的skuImage
        QueryWrapper<SkuImage> skuImageQueryWrapper = new QueryWrapper<>();
        skuImageQueryWrapper.in("spuId",id);
        List<SkuImage> skuImageList = skuImageMapper.selectList(skuImageQueryWrapper);
        //删除硬盘上的图片文件
        deleteImages(rootPath, skuImageList);
        //删除spu对应的skuImage
        skuImageMapper.delete(skuImageQueryWrapper);
        //删除缓存
        RedisUtil.delete("skuList");
        return ServerResponse.success();
    }

    private void deleteImages(String rootPath, List<SkuImage> skuImageList) {
        /*skuImageList.stream().forEach(x -> {
            String image = x.getImage();
            File file = new File(rootPath + image);
            if(file.exists()){
                file.delete();
            }
        });*/
        List<String> imageList = skuImageList.stream().map(x -> x.getImage()).collect(Collectors.toList());
        OSSUtil.deleteFiles(imageList);
    }

    @Override
    public ServerResponse deleteBatch(String ids,String rootPath) {
        if(StringUtils.isEmpty(ids)){
            return ServerResponse.error();
        }
        //得到idList
        List<Long> idList = Arrays.stream(ids.split(",")).map(x -> Long.parseLong(x)).collect(Collectors.toList());
        //批量删除spu
        spuMapper.deleteBatchIds(idList);
        //批量删除sku
        QueryWrapper<Sku> skuQueryWrapper = new QueryWrapper<>();
        skuQueryWrapper.in("spuId",idList);
        skuMapper.delete(skuQueryWrapper);
        //查询多个spu对应的所有图片
        QueryWrapper<SkuImage> skuImageQueryWrapper = new QueryWrapper<>();
        skuImageQueryWrapper.in("spuId",idList);
        List<SkuImage> skuImageList = skuImageMapper.selectList(skuImageQueryWrapper);
        deleteImages(rootPath, skuImageList);
        //批量删除skuImage
        skuImageMapper.delete(skuImageQueryWrapper);
        //删除缓存
        RedisUtil.delete("skuList");
        return ServerResponse.success();
    }

    @Override
    public ServerResponse findSpu(Long spuId) {
        //获取spu信息
        Spu spu = spuMapper.selectById(spuId);
        //获取typeId
        Long cate3 = spu.getCate3();
        Cate cate = cateMapper.selectById(cate3);
        Long typeId = cate.getTypeId();
        //获取对应的skuList
        QueryWrapper<Sku> skuQueryWrapper = new QueryWrapper<>();
        skuQueryWrapper.eq("spuId",spuId);
        List<Sku> skuList = skuMapper.selectList(skuQueryWrapper);
        //获取对应的skuImageVoList
        //根据spuId找到该spu下各个颜色的图片
        QueryWrapper<SkuImage> skuImageQueryWrapper = new QueryWrapper<>();
        skuImageQueryWrapper.eq("spuId",spuId);
        List<SkuImage> skuImageList = skuImageMapper.selectList(skuImageQueryWrapper);
        //找到所有颜色id集合
        Set<Long> colorIdSet = skuImageList.stream().map(x -> x.getColorId()).collect(Collectors.toSet());
        //找到对应的所有specValueList
        List<SpecValue> specValueList = specValueMapper.selectBatchIds(colorIdSet);
        //将specValueList和skuImageList进行关联
        List<SkuImageVo> skuImageVoList = specValueList.stream().map(x -> {
            SkuImageVo skuImageVo = new SkuImageVo();
            skuImageVo.setColorId(x.getId());
            skuImageVo.setColorName(x.getSpecValue());
            skuImageVo.setSkuImageList(skuImageList.stream().filter(y -> y.getColorId().longValue() == x.getId().longValue()).collect(Collectors.toList()));
            return skuImageVo;
        }).collect(Collectors.toList());
        //组装数据
        SpuEditVo spuEditVo = new SpuEditVo();
        spuEditVo.setSpu(spu);
        spuEditVo.setTypeId(typeId);
        spuEditVo.setSkuList(skuList);
        spuEditVo.setSkuImageVoList(skuImageVoList);
        return ServerResponse.success(spuEditVo);
    }

    @Override
    public ServerResponse deleteSkuImage(Long key,String rootPath) {
        SkuImage skuImage = skuImageMapper.selectById(key);
        String image = skuImage.getImage();
        //服务器硬盘的图片删除了
        /*File file = new File(rootPath + image);
        if(file.exists()){
            file.delete();
        }*/
        OSSUtil.deleteFile(image);
        //数据库的记录要删除了
        skuImageMapper.deleteById(key);
        return ServerResponse.success(image);
    }

    @Override
    public ServerResponse updateSpu(SpuParam spuParam) {
        Spu spu = spuParam.getSpu();
        //更新spu的信息
        spuMapper.updateById(spu);
        Long spuId = spu.getId();
        //sku的信息 [需要删除的delete，需要插入的insert，需要更新的update]
        String[] priceArr = spuParam.getPrices().split(",");
        String[] stockArr = spuParam.getStocks().split(",");
        //["100_16:黑色,20:8G,22:标准版","101_18:红色,22:16G,22:标准版","-1_19:蓝色,22:16G,22:标准版"]
        String[] specItemArr = spuParam.getSpecInfos().split(";");
        // 16:x,y,z;19:a,b,c
        String[] skuImageArr = spuParam.getSkuImages().split(";");
        List<Sku> insertSkuList = new ArrayList<>();
        //获取原有的skuList
        QueryWrapper<Sku> skuQueryWrapper = new QueryWrapper<>();
        skuQueryWrapper.eq("spuId",spuId);
        List<Sku> skuList = skuMapper.selectList(skuQueryWrapper);
        List<Long> updateIdList = new ArrayList<>();
        for (int i = 0; i < specItemArr.length; i++) {
            //100_16:黑色,20:8G,22:标准版
            String specItem = specItemArr[i];
            long skuId = Long.parseLong(specItem.split("_")[0]);
            String specInfo = specItem.split("_")[1];
            //spu的name加上规格信息 "华为p30 黑色 8G 标准版"
            List<String> specValueNameList = Arrays.stream(specInfo.split(","))
                    .map(x -> x.split(":")[1]).collect(Collectors.toList());
            String skuName = spu.getSpuName()+" "+StringUtils.join(specValueNameList," ");
            //skuImage 上传的多张图片的第一张
            String colorId = specInfo.split(",")[0].split(":")[0];
            String skuImage = Arrays.stream(skuImageArr).filter(x -> Long.parseLong(x.split("=")[0]) == Long.parseLong(colorId))
                    .map(y -> y.split("=")[1].split(",")[0])
                    .findFirst()
                    .get();
            if(skuId == -1){
                //新增
                Sku sku = buildSku(spuId, priceArr[i], stockArr[i], specInfo, skuName, colorId, skuImage);
                insertSkuList.add(sku);
            } else {
                //更新
                Sku sku = buildSku(spuId, priceArr[i], stockArr[i], specInfo, skuName, colorId, skuImage);
                sku.setId(skuId);
                updateIdList.add(skuId);
                skuMapper.updateById(sku);
            }
        }
        //批量插入sku
        if(insertSkuList.size() > 0){
            skuMapper.addBatch(insertSkuList);
        }
        //删除对应的sku
        //循环skuList中的每项元素，找到元素对应的skuId
        //如果skuId的值不在updateIdList中包含，那么就是要删除的
        List<Long> deleteIdList = skuList.stream().filter(x -> !updateIdList.contains(x.getId()))
                .map(y -> y.getId()).collect(Collectors.toList());
        if(deleteIdList.size() > 0){
            skuMapper.deleteBatchIds(deleteIdList);
        }
        //skuImage的信息
        //先删除
        QueryWrapper<SkuImage> skuImageQueryWrapper = new QueryWrapper<>();
        skuImageQueryWrapper.eq("spuId",spuId);
        skuImageMapper.delete(skuImageQueryWrapper);
        //再插入
        List<SkuImage> skuImageList = new ArrayList<>();
        Arrays.stream(skuImageArr).forEach(x -> {
            Long colorId = Long.valueOf(x.split("=")[0]);
            Arrays.stream(x.split("=")[1].split(",")).forEach(y -> {
                SkuImage skuImage = new SkuImage();
                skuImage.setSpuId(spuId);
                skuImage.setImage(y);
                skuImage.setColorId(colorId);
                skuImageList.add(skuImage);
            });
        });
        //批量插入
        if(skuImageList.size() > 0){
            skuImageMapper.addBatch(skuImageList);
        }
        //删除缓存
        RedisUtil.delete("skuList");
        return ServerResponse.success();
    }

    @Override
    public ServerResponse updateStatus(Long id) {
        Spu spu = new Spu();
        spu.setId(id);
        spu.setStatus(0);
        spuMapper.updateById(spu);
        //更新对应的sku
        //更新 update 表 set 字段=xxx where spuId=xxx
        Sku sku = new Sku();
        sku.setStatus(0);
        QueryWrapper<Sku> updateWrapper = new QueryWrapper<>();
        updateWrapper.eq("spuId", id);
        skuMapper.update(sku, updateWrapper);
        //删除缓存
        RedisUtil.delete("skuList");
        return ServerResponse.success();
    }

    @Override
    public ServerResponse updateStatus1(Long id) {
        Spu spu = new Spu();
        spu.setId(id);
        spu.setStatus(1);
        spuMapper.updateById(spu);
        //更新对应的sku
        //更新 update 表 set 字段=xxx where spuId=xxx
        Sku sku = new Sku();
        sku.setStatus(1);
        QueryWrapper<Sku> updateWrapper = new QueryWrapper<>();
        updateWrapper.eq("spuId", id);
        skuMapper.update(sku, updateWrapper);
        //删除缓存
        RedisUtil.delete("skuList");
        return ServerResponse.success();
    }

    @Override
    public ServerResponse updateNewYes(Long id) {
        Spu spu = new Spu();
        spu.setId(id);
        spu.setNews(1);
        spuMapper.updateById(spu);
        //更新对应的sku
        //更新 update 表 set 字段=xxx where spuId=xxx
        Sku sku = new Sku();
        sku.setNews(1);
        QueryWrapper<Sku> updateWrapper = new QueryWrapper<>();
        updateWrapper.eq("spuId", id);
        skuMapper.update(sku, updateWrapper);
        //删除缓存
        RedisUtil.delete("skuList");
        return ServerResponse.success();
    }

    @Override
    public ServerResponse updateNewNo(Long id) {
        Spu spu = new Spu();
        spu.setId(id);
        spu.setNews(0);
        spuMapper.updateById(spu);
        //更新对应的sku
        //更新 update 表 set 字段=xxx where spuId=xxx
        Sku sku = new Sku();
        sku.setNews(0);
        QueryWrapper<Sku> updateWrapper = new QueryWrapper<>();
        updateWrapper.eq("spuId", id);
        skuMapper.update(sku, updateWrapper);
        //删除缓存
        RedisUtil.delete("skuList");
        return ServerResponse.success();
    }

    @Override
    public ServerResponse updateTuiYes(Long id) {
        Spu spu = new Spu();
        spu.setId(id);
        spu.setRecommend(1);
        spuMapper.updateById(spu);
        //更新对应的sku
        //更新 update 表 set 字段=xxx where spuId=xxx
        Sku sku = new Sku();
        sku.setRecommend(1);
        QueryWrapper<Sku> updateWrapper = new QueryWrapper<>();
        updateWrapper.eq("spuId", id);
        skuMapper.update(sku, updateWrapper);
        //删除缓存
        RedisUtil.delete("skuList");
        return ServerResponse.success();
    }

    @Override
    public ServerResponse updateTuiNo(Long id) {
        Spu spu = new Spu();
        spu.setId(id);
        spu.setRecommend(0);
        spuMapper.updateById(spu);
        //更新对应的sku
        //更新 update 表 set 字段=xxx where spuId=xxx
        Sku sku = new Sku();
        sku.setRecommend(0);
        QueryWrapper<Sku> updateWrapper = new QueryWrapper<>();
        updateWrapper.eq("spuId", id);
        skuMapper.update(sku, updateWrapper);
        //删除缓存
        RedisUtil.delete("skuList");
        return ServerResponse.success();
    }


    private Sku buildSku(Long spuId, String val, String s, String specInfo, String skuName, String colorId, String skuImage) {
        Sku sku = new Sku();
        sku.setPrice(new BigDecimal(val));
        sku.setStock(Integer.parseInt(s));
        sku.setSpecInfo(specInfo);
        sku.setSpuId(spuId);
        sku.setSkuName(skuName);
        sku.setImage(skuImage);
        sku.setColorId(Long.parseLong(colorId));
        return sku;
    }

}
