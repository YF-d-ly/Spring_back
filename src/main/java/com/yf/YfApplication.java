package com.yf;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@MapperScan("com.yf.mapper")
@EnableTransactionManagement
public class YfApplication {

    public static void main(String[] args) {
        SpringApplication.run(YfApplication.class, args);
    }

}