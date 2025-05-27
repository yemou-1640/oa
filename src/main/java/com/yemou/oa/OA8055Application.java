package com.yemou.oa;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.yemou.oa.dao")
public class OA8055Application {
    public static void main(String[] args) {
        SpringApplication.run(OA8055Application.class, args);
    }
}
