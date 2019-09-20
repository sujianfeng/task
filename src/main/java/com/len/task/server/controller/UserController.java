package com.len.task.server.controller;

import com.len.task.common.bean.ResultBean;
import com.len.task.common.config.SystemConfig;
import com.len.task.common.constant.ServerConstant;
import com.len.task.common.entity.User;
import com.len.task.common.enums.PageEnum;
import com.len.task.server.manager.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @author sujianfeng
 * @date 2019-08-06 23:14
 */
@Slf4j
@Controller
@RequestMapping("/server/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/login")
    public String login(HttpServletRequest request, @Valid @NotBlank String username, @Valid @NotBlank String password) {

        log.info("登录用户：" + username);
        log.info("登录密码：" + password);
        User user = userService.login(username, password);

        if (user == null) {
            request.setAttribute(ServerConstant.MSG, "账号密码有误，请重新登陆");
            return PageEnum.LOGIN.getPage();
        }
        if (user.getStatus() == 0) {
            request.setAttribute(ServerConstant.MSG, "该用户已停用");
            return PageEnum.LOGIN.getPage();
        }
        request.getSession().setAttribute(ServerConstant.USER, user);
        SystemConfig.sessionMap.put(user.getId(),request.getSession().getId());
        log.info("登陆成功");
        return PageEnum.INDEX.getPage();

    }

    @RequestMapping(value = "/exit")
    public String exit(HttpServletRequest request) {
        request.getSession().removeAttribute(ServerConstant.USER);
        return PageEnum.LOGIN.getPage();
    }

    @RequestMapping("/updatePassword")
    @ResponseBody
    public ResultBean updatePassword(HttpServletRequest request, @RequestParam(value = "password", defaultValue = "0") String password) {
        return userService.updatePassword(request, password);

    }


    @RequestMapping("/list")
    public String list(HttpServletRequest request) {

        List<User> fileList = userService.getUserList();
        //页面数据存储
        request.setAttribute(ServerConstant.USER_LIST, fileList);

        return PageEnum.USER_LIST.getPage();
    }

    @RequestMapping("/edit")
    public String edit(HttpServletRequest request, @RequestParam(value = "id", defaultValue = "0") int id) {
        User user = id == 0 ? User.builder().status(ServerConstant.ENABLE).build() : userService.getUser(id);
        log.info(user.toString());
        request.setAttribute(ServerConstant.USER, user);
        return PageEnum.USER_EDIT.getPage();
    }


    @RequestMapping("/save")
    @ResponseBody
    public ResultBean save(User user) {
        String operation = user.getId() != null ? "编辑" : "新增";
        log.info(user.toString());
        if (!userService.checkUser(new User(user.getId(), user.getUsername()))) {
            return ResultBean.fail(operation + "用户失败:用户名重复");
        }
        return userService.save(user) != null ? ResultBean.success(operation + "用户成功") : ResultBean.fail(operation + "用户失败");
    }

}
