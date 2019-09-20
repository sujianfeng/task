package com.len.task.common.entity;

import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

/**
 * @author Administrator
 * @date 2019/8/6 10:15
 */
@Entity
@Table(name = "t_user")
@DynamicUpdate
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class User extends BaseEntity{
    private String username;
    @Column(name = "[password]")
    private String password;
    /**
     * 是否可用 1 可用 0 不可用
     */
    private int status;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "roleid")
    private Role role;

    public User(Integer id, String username) {
        this.id = id;
        this.username = username;
    }
}
