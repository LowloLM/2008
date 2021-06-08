package com.fh.shop.admin.biz.brand;

import com.fh.shop.admin.param.BrandQueryParam;
import com.fh.shop.admin.po.brand.Brand;
import com.fh.shop.common.DataTableResult;
import com.fh.shop.common.ServerResponse;

public interface IBrandService {

    /***
     * 新增
     * @param brand
     */
    void addBrand(Brand brand);

    /***
     * 查询
     * @param brandQueryParam
     * @return
     */
    DataTableResult findList(BrandQueryParam brandQueryParam);

    /***
     * 删除
     * @param id
     */
    void del(Long id,String rootRealPath);

    /***
     * 回显
     * @param id
     * @return
     */
    ServerResponse findBrand(Long id);

    /***
     * 修改
     * @param brand
     * @return
     */
    ServerResponse updateBrand(Brand brand,String rootRealPath);

    /***
     * 批量删除
     * @param ids
     * @param realPath
     * @return
     */
    ServerResponse deleteBatch(String ids, String realPath);

    ServerResponse findBrandList(Long cateId);

}
