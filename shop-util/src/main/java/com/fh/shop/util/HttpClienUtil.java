package com.fh.shop.util;

import org.apache.http.HttpEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HttpClienUtil {

    public static String sendPost(String url, Map<String,String> headers,Map<String ,String> params){
        CloseableHttpClient client = HttpClientBuilder.create().build();
        HttpPost httpPost = new HttpPost(url);
        CloseableHttpResponse response=null;
        try {
            if (headers !=null && headers.size()>0){
                // 添加头部薪资
                headers.forEach((x,y) -> httpPost.addHeader(x,y));
            }
            if (params !=null && params.size()>0){
                List<BasicNameValuePair>pairs=new ArrayList<>();
                params.forEach((x,y) -> pairs.add(new BasicNameValuePair(x,y)));
                UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(pairs, "utf-8");
                httpPost.setEntity(urlEncodedFormEntity);
            }
            response=client.execute(httpPost);
            HttpEntity entity = response.getEntity();
            String res = EntityUtils.toString(entity, "utf-8");
            System.out.println(res);
            return res;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            try {

                if (response!=null){
                    response.close();
                }
                if (httpPost!=null){
                    httpPost.releaseConnection();
                }
                if (client !=null){
                    client.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
