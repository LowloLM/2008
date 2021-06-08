package com.fh.shop.admin.mapper.type;

import com.fh.shop.admin.param.TypeQueryParam;
import com.fh.shop.admin.po.type.Type;

import java.util.List;

public interface ITypeMapper {

    void addType(Type type);

    Long findListCount(TypeQueryParam typeQueryParam);

    List<Type> findPageList(TypeQueryParam typeQueryParam);

    Type findType(Long id);

    void updateType(Type type);

    void deleteType(Long id);

    void deleteBatch(List<Long> idList);

    List<Type> findAllType();
}
