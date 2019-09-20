package com.len.task.common.bean;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author Administrator
 * @date 2019/8/15 14:44
 */
@Data
@Builder(toBuilder = true)
public class DataBean implements Serializable {
    private String dateStr;
    private int reqCount;
    private int deviceCount;
    private String beanName;
    private BigDecimal price;
}
