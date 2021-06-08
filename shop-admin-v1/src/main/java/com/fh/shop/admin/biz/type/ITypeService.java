package com.fh.shop.admin.biz.type;

import com.fh.shop.admin.param.TypeParam;
import com.fh.shop.admin.param.TypeQueryParam;
import com.fh.shop.common.DataTableResult;
import com.fh.shop.common.ServerResponse;

public interface ITypeService {

    ServerResponse findInfo();

    ServerResponse addType(TypeParam typeParam);

    DataTableResult findList(TypeQueryParam typeQueryParam);

    ServerResponse findType(Long id);

    ServerResponse updateType(TypeParam typeParam);

    ServerResponse deleteType(Long id);

    ServerResponse deleteBatch(String ids);

    ServerResponse findAllType();

}
