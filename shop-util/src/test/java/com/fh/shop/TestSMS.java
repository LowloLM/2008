package com.fh.shop;

import com.aliyun.oss.model.LiveChannelListing;
import com.fh.shop.util.SHA1Util;
import com.fh.shop.util.SMSUtil;
import org.apache.http.HttpEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TestSMS {
    @Test
    public  void testSMS() throws IOException {


        //打开浏览器
        CloseableHttpClient build = HttpClientBuilder.create().build();
        //创建一个post请求
        HttpPost httpPost = new HttpPost("https://api.netease.im/sms/sendcode.action");
        //创建头部信息
        String uuid = UUID.randomUUID().toString();
        String time = System.currentTimeMillis() + "";
        httpPost.addHeader("AppKey" ,"795358b35360e84a61a1fbecc9059379");
        httpPost.addHeader("Nonce" ,uuid);
        httpPost.addHeader("CurTime" ,time);
        httpPost.addHeader("CheckSum" ,SHA1Util.getCheckSum("384b1f7d902b",uuid,time));
        //传参数
        try {
            List<BasicNameValuePair>basicNameValuePairs=new ArrayList<>();
            basicNameValuePairs.add(new BasicNameValuePair("mobile","13938573758"));
            UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(basicNameValuePairs);
            httpPost.setEntity(urlEncodedFormEntity);
            build.execute(httpPost);
            //打印到控制台
            //获取参数
            HttpEntity entity = httpPost.getEntity();
            String s = EntityUtils.toString(entity);
            System.out.println(s);

            System.out.println(basicNameValuePairs);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } finally {
            try {
                if (build !=null){
                    build.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    @Test
    public void testPhone(){
        SMSUtil.sms("13938573758");
    }
}
