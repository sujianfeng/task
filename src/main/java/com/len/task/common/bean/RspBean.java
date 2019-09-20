package com.len.task.common.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;

/**
 * @author sujianfeng
 * @date 2019-08-15 00:30
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@ApiModel
public class RspBean implements Serializable {
    @ApiModelProperty(value = "响应类型")
    private String type;
    /**
     * 非0为错误码
     */
    @ApiModelProperty(value = "响应状态:0成功，非0为错误码")
    private int status;
    /**
     * 动态加载文件下载址
     */
    @ApiModelProperty(value = "动态加载文件下载址")
    private String durl;
    /**
     * 动态加载文件版本,客户端对比本地缓存文件版本号，不同则重下载
     */
    @ApiModelProperty(value = "动态加载文件版本,客户端对比本地缓存文件版本号，不同则重下载")
    private String dver;
    /**
     * 模块id
     */
    @ApiModelProperty(value = "模块ID")
    private int mid;
    /**
     * 任务id
     */
    @ApiModelProperty(value = "任务ID")
    private int tid;
    /**
     * 任务JSON
     */
    @ApiModelProperty(value = "任务JSON")
    private String detail;
}
