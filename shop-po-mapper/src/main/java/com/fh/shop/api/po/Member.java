package com.fh.shop.api.po;

import lombok.Data;

@Data
public class Member {
    private Long id;
    private String memberName;
    private String password;
    private String phone;
    private String mail;
    private String nickName;
    private String state;
    private Long score;
}
