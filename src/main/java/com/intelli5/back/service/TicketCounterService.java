package com.intelli5.back.service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by fhliu on 2016/11/26.
 */
public class TicketCounterService {
    private static Map<String,Integer> map = new HashMap<>();

    public static Integer getNextTicketNumber(String key) {
        Integer nextNum = 1;
        if(map.containsKey(key)) {
            nextNum = map.get(key);
            map.replace(key, nextNum, nextNum + 1);
            nextNum++;
        } else {
            map.put(key, nextNum);
        }
        return nextNum;
    }
}
