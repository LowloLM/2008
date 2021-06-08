package com.fh.shop.api.config;

import com.alibaba.fastjson.JSON;
import com.fh.shop.api.mapper.IMsgLogMapper;
import com.fh.shop.api.mq.MailMessage;
import com.fh.shop.api.po.MsgLog;
import com.fh.shop.common.SystemConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.UnsupportedEncodingException;
import java.util.Date;

@Configuration
@Slf4j
public class MQConfig {

    @Autowired
    private CachingConnectionFactory connectionFactory;
    @Autowired
    private IMsgLogMapper msgLogMapper;

    @Bean
    public RabbitTemplate rabbitTemplate(){
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        //设置回调方法{p-e}
        rabbitTemplate.setConfirmCallback((correlationData,ack,cause)-> {
            if (ack){
                String msgId = correlationData.getId();
                MsgLog messageLog = msgLogMapper.selectById(msgId);
                if (messageLog.getStatus() == SystemConstant.MESSAGE_LOG_STATUS.SENDING){
                    //成功投递，更新状态
                    MsgLog msgLog = new MsgLog();
                    msgLog.setStatus(SystemConstant.MESSAGE_LOG_STATUS.SEND_SUCCESS);
                    msgLog.setUpdateTime(new Date());
                    msgLog.setMsgId(msgId);
                    msgLogMapper.updateById(msgLog);
                }

            }else {
                //投递失败
                log.info("从P-E投递失败，消息{}，失败原因",correlationData,cause);
            }
        });
        //设置e-q的回调,只有在失败的时候才执行Return Callback
        rabbitTemplate.setMandatory(true);
        rabbitTemplate.setReturnCallback((message,replyCode,replyText,exchange,routingKey) ->{
//            E-Q失败了
            log.info("从e-q失败了，消息{}，replyCode:{},replyText{},交换机:{},路由：{}",
                    message,replyCode,replyText,exchange,routingKey);
            //更新数据库的状态
            byte[] body = message.getBody();
            try {
                String msgJson =new String(body,"utf-8");
                MailMessage mailMessage = JSON.parseObject(msgJson, MailMessage.class);
                String msgId = mailMessage.getMsgId();
                MsgLog msgLog = new MsgLog();
                msgLog.setStatus(SystemConstant.MESSAGE_LOG_STATUS.SEND_SUCCESS);
                msgLog.setUpdateTime(new Date());
                msgLog.setMsgId(msgId);
                msgLogMapper.updateById(msgLog);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        });
        return rabbitTemplate;
    }

}
