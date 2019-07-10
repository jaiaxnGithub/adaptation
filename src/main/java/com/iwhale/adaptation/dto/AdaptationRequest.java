package com.iwhale.adaptation.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Map;

/**
 * @author: wang.jiaxin
 * @date: 2019年07月08日
 * @description: 请求入参
 **/
@Data
@ApiModel(value = "请求入参")
public class AdaptationRequest {
    @ApiModelProperty(value = "服务编码")
    private String serverCode;

    @ApiModelProperty(value = "服务类型，INSERT、UPDATE、DELETE")
    private String serverType;

    @ApiModelProperty(value = "sql参数Map")
    private Map<String, Object> paramMap;
}