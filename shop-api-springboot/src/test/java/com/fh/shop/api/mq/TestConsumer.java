package com.fh.shop.api.mq;

import org.springframework.stereotype.Component;

@Component
public class TestConsumer {

//    @RabbitListener(queues = MQConfig.TEST_EXCHANGE_DTRECT)
//    public void handleMessage(String msg){
//        System.out.println(msg);
//    }
 /*@RabbitListener(queues = MQConfig.TEST_EXCHANGE_FANOUT)
    public void sendFanout(String msg){
        System.out.println(msg);
    }
*/

}
