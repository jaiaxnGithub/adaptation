package com.jaiaxn.adaptation.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jaiaxn.adaptation.constant.ResultCodeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @author: wang.jiaxin
 * @date: 2019年04月01日
 * @description:
 **/
@ApiModel("接口返回对象")
public class ResultVO<T> implements Serializable {
    @ApiModelProperty("响应编码")
    private String resultCode;
    @ApiModelProperty("响应消息")
    private String resultMsg;
    @ApiModelProperty("响应结果")
    private T resultData;

    public ResultVO() {
    }

    private ResultVO(String resultCode) {
        this.resultCode = resultCode;
    }

    private ResultVO(String resultCode, T resultData) {
        this.resultCode = resultCode;
        this.resultData = resultData;
    }

    private ResultVO(String resultCode, String resultMsg, T resultData) {
        this.resultCode = resultCode;
        this.resultMsg = resultMsg;
        this.resultData = resultData;
    }

    private ResultVO(String resultCode, String resultMsg) {
        this.resultCode = resultCode;
        this.resultMsg = resultMsg;
    }

    public static <T> ResultVO<T> success() {
        return new ResultVO(ResultCodeEnum.SUCCESS.getCode(), ResultCodeEnum.SUCCESS.getDesc());
    }

    public static <T> ResultVO<T> successMessage(String msg) {
        return new ResultVO(ResultCodeEnum.SUCCESS.getCode(), msg);
    }

    public static <T> ResultVO<T> success(T data) {
        return new ResultVO(ResultCodeEnum.SUCCESS.getCode(), ResultCodeEnum.SUCCESS.getDesc(), data);
    }

    public static <T> ResultVO<T> success(String msg, T data) {
        return new ResultVO(ResultCodeEnum.SUCCESS.getCode(), msg, data);
    }

    public static <T> ResultVO<T> error() {
        return new ResultVO(ResultCodeEnum.ERROR.getCode(), ResultCodeEnum.ERROR.getDesc());
    }

    public static <T> ResultVO<T> error(String errorMessage) {
        return new ResultVO(ResultCodeEnum.ERROR.getCode(), errorMessage);
    }

    public static <T> ResultVO<T> errorEnum(ResultCode resultCode) {
        return new ResultVO(resultCode.getCode(), resultCode.getDesc());
    }

    public static <T> ResultVO<T> error(String errorCode, String errorMessage) {
        return new ResultVO(errorCode, errorMessage);
    }

    public static <T> ResultVO<T> error(ResultCode resultCode) {
        return new ResultVO(resultCode.getCode(), resultCode.getDesc());
    }

    @JsonIgnore
    public boolean isSuccess() {
        return ResultCodeEnum.SUCCESS.getCode().equals(this.resultCode);
    }

    public String getResultCode() {
        return this.resultCode;
    }

    public String getResultMsg() {
        return this.resultMsg;
    }

    public T getResultData() {
        return this.resultData;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public void setResultMsg(String resultMsg) {
        this.resultMsg = resultMsg;
    }

    public void setResultData(T resultData) {
        this.resultData = resultData;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof ResultVO)) {
            return false;
        } else {
            ResultVO<?> other = (ResultVO)o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                label47: {
                    Object this$resultCode = this.getResultCode();
                    Object other$resultCode = other.getResultCode();
                    if (this$resultCode == null) {
                        if (other$resultCode == null) {
                            break label47;
                        }
                    } else if (this$resultCode.equals(other$resultCode)) {
                        break label47;
                    }

                    return false;
                }

                Object this$resultMsg = this.getResultMsg();
                Object other$resultMsg = other.getResultMsg();
                if (this$resultMsg == null) {
                    if (other$resultMsg != null) {
                        return false;
                    }
                } else if (!this$resultMsg.equals(other$resultMsg)) {
                    return false;
                }

                Object this$resultData = this.getResultData();
                Object other$resultData = other.getResultData();
                if (this$resultData == null) {
                    if (other$resultData != null) {
                        return false;
                    }
                } else if (!this$resultData.equals(other$resultData)) {
                    return false;
                }

                return true;
            }
        }
    }

    private boolean canEqual(Object other) {
        return other instanceof ResultVO;
    }

    @Override
    public int hashCode() {
        Object $resultCode = this.getResultCode();
        int result = 59 + ($resultCode == null ? 43 : $resultCode.hashCode());
        Object $resultMsg = this.getResultMsg();
        result = result * 59 + ($resultMsg == null ? 43 : $resultMsg.hashCode());
        Object $resultData = this.getResultData();
        result = result * 59 + ($resultData == null ? 43 : $resultData.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return "ResultVO(resultCode=" + this.getResultCode() + ", resultMsg=" + this.getResultMsg() + ", resultData=" + this.getResultData() + ")";
    }
}
