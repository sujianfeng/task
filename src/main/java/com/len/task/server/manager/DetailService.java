package com.len.task.server.manager;

import com.len.task.common.constant.ServerConstant;
import com.len.task.common.entity.Module;
import com.len.task.common.entity.TaskComplete;
import com.len.task.common.entity.TaskRequest;
import com.len.task.common.entity.User;
import com.len.task.common.repo.ModuleRepo;
import com.len.task.common.repo.TaskCompleteRepo;
import com.len.task.common.repo.TaskRequestRepo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author sujianfeng
 * @date 2019-08-12 21:44
 */
@Service
public class DetailService {
    @Autowired
    private TaskRequestRepo taskRequestRepo;
    @Autowired
    private TaskCompleteRepo taskCompleteRepo;
    @Autowired
    private ModuleRepo moduleRepo;


    public Page<TaskRequest> findTaskRequestPage(String[] condition, Pageable pageable, User user) {
        return taskRequestRepo.findAll(new Specification<TaskRequest>() {
            @Override
            public Predicate toPredicate(Root<TaskRequest> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                return getPredicate(cb, condition, user, root.get("imsi"), root.get("imei"), root.get("mobile"), root.get("price"), root.get("module_id"));
            }
        }, pageable);
    }

    public Page<TaskComplete> findTaskCompletePage(String[] condition, Pageable pageable, User user) {
        return taskCompleteRepo.findAll(new Specification<TaskComplete>() {
            @Override
            public Predicate toPredicate(Root<TaskComplete> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                return getPredicate(cb, condition, user, root.get("imsi"), root.get("imei"), root.get("mobile"), root.get("price"), root.get("module_id"));
            }
        }, pageable);
    }

    private Predicate getPredicate(CriteriaBuilder cb, String[] condition, User user, Path<Object> imsi, Path<Object> imei, Path<Object> mobile, Path<Object> price, Path<Object> module_id) {
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
        //price
        if (StringUtils.isNotBlank(condition[3])) {
            list.add(cb.equal(price.as(BigDecimal.class), new BigDecimal(condition[3])));
        }

        if (StringUtils.isNotBlank(condition[4])) {
            list.add(cb.equal(module_id.as(Integer.class), new Integer(condition[4])));
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
}
