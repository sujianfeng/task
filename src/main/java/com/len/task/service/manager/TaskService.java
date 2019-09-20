package com.len.task.service.manager;

import com.len.task.common.bean.ReqBean;
import com.len.task.common.bean.RspBean;
import com.len.task.common.constant.ServerConstant;
import com.len.task.common.entity.*;
import com.len.task.common.enums.StatusCode;
import com.len.task.common.redis.RedisHelper;
import com.len.task.common.repo.*;
import com.len.task.common.util.CloudUtil;
import com.len.task.common.util.FantasticUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author sujianfeng
 * @date 2019-08-15 00:21
 */
@Slf4j
@Service
public class TaskService {
    private static final int TASK_LIST_UPDATE_INTERVAL = 5 * 60 * 1000;

    @Autowired
    private TaskCompleteRepo taskCompleteRepo;
    @Autowired
    private TaskRequestRepo taskRequestRepo;
    @Autowired
    private ModuleRepo moduleRepo;
    @Autowired
    private TaskRepo taskRepo;
    @Autowired
    private TaskLimitRepo taskLimitRepo;
    @Autowired
    private FileVersionRepo fileVersionRepo;
    @Autowired
    private ApkVersionRepo apkVersionRepo;
    @Autowired
    private RedisHelper redisService;

    /**
     * 任务请求处理
     *
     * @param reqBean
     * @return
     */
    public RspBean doTaskRequest(ReqBean reqBean) {
        //找到当前可用任务，按优先级排序
        //List<Task> taskList = getEnableTasks(reqBean.getImsi());
        List<Task> taskList = getEnableTasks();

        if (taskList == null) {
            return RspBean.builder().type(ServerConstant.TYPE_TASKRSP).status(StatusCode.NO_TASK.getCode()).build();
        }

        Task sendTask = null;

        //和日期(yyyy-MM-dd)对比的时间
        Date curDate = new Date();
        //和时间(hh:mm:ss)对比的时间
        Date curTime = new Date();
        curTime.setYear(70);
        curTime.setMonth(0);
        curTime.setDate(1);

//        Date curDate = new Date();
//        long curDateMs =  curDate.getTime();
//        long curTimeMs = curDateMs % 86400000;

        for (Task task : taskList) {
            //校验是否在下发日期
            if (curDate.before(task.getStartDate())) {
                //当前任务{}还未到可下发日期
                continue;
            }
            if (curDate.after(task.getEndDate())) {
                //当前任务{}已超过最后可下发日期
                continue;
            }

            if (curTime.before(task.getStartTime())) {
                //当前任务{}还未到可下发时间
                continue;
            }

            if (curTime.after(task.getEndTime())) {
                //当前任务{}已超过最后可下发时间
                continue;
            }

//            if ((curDateMs < task.getStartDateMs()) || (curDateMs > task.getEndDateMs()) ||
//                (curTimeMs < task.getStartTimeMs()) || (curTimeMs > task.getEndTimeMs())) {
//                continue;
//            }

            //TaskLimit taskLimit = taskLimitRepo.findByTask(task);
            TaskLimit taskLimit = getTaskLimit(task);
            if (taskLimit != null) {
                //当前任务存在任务周期
                if (taskLimit.getCurCount() >= task.getTotal()) {
                    //当前任务{}已超过下发次数限制
                    continue;
                }
                if (taskLimit.getEndTime().after(curDate)) {
                    //当前任务{}时间还在周期内
                    String startTime = FantasticUtil.SDF_4_NOTIFY.get().format(taskLimit.getStartTime());
                    String endTime = FantasticUtil.SDF_4_NOTIFY.get().format(taskLimit.getEndTime());

                    //查看当前用户在当前周期时间内是否完成了当前任务
                    TaskComplete taskComplete = getTaskComplete(reqBean.getImsi(), task.getId(), startTime, endTime);
                    //TaskComplete taskComplete = getTaskComplete(reqBean.getImsi(), task.getId(), taskLimit.getStartTimeStr(), taskLimit.getEndTimeStr());

                    if (taskComplete != null) {
                        //当前任务{}在当前周期内已完成，需要等待下一个周期进行下发
                        continue;
                    }
                } else {
                    //当前任务不在任务周期内
                    if (task.getPeriod() != 0) {
                        //周期任务 更新任务周期
                        taskLimit.setPeriod(task.getPeriod());
                        taskLimit.setPeriodUnit(task.getPeriodUnit());
                        taskLimit.setCurCount(0);
                        setTaskLimitTime(taskLimit);
                        //taskLimitRepo.saveAndFlush(taskLimit);
                        setTaskLimit(taskLimit);
                    } else {
                        //非周期任务，直接退出
                        continue;
                    }
                }
            } else {
                //当前任务不存在任务周期（还未下发过）初始化任务下发限制
                taskLimit = createTaskLimit(task);
                //taskLimitRepo.saveAndFlush(taskLimit);
                setTaskLimit(taskLimit);
            }
            sendTask = task;
            break;
        }

        if (sendTask == null) {
            return RspBean.builder().type(ServerConstant.TYPE_TASKRSP).status(StatusCode.NO_ENABLE_TASK.getCode()).build();
        }

        Module module = sendTask.getModule();

        FileVersion fileVersion = getFileVersion(module);
        if (fileVersion == null) {
            return RspBean.builder().type(ServerConstant.TYPE_TASKRSP).status(StatusCode.NO_MODULE_FILE.getCode()).build();
        }

        TaskRequest taskRequest = TaskRequest.builder().imei(reqBean.getImei()).imsi(reqBean.getImsi()).mobile(reqBean.getMobile())
                .module_id(module.getId()).moduleName(module.getModuleName())
                .task_id(sendTask.getId()).taskName(sendTask.getTaskName()).price(sendTask.getPrice()).build();

        taskRequest = taskRequestRepo.saveAndFlush(taskRequest);
        if (taskRequest == null) {
            return RspBean.builder().type(ServerConstant.TYPE_TASKRSP).status(StatusCode.REQ_DATA_SAVE_ERROR.getCode()).build();
        }

        return RspBean.builder().type(ServerConstant.TYPE_TASKRSP).status(StatusCode.SUCCESS.getCode()).mid(module.getId())
                .tid(sendTask.getId()).dver(fileVersion.getVersion()).durl(fileVersion.getUrl())
                .detail(CloudUtil.stringToBase64(sendTask.getDetail())).build();
    }

