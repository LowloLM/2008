package com.fh.shop.admin.param;

import com.fh.shop.common.Page;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Data
public class LogQueryParam extends Page implements Serializable {

    private String userName;

    private String realName;

    private String info;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date startDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date endDate;

}
