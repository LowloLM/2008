package com.fh.shop.type.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fh.shop.type.po.TypeSpec;

import java.util.List;

public interface ITypeSpecMapper extends BaseMapper<TypeSpec> {

    void addBatch(List<TypeSpec> typeSpecList);

    List<Long> findTypeSpecIdList(Long id);

    void deleteTypeSpecByTypeId(Long id);

    void deleteBatch(List<Long> idList);
}
