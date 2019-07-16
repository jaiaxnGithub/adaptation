package com.jaiaxn.adaptation.service;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jaiaxn.adaptation.ApplicationContextProvider;
import com.jaiaxn.adaptation.config.JavaCustomizeComponent;
import com.jaiaxn.adaptation.constant.Constant;
import com.jaiaxn.adaptation.dao.AdaptationDao;
import com.jaiaxn.adaptation.utils.dao.BaseDao;
import com.jaiaxn.adaptation.dto.AdaptationRequest;
import com.jaiaxn.adaptation.utils.dto.ResultVO;
import com.jaiaxn.adaptation.model.AdaptationServer;
import com.jaiaxn.adaptation.model.AdaptationServerParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author: wang.jiaxin
 * @date: 2019年07月08日
 * @description: 数据库操作统一适配处理类
 **/
@Slf4j
@Service
public class AdaptationService {

    private final AdaptationDao adaptationDao;

    private final BaseDao baseDao;

    @Autowired
    public AdaptationService(AdaptationDao adaptationDao, BaseDao baseDao) {
        this.adaptationDao = adaptationDao;
        this.baseDao = baseDao;
    }

    /**
     * 数据库操作统一请求入口
     *
     * @param adaptationRequest 请求参数
     * @return 结果
     */
    public ResultVO<Map> adaptationExec(AdaptationRequest adaptationRequest) {
        log.info("AdaptationServiceImpl.adaptationExec req={}", JSON.toJSON(adaptationRequest));
        String serverType = adaptationRequest.getServerType();
        AdaptationServer adaptationServerReq = new AdaptationServer();
        adaptationServerReq.setServerCode(adaptationRequest.getServerCode());
        adaptationServerReq.setServerType(serverType);
        AdaptationServer adaptationServerResp = adaptationDao.queryAdaptationServer(adaptationServerReq);
        if (null != adaptationServerResp) {
            Map<String, Object> resultMap;
            StringBuffer execSql = new StringBuffer();
            //判断是否存在条件
            String paramCode = adaptationServerResp.getParamCode();
            if (StringUtils.isNotBlank(paramCode)) {
                execSql.append(adaptationServerResp.getServerSql());
                AdaptationServerParam adaptationServerParamReq = new AdaptationServerParam();
                adaptationServerParamReq.setParamCode(paramCode);
                Map<String, Object> paramMap = adaptationRequest.getParamMap();
                List<String> paramCodes = Lists.newArrayList();
                paramMap.forEach((k, v) -> paramCodes.add(k));
                List<AdaptationServerParam> adaptationServerParamList = adaptationDao.queryAdaptationServerParam(adaptationServerParamReq, paramCodes);
                if (CollectionUtils.isNotEmpty(adaptationServerParamList)) {
                    StringBuilder insertFieldSql = new StringBuilder();
                    StringBuilder insertValueSql = new StringBuilder();
                    StringBuilder updateSetSql = new StringBuilder();
                    StringBuilder updateWhereSql = new StringBuilder();
                    // 生成执行sql
                    getExecSql(serverType, execSql, adaptationServerParamList, insertFieldSql, insertValueSql, updateSetSql, updateWhereSql);
                    if (Constant.SERVER_TYPE_INSERT.equals(serverType)) {
                        execSql.append(insertFieldSql).append(") ").append(insertValueSql).append(")");
                    } else if (Constant.SERVER_TYPE_UPDATE.equals(serverType)) {
                        execSql.append(updateSetSql).append(updateWhereSql);
                    }
                    log.info("AdaptationService adaptationExec execSql-----{}", execSql.toString());
                }
            }

            // 判断是否存在自定义执行类
            resultMap = execCustomizeClazz(adaptationRequest, adaptationServerResp, execSql);
            log.info("AdaptationServiceImpl.adaptationExec resp={}", JSON.toJSON(resultMap));
            return ResultVO.success(resultMap);
        } else {
            return ResultVO.error("该服务不存在，请确认配置！");
        }
    }

