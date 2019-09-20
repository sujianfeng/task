package com.len.task.common.enums;

/**
 * @author sujianfeng
 * @date 2019-08-15 00:45
 */
public enum StatusCode {

    SUCCESS(0, "成功"),
    SYSTEM_ERROR(-1, "系统内部错误"),
    MODULE_NOT_EXIST(1, "模块不存在"),
    TASK_NOT_EXIST(2, "任务不存在"),
    NO_ENABLE_TASK(3, "无可用任务下发"),
    NO_MODULE_FILE(4, "没有模块文件"),
    REQ_DATA_SAVE_ERROR(5, "请求数据存储失败"),
    COMP_DATA_SAVE_ERROR(6, "完成数据存储失败"),
    DATA_ERROR(7, "请求数据不完整"),
    IMSI_NOT_EXIST_ERROR(7, "IMSI不存在"),
    BLACK_USER_ERROR(8, "用户黑名单"),
    NO_VERSION_ERROR(9, "无可用版本"),
    TIME_OUT_ERROR(10, "任务超时完成"),
    NO_TASK(11, "无任务");


    private int code;
    private String msg;

    StatusCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public StatusCode setCode(int code) {
        this.code = code;
        return this;
    }

    public StatusCode setMsg(String msg) {
        this.msg = msg;
        return this;
    }

}
