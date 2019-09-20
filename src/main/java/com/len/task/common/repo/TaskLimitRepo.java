package com.len.task.common.repo;

import com.len.task.common.entity.Task;
import com.len.task.common.entity.TaskLimit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * @author Administrator
 * @date 2019/8/6 15:39
 */
@Repository
public interface TaskLimitRepo extends JpaRepository<TaskLimit, Integer>, JpaSpecificationExecutor<TaskLimit> {

    TaskLimit findByTask(Task task);
}
