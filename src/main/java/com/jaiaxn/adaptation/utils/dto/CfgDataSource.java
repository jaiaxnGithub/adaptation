package com.jaiaxn.adaptation.utils.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: wang.jiaxin
 * @date: 2019年05月09日
 * @description:
 **/
@Data
@ApiModel(value = "数据源对象")
public class CfgDataSource {
    @ApiModelProperty(value = "数据源ID，序列主键")
    private int datasourceId;

    @ApiModelProperty(value = "数据库URL")
    private String dbUrl;

    @ApiModelProperty(value = "数据库用户名")
    private String dbUserName;

    @ApiModelProperty(value = "数据库密码")
    private String dbPassword;

    @ApiModelProperty(value = "最大连接数，默认值:10")
    private int maxActive;

    @ApiModelProperty(value = "最大等待时间（秒），默认值:600")
    private int connectionTimeOut;

    @ApiModelProperty(value = "数据源名称")
    private String dbSourceName;

    @ApiModelProperty(value = "最大空闲连接数")
    private int maxIdle;

    @ApiModelProperty(value = "超时时间")
    private int removeAbandoneTimeout;

    @ApiModelProperty(value = "数据库类型，oracle、mysql")
    private String dbType;

    @ApiModelProperty(value = "数据库驱动")
    private String dbDriverClassName;

}
