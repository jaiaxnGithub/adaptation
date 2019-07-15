package com.jaiaxn.adaptation;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author: wang.jiaxin
 * @date: 2019年07月08日
 * @description:
 **/
@SpringBootApplication
@MapperScan("com.jaiaxn")
public class AdaptationApplication {
    public static void main(String[] args) {
        SpringApplication.run(AdaptationApplication.class, args);
    }
}
