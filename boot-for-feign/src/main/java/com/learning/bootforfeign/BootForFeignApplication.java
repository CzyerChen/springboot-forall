package com.learning.bootforfeign;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class BootForFeignApplication {

    public static void main(String[] args) {
        SpringApplication.run(BootForFeignApplication.class, args);
    }

}
