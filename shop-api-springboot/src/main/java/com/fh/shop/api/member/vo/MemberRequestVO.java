package com.fh.shop.api.member.vo;

import lombok.Data;

@Data
public class MemberRequestVO {
    private String memberName;
    private String mail;
    private String code;
    private String newPassword;
}
