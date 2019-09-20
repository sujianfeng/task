package com.len.task.common.entity;

/**
 * @author Administrator
 * @date 2019/8/6 10:50
 */


import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@Table(name = "t_apk_version")
@DynamicUpdate
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ApkVersion extends BaseEntity {
    @Column(name = "[version]")
    private String version;
    @Column(name = "[desc]")
    private String desc;
    private String url;

    public ApkVersion(Integer id, String version) {
        super();
        this.id = id;
        this.version = version;
    }
}

