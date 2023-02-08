package com.example.seckill.utils;


import org.springframework.util.StringUtils;

/**
 * @author Xiaoxuan
 */
public class RegexUtils {
    /**
     * Check if phone number is valid
     * @param phone phone number
     * @return true:valid, false：invalid
     */
    public static boolean isPhoneInvalid(String phone){
        return mismatch(phone, RegexPatterns.PHONE_REGEX);
    }
    /**
     * Check if email is valid
     * @param email email address
     * @return true:valid, false：invalid
     */
    public static boolean isEmailInvalid(String email){
        return mismatch(email, RegexPatterns.EMAIL_REGEX);
    }

    /**
     * Check if verification code is valid
     * @param code verification code
     * @return true:valid, false：invalid
     */
    public static boolean isCodeInvalid(String code){
        return mismatch(code, RegexPatterns.VERIFY_CODE_REGEX);
    }

    /**
     * @param str input string
     * @param regex regex pattern
     * @return boolean true:valid, false：invalid
     */
    private static boolean mismatch(String str, String regex){
        if (!StringUtils.hasLength(str)) {
            return true;
        }
        return !str.matches(regex);
    }
}
