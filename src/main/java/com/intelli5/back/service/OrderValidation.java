package com.intelli5.back.service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by fhliu on 2016/11/26.
 */
public class OrderValidation {
    public static final int MAX_ORDER = 100;
    private static Map<String, Integer> map = new HashMap<>();

    public static boolean validate(String key) {
        Integer currentVal = 1;
        if (map.containsKey(key)) {
            currentVal = map.get(key);
            if (currentVal >= MAX_ORDER) {
                return true;
            }
            map.replace(key, currentVal, currentVal + 1);
            currentVal++;
        } else {
            map.put(key, currentVal);
        }
        return currentVal > MAX_ORDER;
    }

    public static boolean decrease(String key) {
        Integer currentVal = 0;
        if (map.containsKey(key)) {
            currentVal = map.get(key);
            map.replace(key, currentVal, currentVal - 1);
            currentVal--;
        } else {
            map.put(key, currentVal);
        }
        return currentVal > MAX_ORDER;
    }
}
