package com.fh.shop.type.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fh.shop.type.param.TypeQueryParam;
import com.fh.shop.type.po.Type;

import java.util.List;

public interface ITypeMapper extends BaseMapper<Type> {

    void addType(Type type);

    Long findListCount(TypeQueryParam typeQueryParam);

    List<Type> findPageList(TypeQueryParam typeQueryParam);

    Type findType(Long id);

    void updateType(Type type);

    void deleteType(Long id);

    void deleteBatch(List<Long> idList);

    List<Type> findAllType();
}
