package com.jaiaxn.adaptation.config.client;

/**
 * @author: wang.jiaxin
 * @date: 2020年07月03日
 * @description: 操作客户端接口
 **/
public interface IDemoClient {

    /**
     * 连接客户端
     */
    void connect();

    /**
     * 关闭客户端
     */
    void disconnect();

    /**
     * 获取真实客户端
     *
     * @return 真实客户端
     */
    Object getNativeClient();
}
