package com.yf;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan("com.yf.mapper")
public class YfApplication {

    public static void main(String[] args) {
        SpringApplication.run(YfApplication.class, args);
    }

}