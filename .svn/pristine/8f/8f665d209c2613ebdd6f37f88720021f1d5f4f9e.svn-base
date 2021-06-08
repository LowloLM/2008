package com.fh.shop.common;


import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.PutObjectRequest;

import java.io.File;

public class OssImage {

    public static void upOss(){
        String endpoint = "http://oss-cn-beijing.aliyuncs.com";
        // 阿里云账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM用户进行API访问或日常运维，请登录RAM控制台创建RAM用户。
        String accessKeyId = "LTAI5tP1wXvzmDRM68ZTYHsi";
        String accessKeySecret = "88xMyNyizPrmujWaH8JoSNon9pWRVy";

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        // 创建PutObjectRequest对象。
// 填写Bucket名称、Object完整路径和本地文件的完整路径。Object完整路径中不能包含Bucket名称。
// 如果未指定本地路径，则默认从示例程序所属项目对应本地路径中上传文件。
        PutObjectRequest putObjectRequest = new PutObjectRequest("xyz-workspaces", "IMG_0621.111.JPG", new File("D:\\图片\\IMG_0621.111.JPG"));

        ossClient.putObject(putObjectRequest);
    }

    public static void main(String[] args) {
        upOss();
    }
}
