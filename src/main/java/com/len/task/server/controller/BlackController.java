package com.len.task.server.controller;

import com.len.task.common.bean.ResultBean;
import com.len.task.common.constant.ServerConstant;
import com.len.task.common.entity.BlackMobile;
import com.len.task.common.entity.Module;
import com.len.task.common.entity.User;
import com.len.task.common.enums.PageEnum;
import com.len.task.common.util.CloudUtil;
import com.len.task.server.manager.BlackService;
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
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author sujianfeng
 * @date 2019-08-12 23:10
 */
@Slf4j
@Controller
@RequestMapping("/server/black")
public class BlackController {
    @Autowired
    private BlackService blackService;
    @Autowired
    private ModuleService moduleService;

    @RequestMapping("/save")
    @ResponseBody
    public ResultBean save(int id, int type) {
        return blackService.save(id, type) != null ? ResultBean.success("加入黑名单成功") : ResultBean.fail("加入黑名单失败");
    }

    @RequestMapping("/list")
    public String list(HttpServletRequest request,
                       @RequestParam(value = "page", defaultValue = "0") Integer page,
                       @RequestParam(value = "size", defaultValue = ServerConstant.PAGE_SIZE) Integer size,
                       String[] condition) {

        //初始化分页条件
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(page < 0 ? 0 : page, size, sort);

        //初始化查询条件
        condition = condition == null ? new String[]{"", "", "", ""} : condition;

        CloudUtil.logCondition(condition);

        User user = (User) request.getSession().getAttribute(ServerConstant.USER);

        Page<BlackMobile> blackMobilePage = blackService.findBlackMobilePage(condition, pageable, user);

        List<Module> moduleList = getModules(user);

        //页面数据存储
        request.setAttribute(ServerConstant.PAGE, blackMobilePage);
        request.setAttribute(ServerConstant.MODULE_LIST, moduleList);
        request.setAttribute(ServerConstant.CONDITION, condition);

        return PageEnum.BLACK_LIST.getPage();
    }


    @RequestMapping("/remove")
    @ResponseBody
    public ResultBean remove(int id) {
        blackService.remove(id);
        return ResultBean.success("移出黑名单成功");
    }

    private List<Module> getModules(User user) {
        List<Module> moduleList = moduleService.getModuleList(user);
        moduleList.add(0, new Module(null, "全部模块"));
        return moduleList;
    }

}
