package com.eshop.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum VerifyRegexEnums {
    NO("", "No validation"),
    POSITIVE_INTEGER("^[0-9]*[1-9][0-9]*$", "Positive Integer"),
    PASSWORD("^(?=.*\\d)(?=.*[a-zA-Z])[\\da-zA-Z~!@#$%^&*_]{8,18}$", "Must contain 8-18 characters with letters, numbers, and symbols"),
    NUMBERS_AND_LETTERS("^[a-zA-Z0-9]+$", "number, letter");
    private final String regex;
    private final String description;

}
