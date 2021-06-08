package com.fh.shop.api.mq;

import com.fh.shop.api.config.MQConfig;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TestProducer {

    @Autowired
    private AmqpTemplate amqpTemplate;

//    public void send(String msg){
//        amqpTemplate.convertAndSend(MQConfig.TEST_EXCHANGE_DTRECT,MQConfig.TEST_ROUTE_KEY,msg);
//    }

//    public void sendFanout(String msg){
//        amqpTemplate.convertAndSend(MQConfig.TEST_EXCHANGE_FANOUT,null,msg);
//    }
//  public void sendTopic(String msg,String key){
//        amqpTemplate.convertAndSend(MQConfig.TEST_EXCHANGE_TOPICE,key,msg);
//    }

}
