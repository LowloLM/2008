package com.fh.shop.api.mq;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MQConfig {

    public static final String TEST_EXCHANGE_DTRECT="test_exchange_direct";
    public static final String TEST_QUEUE_DTRECT="test_queue_direct";
    public static final String TEST_ROUTE_KEY="test";

    public static final String TEST_EXCHANGE_FANOUT="test_exchange_fanout";
    public static final String TEST_QUEUE_FANOUT="test_queue_fanout";
    public static final String TEST_QUEUE1_FANOUT="test_queue1_fanout";

    public static final String TEST_EXCHANGE_TOPICE="test_exchange_topice";
    public static final String TEST_QUEUE_TOPICE="test_queue_topice";
    public static final String TEST_ROUTE_KEY_TOPICE="#.test.a1";


    //交换价
    @Bean
    public DirectExchange testDirectExchange(){
        return new DirectExchange(TEST_EXCHANGE_DTRECT,true,false);
    }
    //队列
    @Bean
    public Queue testQueue(){
        return new Queue(TEST_QUEUE_DTRECT,true);
    }
    //将他们组合
    @Bean
    public Binding testBinding(){
        return BindingBuilder.bind(testQueue()).to(testDirectExchange()).with(TEST_ROUTE_KEY);
    }


    @Bean
    public FanoutExchange testFanoutExchange(){
        return new FanoutExchange(TEST_EXCHANGE_FANOUT);
    }
    @Bean
    public Queue testFanoutQueue(){
        return new Queue(TEST_QUEUE_FANOUT);
    }

    @Bean
    public Queue testFanoutQueue1(){
        return new Queue(TEST_QUEUE1_FANOUT);
    }
    @Bean
    public Binding testBindingFanout(){
        return BindingBuilder.bind(testFanoutQueue()).to(testFanoutExchange());
    }

    @Bean
    public Binding testBindingFanout1(){
        return BindingBuilder.bind(testFanoutQueue1()).to(testFanoutExchange());
    }



    //交换价
    @Bean
    public TopicExchange testTopicetExchange(){
        return new TopicExchange(TEST_EXCHANGE_TOPICE);
    }
    //队列
    @Bean
    public Queue testQueueTopice(){
        return new Queue(TEST_QUEUE_TOPICE);
    }
    //将他们组合
    @Bean
    public Binding testBindingTopice(){
        return BindingBuilder.bind(testQueueTopice()).to(testTopicetExchange()).with(TEST_ROUTE_KEY_TOPICE);
    }

}
