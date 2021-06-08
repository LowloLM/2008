package com.fh.shop.cate.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fh.shop.cate.param.CateParam;
import com.fh.shop.cate.po.Cate;

import java.util.List;

public interface ICateMapper extends BaseMapper<Cate> {

    List<Cate> findAllList();

    void addCate(Cate cate);

    void deleteCate(List<Long> idList);

    void updateCate(Cate cate);

    void updateTypeInfo(CateParam cateParam);

    void addFatherCate(Cate cate);
}
