package com.fh.shop.admin.po.log;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/***
 * 日志
 */
@Data
public class Log implements Serializable {

    private Long id;

    private String userName;

    private String realName;

    private String info;

    private String paramInfo;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    //@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date insertTime;

}
