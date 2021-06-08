package com.fh.shop.api.member.biz;

import com.fh.shop.api.member.param.MemberParam;
import com.fh.shop.api.member.param.UpdatePasswordParam;
import com.fh.shop.api.member.vo.MemberRequestVO;
import com.fh.shop.common.ServerResponse;

public interface IMemberService  {
    ServerResponse addMember(MemberParam memberParam);

    ServerResponse login(String memberName, String password,String state);

    ServerResponse checkName(String memberName);

    ServerResponse checkEmail(String mail);

    ServerResponse checkPhone(String phone);


    ServerResponse verifyName(MemberRequestVO memberRequestVO);

   // ServerResponse emailCode(MemberRequestVO memberRequestVO);

    ServerResponse acquisitionEmailCode(MemberRequestVO memberRequestVO);

    ServerResponse findPassword(String mail, String code,String imgKey);

    ServerResponse updatePassword(UpdatePasswordParam updatePasswordParam);


    int activate(String id);

    ServerResponse activeMail(String uuid,String mail);

}
