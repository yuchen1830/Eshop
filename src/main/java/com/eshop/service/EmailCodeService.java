package com.eshop.service;

public interface EmailCodeService {
    void disableEmailCode(String email);
    void sendEmailCode(String email, Integer status);
    void verifyEmailCode(String mail, String code);
}
