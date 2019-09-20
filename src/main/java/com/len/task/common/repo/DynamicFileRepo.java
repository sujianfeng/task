package com.len.task.common.repo;

import com.len.task.common.entity.DynamicFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Administrator
 * @date 2019/8/6 15:34
 */
@Repository
public interface DynamicFileRepo extends JpaRepository<DynamicFile, Integer>, JpaSpecificationExecutor<DynamicFile> {

    List<DynamicFile> findAllByFileName(String fileName);
}
