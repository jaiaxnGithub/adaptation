package com.jaiaxn.adaptation.utils.dao;

import com.jaiaxn.adaptation.ApplicationContextProvider;
import com.jaiaxn.adaptation.utils.dto.CfgDataSource;
import com.jaiaxn.adaptation.utils.page.Page;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectItem;
import net.sf.jsqlparser.statement.select.SetOperationList;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.*;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;

import java.io.StringReader;
import java.sql.Clob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * NamedParameterJdbcTemplate请求封装类
 * 使用法法：1、继承，在子类中super.method即可使用
 * 2、在该类有注解@org.springframework.stereotype.Service的情况下可以使用注解@org.springframework.beans.factory.annotation.Autowired来获取实例
 *
 * @author: wang.jiaxin
 * @date: 2019年07月09日
 * @description:
 **/
@Slf4j
@Service
public class BaseDao {

    private static final Map<Integer, BasicDataSource> DATA_SOURCE_POOL = new ConcurrentHashMap<>();

    private static final String SQL_DOT_TAG = ".";
    private static final String SQL_SPACE_TAG = " ";

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    protected JdbcOperations getJdbcOperations() {
        return getParameterJdbcTemplate().getJdbcOperations();
    }

    protected void setCacheLimit(int cacheLimit) {
        getParameterJdbcTemplate().setCacheLimit(cacheLimit);
    }

    protected int getCacheLimit() {
        return getParameterJdbcTemplate().getCacheLimit();
    }

    NamedParameterJdbcTemplate getParameterJdbcTemplate() throws DataAccessException {
        if (namedParameterJdbcTemplate == null) {
            log.info("===================初始化namedParameterJdbcTemplate==============");
            namedParameterJdbcTemplate = (NamedParameterJdbcTemplate) ApplicationContextProvider.getBean("namedParameterJdbcTemplate");
            if (namedParameterJdbcTemplate != null) {
                log.info("===================初始化namedParameterJdbcTemplate成功============");
            } else {
                log.error("===================获取namedParameterJdbcTemplate失败============");
                throw new DataAccessException("获取namedParameterJdbcTemplate失败") {
                };
            }
        }
        return namedParameterJdbcTemplate;
    }

    /**
     * 设置动态数据源，获取连接池
     *
     * @param cfgDataSource 数据源实体
     */
    public void getDataSourcePool(CfgDataSource cfgDataSource) {
        BasicDataSource basicDataSource = createDataSourcePool(cfgDataSource);
        namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(basicDataSource);
    }

    /**
     * 创建数据源
     *
     * @param cfgDataSource 数据源实体
     * @return BasicDataSource
     */
    public static BasicDataSource createDataSourcePool(CfgDataSource cfgDataSource) {
        int newDatasourceId = cfgDataSource.getDatasourceId();
        BasicDataSource basicDataSource = DATA_SOURCE_POOL.get(newDatasourceId);
        if (null != basicDataSource) {
            return basicDataSource;
        } else {
            if (!DATA_SOURCE_POOL.isEmpty()){
                int oldDatasourceId = new ArrayList<>(DATA_SOURCE_POOL.keySet()).get(0);
                BasicDataSource oldDruidDataSource = DATA_SOURCE_POOL.get(oldDatasourceId);
                try {
                    oldDruidDataSource.close();
                } catch (SQLException e) {
                    log.error("oldDruidDataSource close error, error = {}", e.getMessage());
                }
                DATA_SOURCE_POOL.clear();
            }
            basicDataSource = new BasicDataSource();
            try {
                basicDataSource.setDriverClassName(cfgDataSource.getDbDriverClassName());
                basicDataSource.setUrl(cfgDataSource.getDbUrl());
                basicDataSource.setUsername(cfgDataSource.getDbUserName());
                basicDataSource.setPassword(cfgDataSource.getDbPassword());
                //最大空闲连接
                basicDataSource.setMaxIdle(cfgDataSource.getMaxIdle());
                //最大连接数
                basicDataSource.setMaxTotal(cfgDataSource.getMaxActive());
                basicDataSource.setMaxWaitMillis(cfgDataSource.getConnectionTimeOut());
                //超时时间
                basicDataSource.setRemoveAbandonedTimeout(cfgDataSource.getRemoveAbandoneTimeout());
            } catch (Exception e) {
                log.error("createDataSourcePool error, error = {}", e.getMessage());
            }
        }
        return basicDataSource;
    }

