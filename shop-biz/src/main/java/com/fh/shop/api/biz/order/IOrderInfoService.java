package com.fh.shop.api.biz.order;

import com.fh.shop.common.ServerResponse;

public interface IOrderInfoService {

    ServerResponse cancelInfoOrder(String id);
}
