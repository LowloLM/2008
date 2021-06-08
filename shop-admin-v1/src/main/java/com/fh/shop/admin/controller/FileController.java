package com.fh.shop.admin.controller;

import com.fh.shop.common.ServerResponse;
import com.fh.shop.common.SystemConstant;
import com.fh.shop.util.FileUtil;
import com.fh.shop.util.OSSUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/file")
public class FileController extends BaseController{

    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
    public @ResponseBody
    ServerResponse uploadFile(@RequestParam MultipartFile uploadFileInfo, HttpServletRequest request){
        try {
            InputStream inputStream = uploadFileInfo.getInputStream();
            // 原始的文件名
            String originalFilename = uploadFileInfo.getOriginalFilename();
            // 调用工具类完成拷贝以及重命名
            String uploadedFileName = FileUtil.copyFile(inputStream, originalFilename, SystemConstant.UPLOAD_FILE_PATH);
            // 将上传后的路径响应给前台[相对路径 文件夹的名字+文件的名字]
            return ServerResponse.success(SystemConstant.UPLOAD_FILE_PATH + uploadedFileName);
        } catch (IOException e) {
            e.printStackTrace();
            //抛异常
            throw new RuntimeException(e);
        }
    }

    /***
     * 文件(图片)上传
     * @param imageFile
     * @param request
     * @return
     */
    @RequestMapping(value = "/uploadImage", method = RequestMethod.POST)
    public @ResponseBody Map uploadImage(@RequestParam MultipartFile imageFile, HttpServletRequest request) {
        Map result = new HashMap();
        System.out.println(imageFile);
        /**
         * 1.将客户端硬盘上的图片上传到服务器硬盘上
         *      将客户端硬盘上的图片上传到服务器的过程，spring框架已经帮我们搞定了，上传后的图片被存到了一个临时文件夹
         *      咱们做的：将临时文件夹的图片复制到指定的文件夹中，并且重命名
         *      为了能够访问上传后的文件，所以需要将文件放到项目的文件夹中
         * 2.将上传后的路径响应给前台
         */
        try {
            InputStream inputStream = imageFile.getInputStream();
            // 原始的文件名
            String originalFilename = imageFile.getOriginalFilename();
            // 怎么根据相对路径获取绝对路径
            String realPath = getRealPath(SystemConstant.UPLOAD_PATH, request);
            // 调用工具类完成拷贝以及重命名
            String uploadedFileName = FileUtil.copyFile(inputStream, originalFilename, realPath);
            // 将上传后的路径响应给前台[相对路径 文件夹的名字+文件的名字]
            result.put("data", SystemConstant.UPLOAD_PATH +uploadedFileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }


    @RequestMapping(value = "/uploadImages", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse uploadImages(@RequestParam MultipartFile[] imageFiles, HttpServletRequest request){
        try {
            StringBuilder uploadPaths = new StringBuilder();
            for (MultipartFile imageFile : imageFiles) {
                /*InputStream inputStream = imageFile.getInputStream();
                // 原始的文件名
                String originalFilename = imageFile.getOriginalFilename();
                // 怎么根据相对路径获取绝对路径
                String realPath = getRealPath(SystemConstant.UPLOAD_PATH, request);
                // 调用工具类完成拷贝以及重命名
                String uploadedFileName = FileUtil.copyFile(inputStream, originalFilename, realPath);
                // 将上传后的路径响应给前台[相对路径 文件夹的名字+文件的名字]
                uploadPaths.append(",").append(SystemConstant.UPLOAD_PATH+uploadedFileName);*/
                InputStream inputStream = imageFile.getInputStream();
                // 原始的文件名
                String originalFilename = imageFile.getOriginalFilename();
                String upload = OSSUtil.upload(originalFilename, inputStream);
                // 将上传后的路径响应给前台[相对路径 文件夹的名字+文件的名字]
                uploadPaths.append(",").append(upload);
            }
            return ServerResponse.success(uploadPaths.toString());
        } catch (IOException e) {
            //遇到异常的处理方式
            //1.打印异常
            //2.吧异常抛出去
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

}
