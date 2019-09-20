package com.len.task.server.controller;

import com.len.task.common.bean.ResultBean;
import com.len.task.common.constant.ServerConstant;
import com.len.task.common.entity.ApkVersion;
import com.len.task.common.entity.DynamicFile;
import com.len.task.common.entity.Module;
import com.len.task.common.entity.User;
import com.len.task.common.enums.PageEnum;
import com.len.task.common.util.CloudUtil;
import com.len.task.server.manager.FileService;
import com.len.task.server.manager.ModuleService;
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
@RequestMapping("/server/module")
public class ModuleController {
    @Autowired
    private ModuleService moduleService;
    @Autowired
    private FileService fileService;

    @RequestMapping("/list")
    public String list(HttpServletRequest request, String[] condition, String msg) {
        //初始化查询条件
        condition = condition == null ? new String[]{"", "-1"} : condition;
        CloudUtil.logCondition(condition);

        User user = (User) request.getSession().getAttribute(ServerConstant.USER);
//        List<Module> moduleList = moduleService.getModuleList(user);

        List<Module> moduleList = moduleService.getModuleList(condition, user);

        //页面数据存储
        request.setAttribute(ServerConstant.MODULE_LIST, moduleList);
        request.setAttribute(ServerConstant.CONDITION, condition);
        if (StringUtils.isNotBlank(msg)) {
            request.setAttribute(ServerConstant.MSG, msg);
        }
        return PageEnum.MODULE_LIST.getPage();
    }

    @RequestMapping("/edit")
    public String edit(HttpServletRequest request, @RequestParam(value = "id", defaultValue = "0") int id) {
        Module module = id == 0 ? Module.builder().status(ServerConstant.ENABLE).build() : moduleService.getModule(id);
        log.info(module.toString());
        List<DynamicFile> dynamicFileList = fileService.getFileList();
        request.setAttribute(ServerConstant.MODULE, module);
        request.setAttribute(ServerConstant.FILE_LIST, dynamicFileList);

        return PageEnum.MODULE_EDIT.getPage();
    }


    @RequestMapping("/save")
    @ResponseBody
    public ResultBean save(Module module) {
        String operation = module.getId() != null ? "编辑" : "新增";
        log.info(module.toString());
        if (!moduleService.checkModule(new Module(module.getId(), module.getModuleName()))) {
            return ResultBean.fail(operation + "用户失败:用户名重复");
        }
        return moduleService.save(module) != null ? ResultBean.success(operation + "模块成功") : ResultBean.fail(operation + "模块失败");
    }
}
