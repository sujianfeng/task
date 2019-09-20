package com.len.task.common.repo;

import com.len.task.common.entity.DynamicFile;
import com.len.task.common.entity.FileVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Administrator
 * @date 2019/8/6 15:34
 */
@Repository
public interface FileVersionRepo extends JpaRepository<FileVersion, Integer>, JpaSpecificationExecutor<FileVersion> {
    List<FileVersion> findAllByDynamicFileOrderByVersionDesc(DynamicFile dynamicFile);

    List<FileVersion> findAllByVersionAndDynamicFile(String version, DynamicFile dynamicFile);

    String GET_LATEST_VERSION = "select * from t_file_version where file_id = :fileId order by version desc limit 1";

    @Query(value = GET_LATEST_VERSION, nativeQuery = true)
    FileVersion getLatestVersion(@Param("fileId") int fileId);

}
