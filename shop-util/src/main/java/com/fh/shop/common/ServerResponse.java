package com.fh.shop.common;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class ServerResponse implements Serializable {

    private Integer code;

    private String msg;

    private Object data;

    private ServerResponse(){

    }

    private ServerResponse(Integer code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static ServerResponse success(){
        return new ServerResponse(200,"ok",null);
    }

    public static ServerResponse success(Object data){
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
