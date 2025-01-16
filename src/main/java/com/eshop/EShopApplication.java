package com.eshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.eshop")
public class EShopApplication {
    public static void main(String[] args) {
        SpringApplication.run(EShopApplication.class, args);
    }
}