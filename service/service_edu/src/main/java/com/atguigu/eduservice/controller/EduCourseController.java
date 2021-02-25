package com.atguigu.eduservice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.vo.CourseInfoVo;
import com.atguigu.eduservice.entity.vo.CoursePublishVo;
import com.atguigu.eduservice.entity.vo.CourseQuery;
import com.atguigu.eduservice.service.EduCourseService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2020-12-30
 */
@RestController
@RequestMapping("/eduservice/course")
@CrossOrigin
public class EduCourseController {
    @Autowired
    private EduCourseService courseService;

    //分页显示数据
    //current 当前页
    //limit 每页的记录数
    @GetMapping("pageCourse/{current}/{limit}")
    public R pageListCourse(@PathVariable long current, @PathVariable long limit){
        //创建page对象
        Page<EduCourse> coursePage=new Page<>(current,limit);

        //调用方法做个分页
        //调用方法的时候，底层封装，把分页所有数据封装到coursePage对象中
        courseService.page(coursePage,null);
        long total=coursePage.getTotal();//总记录数
        List<EduCourse> records=coursePage.getRecords();//数据list集合
        return R.ok().data("total",total).data("rows",records);
    }

    //条件查询带分页方法
    @PostMapping("pageCourseCondition/{current}/{limit}")
    public R pageCourseCondition(@PathVariable long current,@PathVariable long limit,
                                 @RequestBody(required = false) CourseQuery courseQuery){
        //创建page对象
        Page<EduCourse> coursePage=new Page<>(current,limit);

        //构建条件
        QueryWrapper<EduCourse> wrapper=new QueryWrapper<>();

        //多条件组合查询
        String title=courseQuery.getTitle();
        String status=courseQuery.getStatus();

        //判断掉件值是否为囧，如果不为空拼接条件
        if(!StringUtils.isEmpty(title)){
            wrapper.like("title",title);
        }
        if(!StringUtils.isEmpty(status)){
            wrapper.like("status",status);
        }
        wrapper.orderByDesc("gmt_create");

        //调用方法实现分页查询
        courseService.page(coursePage,wrapper);
        long total= coursePage.getTotal();
        List<EduCourse> records=coursePage.getRecords();
        return R.ok().data("total",total).data("rows",records);
    }

    //课程列表 基本实现
    @GetMapping
    public R getCourseList(){
        List<EduCourse> list = courseService.list(null);
        return R.ok().data("list",list);
    }

    //添加课程信息基本方法
    @PostMapping("addCourseInfo")
    public R addCourseInfo(@RequestBody CourseInfoVo courseInfoVo){
        //返回添加之后课程id，为了之后添加课程大纲使用
        String id=courseService.saveCourseInfo(courseInfoVo);
        return R.ok().data("courseId",id);
    }

    //根据课程id查询课程的基本信息
    @GetMapping("getCourseInfo/{courseId}")
    public R getCourseInfo(@PathVariable String courseId){
        CourseInfoVo courseInfoVo = courseService.getCourseInfo(courseId);
        return R.ok().data("courseInfoVo",courseInfoVo);
    }

    //修改课程信息
    @PostMapping("updateCourseInfo")
    public R updateCourseInfo(@RequestBody CourseInfoVo courseInfoVo){
        courseService.updateCourseInfo(courseInfoVo);
        return R.ok();
    }

    //根据课程id查询课程确认信息
    @GetMapping("getPublishCourseInfo/{id}")
    public R getPublishCourseInfio(@PathVariable String id){
        CoursePublishVo coursePublishVo=courseService.publishCourseInfo(id);
        return R.ok().data("publishCourse",coursePublishVo);
    }

    //课程最终发布
    //修改课程状态
    @PostMapping("publishCourse/{id}")
    public R publishCourse(@PathVariable String id){
        EduCourse eduCourse=new EduCourse();
        eduCourse.setId(id);
        eduCourse.setStatus("Nomal");//设置课程发布状态
        courseService.updateById(eduCourse);
        return R.ok();
    }

    //课程删除
    @DeleteMapping("{courseId}")
    public R deleteCourse(@PathVariable String courseId){
        courseService.removeCourse(courseId);
        return R.ok();
    }

}

