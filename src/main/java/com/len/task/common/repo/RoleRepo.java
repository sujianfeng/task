package com.len.task.common.repo;

import com.len.task.common.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * @author Administrator
 * @date 2019/8/6 15:36
 */
@Repository
public interface RoleRepo extends JpaRepository<Role, Integer>, JpaSpecificationExecutor<Role> {

    Role findRoleByRoleLevel(int level);
}
