package com.fh.shop.api.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fh.shop.api.po.OrderItem;

import java.util.ArrayList;

public interface IOrderItemMapper extends BaseMapper<OrderItem> {

    void batchInster(ArrayList<OrderItem> orderItems);
}
