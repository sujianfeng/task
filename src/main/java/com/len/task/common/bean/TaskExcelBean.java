package com.len.task.common.bean;

import lombok.*;

import java.io.Serializable;

/**
 * @author Administrator
 * @date 2019/9/6 15:20
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class TaskExcelBean implements Serializable {
    private int taskId;
    private String taskName;
    private String detail;
}
