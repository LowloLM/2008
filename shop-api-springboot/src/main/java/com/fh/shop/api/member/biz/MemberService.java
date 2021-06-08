package com.fh.shop.api.member.biz;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fh.shop.api.commone.Constans;
import com.fh.shop.api.commone.KeyUtil;
import com.fh.shop.api.mapper.IMemberMapper;
import com.fh.shop.api.member.param.MemberParam;
import com.fh.shop.api.member.param.UpdatePasswordParam;
import com.fh.shop.api.member.vo.MemberRequestVO;
import com.fh.shop.api.member.vo.MemberVO;
import com.fh.shop.api.mq.MailMessage;
import com.fh.shop.api.mq.MailProducer;
import com.fh.shop.api.po.Member;
import com.fh.shop.common.ResponseEnum;
import com.fh.shop.common.ServerResponse;
import com.fh.shop.util.MailUtil;
import com.fh.shop.util.Md5Util;
import com.fh.shop.util.RedisUtil;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service("memberService")
@Transactional(rollbackFor = Exception.class)
public class MemberService implements IMemberService {

    @Autowired
    private MailProducer mailProducer;
    @Autowired
    private IMemberMapper memberMapper;

    @Override
    public ServerResponse addMember(MemberParam memberParam) {

        String memberName = memberParam.getMemberName();
        String password = memberParam.getPassword();
        String confirmPassword = memberParam.getConfirmPassword();
        String phone = memberParam.getPhone();
        String mail = memberParam.getMail();
        String nickName = memberParam.getNickName();
        String code = memberParam.getCode();
        //后台验证字段是否位空
        if (StringUtils.isEmpty(code) || StringUtils.isEmpty(confirmPassword)
                                        || StringUtils.isEmpty(memberName)
                                        || StringUtils.isEmpty(password)
                                        || StringUtils.isEmpty(phone)
                                        || StringUtils.isEmpty(mail)
                                        || StringUtils.isEmpty(nickName))
            return ServerResponse.error(ResponseEnum.DATA_IS_INCOMPLETE);
        //验证两次密码是否一致
        if (!confirmPassword.equals(password)){
            ServerResponse.error(ResponseEnum.DATA_PASSWORD_IS_INCOMPLETE);
        }
        //判断短信验证码是否一致
//        String result = RedisUtil.get(phone);
//        if (StringUtils.isEmpty(result) || !result.equals(code) ){
//            return ServerResponse.error(ResponseEnum.DATA_SMS_IS_INCOMPLETE);
//        }
        //账号重复
        QueryWrapper<Member> memberQueryWrapper = new QueryWrapper<>();
            memberQueryWrapper.eq("memberName",memberName);
        Member member = memberMapper.selectOne(memberQueryWrapper);
        if (member!=null){
            return ServerResponse.error(ResponseEnum.DATA_MEMBER_IS_INCOMPLETE);
        }
        //添加会员
        Member memberInfo = new Member();
        memberInfo.setMail(mail);
        memberInfo.setPhone(phone);
        memberInfo.setMemberName(memberName);
        memberInfo.setPassword(Md5Util.md5(password));
        memberInfo.setNickName(nickName);
        memberInfo.setState("2");
        memberMapper.insert(memberInfo);
        //生成一个uuid随机数取代我们的id
        String uuid = UUID.randomUUID().toString();
        String key = KeyUtil.buildActiveMemberKey(uuid);
        Long id = memberInfo.getId();
        RedisUtil.setEx(key,id+"",5*60);
        //发送激活邮件 (同步发送)
      //  mailUtil.sendMailHtml(mail,"激活验证",KeyUtil.activateMailUrl(uuid));
      //异步发送邮件
        MailMessage mailMessage = new MailMessage();
        mailMessage.setTo(mail);
        mailMessage.setTitle("激活验证");
        mailMessage.setConect(KeyUtil.activateMailUrl(uuid));
        mailProducer.sendMail(mailMessage);

        //删除缓存
        RedisUtil.delete(phone);


        return ServerResponse.success();
    }

