package com.jaiaxn.adaptation.config.client;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author: wang.jiaxin
 * @date: 2020年07月03日
 * @description: DemoClient相关配置
 **/
@Data
@Component
@ConfigurationProperties(prefix = "demo.client")
public class DemoClientProperties {

    private String url;

    private String username;

    private String password;

    private boolean skipVerify = false;
}
