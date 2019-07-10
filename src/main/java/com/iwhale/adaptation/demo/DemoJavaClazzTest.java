package com.iwhale.adaptation.demo;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.iwhale.adaptation.config.JavaCustomizeComponent;
import com.iwhale.adaptation.utils.dao.BaseDao;
import com.iwhale.adaptation.dto.AdaptationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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
@Service
public class DemoJavaClazzTest implements JavaCustomizeComponent {

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
        List<Map<String, Object>> resultList = baseDao.queryForList(execSql, adaptationRequest.getParamMap());
        resultMap.put("data", resultList);
        return resultMap;
    }
}
