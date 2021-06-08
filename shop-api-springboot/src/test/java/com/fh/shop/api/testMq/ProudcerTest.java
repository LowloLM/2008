package com.fh.shop.api.testMq;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProudcerTest {

    @Autowired
    private AmqpTemplate amqpTemplate;

    public void sendMsg(String msg){
        amqpTemplate.convertAndSend(MQTestConfig.EX1,MQTestConfig.KEY1,msg);
    }
}
