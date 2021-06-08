package com.fh.shop.api;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradePagePayModel;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.response.AlipayTradePagePayResponse;
import org.junit.jupiter.api.Test;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl {

    final static String APP_ID = "2021000117659260";
    final static String APP_PRIVATE_KEY = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQClJ+8qj8Iq/w4bJsSCgge0oFlk6rwDsgaJ6l/mV+siBvOxLPWuTgKISzOTNze5X/SouV9c3K/6nd815z9JvRc8rVQNAKmuUeZ5tGq2LlwX2F3x5osshghwl1TKC/zhvJm2mfub/o8Weo9PLvw7igkOpQounKx8YGLX/mH/Px5zuK8zuy7zf2Edfcf3GJ6lChnUNYbkYOG3JJhFEZtpBYNbs7ZV5ykmHxIkkYew3wpdxjbqPOvcElaU8q+aK9Kd78e0ihxtiVU5vj46h60iojF0AADr6h9WnYl7NXAl13pEI3B01Bf7gTQTp2t0Er2T06rG6UwQwWqGVC3bTuaog493AgMBAAECggEABbusJrsUz29JKxIecjVXVSLX15P6cNpmNbVoHNPieqzpGrZn1OQqXFbX9H1PNuUo1g6RS+TxSu7eB73+SW4B935A0665NS7spD7qEWbslXtST4pkPGhutJHcRKND5QYqA9e2j0S37NzWSrZj1SFGjTaXcuTHbA3ggIT5G+YyBWxOdRuEOC0vwkttGMHahG1unq6f4rp1Tkl9hKcPq0ztWPkdngIBGasmw+Kl9m48WUOyryq7LkNp6+17FyXmS7udsimXN/dRLkaC0i9c/8NpRbbASYurTT19cio7Vu3Nk+RJ44b/PDYwmUZoeOqLiiSEF4UC0UbwF0wVFYB8BpX3cQKBgQDk282I9kFRPUDgbpd5w5art4AOnYqzf6HYQgOdfEHE58ttKnhxfxKnpHxMWFr9G7jj1jUUywUv6ZpJ7e5aRP71JOTYJOvE1oB7WofcDG9nMrq0mn1bgGIJzh8pm+5x8o+3e+WxSBJTgL0aP1sk6SaWvtw37J+QdykJIGKMQ10MzwKBgQC4vhsva3lCO+1NsAHYN8PhxwxG8zCmFlaMdVE/Qzss2nr0iPxDyTqA45+LkahPevxWsb9VUrNVVG3GfPrNzBD1BPq+pqDPj7lrBnaDNBWkNOPZZOWi/TC7PfI1wQhe4vpCgG1b3/I318gGVrLRpAfcjSjVxLyJICQC5/uY89YM2QKBgCvTQ6rErYFnH3Na+jrhD4tWmnkyBguVl2KYUaTo1JdmY/hFt8bUNLhRsVPVX6c0ldVbgdK3hgjEzTz3CTvUFpbPN9uERnntMbFthrLG1taGSmanZvYMwblq9WC8cY70ijzyxuuKB6hh8NSD6WpKE4+ugPFcqLm7d+o9f2GozLrVAoGAL3kisT/T7DJMG7+T6cUCwB0PSZgqszNgUD3BgBk9tpv6Jqag9KZcM0zK7emjL7Fx8LFCcqGK1e2ZO/1X0dSTsvLxoFZfUpcjVZ5WjAaxXP3+YxnrbMPVZvhXKnsNNJztJHOuJoLY0oLPstlKoFafcoyPpsckj7RDrL+CRt657ZkCgYEAkW4+ysSApPqnsU1WDo9DexYBC/NpTDkTV6WdyMNFP6xnRktvSQvvSM3jL4+TiwZYccAdY3eaie+kQS+LimCFs+h+wLa10P+TBFKWYXQXyP+ONDjvU8P36PiaiPKSNOLKJwCSYes3Lz/WrLtmlh5Y6hqX2sYQIcv/C6U2ynGsALg=";
    final static String ALIPAY_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEApSfvKo/CKv8OGybEgoIHtKBZZOq8A7IGiepf5lfrIgbzsSz1rk4CiEszkzc3uV/0qLlfXNyv+p3fNec/Sb0XPK1UDQCprlHmebRqti5cF9hd8eaLLIYIcJdUygv84byZtpn7m/6PFnqPTy78O4oJDqUKLpysfGBi1/5h/z8ec7ivM7su839hHX3H9xiepQoZ1DWG5GDhtySYRRGbaQWDW7O2VecpJh8SJJGHsN8KXcY26jzr3BJWlPKvmivSne/HtIocbYlVOb4+OoetIqIxdAAA6+ofVp2JezVwJdd6RCNwdNQX+4E0E6drdBK9k9OqxulMEMFqhlQt207mqIOPdwIDAQAB";

    @Test
    public void pay() {

        //实例化客户端
        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipaydev.com/gateway.do",
                APP_ID,
                APP_PRIVATE_KEY,
                "json",
                "utf-8",
                ALIPAY_PUBLIC_KEY,
                "RSA2");

        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();

        AlipayTradePagePayModel model = new AlipayTradePagePayModel();
        model.setOutTradeNo("20150320010112312323");
        model.setProductCode("FAST_INSTANT_TRADE_PAY");
        model.setSubject("Iphone6 16G");
        model.setTotalAmount("0.01");
        //以上四个为必选内容。ProductCode是写死的，订单号这些可以自己生成
        //model.setTimeoutExpress("30m");
        request.setBizModel(model);

        request.setReturnUrl("https://xxx");//据说是支付成功后返回的页面
        request.setNotifyUrl("https://xxx");//回调的页面，可以用来执行支付成功后的接口调用什么的
        // 这两个据说都得是外网能访问的，不是localhost那种，而且不能传参
        try {
            AlipayTradePagePayResponse response = alipayClient.pageExecute(request);
            System.out.println(response.getBody());//输出的就是Form表单
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
    }
}
