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
    @Select("SELECT A.ID,A.SERVER_SQL,A.CLAZZ_NAME,A.PARAM_CODE " +
            "FROM IOM_SERVER_ADAPTATION A " +
            "WHERE A.STATE = 0 AND A.SERVER_CODE = #{adaptation.serverCode} " +
            "AND A.SERVER_TYPE = #{adaptation.serverType} ")
    @Results({
            @Result(property = "id", column = "ID"),
            @Result(property = "serverSql", column = "SERVER_SQL"),
            @Result(property = "clazzName", column = "CLAZZ_NAME"),
            @Result(property = "paramCode", column = "PARAM_CODE")
    })
    AdaptationServer queryAdaptationServer(@Param("adaptation") AdaptationServer adaptationServer);

    /**
     * 根据参数编码查询适配服务参数
     * @param adaptationServerParam 服务参数编码
     * @param pageCodes 参数编码集合
     * @return 适配服务参数
     */
    @Select("<script>" +
            "SELECT A.ID,A.PARAM_CODE,A.PAGE_CODE,A.DB_CODE,A.PARAM_SQL,A.PARAM_SIGN " +
            "FROM IOM_SERVER_ADAPTATION_PARAM A " +
            "WHERE A.STATE = 0 AND A.PARAM_CODE = #{adaptationParam.paramCode} AND A.PAGE_CODE IN " +
            "<foreach collection=\"pageCodes\" index=\"index\" item=\"pageCode\" open=\"(\" close=\")\" separator=\",\">" +
            "#{pageCode}" +
            "</foreach>" +
            "</script>")
    @Results({
            @Result(property = "id", column = "ID"),
            @Result(property = "pageCode", column = "PAGE_CODE"),
            @Result(property = "dbCode", column = "DB_CODE"),
            @Result(property = "paramSql", column = "PARAM_SQL"),
            @Result(property = "paramSign", column = "PARAM_SIGN")
    })
    List<AdaptationServerParam> queryAdaptationServerParam(@Param("adaptationParam") AdaptationServerParam adaptationServerParam, @Param("pageCodes") List<String> pageCodes);
}