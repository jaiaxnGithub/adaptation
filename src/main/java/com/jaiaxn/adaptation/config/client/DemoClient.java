package com.jaiaxn.adaptation.config.client;

/**
 * @author: wang.jiaxin
 * @date: 2020年07月03日
 * @description:
 **/
public class DemoClient implements IDemoClient {

    private DemoClientService demoClientService;

    private final DemoClientProperties demoClientProperties;

    public DemoClient(DemoClientProperties demoClientProperties) {
        super();
        this.demoClientProperties = demoClientProperties;
    }

    @Override
    public void connect() {
        demoClientService = DemoClientService.initClient(demoClientProperties.getUrl(), demoClientProperties.getUsername(), demoClientProperties.getPassword());
    }

    @Override
    public void disconnect() {
        DemoClientService.closeClient();
    }

    @Override
    public Object getNativeClient() {
        return demoClientService;
    }
}
