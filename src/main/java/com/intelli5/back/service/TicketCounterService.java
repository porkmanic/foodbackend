package com.intelli5.back.service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by fhliu on 2016/11/26.
 */
public class TicketCounterService {
    private static Map<String,Integer> map = new HashMap<>();

    public static Integer increase(String key) {
        Integer currentVal = 1;
        if(map.containsKey(key)) {
            currentVal = map.get(key);
            map.replace(key, currentVal, currentVal + 1);
        } else {
            map.put(key, currentVal);
        }
        return currentVal;
    }
}
