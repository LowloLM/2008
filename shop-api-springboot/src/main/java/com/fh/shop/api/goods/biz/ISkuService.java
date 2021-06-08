package com.fh.shop.api.goods.biz;

import com.fh.shop.api.vo.SkuTaskVO;
import com.fh.shop.common.ServerResponse;

import java.util.List;

public interface ISkuService {

    ServerResponse findRecommendNewsList();

    List<SkuTaskVO> skuTaskList(int stockLimit);


}
