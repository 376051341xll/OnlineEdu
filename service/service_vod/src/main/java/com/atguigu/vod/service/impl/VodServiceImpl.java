package com.atguigu.vod.service.impl;

import com.aliyun.vod.upload.impl.UploadVideoImpl;
import com.aliyun.vod.upload.req.UploadFileStreamRequest;
import com.aliyun.vod.upload.resp.UploadFileStreamResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.vod.model.v20170321.DeleteVideoRequest;
import com.atguigu.commonutils.R;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.atguigu.vod.Utils.ConstantVodUtils;
import com.atguigu.vod.Utils.InitVodClient;
import com.atguigu.vod.service.VodService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class VodServiceImpl implements VodService {

    //上传视频
    @Override
    public String uploadVideoAly(MultipartFile file) {
        try{

            //filename 上传文件的原始名称
            //01.mp4
            String fileName=file.getOriginalFilename();

            //inputstream 上传文件的输入流
            InputStream inputStream=file.getInputStream();
            //title 上传之后显示名称
            String title=fileName.substring(0,fileName.lastIndexOf("."));

            UploadFileStreamRequest request = new UploadFileStreamRequest(ConstantVodUtils.ACCESS_KEY_ID, ConstantVodUtils.ACCESS_KEY_SECRET, title, fileName);

            UploadVideoImpl uploader = new UploadVideoImpl();
            UploadFileStreamResponse response = uploader.uploadFileStream(request);

            String videoId=null;
            if (response.isSuccess()) {
                videoId=response.getVideoId();
            } else {/* 如果设置回调URL无效，不影响视频上传，可以返回VideoId同时会返回错误码。其他情况上传失败时，VideoId为空，此时需要根据返回错误码分析具体错误原因 */
                videoId=response.getVideoId();
            }
            return videoId;

        }catch(Exception e){
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public void removeMoreAlyVideo(List<String> videoIdList) {
        try {
            //初始化对象
            DefaultAcsClient client= InitVodClient.initVodClient(ConstantVodUtils.ACCESS_KEY_ID,ConstantVodUtils.ACCESS_KEY_SECRET);
            //创建删除视频request对象
            DeleteVideoRequest request=new DeleteVideoRequest();

            //videoList值转换成1,2,3
            String videoIds = StringUtils.join(videoIdList.toArray(), ",");

            //想request设置视频id
            request.setVideoIds(videoIds);
            //调用初始化对象的方法实现删除
            client.getAcsResponse(request);
        }catch(Exception e){
            e.printStackTrace();
            throw new GuliException(20001,"删除视频失败");
        }
    }

}
