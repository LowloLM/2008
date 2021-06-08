package com.fh.shop.type.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fh.shop.type.po.TypeBrand;

import java.util.List;

public interface ITypeBrandMapper extends BaseMapper<TypeBrand> {

    void addBatch(List<TypeBrand> typeBrandList);

    List<Long> findTypeBrandIdList(Long id);

    void deleteTypeBrandByTypeId(Long id);

    void deleteBatch(List<Long> idList);
}
