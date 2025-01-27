package com.eshop.annotation;

import org.springframework.web.bind.annotation.Mapping;

import java.lang.annotation.*;

@Target({ElementType.METHOD}) // used for method only
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Mapping // for further request
public @interface AccessControl {
    boolean requireLogin() default true;
    boolean requireAdmin() default false;
}
