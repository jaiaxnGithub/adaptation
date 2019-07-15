package com.jaiaxn.adaptation.clazz;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.jaiaxn.adaptation.config.JavaCustomizeComponent;
import com.jaiaxn.adaptation.utils.dao.BaseDao;
import com.jaiaxn.adaptation.dto.AdaptationRequest;
import com.jaiaxn.adaptation.utils.page.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 定制化java类需实现JavaCustomizeComponent接口，并且必须添加注解：@Service（org.springframework.stereotype.Service）
 * exec即为默认执行方法
 * adaptationRequest为页面入参，execSql为数据库配置生成的最终sql
 *
 * @author: wang.jiaxin
 * @date: 2019年07月10日
 * @description: 定制化java类demo
 **/
@Slf4j
@Service
public class DemoJavaClazzTest extends BaseDao implements JavaCustomizeComponent {

    private static Logger logger = Logger.getLogger(DemoJavaClazzTest.class);

    private final BaseDao baseDao;

    @Autowired
    public DemoJavaClazzTest(BaseDao baseDao) {
        this.baseDao = baseDao;
    }

    @Override
    public Map<String, Object> exec(AdaptationRequest adaptationRequest, String execSql) {
        Map<String, Object> resultMap = Maps.newHashMap();
        resultMap.put("req", JSON.toJSON(adaptationRequest));
        resultMap.put("execSql", execSql);
        try {
            Page page = super.queryPageDataByPageForOracle(execSql, 1, 10, adaptationRequest.getParamMap());
            resultMap.put("rows", page.getList());
            resultMap.put("records", page.getTotalNumber());
            resultMap.put("page", page.getCurrentPage());
            resultMap.put("pageSize", page.getPageSize());
            resultMap.put("total", page.getTotalPage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return resultMap;
    }
}
