package com.len.task.common.entity;

import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author Administrator
 * @date 2019/8/6 10:14
 */
@Entity
@Table(name = "t_role")
@DynamicUpdate
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Role extends BaseEntity {
    private String roleName;
    /**
     * 是否可用 1 可用 0 不可用
     */
    private int status;
    private String desc;
    /**
     * 角色级别 1 超管 2 模块管理员
     */
    private int roleLevel;
}
