package com.jaiaxn.adaptation.clazz;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.jaiaxn.adaptation.config.JavaCustomizeComponent;
import com.jaiaxn.adaptation.utils.dao.BaseDao;
import com.jaiaxn.adaptation.dto.AdaptationRequest;
import com.jaiaxn.adaptation.utils.dto.CfgDataSource;
import com.jaiaxn.adaptation.utils.encrypt.EncryptUtil;
import com.jaiaxn.adaptation.utils.page.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
@Slf4j
@Service
public class DemoJavaClazzTest extends BaseDao implements JavaCustomizeComponent {

    private static Logger logger = Logger.getLogger(DemoJavaClazzTest.class);

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

    /**
     * demo-设置动态数据源，执行sql
     *
     * @param paramMap 入参
     * @param execSql  执行sql
     * @return 结果集
     */
    public Map queryListTest(Map paramMap, String execSql) {
        Map<String, Object> resultMap = Maps.newHashMap();
        CfgDataSource dataSource = new CfgDataSource();
        dataSource.setDbType("oracle");
        dataSource.setDbDriverClassName("oracle.jdbc.driver.OracleDriver");
        dataSource.setDbUrl("url");
        dataSource.setDbUserName("username");
        // 保证安全性（传输、数据库中不存明文），加密的数据库密码（可以是页面上传的，也可以是数据库查的）
        String encPwd = "Ws5oJFg7u8vro2O2YE+1fQ==";
        String decPwd = EncryptUtil.getDecrypt(encPwd);
        dataSource.setDbPassword(decPwd);
        dataSource.setMaxActive(100);
        dataSource.setConnectionTimeOut(100);
        dataSource.setMaxIdle(10);
        dataSource.setRemoveAbandoneTimeout(180);
        // 设置数据源
        super.getDataSourcePool(dataSource);
        // 在特定数据源下执行sql
        List<Map<String, Object>> list = super.queryForList(execSql, paramMap);
        resultMap.put("rows", list);
        return resultMap;
    }
}