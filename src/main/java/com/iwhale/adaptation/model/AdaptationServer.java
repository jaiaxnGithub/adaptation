package com.iwhale.adaptation.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: wang.jiaxin
 * @date: 2019年07月08日
 * @description: 适配服务对象
 **/
@Data
@ApiModel(value = "适配服务对象")
public class AdaptationServer {
    @ApiModelProperty(value = "主键ID")
    private Long id;

    @ApiModelProperty(value = "服务编码")
    private String serverCode;

    @ApiModelProperty(value = "执行sql")
    private String serverSql;

    @ApiModelProperty(value = "执行方法（sql）类型，SELECT,INSERT,UPDATE,DELETE")
    private String serverType;

    @ApiModelProperty(value = "定制化Java类名")
    private String clazzName;

    @ApiModelProperty(value = "参数对应编码")
    private String paramCode;

    @ApiModelProperty(value = "状态，0在用1废弃")
    private int state;

    @ApiModelProperty(value = "创建时间")
    private String createDate;

    @ApiModelProperty(value = "状态时间")
    private String stateDate;
}
