package com.atguigu.educenter;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = {"com.atguigu"})
@SpringBootApplication
@MapperScan("com.atguigu.educenter.mapper")
public class UcenterMemberApplication {
    public static void main(String[] args) {
        SpringApplication.run(UcenterMemberApplication.class,args);
    }
}
