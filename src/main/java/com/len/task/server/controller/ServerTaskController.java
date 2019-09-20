package com.len.task.server.controller;

import com.len.task.common.bean.ResultBean;
import com.len.task.common.constant.ServerConstant;
import com.len.task.common.entity.Area;
import com.len.task.common.entity.Module;
import com.len.task.common.entity.Task;
import com.len.task.common.entity.User;
import com.len.task.common.enums.PageEnum;
import com.len.task.common.util.CloudUtil;
import com.len.task.server.manager.ModuleService;
import com.len.task.server.manager.ServerTaskService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author Administrator
 * @date 2019/8/7 11:25
 */
@Slf4j
@Controller
@RequestMapping("/server/task")
public class ServerTaskController {
    @Autowired
    private ServerTaskService serverTaskService;
    @Autowired
    private ModuleService moduleService;

    @RequestMapping("/list")
    public String list(HttpServletRequest request, @RequestParam(value = "moduleId", defaultValue = "0") int moduleId, String[] condition, String msg) {
        Module module = moduleService.getModule(moduleId);
        condition = condition == null ? new String[]{"", "-1"} : condition;
        CloudUtil.logCondition(condition);
        List<Task> taskList = serverTaskService.getTaskList(module, condition);
        //页面数据存储
        request.setAttribute(ServerConstant.MODULE, module);
        request.setAttribute(ServerConstant.TASK_LIST, taskList);
        request.setAttribute(ServerConstant.CONDITION, condition);
        if (StringUtils.isNotBlank(msg)) {
            request.setAttribute(ServerConstant.MSG, msg);
        }
        return PageEnum.TASK_LIST.getPage();
    }

    @RequestMapping("/edit")
    public String edit(HttpServletRequest request, @RequestParam(value = "id", defaultValue = "0") int id,
                       @RequestParam(value = "moduleId") int moduleId) {
        Module module = moduleService.getModule(moduleId);
        Task task = id == 0 ? Task.builder().status(ServerConstant.ENABLE).
                periodUnit(ServerConstant.PERIOD_UNIT_DAY).module(module).build() : serverTaskService.getTask(id);

        request.setAttribute(ServerConstant.TASK, task);
        return PageEnum.TASK_EDIT.getPage();
    }


    @RequestMapping("/save")
    @ResponseBody
    public ResultBean save(HttpServletRequest request, Task task) {
        String operation = task.getId() != null ? "编辑" : "新增";
        log.info(task.toString());
        if (!serverTaskService.checkTask(new Task(task.getId(), task.getTaskName()))) {
            return ResultBean.fail(operation + "任务失败:任务名称重复");
        }
        User user = (User) request.getSession().getAttribute(ServerConstant.USER);

        return serverTaskService.save(task, user) != null ? ResultBean.success(operation + "任务成功") : ResultBean.fail(operation + "任务失败");
    }

    @RequestMapping("/allowArea")
    public String allowArea(HttpServletRequest request, @RequestParam(value = "id") int id) {
        Task task = serverTaskService.getTask(id);
        request.setAttribute(ServerConstant.TASK, task);
        return PageEnum.TASK_ALLOW_AREA.getPage();
    }

    @RequestMapping("/limitArea")
    public String limitArea(HttpServletRequest request, @RequestParam(value = "id") int id) {
        Task task = serverTaskService.getTask(id);
        request.setAttribute(ServerConstant.TASK, task);
        return PageEnum.TASK_LIMIT_AREA.getPage();
    }

    @RequestMapping("/saveArea")
    @ResponseBody
    public ResultBean saveArea(HttpServletRequest request, @RequestParam(value = "area") String area,
                               @RequestParam(value = "id") int id, @RequestParam(value = "type") int type) {
        Task task = serverTaskService.getTask(id);
        if (type == ServerConstant.AREA_TYPE_LIMIT) {
            task.setLimitArea(area);
        }
        if (type == ServerConstant.AREA_TYPE_ALLOW) {
            task.setAllowArea(area);
        }
        User user = (User) request.getSession().getAttribute(ServerConstant.USER);
        return serverTaskService.save(task, user) != null ? ResultBean.success("成功") : ResultBean.fail("失败");
    }

    @RequestMapping("/uploadFile")
    public String uploadFile(HttpServletRequest request, @RequestParam(value = "moduleId", defaultValue = "0") int moduleId, @RequestParam("file") MultipartFile file) {
        String result = serverTaskService.parseExcel(file,moduleId);
        return list(request, moduleId, null, result);
    }

}
