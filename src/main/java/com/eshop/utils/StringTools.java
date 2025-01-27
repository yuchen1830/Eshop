package com.eshop.utils;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;

public class StringTools {
    public static String getRandomString(Integer length){
        return RandomStringUtils.random(length, true, true);
    }

    public static String getRandomNumber(Integer length) {
        return RandomStringUtils.random(length, false, true);
    }

    public static boolean isEmpty(String str) {
        if(null == str || str.isEmpty() || "null".equals(str) || "\u0000".equals(str)) {
            return true;
        } else if(str.trim().isEmpty()) {
            return true;
        }
        return false;
    }
    public static String encodeByMd5(String originString) {
        return isEmpty(originString)?null: DigestUtils.md5Hex(originString);
    }
}
