package com.fh.shop.api.code;


import com.fh.shop.api.commone.KeyUtil;
import com.fh.shop.common.ServerResponse;
import com.fh.shop.util.RedisUtil;
import com.google.code.kaptcha.impl.DefaultKaptcha;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


@RestController
@RequestMapping("/api")
public class CodeController {

    @Autowired
    private HttpServletRequest request;
    @Autowired
    private DefaultKaptcha defaultKaptcha;
    @Autowired
    private HttpServletResponse response;
    public ServerResponse serverResponse;


    @GetMapping("/code")
    public ServerResponse code(String id){
        //生成随机验证码
        String text = defaultKaptcha.createText();
        //存储
        if (StringUtils.isEmpty(id)){
            id=UUID.randomUUID().toString();
        }
        RedisUtil.setEx(KeyUtil.buildImageCodeKey(id),text,2*60);
        //这是存储在session中的应为是前后台分离所以用不到 request.getSession().setAttribute("code",text);
        //根据文字生成对应图片
        BufferedImage image = defaultKaptcha.createImage(text);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            //将动态生成的图片写入ByteArrayOutputStream
            ImageIO.write(image,"jpg",bos);
            //将ByteArrayOutputStream转换成对应的字节数
            byte[] bytes = bos.toByteArray();
            //将字节数进行base64编码
            String imageBase64 = Base64.getEncoder().encodeToString(bytes);
            Map<String ,String>result=new HashMap<>();
            result.put("id",id);
            result.put("imageBase64",imageBase64);

            return ServerResponse.success(result);
        } catch (IOException e) {
            e.printStackTrace();
            throw  new RuntimeException(e);
        }
    }


    @GetMapping("/code2")
    public void code2(){
        //生成随机验证码
        String text = defaultKaptcha.createText();
        //存储
        //这是存储在session中的 request.getSession().setAttribute("code",text);

       //根据文字生成对应图片
        BufferedImage image = defaultKaptcha.createImage(text);
        ServletOutputStream outputStream=null;
        try {
            response.setDateHeader("Expires", 0);
            // Set standard HTTP/1.1 no-cache headers.
            response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
            // Set IE extended HTTP/1.1 no-cache headers (use addHeader).
            response.addHeader("Cache-Control", "post-check=0, pre-check=0");
            // Set standard HTTP/1.0 no-cache header.
            response.setHeader("Pragma", "no-cache");
            // return a jpeg
            response.setContentType("image/jpeg");
             outputStream = response.getOutputStream();
            //图片打印到浏览器
            ImageIO.write(image,"jpg",outputStream);
            outputStream.flush();
            System.out.println(text);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            if (null!=outputStream){
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


    }
}
