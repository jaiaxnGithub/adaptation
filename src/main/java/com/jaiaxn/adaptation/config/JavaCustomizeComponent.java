package com.jaiaxn.adaptation.config;

import com.jaiaxn.adaptation.dto.AdaptationRequest;

import java.util.Map;

/**
 * 定制化java类需实现JavaCustomizeComponent接口，并且必须添加注解：@Service（org.springframework.stereotype.Service）
 * exec即为默认执行方法
 * adaptationRequest为页面入参，execSql为数据库配置生成的最终sql
 *
 * @author: wang.jiaxin
 * @date: 2019年07月08日
 * @description: 自定义执行父类
 **/
public interface JavaCustomizeComponent {
    /**
     * 自定义执行类-默认执行方法
     * @param adaptationRequest 页面入参
     * @param execSql 数据库配置生成的脚本
     * @return 执行结果
     */
    Map<String, Object> exec(AdaptationRequest adaptationRequest, String execSql);
}