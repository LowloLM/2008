package com.fh.shop.api;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.test.context.junit4.SpringRunner;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TestMail {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${fh.mail.from}")
    private String mailFrom;


    @Test
    public void test(){
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setSubject("头");
        simpleMailMessage.setText("内容");
        simpleMailMessage.setTo("893209645@qq.com");
        simpleMailMessage.setFrom(mailFrom);
        mailSender.send(simpleMailMessage);
    }


    @Test
    public void test2() throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage,true);
        mimeMessageHelper.setFrom(mailFrom);
        mimeMessageHelper.setTo("893209645@qq.com");
        mimeMessageHelper.setSubject("内容");
        mimeMessageHelper.setText("<h1>邮件测试</h1>",true);
        mailSender.send(mimeMessage);
    }

}
