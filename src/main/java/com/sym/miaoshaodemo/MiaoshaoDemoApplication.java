package com.sym.miaoshaodemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

@SpringBootApplication
public class MiaoshaoDemoApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(MiaoshaoDemoApplication.class, args);
    }

    //spring 支持war包部署
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(MiaoshaoDemoApplication.class);
    }
}
