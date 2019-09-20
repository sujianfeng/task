package com.len.task.common.entity;

import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * @author Administrator
 * @date 2019/8/6 10:58
 */
@Entity
@Table(name = "t_task_request")
@DynamicUpdate
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class TaskRequest extends BaseEntity{
    private String imsi;
    private String imei;
    private String mobile;
    private int module_id;
    private String moduleName;
    private int task_id;
    private String taskName;
    private BigDecimal price;

}
