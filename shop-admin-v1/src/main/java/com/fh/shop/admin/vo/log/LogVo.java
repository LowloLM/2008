package com.fh.shop.admin.vo.log;

import lombok.Data;

import java.io.Serializable;

@Data
public class LogVo implements Serializable {

    private Long id;

    private String userName;

    private String realName;

    private String info;

    private String insertTime;

    private String paramInfo;

}