    private TaskComplete getTaskComplete(String imsi, Integer id, String startTime, String endTime) {
        TaskComplete taskComplete = (TaskComplete) redisService.get(imsi + id + startTime + endTime);
        if (taskComplete == null) {
            taskComplete = taskCompleteRepo.getUserTaskInPeriod(imsi, id, startTime, endTime);
            if (taskComplete != null) {
                redisService.set(imsi + id + startTime + endTime, taskComplete, 6, TimeUnit.HOURS);
            }
        }
        return taskComplete;
    }

    private FileVersion getFileVersion(Module module) {

        FileVersion fileVersion = (FileVersion) redisService.get(module.getModuleName());
        if (fileVersion == null) {
            fileVersion = fileVersionRepo.getLatestVersion(module.getDynamicFile().getId());
            if (fileVersion != null) {
                redisService.set(module.getModuleName(), fileVersion, 1, TimeUnit.DAYS);
            }
        }
        return fileVersion;
    }

    /**
     * 随机交换算法
     *
     * @param taskList
     * @return
     */
    private List<Task> reloadTaskList(List<Task> taskList) {
        for (int i = 0; i < taskList.size(); i++) {
            for (int j = i; j < taskList.size(); j++) {
                //先判断是否相同模块下
                if (taskList.get(i).getModule().getId().intValue() == taskList.get(j).getModule().getId().intValue()) {
                    //在判断是否具有相同任务优先级
                    if (taskList.get(i).getPriority() == taskList.get(j).getPriority()) {
                        //随机概率
                        int ran = (int) (Math.random() * 10);
                        if (ran > 4) {
                            //发生交换
                            Collections.swap(taskList, i, j);
                        }
                    }
                }
            }
        }
        return taskList;
    }

    public List<Task> getEnableTasks(String imsi) {
        List<Task> list = (List<Task>) redisService.get("taskList");
        if (list == null) {
            list = taskRepo.getEnableTasksByUpdateTime();
            if (list.size() > 0) {
                list = reloadTaskList(list);
                redisService.set("taskList", list, 1, TimeUnit.HOURS);
            }
        }
        return list;

    }

