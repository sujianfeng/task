package com.len.task.common.repo;

import com.len.task.common.entity.Module;
import com.len.task.common.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Administrator
 * @date 2019/8/6 15:37
 */
@Repository
public interface TaskRepo extends JpaRepository<Task, Integer>, JpaSpecificationExecutor<Task> {
    List<Task> findAllByModule(Module module);

    List<Task> findAllByTaskName(String taskName);


    String GET_ENABLE_TASKS_BY_UPDATETIME = "select task.* FROM t_task task,t_module module where task.module_id = module.id and task.status = 1 order by module.priority desc,task.priority desc,task.updateTime desc";

    @Query(value = GET_ENABLE_TASKS_BY_UPDATETIME, nativeQuery = true)
    List<Task> getEnableTasksByUpdateTime();

    String GET_ENABLE_TASKS_BY_ID = "select task.* FROM t_task task,t_module module where task.module_id = module.id and task.status = 1 order by module.priority desc,task.priority desc,task.id desc";

    @Query(value = GET_ENABLE_TASKS_BY_ID, nativeQuery = true)
    List<Task> getEnableTasksById();

    String GET_ENABLE_TASKS_BY_MODULE = "select task.* FROM t_task task,t_module module where task.module_id = module.id and task.status = 1 order by module.priority desc,task.priority desc,task.module_id desc";

    @Query(value = GET_ENABLE_TASKS_BY_MODULE, nativeQuery = true)
    List<Task> getEnableTasksByModule();

    String GET_ENABLE_TASKS_BY_PRICE = "select task.* FROM t_task task,t_module module where task.module_id = module.id and task.status = 1 order by module.priority desc,task.priority desc,task.price desc";

    @Query(value = GET_ENABLE_TASKS_BY_PRICE, nativeQuery = true)
    List<Task> getEnableTasksByPrice();


    String GET_ENABLE_TASKS_BY_STARTDATE = "select task.* FROM t_task task,t_module module where task.module_id = module.id and task.status = 1 order by module.priority desc,task.priority desc,task.startDate desc";

    @Query(value = GET_ENABLE_TASKS_BY_STARTDATE, nativeQuery = true)
    List<Task> getEnableTasksByStartDate();

    String GET_ENABLE_TASKS_BY_ENDDATE = "select task.* FROM t_task task,t_module module where task.module_id = module.id and task.status = 1 order by module.priority desc,task.priority desc,task.endDate desc";

    @Query(value = GET_ENABLE_TASKS_BY_ENDDATE, nativeQuery = true)
    List<Task> getEnableTasksByEndDate();

    String GET_ENABLE_TASKS_BY_STARTTIME = "select task.* FROM t_task task,t_module module where task.module_id = module.id and task.status = 1 order by module.priority desc,task.priority desc,task.startTime desc";

    @Query(value = GET_ENABLE_TASKS_BY_STARTTIME, nativeQuery = true)
    List<Task> getEnableTasksByStartTime();

    String GET_ENABLE_TASKS_BY_ENDTIME = "select task.* FROM t_task task,t_module module where task.module_id = module.id and task.status = 1 order by module.priority desc,task.priority desc,task.endTime desc";

    @Query(value = GET_ENABLE_TASKS_BY_ENDTIME, nativeQuery = true)
    List<Task> getEnableTasksByEndTime();

    String GET_ENABLE_TASKS_BY_PERIOD = "select task.* FROM t_task task,t_module module where task.module_id = module.id and task.status = 1 order by module.priority desc,task.priority desc,task.period desc";

    @Query(value = GET_ENABLE_TASKS_BY_PERIOD, nativeQuery = true)
    List<Task> getEnableTasksByPeriod();

    String GET_ENABLE_TASKS_BY_TOTAL = "select task.* FROM t_task task,t_module module where task.module_id = module.id and task.status = 1 order by module.priority desc,task.priority desc,task.total desc";

    @Query(value = GET_ENABLE_TASKS_BY_TOTAL, nativeQuery = true)
    List<Task> getEnableTasksByTotal();


}
