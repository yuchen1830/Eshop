package com.eshop.utils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
public class PasswordUtils {
    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    public static String encodePassword(String rawPass) {
        if(rawPass == null || rawPass.isEmpty()) {
            return null;
        }
        return passwordEncoder.encode(rawPass);
    }

    public static boolean matches(String rawPass, String encodedPass) {
        if(rawPass == null || encodedPass == null) {
            return false;
        }
        return passwordEncoder.matches(rawPass, encodedPass);
    }
}
