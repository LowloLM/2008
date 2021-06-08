package com.fh.shop;


import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;


public class MailUtil {
    static String MAIL_HOST = "smtp.163.com";
    //配置发件人的邮箱地址
    static String MAIL_FROM = "qeg5526444@163.com";
    //配置发件人的邮箱密码
    static String MAIL_PASSWORD = "YPBDKCGETUFJVUZM";
    /**
     * 发送邮件
     * @param subject	标题
     * @param content	内容
     * @param to		发给谁
     */
    public static void sendMail(String subject, String content, String to) {
        // 通过发件人的账号和密码，连接发送邮件的服务器【登录邮箱】
        Properties prop = new Properties();
        prop.setProperty("mail.host", MAIL_HOST);
        prop.setProperty("mail.transport.protocol", "smtp");
        prop.setProperty("mail.smtp.auth", "true");
        Session session = Session.getInstance(prop);
        Transport ts = null;
        try {
            ts = session.getTransport();
            ts.connect(MAIL_HOST, MAIL_FROM, MAIL_PASSWORD);
            // 写邮件
            MimeMessage message = new MimeMessage(session);
            // 发件人[当前登录人] from
            message.setFrom(new InternetAddress(MAIL_FROM));
            // 收件人 to
            message.setRecipient(Message.RecipientType.TO, new InternetAddress("893209645@qq.com"));
            // 标题 subject
            message.setSubject(subject);
            // 内容
            message.setContent(content, "text/html;charset=UTF-8");
            // 发送按钮
            ts.sendMessage(message, message.getAllRecipients());
        } catch (MessagingException e) {
            e.printStackTrace();
        } finally {
            if (null !=  ts) {
                try {
                    ts.close();
                    ts = null;
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public static void main(String[] args) {
        MailUtil.sendMail("飞狐教育电影管理平台", "密码已找回是：1234请1111111111111111111尽快修改", "38869288@qq.com");

    }

}