    @Override
    public ServerResponse login(String memberName, String password,String state) {
        //非空验证
        if (StringUtils.isEmpty(memberName) || StringUtils.isEmpty(password)){
            return ServerResponse.error(ResponseEnum.DATA_IS_INCOMPLETE);
        }
        //验证账号是否存在
        QueryWrapper<Member> memberQueryWrapper = new QueryWrapper<>();
        memberQueryWrapper.eq("memberName",memberName);
        Member member = memberMapper.selectOne(memberQueryWrapper);
        if (member ==null ){
            return ServerResponse.error(ResponseEnum.DATA_MEMBER_IS_INCOMPLETE);
        }
        //验证密码是否正确
        String pass = Md5Util.md5(password);
        if (!pass.equals(member.getPassword())){
           return ServerResponse.error(ResponseEnum.DATA_PASSWORD_IS_INCOMPLETE);
        }

        String state1 = member.getState();
        if (state1.equals("2")){
            String mail = member.getMail();
            String uuid = UUID.randomUUID().toString();
            RedisUtil.setEx(KeyUtil.buildActiveMemberKey(uuid),member.getId()+"",5*60);
            Map<String,String>result=new HashMap<>();
            result.put("mail",mail);
            result.put("uuid",uuid);

            return ServerResponse.error(ResponseEnum.State_IS_NOT,result);
        }
        //把用户信息转换成json格式的字符串
        MemberVO memberVO = new MemberVO();
        Long id = member.getId();
        memberVO.setId(member.getId());
        memberVO.setMail(member.getMail());
        memberVO.setMemberName(member.getMemberName());
        memberVO.setPhone(member.getPhone());
        memberVO.setNickName(member.getNickName());
        String memberJson = JSON.toJSONString(memberVO);
        //生成签名
        String sing = Md5Util.md5(memberJson + Constans.SECRET);
        String memberVOBase64 = Base64.getEncoder().encodeToString(memberJson.getBytes());
        String singBase64 = Base64.getEncoder().encodeToString(sing.getBytes());
        //将信息存在redis中
        RedisUtil.setEx(KeyUtil.buildMemberKey(id),"",Constans.TOKEN_EXPIRE);
        return ServerResponse.success(memberVOBase64+"."+singBase64);
    }

    @Override
    public ServerResponse checkName(String memberName) {
        if (StringUtils.isEmpty(memberName)){
            return ServerResponse.error(ResponseEnum.USERNAME_IS_EMPTY);
        }
        QueryWrapper<Member> memberQueryWrapper = new QueryWrapper<>();
        memberQueryWrapper.eq("memberName",memberName);
        Member member = memberMapper.selectOne(memberQueryWrapper);
        if (member != null){
            return ServerResponse.error(ResponseEnum.MEMBER_IS_EXIST);
        }
        return ServerResponse.success();
    }

    @Override
    public ServerResponse checkEmail(String mail) {
        if (StringUtils.isEmpty(mail)){
            return ServerResponse.error(ResponseEnum.EMAIL_IS_NOT);
        }
        QueryWrapper<Member> memberQueryWrapper = new QueryWrapper<>();
        memberQueryWrapper.eq("mail",mail);
        Member member = memberMapper.selectOne(memberQueryWrapper);
        if (member != null){
            return ServerResponse.error(ResponseEnum.EMAIL_IS_EXIST);
        }
        return ServerResponse.success();
    }
    @Override
    public ServerResponse checkPhone(String phone) {
        if (StringUtils.isEmpty(phone)){
            return ServerResponse.error(ResponseEnum.PHONE_IS_NOT);
        }
        QueryWrapper<Member> memberQueryWrapper = new QueryWrapper<>();
        memberQueryWrapper.eq("phone",phone);
        Member member = memberMapper.selectOne(memberQueryWrapper);
        if (member != null){
            return ServerResponse.error(ResponseEnum.PHONE_IS_XIST);
        }
        return ServerResponse.success();
    }

    @Override
    public ServerResponse verifyName(MemberRequestVO memberRequestVO) {
        String mail = memberRequestVO.getMail();
        String memberName = memberRequestVO.getMemberName();
        if (StringUtils.isEmpty(memberName)){
            return ServerResponse.error(ResponseEnum.USERNAME_IS_EMPTY);
        }
        if (StringUtils.isEmpty(mail)){
            return ServerResponse.error(ResponseEnum.EMAIL_IS_NOT);
        }
        QueryWrapper<Member> memberQueryWrapper = new QueryWrapper<>();
        memberQueryWrapper.eq("mail",mail);
        memberQueryWrapper.eq("memberName",memberName);
        Member member = memberMapper.selectOne(memberQueryWrapper);
        if (member == null){
            return ServerResponse.error(ResponseEnum.EMAIL_MEMBERNAME_IS_NOT);
        }

        return ServerResponse.success();

    }

//    @Override
//    public ServerResponse emailCode(MemberRequestVO memberRequestVO) {
//        String mail = memberRequestVO.getMail();
//        long round = Math.round((Math.random() + 1) * 1000);
//        MailUtil.sendMail(round+"",mail);
//        RedisUtil.setEx("emailCode",round+"",60*5);
//        return ServerResponse.success(memberRequestVO);
//    }

