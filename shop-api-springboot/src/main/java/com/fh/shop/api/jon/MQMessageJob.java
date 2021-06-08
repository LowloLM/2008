package com.fh.shop.api.jon;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fh.shop.api.mapper.IMsgLogMapper;
import com.fh.shop.api.mq.MQConstants;
import com.fh.shop.api.po.MsgLog;
import com.fh.shop.common.SystemConstant;
import com.fh.shop.util.DateUtil;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class MQMessageJob {

    @Autowired
    private IMsgLogMapper msgLogMapper;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Scheduled(cron = " 30 * * * * ?")
    public void doJob(){
        //查询状态在“投递中”并且到了重试的时间
        QueryWrapper<MsgLog>msgLogQueryWrapper = new QueryWrapper<>();
        msgLogQueryWrapper.in("status",SystemConstant.MESSAGE_LOG_STATUS.SENDING,SystemConstant.MESSAGE_LOG_STATUS.EXCHANGE_QUEUE_FAIL);
        msgLogQueryWrapper.le("retryTime",new Date());
        List<MsgLog> msgLogs = msgLogMapper.selectList(msgLogQueryWrapper);
        msgLogs.forEach(x -> {
            Integer retryCount = x.getRetryCount();
            String msgId = x.getMsgId();
            if (retryCount >= SystemConstant.RETRY_COUNT){
                //变更状态为“投递失败”
                MsgLog msgLog = new MsgLog();
                msgLog.setMsgId(msgId);
                msgLog.setStatus(SystemConstant.MESSAGE_LOG_STATUS.SEND_FALL);
                msgLog.setUpdateTime(new Date());
                msgLogMapper.updateById(msgLog);
            }else {
                //那么更新 “重试时间=当前时间+1分钟”，“重试次数=重试次数+1”，“更新时间”
                MsgLog msgLog = new MsgLog();
                msgLog.setMsgId(msgId);
                Date date = new Date();
                msgLog.setRetryTime(DateUtil.addMinute(date,1));
                msgLog.setRetryCount(retryCount+1);
                msgLog.setUpdateTime(date);
                msgLogMapper.updateById(msgLog);
                //重投
                CorrelationData correlationData = new CorrelationData(msgId);
                //发送消息
                rabbitTemplate.convertAndSend(MQConstants.MAIL_EXCHANGE,MQConstants.MAIL_ROUTE_KEY,x.getMessage(),correlationData);
            }
        });
    }
}
