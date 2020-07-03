package com.jaiaxn.adaptation.config.schedule;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author: wang.jiaxin
 * @date: 2020年07月03日
 * @description: xxl-job定时器配置
 **/
@Data
@ConfigurationProperties(prefix = "xxl.job")
public class XxlJobProperties {
    private XxlJobAdmin admin;

    private String accessToken;

    private XxlJobExecutor executor;

    @Data
    public static class XxlJobAdmin {
        private String addresses;
    }

    @Data
    public static class XxlJobExecutor {
        private String appName;
        private String ip;
        private Integer port;
        private String logPath;
        private Integer logRetentionDays;
    }
}