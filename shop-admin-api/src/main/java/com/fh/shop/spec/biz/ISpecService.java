package com.fh.shop.spec.biz;

import com.fh.shop.common.DataTableResult;
import com.fh.shop.common.ServerResponse;
import com.fh.shop.spec.param.SpecAddParam;
import com.fh.shop.spec.param.SpecParam;
import com.fh.shop.spec.param.SpecQueryParam;

import java.util.List;

public interface ISpecService {

    /***
     * 新增
     * @param specParam
     * @return
     */
    //ServerResponse addSpec(SpecParam specParam);

    /***
     * 查询分页列表
     * @param specQueryParam
     * @return
     */
    ServerResponse findList(SpecQueryParam specQueryParam);

    ServerResponse findSpec(Long id);

    ServerResponse updateSpec(SpecAddParam specAddParam);

    ServerResponse deleteSpec(Long id);

    ServerResponse deleteBatch(String ids);

    ServerResponse addSpec(List<SpecAddParam> specParam);
}
