package com.fh.shop.admin.mapper.type;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fh.shop.admin.po.type.AttrValue;

import java.util.List;

public interface IAttrValueMapper extends BaseMapper<AttrValue> {


    void addBatch(List<AttrValue> attrValueList);

    void deleteAttrValue(List<Long> attrList);

    List<AttrValue> findAttrValueListByAttrIdList(List<Long> attrIdList);

    void deleteByAttrIdList(List<Long> attrIdList);

}
