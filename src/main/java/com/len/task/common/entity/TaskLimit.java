package com.len.task.common.entity;

import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

/**
 * @author Administrator
 * @date 2019/8/6 10:55
 */
@Entity
@Table(name = "t_task_limit")
@DynamicUpdate
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class TaskLimit extends BaseEntity {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "task_id")
    private Task task;
    /**
     * 周期任务时间 0 无限制  15表示15小时一个周期
     */
    private int period;
    /**
     * 周期单位 1 小时  2天
     */
    private int periodUnit;
    /**
     * 周期开始时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;
    /**
     * 周期结束时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;
    /**
     * 任务阈值
     */
    private int total;
    /**
     * 当前执行次数
     */
    private int curCount;

}
