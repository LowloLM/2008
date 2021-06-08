package com.fh.shop.admin.biz.cate;

import com.fh.shop.admin.param.CateParam;
import com.fh.shop.admin.po.cate.Cate;
import com.fh.shop.common.ServerResponse;

public interface ICateService {

    ServerResponse findAllList();

    ServerResponse addCate(Cate cate);

    ServerResponse deleteCate(String ids);

    ServerResponse findCate(Long id);

    ServerResponse updateCate(CateParam cateParam);

    ServerResponse addFatherCate(Cate cate);

    ServerResponse findCateChilds(Long id);
}
