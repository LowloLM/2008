package com.fh.shop.type.biz;

import com.fh.shop.common.ServerResponse;
import com.fh.shop.type.param.TypeParam;

public interface ITypeService {
    ServerResponse findInfo();

    ServerResponse addType(TypeParam typeParam);

    ServerResponse findType(Long id);
}
