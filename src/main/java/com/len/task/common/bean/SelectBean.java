package com.len.task.common.bean;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Administrator
 * @date 2019/8/15 17:08
 */
@Data
@Builder(toBuilder = true)
public class SelectBean implements Serializable {
    private int id;
    private String name;
}
