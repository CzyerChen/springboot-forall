// 修复包声明，使其与预期包名一致
package com.learning.pulsar;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PulsarDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(PulsarDemoApplication.class, args);
    }

}
