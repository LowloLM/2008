package com.fh.shop.admin.mapper.brand;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fh.shop.admin.param.BrandQueryParam;
import com.fh.shop.admin.po.brand.Brand;

import java.util.List;
import java.util.Set;


public interface IBrandMapper extends BaseMapper<Brand> {

    /***
     * 新增
     * @param brand
     */
    void addBrand(Brand brand);

    /***
     * 查询总条数
     * @param brandQueryParam
     * @return
     */
    Long findListCount(BrandQueryParam brandQueryParam);

    /***
     * 获取分页数据
     * @param brandQueryParam
     * @return
     */
    List<Brand> findPageList(BrandQueryParam brandQueryParam);

    /***
     * 删除
     * @param id
     */
    void del(Long id);

    /***
     * 回显
     * @param id
     * @return
     */
    Brand findBrand(Long id);

    /***
     * 修改
     * @param brand
     */
    void updateBrand(Brand brand);

    /***
     * 查询要删除的记录的图片路径
     * @param idList
     * @return
     */
    List<String> findBrandListByIdList(List<Long> idList);

    /***
     * 批量删除
     * @param idList
     */
    void deleteBatch(List<Long> idList);

    List<Brand> findAllBrandList();

    List<Brand> findBrandListByTypeId(Long typeId);

    List<Brand> findBrandListByTypeIdList(Set<Long> typeIdList);
}
