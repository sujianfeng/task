package com.len.task.common.repo;

import com.len.task.common.entity.TaskComplete;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Administrator
 * @date 2019/8/6 15:38
 */
@Repository
public interface TaskCompleteRepo extends JpaRepository<TaskComplete, Integer>, JpaSpecificationExecutor<TaskComplete> {
//    select  SUBSTR(createTime,1,10) d,moduleName,count(1),SUM(price) from t_task_complete where createTime BETWEEN '2019-08-15 00:00:00' and '2019-08-15 23:59:59' and module_id = 1  and `status` = 0 group by d

    String MODULE_SQL_BY_HOUR = "select  SUBSTR(createTime,1,13) d,count(1),SUM(price) from t_task_complete where createTime BETWEEN :startTime and :endTime and module_id = :moduleId  and `status` = 0 group by d";

    @Query(value = MODULE_SQL_BY_HOUR, nativeQuery = true)
    List<Object[]> getDataModuleByHour(@Param("startTime") String startTime, @Param("endTime") String endTime, @Param("moduleId") String moduleId);

    String MODULE_SQL_BY_DAY = "select  SUBSTR(createTime,1,10) d,count(1),SUM(price) from t_task_complete where createTime BETWEEN :startTime and :endTime and module_id = :moduleId  and `status` = 0 group by d";

    @Query(value = MODULE_SQL_BY_DAY, nativeQuery = true)
    List<Object[]> getDataModuleByDay(@Param("startTime") String startTime, @Param("endTime") String endTime, @Param("moduleId") String moduleId);

    String TASK_SQL_BY_HOUR = "select  SUBSTR(createTime,1,13) d,count(1),SUM(price) from t_task_complete where createTime BETWEEN :startTime and :endTime and task_id = :taskId  and `status` = 0 group by d";

    @Query(value = TASK_SQL_BY_HOUR, nativeQuery = true)
    List<Object[]> getDataTaskByHour(@Param("startTime") String startTime, @Param("endTime") String endTime, @Param("taskId") String taskId);

    String TASK_SQL_BY_DAY = "select  SUBSTR(createTime,1,10) d,count(1),SUM(price) from t_task_complete where createTime BETWEEN :startTime and :endTime and task_id = :taskId  and `status` = 0 group by d";

    @Query(value = TASK_SQL_BY_DAY, nativeQuery = true)
    List<Object[]> getDataTaskByDay(@Param("startTime") String startTime, @Param("endTime") String endTime, @Param("taskId") String taskId);

    String USER_TASK_QUERY = "select * from t_task_complete where imsi = :imsi and task_id = :taskId and createTime BETWEEN :startTime and :endTime limit 1";

    @Query(value = USER_TASK_QUERY, nativeQuery = true)
    TaskComplete getUserTaskInPeriod(@Param("imsi") String imsi, @Param("taskId") int taskId, @Param("startTime") String startTime, @Param("endTime") String endTime);

}
