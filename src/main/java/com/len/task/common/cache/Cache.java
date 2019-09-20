package com.len.task.common.cache;

import com.len.task.common.entity.FileVersion;
import com.len.task.common.entity.Task;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.len.task.common.constant.ServerConstant.ISBLACK;

/**
 * @author Administrator
 * @date 2019/9/6 16:01
 */
@Slf4j
public class Cache {
    private static int countList = 0;
    private static int maxCountList = 1000;

    private static int fileCount = 0;
    private static int maxFileCount = 1000;

    private static Map<Integer, FileVersion> map = new LinkedHashMap<Integer, FileVersion>();

    private static int maxBlackSize = 100000;
    private static Map<String, Integer> blackMap = new LinkedHashMap<String, Integer>() {
        @Override
        protected boolean removeEldestEntry(Map.Entry<String, Integer> eldest) {
            return size() > maxBlackSize;
        }
    };

    private static List<Task> list;

    public static List<Task> getTaskList() {
        if (list != null) {
            countList++;
        }
        if (countList == maxCountList) {
            countList = 0;
            list = null;
        }
        log.info("{}", countList);
        return list;
    }

    public static void setTaskList(List<Task> taskList) {
        Cache.list = taskList;
    }

    public static FileVersion getFileVersion(int moduleId) {
        fileCount++;
        if (fileCount == maxFileCount) {
            fileCount = 0;
            map.remove(moduleId);
        }
        log.info("{}", fileCount);
        if (map.containsKey(moduleId)) {
            return map.get(moduleId);
        }
        return null;
    }

    public static void setFileVersion(int moduleId, FileVersion fileVersion) {
        map.put(moduleId, fileVersion);
    }

    public static void putBlackMap(String imsi, int isBlack) {
        if (StringUtils.isNotBlank(imsi)) {
            blackMap.put(imsi, isBlack);
        }
    }

    public static boolean contains(String imsi) {
        return blackMap.containsKey(imsi);
    }

    public static boolean isBlack(String imsi) {
        if (!blackMap.containsKey(imsi)) {
            return false;
        }
        return blackMap.get(imsi) == ISBLACK;
    }
}