    /**
     * update方法
     *
     * @param sql      sql
     * @param paramMap 入参
     * @return
     * @throws DataAccessException
     */
    public int update(String sql, Map<String, ?> paramMap) throws DataAccessException {
        log.info("print sql is ----:" + sql);
        log.info("print paramMap is ----:" + paramMap);
        return getParameterJdbcTemplate().update(sql, paramMap);
    }

    /**
     * 动态执行sql返回列
     *
     * @param sql      sql
     * @param paramMap 入参
     * @return 列
     * @throws Exception
     */
    public Map executeSqlForRowSet(String sql, Map paramMap) {
        log.info("print sql is ----:" + sql);
        log.info("print paramMap is ----:" + paramMap);
        SqlRowSet rowSet = namedParameterJdbcTemplate.queryForRowSet(sql, paramMap);
        int colCount = rowSet.getMetaData().getColumnCount();
        String[] columns = rowSet.getMetaData().getColumnNames();
        Map<String, String> result = new LinkedHashMap<>();
        for (int i = 0; i < colCount; i++) {
            result.put(columns[i], "");
        }
        return result;
    }

    /**
     * oracle分页查询-返回分页对象
     *
     * @param sql      执行脚本
     * @param offset   偏移量
     * @param limit    每页显示的行数
     * @param paramMap 入参
     * @return
     * @throws Exception
     */
    public <T> Page<T> queryPageDataByPageForOracle(String sql, int offset, int limit, Map paramMap) throws Exception {
        StringBuffer countSql = new StringBuffer();
        countSql.append("select count(1) from (").append(sql).append(")");
        int count = executeSqlForInt(countSql.toString(), paramMap);
        if (count == 0) {
            return new Page<>();
        }
        int startNum = offset;
        int endNum = offset + limit - 1;
        StringBuffer querySql = new StringBuffer();
        querySql.append("select * from (select rownum as idx,tt.* from ( ").append(sql).append(" ) tt ) ttt where ttt.idx between ")
                .append(startNum).append(" and ").append(endNum);
        log.info("print sql is ----:" + querySql.toString());
        log.info("print paramMap is ----:" + paramMap);
        List result = namedParameterJdbcTemplate.queryForList(querySql.toString(), paramMap);
        return new Page<T>(result, limit, count, offset);
    }

    /**
     * oracle分页查询-返回list
     *
     * @param sql      执行脚本
     * @param offset   偏移量
     * @param limit    每页显示的行数
     * @param paramMap 入参
     * @return
     * @throws Exception
     */
    public List<Map<String, Object>> queryListDataByPageForOracle(String sql, int offset, int limit, Map paramMap) throws Exception {
        int startNum = offset;
        int endNum = offset + limit - 1;
        StringBuffer querySql = new StringBuffer();
        querySql.append("select * from (select rownum as idx,tt.* from ( ").append(sql).append(" ) tt ) ttt where ttt.idx between ")
                .append(startNum).append(" and ").append(endNum);
        log.info("print sql is ----:" + querySql.toString());
        log.info("print paramMap is ----:" + paramMap);
        return namedParameterJdbcTemplate.queryForList(querySql.toString(), paramMap);
    }

    /**
     * mysql分页-返回分页对象
     *
     * @param sql      执行脚本
     * @param offset   开始显示的行
     * @param limit    每页显示多少行
     * @param paramMap 入参
     * @return Page
     * @throws Exception
     */
    public <T> Page<T> queryPageDataByPageForMysql(String sql, int offset, int limit, Map paramMap) throws Exception {
        StringBuffer countSql = new StringBuffer();
        countSql.append("select count(1) from (").append(sql).append(")");
        int count = executeSqlForInt(countSql.toString(), paramMap);
        if (count == 0) {
            return new Page<>();
        }
        StringBuffer querySql = new StringBuffer();
        log.info("print sql is ----:" + querySql.toString());
        log.info("print paramMap is ----:" + paramMap);
        querySql.append("select * from (").append(sql).append(") t limit ").append(offset).append(",").append(limit);
        List result = namedParameterJdbcTemplate.queryForList(querySql.toString(), paramMap);
        return new Page<T>(result, limit, count, offset);
    }

