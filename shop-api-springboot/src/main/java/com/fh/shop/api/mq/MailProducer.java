package com.fh.shop.api.mq;

import com.alibaba.fastjson.JSON;
import com.fh.shop.api.mapper.IMsgLogMapper;
import com.fh.shop.api.po.MsgLog;
import com.fh.shop.common.SystemConstant;
import com.fh.shop.util.DateUtil;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

@Component
public class MailProducer {

    @Autowired
    private AmqpTemplate amqpTemplate;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private IMsgLogMapper msgLogMapper;

    public void sendMail(MailMessage mailMessage){
        String mailMessageJson = JSON.toJSONString(mailMessage);
        amqpTemplate.convertAndSend(MQConstants.MAIL_EXCHANGE, MQConstants.MAIL_ROUTE_KEY,mailMessageJson);
    }

    public void sendMailMessage(MailMessage mailMessage){
        //插入数据库
        String msgId = UUID.randomUUID().toString();
        mailMessage.setMsgId(msgId);
        String mailMesageJson = JSON.toJSONString(mailMessage);
        MsgLog msgLog = new MsgLog();
        msgLog.setMsgId(msgId);
        msgLog.setExchangeName(MQConstants.MAIL_EXCHANGE);
        msgLog.setRouteKey(MQConstants.MAIL_ROUTE_KEY);
        msgLog.setMessage(mailMesageJson);
        Date date = new Date();
        msgLog.setInsertTime(date);
        msgLog.setUpdateTime(date);
        //下次时间,当前时间+1分字
        Date retryTime = DateUtil.addMinute(date, 1);
        msgLog.setRetryTime(retryTime);
        msgLog.setStatus(SystemConstant.MESSAGE_LOG_STATUS.SENDING);
        msgLog.setRetryCount(0);
        msgLogMapper.insert(msgLog);
        CorrelationData correlationData = new CorrelationData(msgId);
        //发送消息

        rabbitTemplate.convertAndSend(MQConstants.MAIL_EXCHANGE,MQConstants.MAIL_ROUTE_KEY,mailMesageJson,correlationData);
    }
}