    private static Object updateLock = new Object();
    private static Thread taskUpdateThread;
    private static List<Task> curTaskList;

    public List<Task> getEnableTasks() {
        if (taskUpdateThread == null) {
            taskUpdateThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    long curDateMs = System.currentTimeMillis();
                    long curTimeMs = curDateMs % 86400000;

                    Map<Integer, TaskLimit> taskLimitMap = null;
                    List<TaskLimit> taskLimitList = taskLimitRepo.findAll();
                    if (taskLimitList.size() > 0) {
                        taskLimitMap = new ConcurrentHashMap<>(taskLimitList.size());
                        Iterator<TaskLimit> iterator = taskLimitList.iterator();
                        while (iterator.hasNext()) {
                            TaskLimit taskLimit = iterator.next();
                            //taskLimit.getStartTimeMs();
                            //taskLimit.getEndTimeMs();
//                            if (curDateMs < taskLimit.getStartTimeMs() || curDateMs > taskLimit.getEndTimeMs()) {
//                                iterator.remove();
//                                continue;
//                            }
                            taskLimitMap.put(taskLimit.getTask().getId(), taskLimit);
                        }
                    }
                    synchronized (updateLock) {
                        curTaskLimitMap = taskLimitMap;
                    }

                    List<Task> taskList = taskRepo.getEnableTasksByUpdateTime();
                    if (taskList.size() > 0) {
                        Iterator<Task> iterator = taskList.iterator();
                        while (iterator.hasNext()) {
                            Task task = iterator.next();
                            task.getStartDateMs();
                            task.getEndDateMs();
                            task.getStartTimeMs();
                            task.getEndTimeMs();
                            //if (curDateMs + TASK_LIST_UPDATE_INTERVAL < task.getStartDateMs() || curDateMs > task.getEndDateMs() ||
                            //        curTimeMs + TASK_LIST_UPDATE_INTERVAL < task.getStartTimeMs() || curTimeMs > task.getEndTimeMs()) {
                            //   iterator.remove();
                            //}
                        }
                        taskList = reloadTaskList(taskList);
                    } else {
                        taskList = null;
                    }
                    synchronized (updateLock) {
                        curTaskList = taskList;
                    }

                    try {
                        Thread.sleep(TASK_LIST_UPDATE_INTERVAL);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
            taskUpdateThread.start();
        }
        List<Task> taskList = null;
        synchronized (updateLock) {
            taskList = curTaskList;
        }
        return taskList;
    }

    private static Map<Integer, TaskLimit> curTaskLimitMap;

    public TaskLimit getTaskLimit(Task task) {
        Map<Integer, TaskLimit> taskLimitMap = null;
        synchronized (updateLock) {
            taskLimitMap = curTaskLimitMap;
        }
        if (taskLimitMap == null) {
            return null;
        }
        return taskLimitMap.get(task.getId());
    }

    ExecutorService taskLimitThreadPool = Executors.newFixedThreadPool(16);

    public void setTaskLimit(final TaskLimit taskLimit) {
        taskLimitThreadPool.execute(() -> {
            Map<Integer, TaskLimit> taskLimitMap = null;
            synchronized (updateLock) {
                taskLimitMap = curTaskLimitMap;
            }
            if (taskLimitMap == null) {
                return;
            }
            taskLimitMap.put(taskLimit.getTask().getId(), taskLimit);
            taskLimitRepo.saveAndFlush(taskLimit);
        });
    }

    /**
     * 任务完成处理
     *
     * @param reqBean
     * @return
     */
    public RspBean doTaskComplete(ReqBean reqBean) {
        Module module = moduleRepo.getOne(reqBean.getMid());
        if (module == null) {
            return RspBean.builder().type(ServerConstant.TYPE_COMPLETERSP).status(StatusCode.MODULE_NOT_EXIST.getCode()).build();
        }
        Task task = taskRepo.getOne(reqBean.getTid());
        if (task == null) {
            return RspBean.builder().type(ServerConstant.TYPE_COMPLETERSP).status(StatusCode.TASK_NOT_EXIST.getCode()).build();
        }

        Date curDate = new Date();
        if (task.getEndDate().before(curDate)) {
            //非周期任务在周期外完成
            RspBean.builder().type(ServerConstant.TYPE_COMPLETERSP).status(StatusCode.TIME_OUT_ERROR.getCode()).build();
        }

        //处理 taskLimit
        TaskLimit taskLimit = taskLimitRepo.findByTask(task);
        if (taskLimit == null) {
            taskLimit = createTaskLimit(task);
        } else {
            //周期判定
            if (taskLimit.getEndTime().before(curDate)) {
                if (task.getPeriod() != 0) {
                    //需要开启新的周期
                    taskLimit.setPeriod(task.getPeriod());
                    taskLimit.setPeriodUnit(task.getPeriodUnit());
                    taskLimit.setStartTime(taskLimit.getEndTime());
                    taskLimit.setEndTime(CloudUtil.getEndDate(taskLimit.getStartTime(), task.getPeriod(), task.getPeriodUnit()));
                    taskLimit.setCurCount(0);
                }
            } else {
                taskLimit.setCurCount(taskLimit.getCurCount() + 1);
            }
        }
        //同步更新总量
        if (taskLimit.getTotal() != task.getTotal()) {
            taskLimit.setTotal(task.getTotal());
        }

        TaskComplete taskComplete = TaskComplete.builder()
                .imei(reqBean.getImei()).imsi(reqBean.getImsi()).mobile(reqBean.getMobile())
                .module_id(module.getId()).moduleName(module.getModuleName())
                .task_id(task.getId()).taskName(task.getTaskName()).price(task.getPrice())
                .status(reqBean.getStatus()).build();

        taskComplete = taskCompleteRepo.saveAndFlush(taskComplete);

        if (taskComplete == null) {
            return RspBean.builder().type(ServerConstant.TYPE_COMPLETERSP).status(StatusCode.COMP_DATA_SAVE_ERROR.getCode()).build();
        }

        taskLimitRepo.saveAndFlush(taskLimit);

        return RspBean.builder().type(ServerConstant.TYPE_COMPLETERSP).status(StatusCode.SUCCESS.getCode()).build();
    }


    private TaskLimit createTaskLimit(Task task) {
        TaskLimit taskLimit;
        Date startTime = new Date(task.getStartDate().getYear(), task.getStartDate().getMonth(), task.getStartDate().getDate(),
                task.getStartTime().getHours(), task.getStartTime().getMinutes(), task.getStartTime().getSeconds());

        Date endTime;
        if (task.getPeriod() == 0) {
            endTime = new Date(task.getEndDate().getYear(), task.getEndDate().getMonth(), task.getEndDate().getDate(),
                    task.getEndTime().getHours(), task.getEndTime().getMinutes(), task.getEndTime().getSeconds());
        } else {
            endTime = CloudUtil.getEndDate(startTime, task.getPeriod(), task.getPeriodUnit());
        }
        taskLimit = TaskLimit.builder().task(task).period(task.getPeriod()).periodUnit(task.getPeriodUnit())
                .startTime(startTime).endTime(endTime)
                .total(task.getTotal()).curCount(0).build();
        return taskLimit;
    }

    private void setTaskLimitTime(TaskLimit taskLimit) {
        if (taskLimit.getPeriod() == 0) {
            return;
        }
        if (taskLimit.getEndTime().after(new Date())) {
            return;
        } else {
            taskLimit.setStartTime(taskLimit.getEndTime());
            taskLimit.setEndTime(CloudUtil.getEndDate(taskLimit.getStartTime(), taskLimit.getPeriod(), taskLimit.getPeriodUnit()));
            setTaskLimitTime(taskLimit);
        }
    }

    public RspBean doUpdate(ReqBean reqBean) {
        List<ApkVersion> apkVersionList = apkVersionRepo.findAll(new Sort(Sort.Direction.DESC, "version"));
        if (apkVersionList.size() == 0) {
            return RspBean.builder().type(ServerConstant.TYPE_UPDATE_RSP).status(StatusCode.NO_VERSION_ERROR.getCode()).build();
        }
        return RspBean.builder().type(ServerConstant.TYPE_UPDATE_RSP).status(StatusCode.SUCCESS.getCode()).dver(apkVersionList.get(0).getVersion())
                .durl(apkVersionList.get(0).getUrl()).build();
    }

}
