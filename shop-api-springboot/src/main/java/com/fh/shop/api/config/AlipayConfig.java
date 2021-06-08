package com.fh.shop.api.config;


import java.io.FileWriter;
import java.io.IOException;

/* *
 *类名：AlipayConfig
 *功能：基础配置类
 *详细：设置帐户有关信息及返回路径
 *修改日期：2017-04-05
 *说明：
 *以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 *该代码仅供学习和研究支付宝接口使用，只是提供一个参考。
 */

public class AlipayConfig {

//↓↓↓↓↓↓↓↓↓↓请在这里配置您的基本信息↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

    // 应用ID,您的APPID，收款账号既是您的APPID对应支付宝账号
    public static String app_id = "2021000117659260";

    // 商户私钥，您的PKCS8格式RSA2私钥
    public static String merchant_private_key = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQClJ+8qj8Iq/w4bJsSCgge0oFlk6rwDsgaJ6l/mV+siBvOxLPWuTgKISzOTNze5X/SouV9c3K/6nd815z9JvRc8rVQNAKmuUeZ5tGq2LlwX2F3x5osshghwl1TKC/zhvJm2mfub/o8Weo9PLvw7igkOpQounKx8YGLX/mH/Px5zuK8zuy7zf2Edfcf3GJ6lChnUNYbkYOG3JJhFEZtpBYNbs7ZV5ykmHxIkkYew3wpdxjbqPOvcElaU8q+aK9Kd78e0ihxtiVU5vj46h60iojF0AADr6h9WnYl7NXAl13pEI3B01Bf7gTQTp2t0Er2T06rG6UwQwWqGVC3bTuaog493AgMBAAECggEABbusJrsUz29JKxIecjVXVSLX15P6cNpmNbVoHNPieqzpGrZn1OQqXFbX9H1PNuUo1g6RS+TxSu7eB73+SW4B935A0665NS7spD7qEWbslXtST4pkPGhutJHcRKND5QYqA9e2j0S37NzWSrZj1SFGjTaXcuTHbA3ggIT5G+YyBWxOdRuEOC0vwkttGMHahG1unq6f4rp1Tkl9hKcPq0ztWPkdngIBGasmw+Kl9m48WUOyryq7LkNp6+17FyXmS7udsimXN/dRLkaC0i9c/8NpRbbASYurTT19cio7Vu3Nk+RJ44b/PDYwmUZoeOqLiiSEF4UC0UbwF0wVFYB8BpX3cQKBgQDk282I9kFRPUDgbpd5w5art4AOnYqzf6HYQgOdfEHE58ttKnhxfxKnpHxMWFr9G7jj1jUUywUv6ZpJ7e5aRP71JOTYJOvE1oB7WofcDG9nMrq0mn1bgGIJzh8pm+5x8o+3e+WxSBJTgL0aP1sk6SaWvtw37J+QdykJIGKMQ10MzwKBgQC4vhsva3lCO+1NsAHYN8PhxwxG8zCmFlaMdVE/Qzss2nr0iPxDyTqA45+LkahPevxWsb9VUrNVVG3GfPrNzBD1BPq+pqDPj7lrBnaDNBWkNOPZZOWi/TC7PfI1wQhe4vpCgG1b3/I318gGVrLRpAfcjSjVxLyJICQC5/uY89YM2QKBgCvTQ6rErYFnH3Na+jrhD4tWmnkyBguVl2KYUaTo1JdmY/hFt8bUNLhRsVPVX6c0ldVbgdK3hgjEzTz3CTvUFpbPN9uERnntMbFthrLG1taGSmanZvYMwblq9WC8cY70ijzyxuuKB6hh8NSD6WpKE4+ugPFcqLm7d+o9f2GozLrVAoGAL3kisT/T7DJMG7+T6cUCwB0PSZgqszNgUD3BgBk9tpv6Jqag9KZcM0zK7emjL7Fx8LFCcqGK1e2ZO/1X0dSTsvLxoFZfUpcjVZ5WjAaxXP3+YxnrbMPVZvhXKnsNNJztJHOuJoLY0oLPstlKoFafcoyPpsckj7RDrL+CRt657ZkCgYEAkW4+ysSApPqnsU1WDo9DexYBC/NpTDkTV6WdyMNFP6xnRktvSQvvSM3jL4+TiwZYccAdY3eaie+kQS+LimCFs+h+wLa10P+TBFKWYXQXyP+ONDjvU8P36PiaiPKSNOLKJwCSYes3Lz/WrLtmlh5Y6hqX2sYQIcv/C6U2ynGsALg=";
    public static String alipay_public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA06uvaqci9jL66w5tD0agS8m4+/8E7Pr52ior4GST+h/7V+aGDXVE6LVQbdlQSYMczgAE0/uqJyqp4+Fwpf9wdyn6mBnPZADhpCnqPnHv91RXqAHQ/45F9R3Y7T0zFPcBthp96hC25qQSZy98uUdR9BbisYDDAMAvk18nOFocJoqRmlALB2aGwRUesiC1D3ME9015iZeR5PYMG056hXyn6sewOMRXY/1agkDdUG/8X1FMq3J4weeJ9V2Ag7s7g0n7inQxchQSxY308Q5gpwpkDqE35R3XEfLzTZ4rcBRTuabJvT7IFnis4JtgTrpO8Ygq4Kz8GicUC6bNazX2gdtW2QIDAQAB";
    // 服务器异步通知页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public static String notify_url = " http://3ffq83.natappfree.cc/api/pay/aliNotify";

    // 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public static String return_url = " http://3ffq83.natappfree.cc/api/pay/aliReturn";

    // 签名方式
    public static String sign_type = "RSA2";

    // 字符编码格式
    public static String charset = "utf-8";

    // 支付宝网关
    public static String gatewayUrl = "https://openapi.alipaydev.com/gateway.do";

    // 支付宝网关
    public static String log_path = "C:\\";


//↑↑↑↑↑↑↑↑↑↑请在这里配置您的基本信息↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

    /**
     * 写日志，方便测试（看网站需求，也可以改成把记录存入数据库）
     * @param sWord 要写入日志里的文本内容
     */
    public static void logResult(String sWord) {
        FileWriter writer = null;
        try {
            writer = new FileWriter(log_path + "alipay_log_" + System.currentTimeMillis()+".txt");
            writer.write(sWord);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

