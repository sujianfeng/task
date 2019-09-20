package com.len.task.common.entity;

import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author Administrator
 * @date 2019/8/6 10:27
 */
@Entity
@Table(name = "t_dynamic_file")
@DynamicUpdate
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class DynamicFile extends BaseEntity {
    private String fileName;

    public DynamicFile(Integer id, String fileName) {
        this.id=id;
        this.fileName = fileName;
    }
}
