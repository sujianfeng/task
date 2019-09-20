package com.len.task.common.bean;

import com.len.task.common.constant.ServerConstant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;

/**
 * @author Administrator
 * @date 2018/11/7 14:26
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@ApiModel
public class ResultBean<T> implements Serializable {
    /**
     * SUCCESS or 失败原因的字符串
     */
    @ApiModelProperty(allowableValues = "SUCCESS,FAIL")
    private String resultCode;
    /**
     * 如果是SUCCESS,返回OK;如果是FAIL,填写错误原因
     */
    @ApiModelProperty(value = "如果resultCode=SUCCESS,这里就是SUCCESS.如果resultCode=FAIL,这里就是错误原因")
    private String resultMsg;
    /**
     * 存储具体的data
     */
    @ApiModelProperty("具体的data,可能为空")
    private T data;

    public static <T> ResultBean<T> success(T data) {
        return (ResultBean<T>) ResultBean.builder().resultCode(ServerConstant.SUCCESS).resultMsg(ServerConstant.SUCCESS).data(data).build();
    }

    public static <T> ResultBean<T> success(String resultMsg) {
        return (ResultBean<T>) ResultBean.builder().resultCode(ServerConstant.SUCCESS).resultMsg(resultMsg).build();
    }

    public static <T> ResultBean<T> fail() {
        return (ResultBean<T>) ResultBean.builder().resultCode(ServerConstant.FAIL).resultMsg(ServerConstant.FAIL).build();
    }

    public static <T> ResultBean<T> fail(String resultMsg) {
        return (ResultBean<T>) ResultBean.builder().resultCode(ServerConstant.FAIL).resultMsg(resultMsg).build();
    }

    public static <T> ResultBean<T> fail(T data, String resultMsg) {
        return (ResultBean<T>) ResultBean.builder().resultCode(ServerConstant.FAIL).resultMsg(resultMsg).data(data).build();
    }
}
