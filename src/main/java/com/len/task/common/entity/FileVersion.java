package com.len.task.common.entity;

/**
 * @author Administrator
 * @date 2019/8/6 10:50
 */


import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@Table(name = "t_file_version")
@DynamicUpdate
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class FileVersion extends BaseEntity {
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "file_id")
    private DynamicFile dynamicFile;
    @Column(name = "[version]")
    private String version;
    @Column(name = "[desc]")
    private String desc;
    private String url;

    public FileVersion(Integer id, String version, DynamicFile dynamicFile) {
        this.id = id;
        this.version = version;
        this.dynamicFile = dynamicFile;
    }
}
