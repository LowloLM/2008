package com.fh.shop.common;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class ServerResponse<T> implements Serializable {

    private Integer code;

    private String msg;

    private T data;

    private ServerResponse(){

    }

    private ServerResponse(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static ServerResponse success(){
        return new ServerResponse(200,"ok",null);
    }

    public static <T>ServerResponse<T> success(T data){
        return new ServerResponse(200,"ok",data);
    }

    public static ServerResponse error(){
        return new ServerResponse(-1,"error",null);
    }

    public static ServerResponse error(Integer code,String msg){
        return new ServerResponse(code,msg,null);
    }

    public static ServerResponse error(ResponseEnum responseEnum){
        return new ServerResponse(responseEnum.getCode(),responseEnum.getMsg(),null);
    }

    public static ServerResponse error(ResponseEnum responseEnum,Object data){
        return new ServerResponse(responseEnum.getCode(),responseEnum.getMsg(),data);
    }

}
