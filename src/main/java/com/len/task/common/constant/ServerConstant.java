package com.len.task.common.constant;

/**
 * @author Administrator
 * @date 2019/8/6 15:50
 */
public class ServerConstant {
    public static final String SUCCESS = "SUCCESS";
    public static final String FAIL = "FAIL";
    /**
     * session user key
     */
    public static final String USER = "user";
    /**
     * 每页显示数量
     */
    public static final String PAGE_SIZE = "20";
    /**
     * 页面数据存储page
     */
    public static final String PAGE = "page";
    /**
     * 页面存储条件
     */
    public static final String CONDITION = "condition";
    /**
     * 角色等级
     */
    public static final int ROLE_ADMIN = 1;
    public static final int ROLE_MODULE = 2;

    /**
     * 是否绑定
     */
    public static final int BIND = 1;
    public static final int NOT_BIND = 0;

    /**
     * 是否可用
     */
    public static final int ENABLE = 1;
    public static final int DISABLE = 0;

    /**
     * 是否黑名单
     */
    public static final int ISBLACK = 1;
    public static final int NOTBLACK = 0;


    /**
     * 任务类型
     */
    public static final int TASK_REQUEST = 1;
    public static final int TASK_COMPLETE = 2;


    /**
     * 绑定用户还是模块
     */
    public static final int BIND_MODULE = 1;
    public static final int BIND_USER = 2;

    /**
     * 周期任务单位
     */
    public static final int PERIOD_UNIT_DAY = 1;
    public static final int PERIOD_UNIT_HOUR = 2;

    /**
     * 周期任务单位
     */
    public static final String TYPE_TASKREQ = "taskreq";
    public static final String TYPE_TASKRSP = "taskrsp";
    public static final String TYPE_COMPLETEREQ = "completereq";
    public static final String TYPE_COMPLETERSP = "completersp";
    /**
     * 更新请求，需要提供imsi
     */
    public static final String TYPE_UPDATE_REQ = "updatereq";
    /**
     * 更新响应，返回：dver、durl
     */
    public static final String TYPE_UPDATE_RSP = "updatersp";
    /**
     * 周期任务单位
     */
    public static final int BY_HOUR = 1;
    public static final int BY_DAY = 2;

    /**
     *
     */
    public static final int AREA_TYPE_ALLOW = 1;
    public static final int AREA_TYPE_LIMIT = 0;

    /**
     * 地市级别
     */
    public static final int AREA_LEVEL_COUNTRY = 0;
    public static final int AREA_LEVEL_PROVINCE = 1;
    public static final int AREA_LEVEL_CITY = 2;


    /**
     * 默认密码
     */
    public static final String DEFAULT_PASSWORD = "888888";
    public static final String TYPE = "type";
    public static final String MODULE_LIST = "moduleList";
    public static final String FILE_LIST = "fileList";
    public static final String DYNAMICFILE = "dynamicFile";
    public static final String FILEVERSION_LIST = "fileVersionList";
    public static final String FILEVERSION = "fileVersion";
    public static final String USER_LIST = "userList";
    public static final String MODULEBIND_LIST = "moduleBindList";
    public static final String MODULE = "module";
    public static final String TASK_LIST = "taskList";
    public static final String TASK = "task";
    public static final String DATA_BEAN_LIST = "dataBeanList";

    public static final String STARTTIME = "startTime";
    public static final String ENDTIME = "endTime";
    public static final String PAGECOUNT = "pageCount";
    public static final String DATATYPE = "dataType";
    public static final String SELECT_BEAN_LIST = "selectBeanList";
    public static final String BEAN_ID = "beanId";
    public static final String SESSIONS = "sessions";
    public static final String MSG = "msg";
    public static final String UTF8 = "UTF-8";

    public static final String PROVINCE_LIST = "provinceList";
    public static final String CITY_LIST = "cityList";
    public static final String SELECTED_AREAS = "selectedAreas";
    public static final String APKVERSION_LIST = "apkVersionList";
    public static final String APKVERSION = "apkVersion";
}
