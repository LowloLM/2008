package com.fh.shop.api.exception;

import com.fh.shop.common.ResponseEnum;
import com.fh.shop.common.ServerResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class WebExceptionHandler {

    @ExceptionHandler(ShopException.class)
    @ResponseBody
    public ServerResponse handleShopException(ShopException ex){
        ResponseEnum responseEnum = ex.getResponseEnum();
        return ServerResponse.error(responseEnum);
    }
}
