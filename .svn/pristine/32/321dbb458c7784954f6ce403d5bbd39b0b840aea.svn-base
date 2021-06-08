package com.fh.shop.admin.mapper.cate;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fh.shop.admin.param.CateParam;
import com.fh.shop.admin.po.cate.Cate;

import java.util.List;

public interface ICateMapper extends BaseMapper<Cate> {

    List<Cate> findAllList();

    void addCate(Cate cate);

    void deleteCate(List<Long> idList);

    Cate findCate(Long id);

    void updateCate(Cate cate);

    void updateTypeInfo(CateParam cateParam);

    void addFatherCate(Cate cate);
}
