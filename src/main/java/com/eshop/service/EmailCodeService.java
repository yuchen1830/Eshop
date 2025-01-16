package com.eshop.service;

import com.eshop.entity.EmailCode;

public interface EmailCodeService {
    void disableEmailCode(String email);
    void sendEmailCode(String email, Integer status);
}
