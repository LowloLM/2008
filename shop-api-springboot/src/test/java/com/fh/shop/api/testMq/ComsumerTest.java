package com.fh.shop.api.testMq;

import com.alibaba.fastjson.JSON;
import com.fh.shop.api.msg.mapper.IErrorMsgMapper;
import com.fh.shop.api.msg.po.ErrorMsg;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.SocketException;
import java.sql.SQLException;
import java.util.Date;

@Component
public class ComsumerTest {

    @Autowired
    private IErrorMsgMapper errorMsgMapper;

    @Transactional(rollbackFor = Exception.class)
    @RabbitListener(queues = MQTestConfig.QUEUE1)
    public void bandleMsg(String msg, Message message, Channel channel) throws IOException {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        MsgInfo msgInfo = JSON.parseObject(msg, MsgInfo.class);
        //业务
        try {
            System.out.println(msg);
            if (msg.contains("3")){
                System.out.println(3/0);
            }
            //手动确认
            channel.basicAck(deliveryTag,false);
        } catch (Exception e) {
            e.printStackTrace();
            //插入数据库
            try {
                ErrorMsg errorMsg = new ErrorMsg();
                errorMsg.setInsertTime(new Date());
                errorMsg.setMsg(msg.substring(0,msg.length()>2000?2000:msg.length()));
                errorMsg.setMsgId(msgInfo.getMsgId());
                errorMsgMapper.insert(errorMsg);
                //要从队列中删除（是否从新进入队列TRUE：重新进入 FALSE：拒绝）
                channel.basicReject(deliveryTag,false);
            } catch (SocketException e1) {
                e1.printStackTrace();
                throw new RuntimeException(e);
            }catch (Exception e2) {
                e2.printStackTrace();
                //违反了唯一的约束，这条消息已存在所以删除
                Throwable cause = e2.getCause();
                if (cause instanceof SQLException){
                    channel.basicReject(deliveryTag,false);
                }
                throw new RuntimeException(e2);
            }
        }

    }
}
