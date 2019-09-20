package com.len.task.common.entity;

import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

/**
 * @author Administrator
 * @date 2019/8/6 10:09
 */
@Entity
@Table(name = "t_black_mobile")
@DynamicUpdate
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class BlackMobile extends BaseEntity {
    private String imsi;
    private String imei;
    private String mobile;
    /**
     * 是否为黑名单 1 是  0 否
     */
    private int status;
    private int module_id;
    private String moduleName;
    private int task_id;
    private String taskName;

}
