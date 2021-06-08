package com.fh.shop.api.testMq;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MQTestConfig {
    public static  final String EX1="ex1";
    public static final String QUEUE1="queue1";
    public static final String KEY1="key1";

    @Bean
    public DirectExchange ex1(){
        return new DirectExchange(EX1,true,false);
    }
    @Bean
    public Queue queue1(){
        return QueueBuilder.durable(QUEUE1).build();
    }
    @Bean
    public Binding binding1(){
        return BindingBuilder.bind(queue1()).to(ex1()).with(KEY1);
    }

}
