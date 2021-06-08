package com.fh.shop.api.order.biz;



import com.fh.shop.api.param.OrderFindParam;
import com.fh.shop.api.param.OrderParam;
import com.fh.shop.common.DataTableResult;
import com.fh.shop.common.ServerResponse;

public interface IOrderService {

    ServerResponse addOrder(OrderParam orderParam, Long memberId);

    DataTableResult findOrder(OrderFindParam orderFindParam);

    ServerResponse cancelOrder(String id);
}
