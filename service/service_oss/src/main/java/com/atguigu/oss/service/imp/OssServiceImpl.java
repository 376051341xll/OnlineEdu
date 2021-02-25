package com.atguigu.oss.service.imp;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.atguigu.oss.service.OssService;
import com.atguigu.oss.utils.ConstantPropertiesUtils;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Service
public class OssServiceImpl implements OssService {
    //上传文件到oss
    @Override
    public String uploadFileAvatar(MultipartFile file) {

        //工具类获取值
        String endpoint = ConstantPropertiesUtils.END_POINT;
        String accessKeyId = ConstantPropertiesUtils.ACCESS_KEY_ID;
        String accessKeySecret = ConstantPropertiesUtils.ACCESS_KEY_SECRET;
        String bucketName=ConstantPropertiesUtils.BUCKET_NAME;

        try{
            //创建oss实例
            OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

            //上传文件输入流
            InputStream inputStream = file.getInputStream();

            //获取文件名称
            String filename=file.getOriginalFilename();
            //1 在文件名称里面添加随机唯一的值
            String uuid=UUID.randomUUID().toString().replaceAll("-","");
            filename=uuid+filename;

            //2 把文件按照日期进行分类
            //2019/11/12/01.jpg
            //获取当前日期
            String datePath=new DateTime().toString("yyyy/MM/dd");

            //拼接
            filename=datePath+"/"+filename;

            //调用oss方法实现上传
            //第一个参数 bucketname
            //第二个参数 上传到oss文件路径和文件名称
            //第三个参数 上传文件输入流
            ossClient.putObject(bucketName, filename, inputStream);

            //关闭oss client
            ossClient.shutdown();

            //把上传之后文件路径返回
            //需要把上传到阿里云oss路径收订拼接出来
            String url="https://"+bucketName+"."+endpoint+"/"+filename;
            return url;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
