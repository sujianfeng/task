package com.len.task.common.entity;

import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author sujianfeng
 * @date 2019-08-18 11:01
 */
@Entity
@Table(name = "t_area")
@DynamicUpdate
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Area extends BaseEntity {
    private String name;
    private int parentId;
    private int areaLevel;
}