    /**
     * 根据数据库配置生成sql
     *
     * @param serverType                服务类型
     * @param execSql                   最终sql
     * @param adaptationServerParamList 服务参数列表
     * @param insertFieldSql            插入sql-字段部分
     * @param insertValueSql            插入sql-值部分
     * @param updateSetSql              更新sql-set部分
     * @param updateWhereSql            更新sql-条件部分
     */
    private void getExecSql(String serverType, StringBuffer execSql, List<AdaptationServerParam> adaptationServerParamList, StringBuilder insertFieldSql, StringBuilder insertValueSql, StringBuilder updateSetSql, StringBuilder updateWhereSql) {
        adaptationServerParamList.forEach(execParam -> {
            String paramSign = execParam.getParamSign();
            String paramSql = execParam.getParamSql();
            if (Constant.SERVER_TYPE_SELECT.equals(serverType) || Constant.SERVER_TYPE_DELETE.equals(serverType)) {
                StringBuilder whereSql = new StringBuilder();
                if (Constant.PARAM_SIGN_WHERE.equals(paramSign)) {
                    if (StringUtils.isNotBlank(paramSql)) {
                        whereSql.append(" ").append(paramSql);
                    } else {
                        whereSql.append(" AND ").append(execParam.getDbCode()).append(" = ").append(":").append(execParam.getPageCode());
                    }
                    execSql.append(whereSql);
                }
            } else if (Constant.SERVER_TYPE_INSERT.equals(serverType)) {
                if (insertFieldSql.length() == 0) {
                    insertFieldSql.append(" (");
                    insertValueSql.append(" VALUES(");
                } else {
                    insertFieldSql.append(",");
                    insertValueSql.append(",");
                }
                insertFieldSql.append(execParam.getDbCode());
                insertValueSql.append(":").append(execParam.getPageCode());
            } else if (Constant.SERVER_TYPE_UPDATE.equals(serverType)) {
                if (Constant.PARAM_SIGN_SET.equals(paramSign)) {
                    if (StringUtils.isNotBlank(paramSql)) {
                        updateSetSql.append(" ").append(paramSql);
                    } else {
                        if (updateSetSql.length() == 0) {
                            updateSetSql.append(" SET ");
                        } else {
                            updateSetSql.append(" ,");
                        }
                        updateSetSql.append(execParam.getDbCode()).append(" = ").append(":").append(execParam.getPageCode());
                    }
                } else {
                    if (StringUtils.isNotBlank(paramSql)) {
                        updateWhereSql.append(" ").append(paramSql);
                    } else {
                        if (updateWhereSql.length() == 0) {
                            updateWhereSql.append(" WHERE ");
                        } else {
                            updateWhereSql.append(" AND ");
                        }
                        updateWhereSql.append(execParam.getDbCode()).append(" = ").append(":").append(execParam.getPageCode());
                    }
                }
            }
        });
    }

    /**
     * 判断是否存在自定义执行类并执行产生结果
     *
     * @param adaptationRequest    页面入参
     * @param adaptationServerResp 服务对象
     * @param execSql              数据库配置生成sql
     * @return 结果集
     */
    private Map<String, Object> execCustomizeClazz(AdaptationRequest adaptationRequest, AdaptationServer adaptationServerResp, StringBuffer execSql) {
        Map<String, Object> resultMap = Maps.newHashMap();
        String clazzName = adaptationServerResp.getClazzName();
        if (StringUtils.isNotBlank(clazzName)) {
            // 加载处理类
            JavaCustomizeComponent clazz = ApplicationContextProvider.getApplicationContext().getBean(clazzName, JavaCustomizeComponent.class);
            resultMap = clazz.exec(adaptationRequest, execSql.toString());
        } else {
            if (Constant.SERVER_TYPE_SELECT.equals(adaptationRequest.getServerType())) {
                List<Map<String, Object>> resultList = baseDao.queryForList(execSql.toString(), adaptationRequest.getParamMap());
                resultMap.put("data", resultList);
            } else {
                int updateNum = baseDao.update(execSql.toString(), adaptationRequest.getParamMap());
                resultMap.put("data", updateNum);
            }
        }
        return resultMap;
    }
}