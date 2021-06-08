package com.fh.shop.api.mq;

import com.fh.shop.api.biz.order.IOrderInfoService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class OrderConsumer {

    @Resource(name="orderInfoService")
    private IOrderInfoService orderInfoService;

    @RabbitListener(queues = MQConstants.ORDER_DEAD_QUEUE)
    public void handleOrderMsg(String orderId){
        orderInfoService.cancelInfoOrder(orderId);
    }
}
