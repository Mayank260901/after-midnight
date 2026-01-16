package com.aftermidnight;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

@SpringBootApplication
@EnableSpringDataWebSupport
public class AfterMidnightApplication {
    public static void main(String[] args) {
        SpringApplication.run(AfterMidnightApplication.class, args);
    }
}
