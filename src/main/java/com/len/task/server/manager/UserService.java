package com.len.task.server.manager;

import com.len.task.common.bean.ResultBean;
import com.len.task.common.constant.ServerConstant;
import com.len.task.common.entity.Module;
import com.len.task.common.entity.ModuleBind;
import com.len.task.common.entity.User;
import com.len.task.common.repo.ModuleBindRepo;
import com.len.task.common.repo.ModuleRepo;
import com.len.task.common.repo.RoleRepo;
import com.len.task.common.repo.UserRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author sujianfeng
 * @date 2019-08-06 23:23
 */
@Slf4j
@Service
public class UserService {
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private RoleRepo roleRepo;

    @Autowired
    private ModuleRepo moduleRepo;

    @Autowired
    private ModuleBindRepo moduleBindRepo;

    public User login(String username, String password) {
        User user = userRepo.findUserByUsernameAndPassword(username, password);
        return user;
    }

    public ResultBean updatePassword(HttpServletRequest request, String password) {
        User user = (User) request.getSession().getAttribute(ServerConstant.USER);
        user.setPassword(password);
        user = userRepo.saveAndFlush(user);
        request.getSession().removeAttribute(ServerConstant.USER);
        return user != null ? ResultBean.success("修改密码成功") : ResultBean.fail("修改密码失败");
    }

    public List<User> getUserList() {
        return userRepo.getModuleUsers();
    }

    public User getUser(int id) {
        return userRepo.getOne(id);
    }

    public boolean checkUser(User user) {
        List<User> list = userRepo.findAllByUsername(user.getUsername());
        if (list.size() > 0) {
            if (user.getId() == null || user.getId().intValue() == 0) {
                return false;
            } else {
                for (User dbUser : list) {
                    if (dbUser.getId().intValue() != user.getId().intValue()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public User save(User user) {

        User dbUser;
        if (user.getId() != null) {
            //编辑
            dbUser = userRepo.getOne(user.getId());
            dbUser.setUsername(user.getUsername());
            dbUser.setStatus(user.getStatus());
            dbUser = userRepo.saveAndFlush(dbUser);
            log.info("编辑成功");
        } else {
            //新增
            dbUser = User.builder().username(user.getUsername()).password(ServerConstant.DEFAULT_PASSWORD)
                    .status(user.getStatus()).role(roleRepo.findRoleByRoleLevel(ServerConstant.ROLE_MODULE)).build();
            dbUser = userRepo.saveAndFlush(dbUser);
            log.info("新增成功");

            final User bindUser = dbUser;
            //绑定模块
            List<Module> moduleList = moduleRepo.findAll();
            moduleList.stream().forEach(module -> {
                ModuleBind moduleBind = ModuleBind.builder().module(module).user(bindUser).bind(ServerConstant.NOT_BIND).build();
                moduleBindRepo.saveAndFlush(moduleBind);
            });

        }
        return dbUser;
    }
}
