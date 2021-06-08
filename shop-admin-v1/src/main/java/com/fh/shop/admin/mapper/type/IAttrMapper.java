package com.fh.shop.admin.mapper.type;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fh.shop.admin.po.type.Attr;

import java.util.List;

public interface IAttrMapper extends BaseMapper<Attr> {


    void addAttr(Attr attr);

    /***
     * 获取属性表的id
     * @param id
     * @return
     */
    List<Long> findAttr(Long id);

    void deleteAttr(Long id);

    List<Attr> findAttrListByTypeId(Long id);

    void deleteByTypeId(Long id);

    List<Attr> findAttrValue(List<Long> idList);

    void deleteBatch(List<Long> idList);
}
