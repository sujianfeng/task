package com.len.task.server.controller;

import com.len.task.common.bean.ResultBean;
import com.len.task.common.constant.ServerConstant;
import com.len.task.common.entity.Module;
import com.len.task.common.entity.ModuleBind;
import com.len.task.common.entity.User;
import com.len.task.common.enums.PageEnum;
import com.len.task.server.manager.ModuleBindService;
import com.len.task.server.manager.ModuleService;
import com.len.task.server.manager.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

/**
 * @author sujianfeng
 * @date 2019-08-10 13:16
 */
@Slf4j
@Controller
@RequestMapping("/server/modulebind")
public class ModuleBindController {
    @Autowired
    private UserService userService;
    @Autowired
    private ModuleService moduleService;
    @Autowired
    private ModuleBindService moduleBindService;


    @RequestMapping("/bindmodule")
    public String bindModule(HttpServletRequest request, @RequestParam(value = "userId", defaultValue = "0") int userId) {

        User user = userService.getUser(userId);

        List<ModuleBind> moduleBindList = moduleBindService.getModuleBindList(user);

        //页面数据存储
        request.setAttribute(ServerConstant.USER, user);
        request.setAttribute(ServerConstant.MODULEBIND_LIST, moduleBindList);

        return PageEnum.USER_BIND.getPage();
    }

    @RequestMapping("/binduser")
    public String bindUser(HttpServletRequest request, @RequestParam(value = "moduleId", defaultValue = "0") int moduleId) {

        Module module = moduleService.getModule(moduleId);

        List<ModuleBind> moduleBindList = moduleBindService.getModuleBindList(module);

        //页面数据存储
        request.setAttribute(ServerConstant.MODULE, module);
        request.setAttribute(ServerConstant.MODULEBIND_LIST, moduleBindList);

        return PageEnum.MODULE_BIND.getPage();
    }

    @RequestMapping("/saveBindModule")
    @ResponseBody
    public ResultBean saveBindModule(int userId, int[] moduleBindIds) {
        moduleBindService.unbindAll(userId,ServerConstant.BIND_MODULE);
        log.info("解除所有绑定");
        if (moduleBindIds.length > 0) {
            Arrays.stream(moduleBindIds).forEach(moduleBindId -> {
                moduleBindService.saveModuleBind(moduleBindId);
            });
        }

        return ResultBean.success("绑定成功");

    }

    @RequestMapping("/saveBindUser")
    @ResponseBody
    public ResultBean saveBindUser(int moduleId, int[] moduleBindIds) {
        moduleBindService.unbindAll(moduleId,ServerConstant.BIND_USER);
        log.info("解除所有绑定");
        if (moduleBindIds.length > 0) {
            Arrays.stream(moduleBindIds).forEach(moduleBindId -> {
                moduleBindService.saveModuleBind(moduleBindId);
            });
        }

        return ResultBean.success("绑定成功");

    }

}
