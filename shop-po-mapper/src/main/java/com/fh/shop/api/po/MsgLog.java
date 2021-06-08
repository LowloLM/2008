package com.fh.shop.api.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

@Data
public class MsgLog {


    @TableId(type = IdType.INPUT,value = "msgId")
    private String msgId;
    private String message;
    private Integer retryCount;
    private Date insertTime;
    private Date updateTime;
    private Date retryTime;
    private String exchangeName;
    private String routeKey;
    private Integer status;




}
