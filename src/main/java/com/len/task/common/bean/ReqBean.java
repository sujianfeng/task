package com.len.task.common.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;

/**
 * @author sujianfeng
 * @date 2019-08-15 00:29
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@ApiModel
public class ReqBean implements Serializable {
    @ApiModelProperty(value = "请求类型", required = true)
    private String type;
    @ApiModelProperty(value = "IMEI", required = true)
    private String imei;
    @ApiModelProperty(value = "IMSI", required = true)
    private String imsi;
    @ApiModelProperty(value = "手机号", required = true)
    private String mobile;
    @ApiModelProperty(value = "模块ID")
    private int mid;
    @ApiModelProperty(value = "任务ID")
    private int tid;
    @ApiModelProperty(value = "完成状态")
    private int status;
}
