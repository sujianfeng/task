package com.len.task.service.controller;

import com.len.task.common.bean.ReqBean;
import com.len.task.common.bean.RspBean;
import com.len.task.common.enums.StatusCode;
import com.len.task.server.manager.BlackService;
import com.len.task.service.manager.TaskService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.len.task.common.constant.ServerConstant.*;

/**
 * @author Administrator
 * @date 2019/8/6 15:55
 */
@Slf4j
@RestController
@RequestMapping("/service/task/")
@Api(tags = "任务服务")
public class TaskController {

    @Autowired
    private TaskService taskService;
    @Autowired
    private BlackService blackService;

    @ApiOperation("任务接口")
    @PostMapping("/doTask")
    public RspBean doTask(@RequestBody @ApiParam(name = "任务提交信息", value = "传入json格式", required = true) ReqBean reqBean) {
        if (reqBean == null) {
            return RspBean.builder().status(StatusCode.DATA_ERROR.getCode()).build();
        }

        if (StringUtils.isBlank(reqBean.getImsi())) {
            return RspBean.builder().status(StatusCode.IMSI_NOT_EXIST_ERROR.getCode()).build();
        }


        //检查黑名单
        //if (blackService.checkBlackList(reqBean)) {
        //    return RspBean.builder().status(StatusCode.BLACK_USER_ERROR.getCode()).build();
        //}

        RspBean rspBean = RspBean.builder().build();
        if (reqBean.getType().equals(TYPE_TASKREQ)) {
            log.info("task started :{},do task {}", reqBean.getImsi(), TYPE_TASKREQ);
            rspBean = taskService.doTaskRequest(reqBean);
            log.info("task finished:{},do task {}", reqBean.getImsi(), TYPE_TASKREQ);
        }

        if (reqBean.getType().equals(TYPE_COMPLETEREQ)) {
            log.info("task started :{},do task {}", reqBean.getImsi(), TYPE_COMPLETEREQ);
            rspBean = taskService.doTaskComplete(reqBean);
            log.info("task finished:{},do task {}", reqBean.getImsi(), TYPE_COMPLETEREQ);
        }

        if (reqBean.getType().equals(TYPE_UPDATE_REQ)) {
            log.info("task started :{},do task {}", reqBean.getImsi(), TYPE_UPDATE_REQ);
            rspBean = taskService.doUpdate(reqBean);
            log.info("task finished:{},do task {}", reqBean.getImsi(), TYPE_UPDATE_REQ);
        }
        return rspBean;
    }


}
