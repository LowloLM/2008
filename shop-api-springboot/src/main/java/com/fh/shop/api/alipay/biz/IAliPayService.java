package com.fh.shop.api.alipay.biz;

import com.fh.shop.common.ServerResponse;

import java.util.Map;

public interface IAliPayService {
    ServerResponse alipay(String orderId);

    void updateStatus(String out_trade_no);

    String aliNotify(Map<String, String[]> requestParams);
}
