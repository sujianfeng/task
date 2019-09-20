package com.len.task.common.repo;
import	java.rmi.Remote;

import com.len.task.common.entity.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * @author Administrator
 * @date 2019/8/6 15:33
 */
@Repository
public interface DeviceRepo extends JpaRepository<Device, Integer>, JpaSpecificationExecutor<Device> {
}
