package com.jaiaxn.adaptation.schedule;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author: wang.jiaxin
 * @date: 2020年07月03日
 * @description: 定时任务
 **/
@JobHandler(value="demoJobHandler")
@Component
@Slf4j
public class DemoJobHandler extends IJobHandler {

    @Override
    public ReturnT<String> execute(String args) throws Exception {
        XxlJobLogger.log("XXL-JOB, demoJobHandler");
        log.info("-----------XXL-JOB, demoJobHandler-----------");
        return SUCCESS;
    }
}
