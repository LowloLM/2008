package com.fh.shop.util;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SMSUtil {
    private static final String URL="https://api.netease.im/sms/sendcode.action";
    private static final String CHECKSUM="384b1f7d902b";
    private static final String APPKEY="795358b35360e84a61a1fbecc9059379";

    public static String sms(String phone) {
        HashMap<String,String> headers = new HashMap<>();
        String uuid = UUID.randomUUID().toString();
        String time = System.currentTimeMillis() + "";
        String checkSum = SHA1Util.getCheckSum(CHECKSUM, uuid, time);
        headers.put("AppKey",APPKEY);
        headers.put("CheckSum",checkSum);
        headers.put("Nonce",uuid);
        headers.put("CurTime",time);
        Map<String,String>params=new HashMap<>();
        params.put("mobile",phone);
        String res = HttpClienUtil.sendPost(URL, headers, params);
        return res;
    }
}
