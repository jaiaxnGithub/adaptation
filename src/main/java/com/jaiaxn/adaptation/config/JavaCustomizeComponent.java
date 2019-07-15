package com.jaiaxn.adaptation.config;

import com.jaiaxn.adaptation.dto.AdaptationRequest;

import java.util.Map;

/**
 * @author: wang.jiaxin
 * @date: 2019年07月08日
 * @description: 自定义执行父类
 **/
public interface JavaCustomizeComponent {
    /**
     * 自定义执行类
     * @param adaptationRequest 页面入参
     * @param execSql 数据库配置生成的脚本
     * @return 执行结果
     */
    Map<String, Object> exec(AdaptationRequest adaptationRequest, String execSql);
}
