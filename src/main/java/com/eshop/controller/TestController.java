package com.eshop.controller;

import com.eshop.entity.Constant;
import jakarta.servlet.http.HttpSession;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Profile("test")
@RestController
public class TestController {
    @PostMapping("/test/setCaptcha")
    public void setCaptcha(HttpSession session, String captcha) {
        session.setAttribute(Constant.CAPTCHA_KEY_EMAIL, captcha);
    }
}
