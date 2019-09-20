package com.len.task.common.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by xs on 2019/4/16.
 * Who's who of the hook, and who is who of redemption.
 */
public class MyDateUtil {
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

    /**
     * @Auther:xs
     * @Description:积分获取时间戳
     * @Date: 18:26 2019/4/16
     */
    public static String timestamp() {

        return sdf.format(new Date());
    }
}
