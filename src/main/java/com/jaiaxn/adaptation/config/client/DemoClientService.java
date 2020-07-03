package com.jaiaxn.adaptation.config.client;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: wang.jiaxin
 * @date: 2020年07月03日
 * @description: 模拟一个必须在容器创建时就创建的bean
 **/
@Slf4j
public class DemoClientService {

    private static DemoClientService clientService;

    /**
     * 初始化方法
     */
    public static DemoClientService initClient(String url, String username, String password) {
        return initClient(url, username, password, false);
    }

    /**
     * 初始化方法
     */
    public static DemoClientService initClient(String url, String username, String password, boolean skipVerify) {
        log.info("------------>DemoClientService init<------------");
        if (null != url && null != username && null != password) {
            if (null == clientService) {
                clientService = new DemoClientService();
            }
            log.info("------------>DemoClientService init success<------------");
            return clientService;
        }
        log.info("------------>DemoClientService init failed<------------");
        return null;
    }

    /**
     * 销毁方法
     */
    public static void closeClient(){
        log.info("------------>DemoClientService closed!<------------");
    }

    /**
     * 测试方法
     *
     * @param something 入参
     * @return result
     */
    public String doSomething(String something){
        log.info("------------>DemoClientService do {}<------------", something);
        return "SUCCESS";
    }
}
