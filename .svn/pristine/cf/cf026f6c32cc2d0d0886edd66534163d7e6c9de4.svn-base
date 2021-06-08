package com.fh.shop.admin.po.user;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/***
 * 用户
 */
@Data
public class User implements Serializable {

    private Long id;

    private String loginName;

    private String password;

    private String realName;

    private String mail;

    private String phone;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date birthday;

    private Integer sex;

    private String headImage;

    @TableField(exist = false)
    private String confirmPassword;

    @TableField(exist = false)
    private String oldLogo;

    private String salt;

    private Integer loginCount;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date loginTime;

    private Date newTime;

}
