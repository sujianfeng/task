package com.len.task.common.enums;

/**
 * @author sujianfeng
 * @date 2019-08-06 22:59
 */
public enum PageEnum {
    LOGIN("login"),
    MODULE_LIST("module_list"),
    INDEX("index"),
    FILE_LIST("file_list"),
    FILE_EDIT("file_edit"),
    FILEVERSION_LIST("fileversion_list"),
    FILEVERSION_EDIT("fileversion_edit"),
    USER_LIST("user_list"),
    USER_BIND("user_bind"),
    MODULE_BIND("module_bind"),
    MODULE_EDIT("module_edit"),
    USER_EDIT("user_edit"),
    TASK_LIST("task_list"),
    TASK_EDIT("task_edit"),
    DETAIL_LIST_REQUEST("detail_list_request"),
    DETAIL_LIST_COMPLETE("detail_list_complete"),
    BLACK_LIST("black_list"),
    DATA_DEVICE_LIST("data_device_list"),
    DATA_BEAN_LIST("data_bean_list"),
    TASK_ALLOW_AREA("task_allow_area"),
    TASK_LIMIT_AREA("task_limit_area"), APKVERSION_LIST("apkversion_list"), APKVERSION_EDIT("apkversion_edit");

    private String page;

    PageEnum(String page) {
        this.page = page;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }
}
