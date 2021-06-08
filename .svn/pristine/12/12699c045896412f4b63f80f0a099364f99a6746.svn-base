package com.fh.shop.admin.exception;

import com.fh.shop.common.ServerResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/***
 * 异常的统一处理
 */
@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ServerResponse handlerException(Exception e){
        e.printStackTrace();
        return ServerResponse.error();
    }

}
