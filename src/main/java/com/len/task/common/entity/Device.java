package com.len.task.common.entity;

import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author Administrator
 * @date 2019/8/6 10:11
 */
@Entity
@Table(name = "t_device")
@DynamicUpdate
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Device extends BaseEntity {
    private String imsi;
    private String imei;
    private String mobile;
}
