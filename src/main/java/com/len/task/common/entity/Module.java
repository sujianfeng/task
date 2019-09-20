package com.len.task.common.entity;

import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

/**
 * @author Administrator
 * @date 2019/8/6 10:25
 */
@Entity
@Table(name = "t_module")
@DynamicUpdate
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Module extends BaseEntity {
    private String moduleName;
    /**
     * 是否开启 1 开启  0关闭
     */
    private int status;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "file_id")
    private DynamicFile dynamicFile;

    private int priority;

    public Module(Integer id, String moduleName) {
        this.id = id;
        this.moduleName = moduleName;
    }
}
