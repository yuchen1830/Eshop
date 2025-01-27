package com.eshop.entity.config;

import com.eshop.entity.Constant;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SessionConfig implements WebMvcConfigurer {

    @Autowired
    private SessionInterceptor sessionInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(sessionInterceptor)
                .addPathPatterns("/users/sendEmailCode");
    }

    @Component
    public static class SessionInterceptor implements HandlerInterceptor {

        @Value("${app.testing.default-captcha.enabled:true}")
        private boolean defaultCaptchaEnabled = true;

        @Override
        public boolean preHandle(@NonNull HttpServletRequest request,
                                 @NonNull HttpServletResponse response,
                                 @NonNull Object handler) {
            if (defaultCaptchaEnabled) {
                HttpSession session = request.getSession();
                String captcha = (String) session.getAttribute(Constant.CAPTCHA_KEY_EMAIL);
                if (captcha == null) {
                    session.setAttribute(Constant.CAPTCHA_KEY_EMAIL, "ABC1");
                    System.out.println("Setting default CAPTCHA: ABC1");
                } else {
                    System.out.println("Existing CAPTCHA in session: " + captcha);
                }
            }
            return true;
        }
    }
}

//@Configuration
//@Component
//public class SessionConfig implements HandlerInterceptor {
//
//    @Value("${app.testing.default-captcha.enabled:true}")
//    private boolean defaultCaptchaEnabled = true;
//
//
//    @Override
//    public boolean preHandle(@NonNull HttpServletRequest request,
//                             @NonNull HttpServletResponse response,
//                             @NonNull Object handler) {
//        if (defaultCaptchaEnabled) {
//            HttpSession session = request.getSession();
//            String captcha = (String) session.getAttribute(Constant.CAPTCHA_KEY_EMAIL);
//            if (captcha == null) {
//                session.setAttribute(Constant.CAPTCHA_KEY_EMAIL, "ABC1");
//                System.out.println("Setting default CAPTCHA: ABC1");
//            } else {
//                System.out.println("Existing CAPTCHA in session: " + captcha);
//            }
//        }
//        return true;
//    }
//
//    @Bean
//    public WebMvcConfigurer webMvcConfigurer() {
//        return new WebMvcConfigurer() {
//            @Override
//            public void addInterceptors(InterceptorRegistry registry) {
//                registry.addInterceptor(new SessionConfig())
//                        .addPathPatterns("/users/sendEmailCode");
//            }
//        };
//    }
//}
