package com.len.task.common.repo;

import com.len.task.common.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Administrator
 * @date 2019/8/6 15:42
 */
@Repository
public interface UserRepo extends JpaRepository<User, Integer>, JpaSpecificationExecutor<User> {

    User findUserByUsernameAndPassword(String username,String password);

    List<User> findAllByUsername(String username);


    String GET_MODULE_USERS = "select * from t_user where roleid=2";

    @Query(value = GET_MODULE_USERS, nativeQuery = true)
    List<User> getModuleUsers();
}
