package com.len.task.common.repo;

import com.len.task.common.entity.Module;
import com.len.task.common.entity.ModuleBind;
import com.len.task.common.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Administrator
 * @date 2019/8/6 15:36
 */
@Repository
public interface ModuleBindRepo extends JpaRepository<ModuleBind, Integer>, JpaSpecificationExecutor<ModuleBind> {

    List<ModuleBind> findAllByUser(User user);

    List<ModuleBind> findAllByModule(Module module);

    String UNBIND_BY_USER = "update t_module_bind set bind = 0 where user_id = :userId";

    @Modifying
    @Query(value = UNBIND_BY_USER, nativeQuery = true)
    void unbindAllByUser(@Param("userId") int userId);

    String BIND_MODULEBIND = "update t_module_bind set bind = 1 where id = :id";

    @Modifying
    @Query(value = BIND_MODULEBIND, nativeQuery = true)
    void bindModuleBind(@Param("id") int id);

    String UNBIND_BY_MODULE = "update t_module_bind set bind = 0 where module_id = :moduleId";

    @Modifying
    @Query(value = UNBIND_BY_MODULE, nativeQuery = true)
    void unbindAllByModule(@Param("moduleId") int moduleId);
}
