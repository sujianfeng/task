package com.len.task.server.manager;

import com.len.task.common.constant.ServerConstant;
import com.len.task.common.entity.Module;
import com.len.task.common.entity.ModuleBind;
import com.len.task.common.entity.User;
import com.len.task.common.repo.ModuleBindRepo;
import com.len.task.common.repo.ModuleRepo;
import com.len.task.common.repo.UserRepo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;

import static com.len.task.common.constant.ServerConstant.ROLE_ADMIN;

/**
 * @author Administrator
 * @date 2019/8/7 11:34
 */
@Service
@Slf4j
public class ModuleService {

    @Autowired
    private ModuleRepo moduleRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ModuleBindRepo moduleBindRepo;

    public List<Module> getModuleList(User user) {
        List<Module> moduleList;
        if (user.getRole().getRoleLevel() == ROLE_ADMIN) {
            moduleList = moduleRepo.findAll();
        } else {
            moduleList = moduleRepo.getBindModules(user.getId());
        }
        return moduleList;
    }

    public List<Module> getModuleList() {
        return moduleRepo.findAll();
    }

    public Module getModule(int moduleId) {
        return moduleRepo.getOne(moduleId);
    }

    public boolean checkModule(Module module) {
        List<Module> list = moduleRepo.findAllByModuleName(module.getModuleName());
        if (list.size() > 0) {
            if (module.getId() == null || module.getId().intValue() == 0) {
                return false;
            } else {
                for (Module dbModule : list) {
                    if (dbModule.getId().intValue() != module.getId().intValue()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public Module save(Module module) {
        Module dbModule;
        if (module.getId() != null) {
            //编辑
            dbModule = moduleRepo.getOne(module.getId());
            dbModule.setModuleName(module.getModuleName());
            dbModule.setStatus(module.getStatus());
            dbModule.setPriority(module.getPriority());
            dbModule.setDynamicFile(module.getDynamicFile());
            dbModule = moduleRepo.saveAndFlush(dbModule);
        } else {

            dbModule = Module.builder().moduleName(module.getModuleName()).status(module.getStatus())
                    .priority(module.getPriority()).dynamicFile(module.getDynamicFile()).build();
            dbModule = moduleRepo.saveAndFlush(dbModule);


            final Module bindModuler = dbModule;
            //绑定模块
            List<User> userList = userRepo.getModuleUsers();
            userList.stream().forEach(user -> {
                ModuleBind moduleBind = ModuleBind.builder().module(bindModuler).user(user).bind(ServerConstant.NOT_BIND).build();
                moduleBindRepo.saveAndFlush(moduleBind);
            });

        }
        return dbModule;
    }

    public List<Module> getModuleList(String[] condition, User user) {
        List<Module> moduleList;
        if (user.getRole().getRoleLevel() == ROLE_ADMIN) {
            moduleList = moduleRepo.findAll();
        } else {
            moduleList = moduleRepo.getBindModules(user.getId());
        }
        if (condition.length == 2) {
            if (StringUtils.isNotBlank(condition[0])) {
                //匹配名称
                Iterator<Module> iterator = moduleList.iterator();
                while (iterator.hasNext()) {
                    Module module = iterator.next();
                    if (!module.getModuleName().contains(condition[0])) {
                        iterator.remove();
                    }
                }
            }
            if (StringUtils.isNotBlank(condition[1])) {
                //匹配状态
                int status = Integer.parseInt(condition[1]);
                if (status != -1) {
                    Iterator<Module> iterator = moduleList.iterator();
                    while (iterator.hasNext()) {
                        Module module = iterator.next();
                        if (module.getStatus() != status) {
                            iterator.remove();
                        }
                    }
                }
            }
        }
        return moduleList;
    }
}
