package com.len.task.server.controller;

import com.len.task.common.constant.ServerConstant;
import com.len.task.common.entity.Module;
import com.len.task.common.entity.TaskComplete;
import com.len.task.common.entity.TaskRequest;
import com.len.task.common.entity.User;
import com.len.task.common.enums.PageEnum;
import com.len.task.common.util.CloudUtil;
import com.len.task.server.manager.DetailService;
import com.len.task.server.manager.ModuleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author Administrator
 * @date 2019/8/7 15:56
 */
@Slf4j
@Controller
@RequestMapping("/server/detail")
public class DetailController {
    @Autowired
    private DetailService detailService;
    @Autowired
    private ModuleService moduleService;

    @RequestMapping("/listrequest")
    public String listrequest(HttpServletRequest request,
                              @RequestParam(value = "page", defaultValue = "0") Integer page,
                              @RequestParam(value = "size", defaultValue = ServerConstant.PAGE_SIZE) Integer size,
                              String[] condition) {

        //初始化分页条件
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(page < 0 ? 0 : page, size, sort);

        //初始化查询条件
        condition = condition == null ? new String[]{"", "", "", "", ""} : condition;

        CloudUtil.logCondition(condition);

        User user = (User) request.getSession().getAttribute(ServerConstant.USER);

        Page<TaskRequest> taskRequestPage = detailService.findTaskRequestPage(condition, pageable, user);

        List<Module> moduleList = getModules(user);

        //页面数据存储
        request.setAttribute(ServerConstant.PAGE, taskRequestPage);
        request.setAttribute(ServerConstant.MODULE_LIST, moduleList);
        request.setAttribute(ServerConstant.CONDITION, condition);

        return PageEnum.DETAIL_LIST_REQUEST.getPage();
    }

    @RequestMapping("/listcomplete")
    public String listcomplete(HttpServletRequest request,
                               @RequestParam(value = "page", defaultValue = "0") Integer page,
                               @RequestParam(value = "size", defaultValue = ServerConstant.PAGE_SIZE) Integer size,
                               String[] condition) {


        //初始化分页条件
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(page < 0 ? 0 : page, size, sort);

        //初始化查询条件
        condition = condition == null ? new String[]{"", "", "", "", ""} : condition;

        CloudUtil.logCondition(condition);

        User user = (User) request.getSession().getAttribute(ServerConstant.USER);

        Page<TaskComplete> taskCompletePage = detailService.findTaskCompletePage(condition, pageable, user);

        List<Module> moduleList = getModules(user);

        //页面数据存储
        request.setAttribute(ServerConstant.PAGE, taskCompletePage);
        request.setAttribute(ServerConstant.MODULE_LIST, moduleList);
        request.setAttribute(ServerConstant.CONDITION, condition);

        return PageEnum.DETAIL_LIST_COMPLETE.getPage();
    }

    private List<Module> getModules(User user) {
        List<Module> moduleList = moduleService.getModuleList(user);
        moduleList.add(0, new Module(null, "全部模块"));
        return moduleList;
    }


}
