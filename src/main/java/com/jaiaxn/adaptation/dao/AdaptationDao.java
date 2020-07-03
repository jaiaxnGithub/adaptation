package com.jaiaxn.adaptation.dao;

import com.jaiaxn.adaptation.model.AdaptationServer;
import com.jaiaxn.adaptation.model.AdaptationServerParam;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author: wang.jiaxin
 * @date: 2019年07月08日
 * @description: 适配基础服务DAO
 **/
@Component
@Mapper
public interface AdaptationDao {

    /**
     * 根据服务编码和服务类型查询适配服务
     * @param adaptationServer 服务编码、服务类型
     * @return 适配服务
     */
    @Select("select a.id,a.server_sql,a.clazz_name,a.param_code " +
            "from server_adaptation a " +
            "where a.state = 0 and a.server_code = #{adaptation.serverCode} " +
            "and a.server_type = #{adaptation.serverType} ")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "serverSql", column = "server_sql"),
            @Result(property = "clazzName", column = "clazz_name"),
            @Result(property = "paramCode", column = "param_code")
    })
    AdaptationServer queryAdaptationServer(@Param("adaptation") AdaptationServer adaptationServer);

    /**
     * 根据参数编码查询适配服务参数
     * @param adaptationServerParam 服务参数编码
     * @param pageCodes 参数编码集合
     * @return 适配服务参数
     */
    @Select("<script>" +
            "select a.id,a.param_code,a.page_code,a.db_code,a.param_sql,a.param_sign " +
            "from param_adaptation a " +
            "where a.state = 0 and a.param_code = #{adaptationParam.paramCode} " +
            "and a.page_code in " +
            "<foreach collection=\"pageCodes\" index=\"index\" item=\"pageCode\" open=\"(\" close=\")\" separator=\",\">" +
            "#{pageCode}" +
            "</foreach>" +
            "</script>")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "pageCode", column = "page_code"),
            @Result(property = "dbCode", column = "db_code"),
            @Result(property = "paramSql", column = "param_sql"),
            @Result(property = "paramSign", column = "param_sign")
    })
    List<AdaptationServerParam> queryAdaptationServerParam(@Param("adaptationParam") AdaptationServerParam adaptationServerParam, @Param("pageCodes") List<String> pageCodes);
}