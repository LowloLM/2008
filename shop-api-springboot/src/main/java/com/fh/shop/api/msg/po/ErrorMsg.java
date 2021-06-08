package com.fh.shop.api.msg.po;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
@Data
public class ErrorMsg implements Serializable {
    private Long id;
    private String msg;
    private Date insertTime;
    private String msgId;
}
