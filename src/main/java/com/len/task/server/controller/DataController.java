package com.len.task.server.controller;

import com.len.task.common.bean.DataBean;
import com.len.task.common.bean.SelectBean;
import com.len.task.common.constant.ServerConstant;
import com.len.task.common.entity.Module;
import com.len.task.common.entity.Task;
import com.len.task.common.entity.User;
import com.len.task.common.enums.PageEnum;
import com.len.task.server.manager.DataService;
import com.len.task.server.manager.ModuleService;
import com.len.task.server.manager.ServerTaskService;
import com.len.task.service.manager.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Administrator
 * @date 2019/8/15 14:29
 */
@Slf4j
@Controller
@RequestMapping("/server/data")
public class DataController {
    @Autowired
    private DataService dataService;
    @Autowired
    private ModuleService moduleService;
    @Autowired
    private ServerTaskService serverTaskService;

    @RequestMapping("/device/list")
    public String deviceList(HttpServletRequest request, @RequestParam(value = "type", defaultValue = "1") int type, String startTime, String endTime) {

        List<DataBean> dataBeanList = dataService.getDataDeviceList(type, startTime, endTime);
        //页面数据存储
        request.setAttribute(ServerConstant.DATA_BEAN_LIST, dataBeanList);
        request.setAttribute(ServerConstant.TYPE, type);
        request.setAttribute(ServerConstant.STARTTIME, startTime);
        request.setAttribute(ServerConstant.ENDTIME, endTime);
        request.setAttribute(ServerConstant.PAGECOUNT, dataBeanList.size());

        return PageEnum.DATA_DEVICE_LIST.getPage();
    }

    @RequestMapping("/bean/list")
    public String dataList(HttpServletRequest request, @RequestParam(value = "type", defaultValue = "1") int type,
                           @RequestParam(value = "dataType", defaultValue = "module") String dataType,
                           @RequestParam(value = "beanId", defaultValue = "0") String beanId,
                           String startTime, String endTime) {
        List<SelectBean> selectBeanList = new ArrayList<>();
        User user = (User) request.getSession().getAttribute(ServerConstant.USER);
        if (dataType.equals(ServerConstant.MODULE)) {
            List<Module> moduleList = moduleService.getModuleList(user);
            moduleList.stream().forEach(module -> {
                selectBeanList.add(SelectBean.builder().id(module.getId()).name(module.getModuleName()).build());
            });
        }
        if (dataType.equals(ServerConstant.TASK)) {
            List<Task> taskList = serverTaskService.getTaskList(user);
            taskList.stream().forEach(task -> {
                selectBeanList.add(SelectBean.builder().id(task.getId()).name(task.getTaskName()).build());
            });
        }
        selectBeanList.add(0, SelectBean.builder().id(0).name("请选择").build());


        List<DataBean> dataBeanList = dataService.getDataBeanList(type,dataType, startTime, endTime, beanId);
        //页面数据存储
        request.setAttribute(ServerConstant.SELECT_BEAN_LIST, selectBeanList);
        request.setAttribute(ServerConstant.DATA_BEAN_LIST, dataBeanList);
        request.setAttribute(ServerConstant.TYPE, type);
        request.setAttribute(ServerConstant.DATATYPE, dataType);
        request.setAttribute(ServerConstant.STARTTIME, startTime);
        request.setAttribute(ServerConstant.ENDTIME, endTime);
        request.setAttribute(ServerConstant.BEAN_ID, beanId);
        request.setAttribute(ServerConstant.PAGECOUNT, dataBeanList.size());

        return PageEnum.DATA_BEAN_LIST.getPage();
    }
}
