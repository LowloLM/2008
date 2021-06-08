package com.fh.shop;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import java.io.IOException;

public class TestHttpClient {

    @Test
    public  void testHttpClient(){
        // 获得Http客户端(可以理解为:你得先有一个浏览器;注意:实际上HttpClient与浏览器是不一样的
        CloseableHttpClient build = HttpClientBuilder.create().build();
        // 创建Get请求
        HttpGet httpGet = new HttpGet("https://zhuanlan.zhihu.com/p/107112317");
        // 响应模型
        CloseableHttpResponse response=null;
        try {
            response=build.execute(httpGet);
            HttpEntity entity = response.getEntity();

            String s = EntityUtils.toString(entity, "utf-8");
            System.out.println(s);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

            try {
                if (build != null){
                    build.close();
                }
                if (response!=null){
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
