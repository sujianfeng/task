package com.len.task.common.config;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author sujianfeng
 * @date 2019-08-17 13:53
 */
@Slf4j
public class SystemConfig {
    public static Map<Integer, String> sessionMap = new ConcurrentHashMap<>();
}
