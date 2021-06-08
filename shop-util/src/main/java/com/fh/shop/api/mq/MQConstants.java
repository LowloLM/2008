package com.fh.shop.api.mq;


import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class MQConstants {
    //交换机
    public static final String MAIL_EXCHANGE = "mail_exchange";
    //队列
    public static final String MAIL_QUEUE = "mail_queue";
    //路由key
    public static final String MAIL_ROUTE_KEY = "mail_route_key";

    //交换机
    public static final String PAY_EXCHANGE = "pay_exchange";
    //队列
    public static final String PAY_QUEUE_SCORE = "pay_queue_score";
    //队列
    public static final String PAY_QUEUE_SELL  = "pay_queue_sell";


    //交换机
    public static final String ORDER_EX = "order_exchange";
    //队列
    public static final String ORDER_QUEUE = "order_queue";
    //路由key
    public static final String ORDER_ROUTE_KEY = "order_queue";


    //交换机
    public static final String ORDER_DEAD_EX = "order_dead_ex";
    //队列
    public static final String ORDER_DEAD_QUEUE = "order_dead_queue";
    //路由key
    public static final String ORDER_DEAD_ROUTE_KEY = "order_dead_route_key";


    @Bean
    public DirectExchange mailDirectExchange(){
        return new DirectExchange(MAIL_EXCHANGE,true,false);
    }

    @Bean
    public Queue mailQueue(){
        return new Queue(MAIL_QUEUE,true);
    }

    @Bean
    public Binding payBinding(){
        return BindingBuilder.bind(mailQueue()).to(mailDirectExchange()).with(MAIL_ROUTE_KEY);
    }




    @Bean
    public FanoutExchange payFanoutExchange(){
        return new FanoutExchange(PAY_EXCHANGE,true,false);
    }

    @Bean
    public Queue payQueue(){
        return new Queue(PAY_QUEUE_SCORE,true);
    }

    @Bean
    public Queue payQueue1(){
        return new Queue(PAY_QUEUE_SELL,true);
    }


    @Bean
    public Binding payBinding0(){
        return BindingBuilder.bind(payQueue()).to(payFanoutExchange());
    }

    @Bean
    public Binding payBinding1(){
        return BindingBuilder.bind(payQueue1()).to(payFanoutExchange());
    }







    @Bean
    public DirectExchange orderDirectExchange(){
        return new DirectExchange(ORDER_EX,true,false);
    }

    @Bean
    public Queue orderQueue(){
        Queue queue=new Queue(ORDER_QUEUE,true);
        queue.addArgument("x-dead-letter-exchange",ORDER_QUEUE);
        queue.addArgument("x-dead-letter-routing-key",ORDER_ROUTE_KEY);
        return queue;
    }

    @Bean
    public Binding orderBinding(){
        return BindingBuilder.bind(orderQueue()).to(orderDirectExchange()).with(ORDER_ROUTE_KEY);
    }




    @Bean
    public DirectExchange orderDeadDirectExchange(){
        return new DirectExchange(ORDER_DEAD_EX,true,false);
    }

    @Bean
    public Queue orderDeadQueue(){
        return new Queue(ORDER_DEAD_QUEUE,true);
    }

    @Bean
    public Binding orderDeadBinding(){
        return BindingBuilder.bind(orderDeadQueue()).to(orderDeadDirectExchange()).with(ORDER_DEAD_ROUTE_KEY);
    }







}
