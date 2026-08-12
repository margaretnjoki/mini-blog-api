package com.margaretnjoki.mini_blog_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MiniBlogApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(MiniBlogApiApplication.class, args);
    }

}
