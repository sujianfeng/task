package com.len.task.common.entity;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Administrator
 * @date 2019/8/6 10:29
 */
@Entity
@Table(name = "t_task")
@DynamicUpdate
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Task extends BaseEntity {
    private String taskName;
    /**
     * 开启状态 1 开启 0 关闭
     */
    private int status;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "module_id")
    private Module module;
    /**
     * 开始日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startDate;
    @Transient
    private long startDateMs = 0;

    public long getStartDateMs() {
        if (startDateMs == 0) {
            startDateMs = startDate.getTime();
        }
        return startDateMs;
    }
    /**
     * 结束日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endDate;
    @Transient
    private long endDateMs = 0;

    public long getEndDateMs() {
        if (endDateMs == 0) {
            endDateMs = endDate.getTime();
        }
        return endDateMs;
    }
    /**
     * 开始时间
     */
    @DateTimeFormat(pattern = "HH:mm:ss")
    private Date startTime;
    @Transient
    private long startTimeMs = -1;

    public long getStartTimeMs() {
        if (startTimeMs < 0) {
            startTimeMs = startTime.getTime() % 86400000;
        }
        return startTimeMs;
    }
    /**
     * 结束时间
     */
    @DateTimeFormat(pattern = "HH:mm:ss")
    private Date endTime;
    @Transient
    private long endTimeMs = -1;

    public long getEndTimeMs() {
        if (endTimeMs < 0) {
            endTimeMs = endTime.getTime() % 86400000;
        }
        return endTimeMs;
    }
    /**
     * 优先级
     */
    private int priority;
    /**
     * 价格
     */
    private BigDecimal price;
    /**
     * 周期任务时间 0 无限制  15表示15小时一个周期
     */
    private int period;
    /**
     * 周期单位 1 小时  2天
     */
    private int periodUnit;
    /**
     * 任务总数限制
     */
    private int total;
    /**
     * 任务详情，json格式
     */
    private String detail;
    /**
     * 随机概率0-100之间
     */
    private int random;
    /**
     * 允许地区
     */
    private String allowArea;
    /**
     * 禁止地区
     */
    private String limitArea;
    /**
     * 创建用户
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "create_user")
    private User createUser;
    /**
     * 更新用户
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "update_user")
    private User updateUser;


    public Task(Integer id, String taskName) {
        this.id = id;
        this.taskName = taskName;
    }
}
