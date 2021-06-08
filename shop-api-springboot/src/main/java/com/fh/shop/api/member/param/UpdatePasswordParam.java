package com.fh.shop.api.member.param;

import com.fh.shop.api.po.Member;
import lombok.Data;

import java.io.Serializable;

@Data
public class UpdatePasswordParam extends Member implements Serializable {
    private String password;
    private String newPassword;
    private String ConfirmNewPassword;
    private Long id;

}
