package com.len.task.common.util;


import lombok.extern.slf4j.Slf4j;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 会重复使用的工具函数
 *
 * @author pk
 * @since 2017/3/15.
 */
@Slf4j
public class FantasticUtil {
    public static final ThreadLocal<SimpleDateFormat> SDF_2_D = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd"));
    public static final ThreadLocal<SimpleDateFormat> SDF_2_DAY_00 = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd 00:00:00"));
    public static final ThreadLocal<SimpleDateFormat> SDF_2_DAY_59 = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd 23:59:59"));
    public static final ThreadLocal<SimpleDateFormat> SDF_2_HOUR_00 = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd HH:00:00"));
    public static final ThreadLocal<SimpleDateFormat> SDF_2_HOUR_59 = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd HH:59:59"));
    public static final ThreadLocal<SimpleDateFormat> SDF_2_1970101 = ThreadLocal.withInitial(() -> new SimpleDateFormat("1970-01-01 HH:mm:ss"));
    public static final ThreadLocal<SimpleDateFormat> TL_SDF_2_MONTH = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyyMM"));
    public static final ThreadLocal<SimpleDateFormat> TL_SDF_2_S = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyyMMddHHmmss"));
    public static final ThreadLocal<SimpleDateFormat> TL_SDF_SHANDONG_TCP = ThreadLocal.withInitial(() -> new SimpleDateFormat("MMddHHmmss"));
    public static final ThreadLocal<SimpleDateFormat> SDF_4_NOTIFY = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
    public static final ThreadLocal<SimpleDateFormat> TL_SDF_2_MS = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS"));
    public static final ThreadLocal<SimpleDateFormat> TL_SDF_2_MS2 = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyyMMddHHmmssSSS"));

    public static final ThreadLocal<DecimalFormat> TL_DF = ThreadLocal.withInitial(() -> new DecimalFormat("0000"));

    public static final ThreadLocal<DecimalFormat> TL_DF_6 = ThreadLocal.withInitial(() -> new DecimalFormat("000000"));

    private static AtomicInteger count = new AtomicInteger(0);
    private static AtomicInteger count4SubmitCommand = new AtomicInteger(0);

}
