package com.fh.shop.admin.mapper.type;

import com.fh.shop.admin.po.type.TypeBrand;

import java.util.List;

public interface ITypeBrandMapper {

    void addBatch(List<TypeBrand> typeBrandList);

    List<Long> findTypeBrandIdList(Long id);

    void deleteTypeBrandByTypeId(Long id);

    void deleteBatch(List<Long> idList);
}
