package com.fh.shop.api.mq;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderProducer {
    @Autowired
    private AmqpTemplate amqpTemplate;

    public void sendOrderMsg(String orderId,String expire){
        amqpTemplate.convertAndSend(MQConstants.ORDER_EX, MQConstants.ORDER_ROUTE_KEY, orderId,new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                message.getMessageProperties().setExpiration(expire);
                return message;
            }
        });
    }
}
