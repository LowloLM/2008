package com.fh.shop.api.mq;

import com.alibaba.fastjson.JSON;
import com.fh.shop.api.mapper.IMsgLogMapper;
import com.fh.shop.api.po.MsgLog;
import com.fh.shop.common.SystemConstant;
import com.fh.shop.util.MailUtil;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import java.io.IOException;
import java.util.Date;

@Component
public class MailConsumer {

    @Autowired
    private IMsgLogMapper msgLogMapper;

    @RabbitListener(queues = MQConstants.MAIL_QUEUE)
    public void handleMailMsg(String mailJson, Message message,Channel channel) throws IOException {
        MessageProperties messageProperties = message.getMessageProperties();
        long deliveryTag = messageProperties.getDeliveryTag();
        MailMessage mailMessage = JSON.parseObject(mailJson, MailMessage.class);
        String msgId = mailMessage.getMsgId();
        try {
            //进行幂等性的判断
            MsgLog msgLog1 = msgLogMapper.selectById(msgId);
            if (msgLog1!=null){
                    if (msgLog1.getStatus() == SystemConstant.MESSAGE_LOG_STATUS.SENDING ||msgLog1.getStatus()==SystemConstant.MESSAGE_LOG_STATUS.EXCHANGE_QUEUE_FAIL ){
                        //如果状态时0或者时4但能在消息队列中获取这个数据就证明他投递成功所以将状态更改成投递成功
                        MsgLog msgLog = new MsgLog();
                        msgLog.setStatus(SystemConstant.MESSAGE_LOG_STATUS.SEND_SUCCESS);
                        msgLog.setUpdateTime(new Date());
                        msgLog.setMsgId(msgId);
                        msgLogMapper.updateById(msgLog);
                    }
            }
            //随着t_msg_log这表的越来越大所以我们要做一个删除功能 ,人工删除或者定时删除
            //每天凌晨4点 将消费成功的删除

            if (msgLog1 != null && msgLog1.getStatus()==SystemConstant.MESSAGE_LOG_STATUS.CONSUME_SUCCESS){
                //响应手动确认删除消息队列的消息
                channel.basicAck(deliveryTag,false);
                return;
            }
            //发邮件
            MailUtil.sendMail(mailMessage.getTitle(),mailMessage.getConect(),mailMessage.getTo());
            //更新数据库表中的消息为“消费成功”
            MsgLog msgLog = new MsgLog();
            msgLog.setStatus(SystemConstant.MESSAGE_LOG_STATUS.CONSUME_SUCCESS);
            msgLog.setUpdateTime(new Date());
            msgLog.setMsgId(msgId);
            msgLogMapper.updateById(msgLog);
            //响应手动确认删除消息队列的消息
            channel.basicAck(deliveryTag,false);
        } catch (Exception e) {
            e.printStackTrace();
            //要从队列中删除（是否重新进入队列）
            channel.basicNack(deliveryTag,false,true);
        }
    }
}
