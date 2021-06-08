package com.fh.shop.api.member.controller;

import com.fh.shop.api.annotation.Check;
import com.fh.shop.api.commone.Constans;
import com.fh.shop.api.commone.KeyUtil;
import com.fh.shop.api.member.biz.IMemberService;
import com.fh.shop.api.member.param.MemberParam;
import com.fh.shop.api.member.param.UpdatePasswordParam;
import com.fh.shop.api.member.vo.MemberRequestVO;
import com.fh.shop.api.member.vo.MemberVO;
import com.fh.shop.common.ServerResponse;
import com.fh.shop.util.RedisUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.http.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@RestController
@Api("会员接口")
@CrossOrigin
@RequestMapping(value = "/api")
public class MemberController {

    @Resource(name = "memberService")
    private IMemberService memberService;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private HttpServletResponse response;
    @Value("${active.success}")
    private String success;

    @Value("${active.error}")
    private String error;


    @GetMapping("/member/findMember")
    @Check
    @ApiOperation("获取会员信息")
    @ApiImplicitParam(name = "x-auth",value = "value",dataType = "java.lang.String",required = true,paramType = "header")
    public ServerResponse finMember(){
      MemberVO memberVO=(MemberVO)request.getAttribute(Constans.CURR_MEMBER);
        return ServerResponse.success(memberVO);
    }

    @PostMapping("/member/reg")
    public ServerResponse addMember(MemberParam memberParam){
        return memberService.addMember( memberParam);
    }


    @PostMapping("/member/login")
    @ApiOperation("登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "memberName",value = "会员名",dataType = "java.lang.string",required = true),
            @ApiImplicitParam(name = "password",value = "密码",dataType = "java.lang.string",required = true)
    }
    )
    public ServerResponse login(String memberName ,String password,String state){
        return memberService.login(memberName,password,state);
    }

    @GetMapping("/member/checkName")
    public ServerResponse checkMemberName(String memberName){
        return memberService.checkName(memberName);
    }
    @GetMapping("/member/checkEmail")
    public ServerResponse checkEmail(String mail){
        return memberService.checkEmail(mail);
    }
    @GetMapping("/member/checkPhone")
    public ServerResponse checkPhone(String phone){
        return memberService.checkPhone(phone);
    }

    @GetMapping("/member/logout")
    @Check
    public ServerResponse lougout(){
        MemberVO memberVO=(MemberVO)request.getAttribute(Constans.CURR_MEMBER);
        RedisUtil.delete(KeyUtil.buildMemberKey(memberVO.getId()));
        return ServerResponse.success();
    }

    @GetMapping("/member/verifyName")
    public ServerResponse verifyName(MemberRequestVO memberRequestVO){
        return memberService.verifyName(memberRequestVO);
    }

//    @GetMapping("/member/emailCode")
//    public ServerResponse emailCode(MemberRequestVO memberRequestVO){
//        return memberService.emailCode(memberRequestVO);
//    }

    @GetMapping("/member/acquisitionEmailCode")
    public ServerResponse acquisitionEmailCode(MemberRequestVO memberRequestVO){
        return memberService.acquisitionEmailCode(memberRequestVO);
    }

    @PostMapping("/member/findPassword")
    @ApiOperation("找回密码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mail", value = "邮箱", dataType = "java.lang.string", required = true),
            @ApiImplicitParam(name = "code", value = "验证码", dataType = "java.lang.string", required = true),
            @ApiImplicitParam(name = "imgKey", value = "图片key", dataType = "java.lang.string", required = true)
    }
    )
    public ServerResponse findPassword(String mail,String code,String imgKey){
        return memberService.findPassword( mail, code,imgKey);
    }

    @PostMapping("/member/updatePassword")
    @Check
    @ApiOperation("修改密码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "password", value = "旧密码", dataType = "java.lang.string", required = true),
            @ApiImplicitParam(name = "newPassword", value = "新密码", dataType = "java.lang.string", required = true),
            @ApiImplicitParam(name = "ConfirmNewPassword", value = "确认新密码", dataType = "java.lang.string", required = true),
            @ApiImplicitParam(name = "x-auth",value = "value",dataType = "java.lang.String",required = true,paramType = "header")
    }
    )
    public ServerResponse updatePassword(UpdatePasswordParam updatePasswordParam){
        MemberVO memberVO = (MemberVO) request.getAttribute(Constans.CURR_MEMBER);
        Long id = memberVO.getId();
        updatePasswordParam.setId(id);
        return memberService. updatePassword( updatePasswordParam);
    }

    @GetMapping("/member/activate")
    public void activate(String id)throws IOException {
        int res = memberService.activate(id);

        if (res == Constans.REQUEST_ERROR){
            response.sendRedirect(error);
        }else{
            response.sendRedirect(success);
        }

    }


    @PostMapping("/member/sendActiveMail")
    public ServerResponse activeMail(@RequestBody Map<String ,String> map){
        String uuid = map.get("uuid");
        String mail = map.get("mail");
        return memberService.activeMail(uuid,mail);
    }


}
