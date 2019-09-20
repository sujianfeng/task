package com.len.task.server.manager;

import com.len.task.common.constant.ServerConstant;
import com.len.task.common.entity.Module;
import com.len.task.common.entity.ModuleBind;
import com.len.task.common.entity.User;
import com.len.task.common.repo.ModuleBindRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * @author sujianfeng
 * @date 2019-08-10 13:24
 */
@Service
public class ModuleBindService {

    @Autowired
    private ModuleBindRepo moduleBindRepo;

    public List<ModuleBind> getModuleBindList(User user) {
        return moduleBindRepo.findAllByUser(user);
    }

    public List<ModuleBind> getModuleBindList(Module module) {
        return moduleBindRepo.findAllByModule(module);
    }

    @Transactional(rollbackOn = Exception.class)
    public void unbindAll(int id, int type) {
        if (type == ServerConstant.BIND_MODULE) {
            moduleBindRepo.unbindAllByUser(id);
        }
        if (type == ServerConstant.BIND_USER){
            moduleBindRepo.unbindAllByModule(id);
        }
    }

    @Transactional(rollbackOn = Exception.class)
    public void saveModuleBind(int moduleBindId) {
        moduleBindRepo.bindModuleBind(moduleBindId);
    }
}
