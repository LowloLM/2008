package test;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.fh.shop.util.DateUtil;
import com.fh.shop.util.FileUtil;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Date;
import java.util.UUID;

public class TestOSS {

    @Test
    public void test1() throws FileNotFoundException {
        // yourEndpoint填写Bucket所在地域对应的Endpoint。以华东1（杭州）为例，Endpoint填写为https://oss-cn-hangzhou.aliyuncs.com。
        String endpoint = "https://oss-cn-beijing.aliyuncs.com";
        // 阿里云账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM用户进行API访问或日常运维，请登录RAM控制台创建RAM用户。
        String accessKeyId = "LTAI5tP1wXvzmDRM68ZTYHsi";
        String accessKeySecret = "88xMyNyizPrmujWaH8JoSNon9pWRVy";
        String bucketName = "xyz-workspaces";
        String url = "https://xyz-workspaces.oss-cn-beijing.aliyuncs.com/";
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        String fileName = "IMG_0641.JPG";
        // 填写本地文件的完整路径。如果未指定本地路径，则默认从示例程序所属项目对应本地路径中上传文件流。
        InputStream inputStream = new FileInputStream("D:/图片/"+fileName);
        // 填写Bucket名称和Object完整路径。Object完整路径中不能包含Bucket名称。
        String uuid = UUID.randomUUID().toString();
        String newFileName = uuid+FileUtil.getSuffix(fileName);
        //把每天存的文件放到一个单独的文件夹中，文件夹名字日期名 如：2020-10-22
        String datePath = DateUtil.date2str(new Date(), DateUtil.Y_M_D);
        String objectName = datePath+"/"+newFileName;
        ossClient.putObject(bucketName, objectName, inputStream);
        System.out.println(url+objectName);
        // 关闭OSSClient。
        ossClient.shutdown();
    }

    @Test
    public void test2(){
        // yourEndpoint填写Bucket所在地域对应的Endpoint。以华东1（杭州）为例，Endpoint填写为https://oss-cn-hangzhou.aliyuncs.com。
        String endpoint = "https://oss-cn-beijing.aliyuncs.com";
        // 阿里云账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM用户进行API访问或日常运维，请登录RAM控制台创建RAM用户。
        String accessKeyId = "LTAI5tP1wXvzmDRM68ZTYHsi";
        String accessKeySecret = "88xMyNyizPrmujWaH8JoSNon9pWRVy";
        String bucketName = "xyz-workspaces";
        String url = "https://xyz-workspaces.oss-cn-beijing.aliyuncs.com/";
        String objectName = "xx/123/tt.jpg";
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        // 删除文件。如需删除文件夹，请将ObjectName设置为对应的文件夹名称。如果文件夹非空，则需要将文件夹下的所有object删除后才能删除该文件夹。
        ossClient.deleteObject(bucketName, objectName);

        // 关闭OSSClient。
        ossClient.shutdown();

    }

}
