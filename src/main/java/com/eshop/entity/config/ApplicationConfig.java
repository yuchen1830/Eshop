package com.eshop.entity.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component("applicationConfig")
@Getter
public class ApplicationConfig {

    @Value("${spring.mail.username}")
    private String sendUserName;

    private final String registerEmailSubject = "Email Verification";
    private final String registerEmailContent = "Your verification code is: %s, it will expire in 15 minutes";

}
