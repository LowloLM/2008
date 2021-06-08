package com.fh.shop.api.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fh.shop.api.param.OrderFindParam;
import com.fh.shop.api.po.Order;

import java.util.List;

public interface IOrderMapper extends BaseMapper<Order> {
    Long findListCount(OrderFindParam orderFindParam);

    List<Order> findPageList(OrderFindParam orderFindParam);
}
