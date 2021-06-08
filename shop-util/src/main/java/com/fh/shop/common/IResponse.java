package com.fh.shop.common;

import lombok.Getter;

@Getter
public class IResponse {

    private int responseCode;
    private String responseMsg;
    private Object data;

    private IResponse(){

    }


    private IResponse(int responseCode, String responseMsg, Object data) {
        this.responseCode = responseCode;
        this.responseMsg = responseMsg;
        this.data = data;
    }

    public static IResponse success(){
        return new IResponse(200,"ok",null);
    }

    public static IResponse error(){
        return new IResponse(100,"失败",null);
    }

    public static IResponse success(Object data){
        return new IResponse(200,"ok",data);
    }

    public static IResponse error(int responseCode,String responseMsg ){
        return new IResponse(responseCode,responseMsg,null);
    }




}
