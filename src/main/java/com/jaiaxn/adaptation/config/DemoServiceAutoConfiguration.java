package com.jaiaxn.adaptation.config;

import com.jaiaxn.adaptation.config.client.DemoClient;
import com.jaiaxn.adaptation.config.client.DemoClientProperties;
import com.jaiaxn.adaptation.config.client.DemoClientService;
import com.jaiaxn.adaptation.service.AdaptationService;
import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author: wang.jiaxin
 * @date: 2020年07月03日
 * @description: 初始化一个必须在容器创建时就创建的bean
 **/
@Configuration
@AutoConfigureAfter({MybatisAutoConfiguration.class})
@ComponentScan(basePackages = {"com.jaiaxn.adaptation"})
@EnableConfigurationProperties({DemoClientProperties.class})
@EnableTransactionManagement
public class DemoServiceAutoConfiguration implements InitializingBean {

    private final ApplicationContext context;

    private final DemoClientProperties demoClientProperties;

    public DemoServiceAutoConfiguration(ApplicationContext context, DemoClientProperties demoClientProperties) {
        this.context = context;
        this.demoClientProperties = demoClientProperties;
    }

    /**
     * 创建DemoClient
     *
     * @return DemoClient
     */
    @Bean(name = "demoClient", destroyMethod = "disconnect")
    public DemoClient sealClient() {
        DemoClient client = new DemoClient(demoClientProperties);
        client.connect();
        return client;
    }

    /**
     * 初始化并将DemoClient注入要使用的Bean中去
     */
    @Override
    public void afterPropertiesSet() {
        DemoClient demoClient = context.getBean(DemoClient.class);
        DemoClientService demoClientService = (DemoClientService) demoClient.getNativeClient();
        if (null != demoClientService) {
            AdaptationService adaptationService = context.getBean(AdaptationService.class);
            adaptationService.initServiceClient(demoClientService);
        }
    }
}
