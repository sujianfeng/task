package com.len.task.server.manager;

import com.len.task.common.bean.ReqBean;
import com.len.task.common.cache.Cache;
import com.len.task.common.constant.ServerConstant;
import com.len.task.common.entity.*;
import com.len.task.common.repo.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

import static com.len.task.common.constant.ServerConstant.ISBLACK;
import static com.len.task.common.constant.ServerConstant.NOTBLACK;

/**
 * @author sujianfeng
 * @date 2019-08-12 23:11
 */
@Service
public class BlackService {
    @Autowired
    private BlackMobileRepo blackMobileRepo;
    @Autowired
    private TaskRequestRepo taskRequestRepo;
    @Autowired
    private TaskCompleteRepo taskCompleteRepo;
    @Autowired
    private ModuleRepo moduleRepo;
    @Autowired
    private TaskRepo taskRepo;

    public BlackMobile save(int id, int type) {
        BlackMobile blackMobile = null;
        if (type == ServerConstant.TASK_REQUEST) {
            TaskRequest taskRequest = taskRequestRepo.getOne(id);
            if (taskRequest != null) {
                blackMobile = BlackMobile.builder().imei(taskRequest.getImei())
                        .imsi(taskRequest.getImsi()).mobile(taskRequest.getMobile())
                        .module_id(taskRequest.getModule_id()).moduleName(taskRequest.getModuleName())
                        .task_id(taskRequest.getTask_id()).taskName(taskRequest.getTaskName())
                        .status(ISBLACK).build();
            }
        }
        if (type == ServerConstant.TASK_COMPLETE) {
            TaskComplete taskComplete = taskCompleteRepo.getOne(id);
            if (taskComplete != null) {
                blackMobile = BlackMobile.builder().imei(taskComplete.getImei())
                        .imsi(taskComplete.getImsi()).mobile(taskComplete.getMobile())
                        .module_id(taskComplete.getModule_id()).moduleName(taskComplete.getModuleName())
                        .task_id(taskComplete.getTask_id()).taskName(taskComplete.getTaskName())
                        .status(ISBLACK).build();
            }
        }
        if (blackMobile != null) {
            if (blackMobileRepo.findAllByImsiAndStatus(blackMobile.getImsi(), ISBLACK).size() > 0) {
                //已在黑名单列表中,无需再次添加
            } else {
                blackMobile = blackMobileRepo.saveAndFlush(blackMobile);
            }
        }
        return blackMobile;
    }

    public void remove(int id) {
        blackMobileRepo.deleteById(id);
    }

    public Page<BlackMobile> findBlackMobilePage(String[] condition, Pageable pageable, User user) {
        return blackMobileRepo.findAll(new Specification<BlackMobile>() {
            @Override
            public Predicate toPredicate(Root<BlackMobile> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                return getPredicate(cb, condition, user, root.get("imsi"), root.get("imei"), root.get("mobile"), root.get("module_id"));
            }
        }, pageable);
    }

    private Predicate getPredicate(CriteriaBuilder cb, String[] condition, User user, Path<Object> imsi, Path<Object> imei, Path<Object> mobile, Path<Object> module_id) {
        List<Predicate> list = new ArrayList<>();

        //imsi
        if (StringUtils.isNotBlank(condition[0])) {
            list.add(cb.like(imsi.as(String.class), '%' + condition[0] + '%'));
        }
        //imei
        if (StringUtils.isNotBlank(condition[1])) {
            list.add(cb.like(imei.as(String.class), '%' + condition[1] + '%'));
        }
        //mobile
        if (StringUtils.isNotBlank(condition[2])) {
            list.add(cb.like(mobile.as(String.class), '%' + condition[2] + '%'));
        }

        if (StringUtils.isNotBlank(condition[3])) {
            list.add(cb.equal(module_id.as(Integer.class), new Integer(condition[3])));
        } else {
            Expression<Integer> exp = module_id.as(Integer.class);
            List<Module> moduleList;
            if (user.getRole().getRoleLevel() == ServerConstant.ROLE_ADMIN) {
                moduleList = moduleRepo.findAll();
            } else {
                moduleList = moduleRepo.getBindModules(user.getId());
            }
            List<Integer> intList = new ArrayList<>();
            for (Module module : moduleList) {
                intList.add(module.getId());
            }
            list.add(exp.in(intList));
        }
        Predicate[] predicate = new Predicate[list.size()];
        return cb.and(list.toArray(predicate));
    }

    public boolean checkBlackList(ReqBean reqBean) {
        if (Cache.contains(reqBean.getImsi())) {
            return Cache.isBlack(reqBean.getImsi());
        } else {
            List<BlackMobile> list = blackMobileRepo.findAllByImsiAndStatus(reqBean.getImsi(), ISBLACK);
            if (list.size() > 0) {
                Cache.putBlackMap(reqBean.getImsi(), ISBLACK);
                return true;
            } else {
                Cache.putBlackMap(reqBean.getImsi(), NOTBLACK);
                return false;
            }
        }
    }
}
