package com.fh.shop.api.biz.order;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fh.shop.api.mapper.IOrderItemMapper;
import com.fh.shop.api.mapper.IOrderMapper;
import com.fh.shop.api.mapper.ISkuMapper;
import com.fh.shop.api.po.Order;
import com.fh.shop.api.po.OrderItem;
import com.fh.shop.common.ResponseEnum;
import com.fh.shop.common.ServerResponse;
import com.fh.shop.common.SystemConstant;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("orderInfoService")
public class IOrderInfoServiceImpl implements IOrderInfoService {

    @Resource
    private IOrderMapper orderMapper;

    @Resource
    private IOrderItemMapper orderItemMapper;

    @Resource
    private ISkuMapper skuMapper;

    @Override
    public ServerResponse cancelInfoOrder(String id) {
            Order orderInfo = orderMapper.selectById(id);
            if(orderInfo.getStatus() != SystemConstant.ORDER_STATUS.WATT_PAY){
                return ServerResponse.error(ResponseEnum.PAY_ORDER_TYPE_IS_ERROR);
            }
            //更新订单状态
            Order order = new Order();
            order.setId(id);
            order.setStatus(SystemConstant.ORDER_STATUS.TRADE_CLOSE);
            orderMapper.updateById(order);
            //更新库存
            QueryWrapper<OrderItem> orderItemQueryWrapper = new QueryWrapper<>();
            orderItemQueryWrapper.eq("orderId",id);
            List<OrderItem> orderItemList = orderItemMapper.selectList(orderItemQueryWrapper);
            orderItemList.stream().forEach(x -> {
                Long skuId = x.getSkuId();
                Long skuCount = x.getSkuCount();
                skuMapper.updateSkuStock(skuId,skuCount);
            });
            return ServerResponse.success();
        }

}
