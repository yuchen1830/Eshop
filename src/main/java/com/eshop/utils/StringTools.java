package com.eshop.utils;

import org.apache.commons.lang3.RandomStringUtils;

public class StringTools {
    public static String getRandomString(Integer length){
        return RandomStringUtils.random(length, true, true);
    }

    public static String getRandomNumber(Integer length) {
        return RandomStringUtils.random(length, false, true);
    }
}
