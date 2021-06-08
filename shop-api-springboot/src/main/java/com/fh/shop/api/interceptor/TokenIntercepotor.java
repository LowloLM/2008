package com.fh.shop.api.interceptor;

import com.alibaba.fastjson.JSON;
import com.fh.shop.api.annotation.Check;
import com.fh.shop.api.annotation.Token;
import com.fh.shop.api.commone.Constans;
import com.fh.shop.api.commone.KeyUtil;
import com.fh.shop.api.exception.ShopException;
import com.fh.shop.api.member.vo.MemberVO;
import com.fh.shop.common.ResponseEnum;
import com.fh.shop.util.Md5Util;
import com.fh.shop.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Base64;

public class TokenIntercepotor extends HandlerInterceptorAdapter  {

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HandlerMethod handlerMethod=(HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        if (!method.isAnnotationPresent(Token.class)){
            return true;
        }
        //获取头信息
        String token = request.getHeader("x-token");
        if (StringUtils.isEmpty(token)){
            throw new ShopException(ResponseEnum.TOKEN_IS_MISS);
        }
        //删除Redis
        Long delete=RedisUtil.delete(KeyUtil.buildTokenKey(token));
        if (delete == 0){
            throw new ShopException(ResponseEnum.TOKEN_IS_ERROR);
        }
        return true;
    }
}
