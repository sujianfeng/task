package com.len.task.common.repo;

import com.len.task.common.entity.TaskRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Administrator
 * @date 2019/8/6 15:40
 */
@Repository
public interface TaskRequestRepo extends JpaRepository<TaskRequest, Integer>, JpaSpecificationExecutor<TaskRequest> {

    String DEVICE_SQL_BY_HOUR = "select SUBSTR(createTime,1,13) d,count(1),count(DISTINCT(imsi)) from t_task_request where createTime BETWEEN :startTime and :endTime  group by d ";

    @Query(value = DEVICE_SQL_BY_HOUR, nativeQuery = true)
    List<Object[]> getDataDeviceByHour(@Param("startTime") String startTime, @Param("endTime") String endTime);

    String DEVICE_SQL_BY_DAY = "select SUBSTR(createTime,1,10) d,count(1),count(DISTINCT(imsi)) from t_task_request where createTime BETWEEN :startTime and :endTime  group by d ";

    @Query(value = DEVICE_SQL_BY_DAY, nativeQuery = true)
    List<Object[]> getDataDeviceByDay(@Param("startTime") String startTime, @Param("endTime") String endTime);
}
