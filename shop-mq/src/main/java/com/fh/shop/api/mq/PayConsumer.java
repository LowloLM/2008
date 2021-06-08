package com.fh.shop.api.mq;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fh.shop.api.mapper.IMemberMapper;
import com.fh.shop.api.mapper.IOrderItemMapper;
import com.fh.shop.api.mapper.ISkuMapper;
import com.fh.shop.api.po.OrderItem;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class PayConsumer {

    @Resource
    private IMemberMapper memberMapper;

    @Resource
    private ISkuMapper skuMapper;
    @Resource
    private IOrderItemMapper orderItemMapper;

    @RabbitListener(queues = MQConstants.PAY_QUEUE_SCORE)
    public void handleScoreMessage(String msg){
        PayMessage payMessage = JSON.parseObject(msg, PayMessage.class);
        Long memberId = payMessage.getMemberId();
        double totalPrice = Math.floor(Double.parseDouble(payMessage.getTotalAmount()));
        memberMapper.updateScore(memberId,totalPrice);
    }

    @RabbitListener(queues = MQConstants.PAY_QUEUE_SELL)
    public void handleSellMessage(String msg){
        PayMessage payMessage = JSON.parseObject(msg, PayMessage.class);
        String orderId = payMessage.getOrderId();
        //加销量
        QueryWrapper<OrderItem> orderItemQueryWrapper = new QueryWrapper<>();
        orderItemQueryWrapper.eq("orderId",orderId);
        List<OrderItem> orderItemList = orderItemMapper.selectList(orderItemQueryWrapper);
        System.out.println(orderItemList.size());
        orderItemList.stream().forEach(x ->{
            skuMapper.updateSalesVolume(x.getSkuId(),x.getSkuCount());
        });

    }
}
