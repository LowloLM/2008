package com.fh.shop.admin.biz.spec;

import com.fh.shop.admin.param.SpecParam;
import com.fh.shop.admin.param.SpecQueryParam;
import com.fh.shop.common.DataTableResult;
import com.fh.shop.common.ServerResponse;

public interface ISpecService {

    /***
     * 新增
     * @param specParam
     * @return
     */
    ServerResponse addSpec(SpecParam specParam);

    /***
     * 查询分页列表
     * @param specQueryParam
     * @return
     */
    DataTableResult findList(SpecQueryParam specQueryParam);

    ServerResponse findSpec(Long id);

    ServerResponse updateSpec(SpecParam specParam);

    ServerResponse deleteSpec(Long id);

    ServerResponse deleteBatch(String ids);
}