    @Override
    public ServerResponse acquisitionEmailCode(MemberRequestVO memberRequestVO) {
        String memberName = memberRequestVO.getMemberName();
        String mail = memberRequestVO.getMail();
        String code = memberRequestVO.getCode();
        String newPassword = memberRequestVO.getNewPassword();
        String emailCode = RedisUtil.get("emailCode");
        if (!emailCode.equals(code)){
            return ServerResponse.error(ResponseEnum.EMAILCODE_IS_NOT);
        }

        QueryWrapper<Member> memberRequestVOQueryWrapper = new QueryWrapper<>();
        memberRequestVOQueryWrapper.eq("memberName",memberName);
        memberRequestVOQueryWrapper.eq("mail",mail);
        Member member = memberMapper.selectOne(memberRequestVOQueryWrapper);

        if (member ==null){
            return ServerResponse.error(ResponseEnum.EMAIL_MEMBERNAME_IS_NOT);
        }
        if (newPassword ==null){
            return ServerResponse.error(ResponseEnum.DATA_PASSWORD_IS_INCOMPLETE);
        }
        member.setPassword(newPassword);
        memberMapper.updateById(member);
        return ServerResponse.success();
    }

    @Override
    public ServerResponse findPassword(String mail, String code,String imgKey) {
        //非空判断
        if (StringUtils.isEmpty(mail) || StringUtils.isEmpty(code) || StringUtils.isEmpty(imgKey)){
            return ServerResponse.error(ResponseEnum.MEMBER_IS_NOT);
        }
        //优先验证验证码
        String imgCode = RedisUtil.get(KeyUtil.buildImageCodeKey(imgKey));
        if (!code.equals(imgCode)){
            return ServerResponse.error(ResponseEnum.CODE_IS_NOT);
        }
        //验证邮箱
        QueryWrapper<Member> memberQueryWrapper = new QueryWrapper<>();
        memberQueryWrapper.eq("mail",mail);
        Member member = memberMapper.selectOne(memberQueryWrapper);
        if (member == null){
            return ServerResponse.error(ResponseEnum.EMAIL_IS_NO_EXIST);
        }
        //发邮件
        String newPassword = RandomStringUtils.randomAlphabetic(6);
        Member member1 = new Member();
        member1.setId(member.getId());
        member1.setPassword(Md5Util.md5(newPassword));
        memberMapper.updateById(member1);
        //同步发送邮件
        //  mailUtil.sendMailHtml(mail,"找回密码","密码重置为"+newPassword+",请及时修改");
        MailMessage mailMessage = new MailMessage();
        mailMessage.setTo(mail);
        mailMessage.setTitle("找回密码");
        mailMessage.setConect("密码重置为"+newPassword+",请及时修改");
        mailProducer.sendMailMessage(mailMessage);
        //清除redis
        RedisUtil.delete(KeyUtil.buildImageCodeKey(imgKey));
        return ServerResponse.success();
    }

    //修改密码
    @Override
    public ServerResponse updatePassword(UpdatePasswordParam updatePasswordParam) {
        String password = updatePasswordParam.getPassword();
        String newPassword = updatePasswordParam.getNewPassword();
        String confirmNewPassword = updatePasswordParam.getConfirmNewPassword();
        //密码的非空验证
        if (StringUtils.isEmpty(password) || StringUtils.isEmpty(newPassword) ||StringUtils.isEmpty(confirmNewPassword)){
            return ServerResponse.error(ResponseEnum.PASSWORD_IS_EMPTY);
        }
//        新密码的一致性验证
        if (!newPassword.equals(confirmNewPassword) ){
            return ServerResponse.error(ResponseEnum.PASSWORD_IS_NOT_SAME);
        }
        Long id = updatePasswordParam.getId();
        Member member = memberMapper.selectById(id);
//        验证是否是老密码
        if (!Md5Util.md5(password).equals(member.getPassword()) ){
            return ServerResponse.error(ResponseEnum.OUT_PASSWORD_IS_EMPT);
        }
        Member member1 = new Member();
        member1.setId(id);
        member1.setPassword(Md5Util.md5(newPassword));
        //更新密码
        memberMapper.updateById(member1);
        //删除redis中的缓存以达到注销的目的
        RedisUtil.delete(KeyUtil.buildMemberKey(id));
        return ServerResponse.success();
    }

    @Override
    public int activate(String id) {
        //判断redis中是否有数据
        String res = RedisUtil.get(KeyUtil.buildActiveMemberKey(id));
        if (StringUtils.isEmpty(res)){
            //返回错误信息
            return Constans.REQUEST_ERROR;
        }
        //更新数据库
        Member member = new Member();
        member.setId(Long.parseLong(res));
        member.setState(Constans.ACTIVE);
        memberMapper.updateById(member);
        //删除redis的缓存
        RedisUtil.delete(KeyUtil.buildActiveMemberKey(id));
        return 0;
    }

    @Override
    public ServerResponse activeMail(String uuid,String mail) {
        //非空判断
        if (StringUtils.isEmpty(uuid) || StringUtils.isEmpty(mail)){
            return ServerResponse.error();
        }
        MailUtil.sendMail("激活验证",KeyUtil.activateMailUrl(uuid),mail);
        return ServerResponse.success();
    }


}
