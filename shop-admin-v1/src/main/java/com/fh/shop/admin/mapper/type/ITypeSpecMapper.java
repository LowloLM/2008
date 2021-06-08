package com.fh.shop.admin.mapper.type;

import com.fh.shop.admin.po.type.TypeSpec;

import java.util.List;

public interface ITypeSpecMapper {

    void addBatch(List<TypeSpec> typeSpecList);

    List<Long> findTypeSpecIdList(Long id);

    void deleteTypeSpecByTypeId(Long id);

    void deleteBatch(List<Long> idList);
}
