package com.atguigu.eduservice.service.impl;

import com.alibaba.excel.EasyExcel;
import com.atguigu.eduservice.entity.EduSubject;
import com.atguigu.eduservice.entity.excel.SubjectData;
import com.atguigu.eduservice.entity.subject.OneSubject;
import com.atguigu.eduservice.entity.subject.TwoSubject;
import com.atguigu.eduservice.listener.SubjectExcelListener;
import com.atguigu.eduservice.mapper.EduSubjectMapper;
import com.atguigu.eduservice.service.EduSubjectService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.ibatis.annotations.One;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程科目 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-12-27
 */
@Service
public class EduSubjectServiceImpl extends ServiceImpl<EduSubjectMapper, EduSubject> implements EduSubjectService {


    //添加课程分类
    @Override
    public void saveSubject(MultipartFile file, EduSubjectService subjectService) {
        try {
            //文件输入流
            InputStream in = file.getInputStream();
            //调用方法进行读取
            EasyExcel.read(in, SubjectData.class, new SubjectExcelListener(subjectService)).sheet().doRead();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    //课程分类列表（树形）
    @Override
    public List<OneSubject> getAllOneTwoSubject() {
        //1 查询一级分类 parentid=0
        QueryWrapper<EduSubject> wrapperone=new QueryWrapper<>();
        wrapperone.eq("parent_id",0);
        List<EduSubject> oneSubjectList = baseMapper.selectList(wrapperone);


        //2 查询二级分类 parent！=0
        QueryWrapper<EduSubject> wrappertwo=new QueryWrapper<>();
        wrapperone.ne("parent_id",0);
        List<EduSubject> twoSubjectList = baseMapper.selectList(wrappertwo);

        //创建list集合，用于存储最终封装数据
        List<OneSubject> finalSubjectList=new ArrayList<>();

        //3 封装一级分类
        //查询出来所有一级分类list集合遍历，得到每个一级分类对象，获取每个一级分类对象值
        //封装到要求的list集合里面
        for (int i=0; i<oneSubjectList.size();i++) {
            //得到oneSubjectList中每个edusubject对象
            EduSubject eduSubject=oneSubjectList.get(i);

            //把edusubject里面的值获取出来，放进onesubject对象里
            //多个onesubject放进finalsubject中
            OneSubject oneSubject=new OneSubject();
//            oneSubject.setId(eduSubject.getId());
//            oneSubject.setTitle(eduSubject.getTitle());
            //edusubject的值复制到onesubject中--找到对应的值进行封装
            BeanUtils.copyProperties(eduSubject,oneSubject);


            finalSubjectList.add(oneSubject);

            //在一级分类循环里遍历查询所有二级分类
            //创建一个list集合封装每个一级分类的二级分类
            List<TwoSubject> twoFinalSubjectList= new ArrayList<>();
            //遍历二级分类list集合
            for(int m=0; m<twoSubjectList.size();m++){
                EduSubject tSubject = twoSubjectList.get(m);
                //判断二级分类parentid级和一级分类id一样
                if(tSubject.getParentId().equals(oneSubject.getId())){
                    TwoSubject twoSubject=new TwoSubject();
                    BeanUtils.copyProperties(tSubject,twoSubject);
                    twoFinalSubjectList.add(twoSubject);
                }
            }

            //把一级下面的二级分类都放进一级分类里
            oneSubject.setChildren(twoFinalSubjectList);


        }


        //4 封装二级分类

        return finalSubjectList;
    }
}
