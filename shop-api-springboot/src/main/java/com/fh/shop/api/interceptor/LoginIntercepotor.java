package com.fh.shop.api.interceptor;

import com.alibaba.fastjson.JSON;
import com.fh.shop.api.annotation.Check;
import com.fh.shop.api.commone.Constans;
import com.fh.shop.api.commone.KeyUtil;
import com.fh.shop.api.exception.ShopException;
import com.fh.shop.api.member.vo.MemberVO;
import com.fh.shop.common.ResponseEnum;
import com.fh.shop.util.Md5Util;
import com.fh.shop.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.protocol.HTTP;
import org.springframework.http.HttpHeaders;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Base64;

public class LoginIntercepotor extends HandlerInterceptorAdapter  {

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        //处理跨域
        response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN,"*");
       //处理自定义头部信息
        response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS,"x-auth,content-type,x-token");
        //处理特殊请求
        response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS,"DELETE,POST,GET,PUT");
        //处理options请求
        //获取请求方式（get post delete put options）
        String requestMethod = request.getMethod();
        if (requestMethod.equalsIgnoreCase("options")){
            //不处理，拦截
            return false;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
       if (!method.isAnnotationPresent(Check.class)){
            //放行
            return true;
        }

        //验证
        //判断是否有头部信息
        String header = request.getHeader("x-auth");
        if (StringUtils.isEmpty(header)){
            throw new ShopException(ResponseEnum.TOKEN_IS_MISS);
        }
        //判断头部信息是否完整
        String[] headerArr = header.split("\\.");
        if (headerArr.length !=2 ){
            throw new ShopException(ResponseEnum.TOKEN_IS_NOT_FULL);
        }

        //验证签名【核心】
        String memberJson = headerArr[0];
        String singBase64= headerArr[1];
        //进行base64的解码过程将其转换成字符串

        String member = new String(Base64.getDecoder().decode(memberJson), "utf-8");
        String sing = new String(Base64.getDecoder().decode(singBase64), "utf-8");
        String newSign = Md5Util.sign(member, Constans.SECRET);
        if (!newSign.equals(sing)){
            throw new ShopException(ResponseEnum.TOKEN_IS_FALL);
        }
        //将json转为Java对象
        MemberVO memberVO = JSON.parseObject(member, MemberVO.class);
        Long id = memberVO.getId();
        //判断是否过期
        if (!RedisUtil.exist(KeyUtil.buildMemberKey(id))){
            throw new ShopException(ResponseEnum.PASSWORD_IS_EMPT);
        }
        //续命
        RedisUtil.expire(KeyUtil.buildMemberKey(id),Constans.TOKEN_EXPIRE);
        //将memberVO存入request
        request.setAttribute(Constans.CURR_MEMBER,memberVO);
        //放行
        return true;
    }
}
