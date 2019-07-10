package com.iwhale.adaptation.constant;

import com.iwhale.adaptation.dto.ResultCode;

/**
 * @author: wang.jiaxin
 * @date: 2019年04月01日
 * @description:
 **/
public enum ResultCodeEnum implements ResultCode {
    /**
     * SUCCESS
     */
    SUCCESS("0", "SUCCESS"),
    /**
     * ERROR
     */
    ERROR("-1", "ERROR");

    private final String code;
    private final String desc;

    ResultCodeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public String getDesc() {
        return this.desc;
    }
}