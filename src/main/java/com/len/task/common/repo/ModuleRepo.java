package com.len.task.common.repo;

import com.len.task.common.entity.Module;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Administrator
 * @date 2019/8/6 15:35
 */
@Repository
public interface ModuleRepo extends JpaRepository<Module, Integer>, JpaSpecificationExecutor<Module> {

    String GET_BIND_MODULES = "SELECT * FROM `t_module` WHERE id IN (SELECT module_id FROM `t_module_bind` WHERE user_id = :userId AND bind = 1)";

    @Query(value = GET_BIND_MODULES, nativeQuery = true)
    List<Module> getBindModules(@Param("userId") int userId);

    List<Module> findAllByModuleName(String moduleName);

}
