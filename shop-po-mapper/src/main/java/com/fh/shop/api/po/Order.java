package com.fh.shop.api.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class Order implements Serializable {

    @TableId(type=IdType.INPUT)
    private String id;
    private Long   memberId;
    private BigDecimal totalPrice;
    private Long totalCount;
    private Integer status;
    private Date createTime;
    private Date sendTime;
    private Date payTime;
    private Date receiveTime;
    private String recipientName;
    private String recipientAddr;
    private String recipientPhone;
    private Integer payType;
}
