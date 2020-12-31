package com.jaiaxn.adaptation.utils.assembler;

/**
 * @author: wangjx@tisntergy.com
 * @date: 2020年10月20日
 * @description: 对象（集合）转换工具类回调接口
 **/
public interface AssemblerCallBack {
    /**
     * 转换之后的钩子函数
     * @param target target
     * @param source source
     */
    void afterAssemble(Object target, Object source);
}
