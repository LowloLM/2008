package com.fh.shop.api.commone;

import com.alibaba.fastjson.JSON;
import com.fh.shop.common.ResponseEnum;
import com.fh.shop.common.ServerResponse;
import com.fh.shop.util.RedisUtil;
import com.fh.shop.util.SMSUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping(value = "/api")
public class SMSController {

    @PostMapping("/member/sendCode")
    public ServerResponse sendCode(String phone){
        //手机号的非空验证
        if (StringUtils.isEmpty(phone)){
            return ServerResponse.error(ResponseEnum.SMS_IS_NOT);
        }
        //手机号格式是否正确

        //调用工具类发送短信验证码
        String result = SMSUtil.sms(phone);
        //将json格式字符串转换成对象
        Map map = JSON.parseObject(result, Map.class);
        int code =(int)map.get("code");
        if (code!=200){
            return ServerResponse.error(ResponseEnum.SMS_CODE_IS_ERROR);
        }
        String smsCode  =(String) map.get("obj");

        //获取验证码存入redis{key:value  手机号：验证吗}
        RedisUtil.setEx(phone,smsCode,60*5);
        return ServerResponse.success();
    }

}
