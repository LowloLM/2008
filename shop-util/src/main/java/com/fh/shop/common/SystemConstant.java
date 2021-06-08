package com.fh.shop.common;

public final class SystemConstant {

    // 用户对象
    public static final String CURR_USER = "user";

    // 登录页面
    public static final String LOGIN_URL = "/login2.jsp";

    // 图片文件夹
    public static final String UPLOAD_PATH = "/upload/";

    public static final String UPLOAD_FILE_PATH = "d:/upload/";

    // 模板文件夹
    public static final String TEMPlATE_FOLDER = "/template";

    // 模板文件.xml
    public static final String TEMPlATE_FILE_XML = "LogWrodTemplate.xml";

    // 模板文件.html
    public static final String TEMPlATE_FILE_HTML = "LogPdfTemplate.html";

    // 编码格式utf-8
    public static final String CODE_UTF_8 = "utf-8";

    // 导出Word文件的前缀名
    public static final String PREFIX_WORD = "D:/";

    // 导出Word文件的后缀名
    public static final String SUFFIX_WORD = ".docx";

    // 日志列表
    public static final String LOG_LIST = "日志列表";

    public static final String UTF_8="utf-8";

    public static final String LOGS="logs";


    public static final String MAIL_HOST = "smtp.qq.com";

    //配置发件人的邮箱地址
    public static final String MAIL_FROM = "756244460@qq.com";

    //配置发件人的邮箱密码
    public static final String MAIL_PASSWORD = "uailstkrvhzhbfdd";

    public interface ORDER_STATUS{
        int WATT_PAY=0;
        int PAY_SUCCESS=10;
        int TRADE_CLOSE =40;
    }

    public interface MESSAGE_LOG_STATUS{
        int SENDING=0;//投递中
        int SEND_SUCCESS=1;//成功
        int SEND_FALL=2;//失败
        int CONSUME_SUCCESS=3;//消费成功
        int EXCHANGE_QUEUE_FAIL=4;//交换机到消息队列的失败
    }

    public static final int RETRY_COUNT=3;

}
