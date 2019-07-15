package com.jaiaxn.adaptation.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: wang.jiaxin
 * @date: 2019年07月08日
 * @description:
 **/
@Data
@ApiModel(value = "适配服务参数对象")
public class AdaptationServerParam {
    @ApiModelProperty(value = "主键ID")
    private Long id;

    @ApiModelProperty(value = "参数对应编码")
    private String paramCode;

    @ApiModelProperty(value = "页面字段key")
    private String pageCode;

    @ApiModelProperty(value = "sql字段key")
    private String dbCode;

    @ApiModelProperty(value = "参数sql")
    private String paramSql;

    @ApiModelProperty(value = "参数位置标记")
    private String paramSign;

    @ApiModelProperty(value = "状态，0在用1废弃")
    private int state;

    @ApiModelProperty(value = "创建时间")
    private String createDate;

    @ApiModelProperty(value = "状态时间")
    private String stateDate;
}
