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
        //????????????id???????????????????????????
        List<Brand> brandList = brandMapper.findBrandListByTypeId(typeId);
        //===============????????????id???????????????SpuAttrVoList
        //????????????id???????????????????????????
        QueryWrapper<Attr> attrQueryWrapper = new QueryWrapper<>();
        attrQueryWrapper.eq("typeId",typeId);
        List<Attr> attrList = attrMapper.selectList(attrQueryWrapper);
        List<Long> idList = attrList.stream().map(x -> x.getId()).collect(Collectors.toList());
        //???????????????????????????????????????????????????
        QueryWrapper<AttrValue> attrValueQueryWrapper = new QueryWrapper<>();
        attrValueQueryWrapper.in("attrId",idList);
        List<AttrValue> attrValueList = attrValueMapper.selectList(attrValueQueryWrapper);
        //?????????????????????????????????
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
        //====================????????????id???????????????spuSpecVoList
        //????????????id???????????????????????????
        List<Spec> specList = specMapper.findListByTypeId(typeId);
        List<Long> specIdList = specList.stream().map(x -> x.getId()).collect(Collectors.toList());
        //????????????id??????????????????????????????
        QueryWrapper<SpecValue> specValueQueryWrapper = new QueryWrapper<>();
        specValueQueryWrapper.in("specId",specIdList);
        List<SpecValue> specValueList = specValueMapper.selectList(specValueQueryWrapper);
        //??????
        List<SpuSpecVo> spuSpecVoList = specList.stream().map(z -> {
            SpuSpecVo spuSpecVo = new SpuSpecVo();
            spuSpecVo.setSpecName(z.getSpecName());
            spuSpecVo.setId(z.getId());
            List<SpecValue> specValues = specValueList.stream().filter(y -> y.getSpecId().longValue() == z.getId()).collect(Collectors.toList());
            spuSpecVo.setSpecValueList(specValues);
            return spuSpecVo;
        }).collect(Collectors.toList());
        //???????????????
        SpuVo spuVo = new SpuVo();
        spuVo.setBrandList(brandList);
        spuVo.setSpuAttrVoList(spuAttrVoList);
        spuVo.setSpuSpecVoList(spuSpecVoList);
        return ServerResponse.success(spuVo);
    }

    @Override
    public ServerResponse addSpu(SpuParam spuParam) {
        //??????spu??????
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
            //???spuName??????????????????????????????
            List<String> specValueNameList = Arrays.stream(specInfo.split(",")).map(x -> x.split(":")[1]).collect(Collectors.toList());
            String skuName = spuName+" "+StringUtils.join(specValueNameList," ");
            sku.setSkuName(skuName);
            skuList.add(sku);
        }
        if(skuList.size() > 0){
            //????????????sku
            skuMapper.addBatch(skuList);
        }
        //??????????????????
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

        //????????????
        if(skuImageList.size()>0) {
            skuImageMapper.addBatch(skuImageList);
        }
        //????????????
        RedisUtil.delete("skuList");
        return ServerResponse.success();
    }

    @Override
    public DataTableResult findList(SpuQueryParam spuQueryParam) {
        //???????????????
        Long count = spuMapper.findListCount(spuQueryParam);
        //??????????????????
        List<Spu> spuList = spuMapper.findPageList(spuQueryParam);
        //??????spuIdList
        List<Long> idList = spuList.stream().map(Spu::getId).collect(Collectors.toList());
        if(idList.size() > 0){
            //???????????????skuList
            QueryWrapper<Sku> skuQueryWrapper = new QueryWrapper<>();
            skuQueryWrapper.in("spuId",idList);
            List<Sku> skuList = skuMapper.selectList(skuQueryWrapper);
            //spuQueryVoList ????????????
            List<SpuQueryVo> spuQueryVoList = spuList.stream().map(x -> {
                SpuQueryVo spuQueryVo = new SpuQueryVo();
                spuQueryVo.setSpu(x);
                spuQueryVo.setSkuList(skuList.stream().filter(y -> y.getSpuId().longValue() == x.getId().longValue()).collect(Collectors.toList()));
                return spuQueryVo;
            }).collect(Collectors.toList());
            //????????????
            return new DataTableResult(spuQueryParam.getDraw(),count,count,spuQueryVoList);
        }
        return new DataTableResult(spuQueryParam.getDraw(),count,count,new ArrayList());
    }

    @Override
    public ServerResponse del(Long id,String rootPath) {
        //??????spu
        spuMapper.deleteById(id);
        //??????spu?????????sku
        QueryWrapper<Sku> skuQueryWrapper = new QueryWrapper<>();
        skuQueryWrapper.eq("spuId",id);
        skuMapper.delete(skuQueryWrapper);
        //??????spu?????????skuImage
        QueryWrapper<SkuImage> skuImageQueryWrapper = new QueryWrapper<>();
        skuImageQueryWrapper.in("spuId",id);
        List<SkuImage> skuImageList = skuImageMapper.selectList(skuImageQueryWrapper);
        //??????????????????????????????
        deleteImages(rootPath, skuImageList);
        //??????spu?????????skuImage
        skuImageMapper.delete(skuImageQueryWrapper);
        //????????????
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
        //??????idList
        List<Long> idList = Arrays.stream(ids.split(",")).map(x -> Long.parseLong(x)).collect(Collectors.toList());
        //????????????spu
        spuMapper.deleteBatchIds(idList);
        //????????????sku
        QueryWrapper<Sku> skuQueryWrapper = new QueryWrapper<>();
        skuQueryWrapper.in("spuId",idList);
        skuMapper.delete(skuQueryWrapper);
        //????????????spu?????????????????????
        QueryWrapper<SkuImage> skuImageQueryWrapper = new QueryWrapper<>();
        skuImageQueryWrapper.in("spuId",idList);
        List<SkuImage> skuImageList = skuImageMapper.selectList(skuImageQueryWrapper);
        deleteImages(rootPath, skuImageList);
        //????????????skuImage
        skuImageMapper.delete(skuImageQueryWrapper);
        //????????????
        RedisUtil.delete("skuList");
        return ServerResponse.success();
    }

    @Override
    public ServerResponse findSpu(Long spuId) {
        //??????spu??????
        Spu spu = spuMapper.selectById(spuId);
        //??????typeId
        Long cate3 = spu.getCate3();
        Cate cate = cateMapper.selectById(cate3);
        Long typeId = cate.getTypeId();
        //???????????????skuList
        QueryWrapper<Sku> skuQueryWrapper = new QueryWrapper<>();
        skuQueryWrapper.eq("spuId",spuId);
        List<Sku> skuList = skuMapper.selectList(skuQueryWrapper);
        //???????????????skuImageVoList
        //??????spuId?????????spu????????????????????????
        QueryWrapper<SkuImage> skuImageQueryWrapper = new QueryWrapper<>();
        skuImageQueryWrapper.eq("spuId",spuId);
        List<SkuImage> skuImageList = skuImageMapper.selectList(skuImageQueryWrapper);
        //??????????????????id??????
        Set<Long> colorIdSet = skuImageList.stream().map(x -> x.getColorId()).collect(Collectors.toSet());
        //?????????????????????specValueList
        List<SpecValue> specValueList = specValueMapper.selectBatchIds(colorIdSet);
        //???specValueList???skuImageList????????????
        List<SkuImageVo> skuImageVoList = specValueList.stream().map(x -> {
            SkuImageVo skuImageVo = new SkuImageVo();
            skuImageVo.setColorId(x.getId());
            skuImageVo.setColorName(x.getSpecValue());
            skuImageVo.setSkuImageList(skuImageList.stream().filter(y -> y.getColorId().longValue() == x.getId().longValue()).collect(Collectors.toList()));
            return skuImageVo;
        }).collect(Collectors.toList());
        //????????????
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
        //?????????????????????????????????
        /*File file = new File(rootPath + image);
        if(file.exists()){
            file.delete();
        }*/
        OSSUtil.deleteFile(image);
        //??????????????????????????????
        skuImageMapper.deleteById(key);
        return ServerResponse.success(image);
    }

    @Override
    public ServerResponse updateSpu(SpuParam spuParam) {
        Spu spu = spuParam.getSpu();
        //??????spu?????????
        spuMapper.updateById(spu);
        Long spuId = spu.getId();
        //sku????????? [???????????????delete??????????????????insert??????????????????update]
        String[] priceArr = spuParam.getPrices().split(",");
        String[] stockArr = spuParam.getStocks().split(",");
        //["100_16:??????,20:8G,22:?????????","101_18:??????,22:16G,22:?????????","-1_19:??????,22:16G,22:?????????"]
        String[] specItemArr = spuParam.getSpecInfos().split(";");
        // 16:x,y,z;19:a,b,c
        String[] skuImageArr = spuParam.getSkuImages().split(";");
        List<Sku> insertSkuList = new ArrayList<>();
        //???????????????skuList
        QueryWrapper<Sku> skuQueryWrapper = new QueryWrapper<>();
        skuQueryWrapper.eq("spuId",spuId);
        List<Sku> skuList = skuMapper.selectList(skuQueryWrapper);
        List<Long> updateIdList = new ArrayList<>();
        for (int i = 0; i < specItemArr.length; i++) {
            //100_16:??????,20:8G,22:?????????
            String specItem = specItemArr[i];
            long skuId = Long.parseLong(specItem.split("_")[0]);
            String specInfo = specItem.split("_")[1];
            //spu???name?????????????????? "??????p30 ?????? 8G ?????????"
            List<String> specValueNameList = Arrays.stream(specInfo.split(","))
                    .map(x -> x.split(":")[1]).collect(Collectors.toList());
            String skuName = spu.getSpuName()+" "+StringUtils.join(specValueNameList," ");
            //skuImage ?????????????????????????????????
            String colorId = specInfo.split(",")[0].split(":")[0];
            String skuImage = Arrays.stream(skuImageArr).filter(x -> Long.parseLong(x.split("=")[0]) == Long.parseLong(colorId))
                    .map(y -> y.split("=")[1].split(",")[0])
                    .findFirst()
                    .get();
            if(skuId == -1){
                //??????
                Sku sku = buildSku(spuId, priceArr[i], stockArr[i], specInfo, skuName, colorId, skuImage);
                insertSkuList.add(sku);
            } else {
                //??????
                Sku sku = buildSku(spuId, priceArr[i], stockArr[i], specInfo, skuName, colorId, skuImage);
                sku.setId(skuId);
                updateIdList.add(skuId);
                skuMapper.updateById(sku);
            }
        }
        //????????????sku
        if(insertSkuList.size() > 0){
            skuMapper.addBatch(insertSkuList);
        }
        //???????????????sku
        //??????skuList??????????????????????????????????????????skuId
        //??????skuId????????????updateIdList????????????????????????????????????
        List<Long> deleteIdList = skuList.stream().filter(x -> !updateIdList.contains(x.getId()))
                .map(y -> y.getId()).collect(Collectors.toList());
        if(deleteIdList.size() > 0){
            skuMapper.deleteBatchIds(deleteIdList);
        }
        //skuImage?????????
        //?????????
        QueryWrapper<SkuImage> skuImageQueryWrapper = new QueryWrapper<>();
        skuImageQueryWrapper.eq("spuId",spuId);
        skuImageMapper.delete(skuImageQueryWrapper);
        //?????????
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
        //????????????
        if(skuImageList.size() > 0){
            skuImageMapper.addBatch(skuImageList);
        }
        //????????????
        RedisUtil.delete("skuList");
        return ServerResponse.success();
    }

    @Override
    public ServerResponse updateStatus(Long id) {
        Spu spu = new Spu();
        spu.setId(id);
        spu.setStatus(0);
        spuMapper.updateById(spu);
        //???????????????sku
        //?????? update ??? set ??????=xxx where spuId=xxx
        Sku sku = new Sku();
        sku.setStatus(0);
        QueryWrapper<Sku> updateWrapper = new QueryWrapper<>();
        updateWrapper.eq("spuId", id);
        skuMapper.update(sku, updateWrapper);
        //????????????
        RedisUtil.delete("skuList");
        return ServerResponse.success();
    }

    @Override
    public ServerResponse updateStatus1(Long id) {
        Spu spu = new Spu();
        spu.setId(id);
        spu.setStatus(1);
        spuMapper.updateById(spu);
        //???????????????sku
        //?????? update ??? set ??????=xxx where spuId=xxx
        Sku sku = new Sku();
        sku.setStatus(1);
        QueryWrapper<Sku> updateWrapper = new QueryWrapper<>();
        updateWrapper.eq("spuId", id);
        skuMapper.update(sku, updateWrapper);
        //????????????
        RedisUtil.delete("skuList");
        return ServerResponse.success();
    }

    @Override
    public ServerResponse updateNewYes(Long id) {
        Spu spu = new Spu();
        spu.setId(id);
        spu.setNews(1);
        spuMapper.updateById(spu);
        //???????????????sku
        //?????? update ??? set ??????=xxx where spuId=xxx
        Sku sku = new Sku();
        sku.setNews(1);
        QueryWrapper<Sku> updateWrapper = new QueryWrapper<>();
        updateWrapper.eq("spuId", id);
        skuMapper.update(sku, updateWrapper);
        //????????????
        RedisUtil.delete("skuList");
        return ServerResponse.success();
    }

    @Override
    public ServerResponse updateNewNo(Long id) {
        Spu spu = new Spu();
        spu.setId(id);
        spu.setNews(0);
        spuMapper.updateById(spu);
        //???????????????sku
        //?????? update ??? set ??????=xxx where spuId=xxx
        Sku sku = new Sku();
        sku.setNews(0);
        QueryWrapper<Sku> updateWrapper = new QueryWrapper<>();
        updateWrapper.eq("spuId", id);
        skuMapper.update(sku, updateWrapper);
        //????????????
        RedisUtil.delete("skuList");
        return ServerResponse.success();
    }

    @Override
    public ServerResponse updateTuiYes(Long id) {
        Spu spu = new Spu();
        spu.setId(id);
        spu.setRecommend(1);
        spuMapper.updateById(spu);
        //???????????????sku
        //?????? update ??? set ??????=xxx where spuId=xxx
        Sku sku = new Sku();
        sku.setRecommend(1);
        QueryWrapper<Sku> updateWrapper = new QueryWrapper<>();
        updateWrapper.eq("spuId", id);
        skuMapper.update(sku, updateWrapper);
        //????????????
        RedisUtil.delete("skuList");
        return ServerResponse.success();
    }

    @Override
    public ServerResponse updateTuiNo(Long id) {
        Spu spu = new Spu();
        spu.setId(id);
        spu.setRecommend(0);
        spuMapper.updateById(spu);
        //???????????????sku
        //?????? update ??? set ??????=xxx where spuId=xxx
        Sku sku = new Sku();
        sku.setRecommend(0);
        QueryWrapper<Sku> updateWrapper = new QueryWrapper<>();
        updateWrapper.eq("spuId", id);
        skuMapper.update(sku, updateWrapper);
        //????????????
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
