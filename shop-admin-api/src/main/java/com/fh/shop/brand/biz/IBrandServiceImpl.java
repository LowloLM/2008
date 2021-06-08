package com.fh.shop.brand.biz;

import com.fh.shop.brand.mapper.IBrandMapper;
import com.fh.shop.brand.param.BrandQueryParam;
import com.fh.shop.brand.po.Brand;
import com.fh.shop.cate.mapper.ICateMapper;
import com.fh.shop.cate.po.Cate;
import com.fh.shop.common.ServerResponse;
import com.fh.shop.common.TableResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service("brandService")
public class IBrandServiceImpl implements IBrandService {

    @Autowired
    private IBrandMapper brandMapper;

    @Autowired
    private ICateMapper cateMapper;

    /***
     * 新增
     * @param brand
     */
    @Override
    public ServerResponse addBrand(Brand brand) {
        brandMapper.addBrand(brand);
        return ServerResponse.success();
    }

    /***
     * 查询
     * @param brandQueryParam
     * @return
     */
    @Override
    public ServerResponse findList(BrandQueryParam brandQueryParam) {
        //获取总条数
        Long count = brandMapper.findListCount(brandQueryParam);
        //获取分页列表
        List<Brand> brandList = brandMapper.findPageList(brandQueryParam);
        //组装数据
        TableResult tableResult = new TableResult(count,brandList);
        return ServerResponse.success(tableResult);
    }

    /***
     * 删除
     * @param id
     */
    @Override
    public void del(Long id,String rootRealPath) {
        //查询图片路径
        Brand brand = brandMapper.findBrand(id);
        String logo = brand.getLogo();
        File file = new File(rootRealPath + logo);
        if(file.exists()){
            file.delete();
        }
        brandMapper.del(id);
    }

    /***
     * 回显
     * @param id
     * @return
     */
    @Override
    public ServerResponse findBrand(Long id) {
        Brand brand = brandMapper.findBrand(id);
        return ServerResponse.success(brand);
    }

    /***
     * 修改
     * @param brand
     * @return
     */
    @Override
    public ServerResponse updateBrand(Brand brand,String rootRealPath) {
        //业务
        String logo = brand.getLogo();
        if(StringUtils.isNotEmpty(logo)){
            //上传了新图片
            //删除旧图片
            String oldLogo = brand.getOldLogo();
            //删除图片，需要的是绝对路径
            String logoPath = rootRealPath + oldLogo;
            File file = new File(logoPath);
            if(file.exists()){
                file.delete();
            }
        } else {
            //没有上传新图片
            brand.setLogo(brand.getOldLogo());
        }
        //更新数据信息
        Integer createYear = brand.getCreateYear();
        brand.setCreateYear(createYear);
        brandMapper.updateBrand(brand);
        return ServerResponse.success();
    }

    /***
     * 批量删除
     * @param ids
     * @param realPath
     * @return
     */
    @Override
    public ServerResponse deleteBatch(String ids, String realPath) {
        if (StringUtils.isNotEmpty(ids)) {
            // 先ids转换为idList
            String[] idArr = ids.split(",");
            List<Long> idList = new ArrayList<>();
            for (String s : idArr) {
                idList.add(Long.parseLong(s));
            }
            // 根据id集合查询要删除的记录的图片路径
            List<String> logoList = brandMapper.findBrandListByIdList(idList);
            // 删除硬盘上的文件
            for (String logo : logoList) {
                File file = new File(realPath + logo);
                if (file.exists()) {
                    file.delete();
                }
            }
            // 删除记录
            brandMapper.deleteBatch(idList);
            return ServerResponse.success();
        }
        return ServerResponse.error();
    }

    @Override
    public ServerResponse findBrandList(Long cateId) {
        //如果没有传过来cateId，那么就获取所有的品牌
        //如果传过来了cateId，那就获取该分类下对应的子子孙孙的类型id集合
        if(cateId == null){
            List<Brand> allBrandList = brandMapper.findAllBrandList();
            return ServerResponse.success(allBrandList);
        } else {
            //获取所有的分类
            List<Cate> cateList = cateMapper.selectList(null);
            //根据cateId找到 该分类下对应的子子孙孙的 类型id 集合
            Set<Long> typeIdList = new HashSet<>();
            getSubTree(cateId,cateList,typeIdList);
            //根据类型id的集合找到对应的品牌列表
            if(typeIdList.size() == 0){
                return ServerResponse.success();
            }
            List<Brand> brandList = brandMapper.findBrandListByTypeIdList(typeIdList);
            return ServerResponse.success(brandList);
        }
    }

    private void getSubTree(Long cateId, List<Cate> cateList, Set<Long> typeIdList){
        for (Cate cate : cateList) {
            if(cateId.longValue() == cate.getFatherId()){
                if(cate.getTypeId() != null && isLeaf(cate.getId(),cateList)){
                    typeIdList.add(cate.getTypeId());
                }
                getSubTree(cate.getId(),cateList,typeIdList);
            }
        }
    }

    private boolean isLeaf(Long cateId,List<Cate> cateList){
        for (Cate cate : cateList) {
            if(cateId.longValue() == cate.getFatherId().longValue()){
                return false;
            }
        }
        return true;
    }

}
