package com.len.task.common.repo;

import com.len.task.common.entity.ApkVersion;
import com.len.task.common.entity.BlackMobile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Administrator
 * @date 2019/8/6 15:32
 */
@Repository
public interface ApkVersionRepo extends JpaRepository<ApkVersion, Integer>, JpaSpecificationExecutor<ApkVersion> {
    List<ApkVersion> findAllByVersion(String version);
}
