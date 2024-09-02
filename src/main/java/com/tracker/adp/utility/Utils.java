package com.tracker.adp.utility;

import org.apache.commons.lang3.RandomStringUtils;

import java.security.SecureRandom;

public class Utils {

    public static int getRandomNumber(int from, int to){
        SecureRandom random= new SecureRandom();
        return from+ random.nextInt(to-from);
    }

    public static String getRandomString(int length){
        return RandomStringUtils.randomAlphanumeric(length);
    }
}