    /**
     * mysql分页-返回list
     *
     * @param sql    执行脚本
     * @param offset 开始显示的行
     * @param limit  每页显示多少行
     * @param param  入参
     * @return list
     * @throws Exception
     */
    public List<Map<String, Object>> queryListDataByPageForMysql(String sql, int offset, int limit, Map param) throws Exception {
        StringBuffer querySql = new StringBuffer();
        querySql.append("select * from (").append(sql).append(") t limit ").append(offset).append(",").append(limit);
        return namedParameterJdbcTemplate.queryForList(querySql.toString(), param);
    }

    /**
     * 查询制定类型列
     *
     * @param sql          sql
     * @param paramMap     入参
     * @param requiredType 返回类型
     * @return
     * @throws DataAccessException
     */
    public <T> T queryForObject(String sql, Map<String, ?> paramMap, Class<T> requiredType)
            throws DataAccessException {
        log.info("print sql is ----:" + sql);
        log.info("print paramMap is ----:" + paramMap);
        try {
            return getParameterJdbcTemplate().queryForObject(sql, paramMap, requiredType);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    /**
     * 动态执行sql返回long类型
     *
     * @param sql      sql
     * @param paramMap 入参
     * @return
     */
    public Long executeSqlForLong(String sql, Map paramMap) throws Exception {
        log.info("print sql is ----:" + sql);
        log.info("print paramMap is ----:" + paramMap);
        return namedParameterJdbcTemplate.queryForObject(sql, paramMap, Long.class);
    }

    /**
     * 动态执行sql返回int类型
     *
     * @param sql      sql
     * @param paramMap 入参
     * @return
     */
    public int executeSqlForInt(String sql, Map paramMap) throws Exception {
        log.info("print sql is ----:" + sql);
        log.info("print paramMap is ----:" + paramMap);
        return getParameterJdbcTemplate().queryForObject(sql, paramMap, Integer.class);
    }

    /**
     * 查询返回List
     *
     * @param sql      sql
     * @param paramMap 入参
     * @return
     * @throws DataAccessException
     */
    public List<Map<String, Object>> queryForList(String sql, Map<String, ?> paramMap)
            throws DataAccessException {
        log.info("print sql is ----:" + sql);
        log.info("print paramMap is ----:" + paramMap);
        String[] propNoAs = propNameForQryColStr(sql);
        if (propNoAs == null) {
            return getParameterJdbcTemplate().queryForList(sql, paramMap);
        } else {
            return getParameterJdbcTemplate().query(sql, paramMap, (RowMapper) (rs, rowNum) -> saveValue(rs, propNoAs));
        }
    }

    /**
     * 查询返回Map
     *
     * @param sql      sql
     * @param paramMap 入参
     * @return
     * @throws DataAccessException
     */
    public Map<String, Object> queryForMap(String sql, Map<String, ?> paramMap) throws DataAccessException {
        String[] propNoAs = propNameForQryColStr(sql);
        log.info("print sql is ----:" + sql);
        log.info("print paramMap is ----:" + paramMap);
        if (propNoAs == null) {
            try {
                return getParameterJdbcTemplate().queryForMap(sql, paramMap);
            } catch (EmptyResultDataAccessException e) {
                return null;
            }
        } else {
            try {
                return (Map<String, Object>) getParameterJdbcTemplate().queryForObject(sql, paramMap, (RowMapper) (rs, rowNum) -> saveValue(rs, propNoAs));
            } catch (EmptyResultDataAccessException e) {
                log.error(e.getMessage(), e);
                return null;
            }
        }
    }

    /**
     * 执行动态sql语句返回list
     *
     * @param sql      sql
     * @param paramMap 参数
     */
    public List<Map<String, Object>> executeSqlForList(String sql, Map paramMap) {
        log.info("print sql is ----:" + sql);
        log.info("print paramMap is ----:" + paramMap);
        List<Map<String, Object>> resultList = namedParameterJdbcTemplate.queryForList(sql, paramMap);
        return resultList;
    }

    /**
     * 动态执行sql语句返回map
     *
     * @param sql      sql
     * @param paramMap 参数
     * @return
     */
    public Map<String, Object> executeSqlForMap(String sql, Map paramMap) {
        log.info("print sql is ----:" + sql);
        log.info("print paramMap is ----:" + paramMap);
        Map<String, Object> resultMap = namedParameterJdbcTemplate.queryForMap(sql, paramMap);
        return resultMap;
    }

    /**
     * 获取序列
     *
     * @param sequenceName 序列名
     * @return
     */
    public Long getSequence(String sequenceName) {
        String sql = "select " + sequenceName + ".nextval from dual";
        log.info("print sql is ----:" + sql);
        try {
            return getParameterJdbcTemplate().queryForObject(sql, new HashMap(), Long.class);
        } catch (EmptyResultDataAccessException e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    /**
     * 从qryColSQL中抽取属性名称
     *
     * @param fromStr String
     * @return String[]
     * @throws JSQLParserException
     */
    private String[] propNameForQryColStr(String fromStr) {
        //JSqlParser解析
        CCJSqlParserManager parserManager = new CCJSqlParserManager();
        List<String> colList = new ArrayList<String>();
        try {
            if (fromStr != null && (fromStr.indexOf("SELECT") < 0 && fromStr.indexOf("select") < 0)) {
                fromStr = "select " + fromStr;
            }
            Select select = (Select) parserManager.parse(new StringReader(fromStr));
            Object selectBody = select.getSelectBody();
            PlainSelect pSelect = null;
            if (selectBody instanceof PlainSelect) {
                pSelect = ((PlainSelect) select.getSelectBody());
            } else if (selectBody instanceof SetOperationList) {
                pSelect = (PlainSelect) ((SetOperationList) selectBody).getSelects().get(0);
            }
            List<SelectItem> itemList = pSelect.getSelectItems();
            for (SelectItem si : itemList) {
                if (si.toString().indexOf(SQL_SPACE_TAG) != -1) {
                    String[] _s = split(si.toString(), SQL_SPACE_TAG);
                    colList.add(_s[_s.length - 1]);
                } else if (si.toString().indexOf(SQL_DOT_TAG) != -1) {
                    // 如果带别名前缀 比如A.NAME,需要去掉A.只保留NAME
                    String[] _s = split(si.toString(), SQL_DOT_TAG);
                    colList.add(_s[_s.length - 1]);
                } else {
                    // 用列名做属性值
                    colList.add(si.toString());
                }
            }
        } catch (JSQLParserException e) {
            log.error(e.getMessage(), e);
            return null;
        }
        return colList.toArray(new String[0]);
    }

    private Map saveValue(ResultSet rs, String[] propNoAs) throws SQLException {
        Map tempMap = new HashMap();
        for (int i = 0; i < propNoAs.length; i++) {
            if (!tempMap.containsKey(propNoAs[i].trim())) {
                Object o = rs.getObject(propNoAs[i]);
                if (o instanceof Clob) {
                    o = ((Clob) o).getSubString(1, (int) ((Clob) o).length());
                }
                tempMap.put(propNoAs[i], o);
            }
        }
        return tempMap;
    }

    private String[] split(String line, String separator) {
        LinkedList list = new LinkedList();
        if (line != null) {
            int start = 0;
            int end = 0;
            int separatorLen = separator.length();
            while ((end = line.indexOf(separator, start)) >= 0) {
                list.add(line.substring(start, end));
                start = end + separatorLen;
            }
            if (start < line.length()) {
                list.add(line.substring(start, line.length()));
            }
        }
        return (String[]) list.toArray(new String[list.size()]);
    }
}
