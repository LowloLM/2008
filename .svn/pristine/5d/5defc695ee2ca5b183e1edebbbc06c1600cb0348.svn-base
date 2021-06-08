package com.fh.shop.admin.interceptor;

import com.fh.shop.common.SystemConstant;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/***
 * 拦截器
 */
public class LoginInterceptor extends HandlerInterceptorAdapter {

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        //获取session的用户信息
        Object user = request.getSession().getAttribute(SystemConstant.CURR_USER);
        request.getSession().getAttribute("loginTime");
        request.getSession().getAttribute("loginCount");
        if(user == null){
            //获取头信息
            String header = request.getHeader("X-Requested-With");
            if(StringUtils.isNotEmpty(header) && header.equalsIgnoreCase("XMLHttpRequest")){
                //如果是ajax请求，服务端向客户端响应一个特殊的头信息【自定义的头信息】
                response.addHeader("Fh-Timeout","sessionTimeout");
            } else {
                //如果用户信息为空则证明用户未登录，跳转到登录页面
                response.sendRedirect(SystemConstant.LOGIN_URL);
            }
            return false;
        } else {
            //否则证明用户成功登录，则放行
            return true;
        }
    }

}
