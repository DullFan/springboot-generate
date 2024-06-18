package com.dullfan.generate.service.impl;

import com.alibaba.druid.DbType;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.statement.*;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlCreateTableStatement;
import com.alibaba.fastjson2.JSONObject;
import com.dullfan.generate.config.DullJavaConfig;
import com.dullfan.generate.entity.*;
import com.dullfan.generate.entity.do_.Table;
import com.dullfan.generate.entity.do_.TablePrimaryKey;
import com.dullfan.generate.entity.do_.TableStructure;
import com.dullfan.generate.mapper.DatabaseManagementMapper;
import com.dullfan.generate.service.DatabaseManagementService;
import com.dullfan.generate.utils.StringUtils;
import com.dullfan.generate.utils.constant.Constants;
import com.dullfan.generate.utils.extremely.ServiceException;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
public class DatabaseManagementServiceImpl implements DatabaseManagementService {

    @Resource
    private DruidDataSource dataSource;

    @Resource
    private DatabaseManagementMapper databaseManagementMapper;

    @Override
    public void switchDatabase(String newIp, String newPort, String newDatabaseName, String username, String password) {
        if (StringUtils.isEmpty(newIp) || StringUtils.isEmpty(newPort) || StringUtils.isEmpty(newDatabaseName) || StringUtils.isEmpty(username) || StringUtils.isEmpty(password))
            return;
        try {
            // 关闭当前数据源
            if (!dataSource.isClosed()) {
                dataSource.close();
            }
            // 构建新的 JDBC URL
            String newUrl = String.format("jdbc:mysql://%s:%s/%s?serverTimezone=Asia/Shanghai&useUnicode=true&characterEncoding=UTF-8&useSSL=false&allowPublicKeyRetrieval=true", newIp, newPort, newDatabaseName);
            dataSource.restart();
            // 重新配置数据源
            dataSource.setUrl(newUrl);
            dataSource.setUsername(username);
            dataSource.setPassword(password);
            // 超时时间设置
            dataSource.setMaxWait(8000);
            // 失败后重连的次数
            dataSource.setConnectionErrorRetryAttempts(3);
            // 请求失败之后中断
            dataSource.setBreakAfterAcquireFailure(true);
            // 切换之后判断是否切换成功
            SELECT1();
        } catch (Exception e) {
            throw new RuntimeException("连接超时或数据库不存在", e);
        }
    }

    @Override
    public List<TableInfo> selectListTables(Boolean tableStructureFlag) {
        List<Table> listTables = databaseManagementMapper.selectListTables();
        List<TableInfo> tableInfoList = new ArrayList<>();
        for (Table listTable : listTables) {
            TableInfo tableInfo = new TableInfo();
            String beanName = listTable.getName();
            tableInfo.setTableName(beanName);
            if (DullJavaConfig.getTablePrefix()) {
                beanName = beanName.substring(beanName.indexOf("_") + 1);
            }
            tableInfo.setBeanName(StringUtils.convertToCamelCase(beanName));
            tableInfo.setComment(listTable.getComment());
            tableInfo.setBeanParamName(beanName + DullJavaConfig.getTablePrefix());
            if (tableStructureFlag) selectTableStructure(tableInfo, null);
            if (tableStructureFlag) selectPrimaryKey(tableInfo, null);
            tableInfoList.add(tableInfo);
        }
        return tableInfoList;
    }

    /**
     * 解析SQL
     * @param sql
     * @return
     */
    @Override
    public List<TableInfo> selectListSQLTables(String sql) {
        List<SQLStatement> sqlStatements = SQLUtils.parseStatements(sql, DbType.mysql);
        List<TableInfo> tableInfoList = new ArrayList<>();

        for (SQLStatement sqlStatement : sqlStatements) {
            List<TableStructure> tableStructureList = new ArrayList<>();
            List<TablePrimaryKey> tablePrimaryKeyList = new ArrayList<>();
            if (sqlStatement instanceof MySqlCreateTableStatement) {
                MySqlCreateTableStatement createTableStatement = (MySqlCreateTableStatement) sqlStatement;
                String tableName = createTableStatement.getTableName().replaceAll("`", "");
                String tableComment;
                if(createTableStatement.getComment() == null){
                    tableComment = "";
                } else {
                    tableComment = createTableStatement.getComment().toString().replaceAll("'", "");
                }
                TableInfo tableInfo = new TableInfo();
                tableInfo.setTableName(tableName);
                if (DullJavaConfig.getTablePrefix()) {
                    tableName = tableName.substring(tableName.indexOf("_") + 1);
                }
                tableInfo.setBeanName(StringUtils.convertToCamelCase(tableName));
                tableInfo.setComment(tableComment);

                for (SQLColumnDefinition columnDefinition : createTableStatement.getColumnDefinitions()) {
                    TableStructure tableStructure = new TableStructure();
                    String columnName = columnDefinition.getName().toString().replaceAll("`", "");
                    String columnComment;
                    if(columnDefinition.getComment() == null){
                        columnComment = "";
                    } else {
                        columnComment = columnDefinition.getComment().toString().replaceAll("'", "");
                    }
                    String columnType = columnDefinition.getDataType().toString();
                    if (columnDefinition.isAutoIncrement()) {
                        tableStructure.setExtra("auto_increment");
                    }
                    tableStructure.setField(columnName);
                    tableStructure.setType(columnType);
                    tableStructure.setComment(columnComment);
                    tableStructureList.add(tableStructure);
                }

                for (SQLTableElement element : createTableStatement.getTableElementList()) {
                    String elementString = element.toString();
                    if (elementString.contains("PRIMARY KEY")) {
                        Pattern pattern = Pattern.compile("\\(([^)]+)\\)");
                        Matcher matcher = pattern.matcher(elementString);
                        if (matcher.find()) {
                            String[] columns = matcher.group(1).split(",\\s*");
                            for (String columnName : columns) {
                                TablePrimaryKey tablePrimaryKey = new TablePrimaryKey();
                                tablePrimaryKey.setNonUnique(0);
                                tablePrimaryKey.setKeyName("PRIMARY");
                                tablePrimaryKey.setColumnName(columnName.trim().replace("`", "")); // 移除反引号，如果存在
                                tablePrimaryKeyList.add(tablePrimaryKey);
                            }
                        }
                    } else if (elementString.contains("UNIQUE KEY")) {
                        // 使用正则表达式提取唯一键名称和字段名
                        Pattern pattern = Pattern.compile("UNIQUE KEY `([^`]+)`.*?\\((.*?)\\)");
                        Matcher matcher = pattern.matcher(elementString);
                        if (matcher.find()) {
                            TablePrimaryKey tablePrimaryKey;
                            String uniqueKeyName = matcher.group(1);
                            String[] uniqueKeyFields = matcher.group(2).split(",\\s*");
                            for (String uniqueKeyField : uniqueKeyFields) {
                                tablePrimaryKey = new TablePrimaryKey();
                                tablePrimaryKey.setNonUnique(0);
                                String columnName = uniqueKeyField.trim().replaceAll("`", "");
                                tablePrimaryKey.setColumnName(columnName);
                                tablePrimaryKey.setKeyName(uniqueKeyName);
                                tablePrimaryKeyList.add(tablePrimaryKey);
                            }
                        }
                    }
                }
                tableInfoList.add(tableInfo);
                selectTableStructure(tableInfo,tableStructureList);
                selectPrimaryKey(tableInfo,tablePrimaryKeyList);
            }
        }
        return tableInfoList;
    }

    @Override
    public void updateConfig(ConfigBean configBean) {
        if(StringUtils.isEmpty(configBean.getAuthor())){
            configBean.setAuthor(DullJavaConfig.getAuthor());
        }
        switchDatabase(configBean.getSqlIp(), configBean.getIpPort(), configBean.getSqlName(), configBean.getSqlUsername(), configBean.getSqlPassword());
        if (configBean.getSpringBootVersion() == null) configBean.setSpringBootVersion(3);
        if (configBean.getSpringBootVersion() == 2) {
            DullJavaConfig.setSpringBoot2();
        } else if (configBean.getSpringBootVersion() == 3) {
            DullJavaConfig.setSpringBoot3();
        } else {
            // 默认为SpringBoot3
            DullJavaConfig.setSpringBoot3();
        }
        DullJavaConfig.setStaticFieldIgnore(configBean.getFieldIgnoreList());
        DullJavaConfig.setStaticPackageBase(configBean.getPackageBase());
        DullJavaConfig.setStaticTablePrefix(configBean.isTablePrefix());
        DullJavaConfig.setStaticAuthor(configBean.getAuthor());
    }

    @Override
    public Integer SELECT1() {
        return databaseManagementMapper.SELECT1();
    }

    /**
     * 获取表字段
     */
    private void selectTableStructure(TableInfo tableInfo, List<TableStructure> tableStructure) {
        List<FieldInfo> fieldInfoList = new ArrayList<>();
        try {
            if (tableStructure == null) {
                tableStructure = databaseManagementMapper.selectTableStructure(tableInfo.getTableName());
            }
            boolean hasDateTime = false;
            boolean hasDate = false;
            boolean hasBigDecimal = false;
            boolean haveJsonIgnore = false;
            for (TableStructure structure : tableStructure) {
                String type = structure.getType();
                if (type.indexOf("(") > 0) {
                    type = type.substring(0, type.indexOf("("));
                }
                FieldInfo fieldInfo = new FieldInfo();
                fieldInfo.setFieldName(structure.getField());
                fieldInfo.setPropertyName(StringUtils.toCamelCase(structure.getField()));
                fieldInfo.setSqlType(type);
                fieldInfo.setComment(structure.getComment());
                fieldInfo.setAutoIncrementFlag("auto_increment".equalsIgnoreCase(structure.getExtra()));
                fieldInfo.setJavaType(StringUtils.processJavaType(type));
                if (ArrayUtils.contains(Constants.SQL_DATE_TIME_TYPES, type)) hasDateTime = true;
                if (ArrayUtils.contains(Constants.SQL_DATE_TYPES, type)) hasDate = true;
                if (ArrayUtils.contains(Constants.SQL_DECIMAL_TYPE, type)) hasBigDecimal = true;
                if (DullJavaConfig.getFieldIgnoreHashSet().contains(fieldInfo.getFieldName())) haveJsonIgnore = true;
                fieldInfoList.add(fieldInfo);
            }
            tableInfo.setHaveBigDecimal(hasBigDecimal);
            tableInfo.setHaveDateTime(hasDateTime);
            tableInfo.setHaveDate(hasDate);
            tableInfo.setHaveJsonIgnore(haveJsonIgnore);
            List<FieldInfo> extendList = new ArrayList<>();
            for (FieldInfo fieldInfo : fieldInfoList) {
                //String类型参数,
                if (ArrayUtils.contains(Constants.SQL_STRING_TYPE, fieldInfo.getSqlType())) {
                    FieldInfo field = new FieldInfo();
                    field.setJavaType(fieldInfo.getJavaType());
                    field.setPropertyName(fieldInfo.getPropertyName() + DullJavaConfig.getQueryFuzzy());
                    field.setFieldName(fieldInfo.getFieldName());
                    field.setSqlType(fieldInfo.getSqlType());
                    extendList.add(field);
                }
                //日期
                if (ArrayUtils.contains(Constants.SQL_DATE_TIME_TYPES, fieldInfo.getSqlType()) || ArrayUtils.contains(Constants.SQL_DATE_TYPES, fieldInfo.getSqlType())) {
                    FieldInfo field = new FieldInfo();
                    field.setJavaType("String");
                    field.setPropertyName(fieldInfo.getPropertyName() + DullJavaConfig.getQueryTimeStart());
                    field.setFieldName(fieldInfo.getFieldName());
                    field.setSqlType(fieldInfo.getSqlType());
                    extendList.add(field);
                    field = new FieldInfo();
                    field.setJavaType("String");
                    field.setPropertyName(fieldInfo.getPropertyName() + DullJavaConfig.getQueryTimeEnd());
                    field.setFieldName(fieldInfo.getFieldName());
                    field.setSqlType(fieldInfo.getSqlType());
                    extendList.add(field);
                }
            }
            tableInfo.setExtendFieldInfoList(extendList);
        } catch (Exception e) {
            throw new ServiceException(e);
        }
        tableInfo.setFieldInfoList(fieldInfoList);
    }

    /**
     * 获取表中索引以及主键
     *
     * @param tableInfo 表信息
     */
    private void selectPrimaryKey(TableInfo tableInfo, List<TablePrimaryKey> primaryKeyList) {
        try {
            if (primaryKeyList == null) {
                primaryKeyList = databaseManagementMapper.selectPrimaryKey(tableInfo.getTableName());
            }
            Map<String, FieldInfo> tempMap = new HashMap<>();
            for (FieldInfo fieldInfo : tableInfo.getFieldInfoList()) {
                tempMap.put(fieldInfo.getFieldName(), fieldInfo);
            }
            for (TablePrimaryKey tablePrimaryKey : primaryKeyList) {
                if (tablePrimaryKey.getNonUnique() == 1) continue;
                tableInfo.getKeyIndexMap()
                        .computeIfAbsent(tablePrimaryKey.getKeyName(), k ->
                                new ArrayList<>()).add(tempMap.get(tablePrimaryKey.getColumnName()));
            }
        } catch (Exception e) {
            throw new ServiceException(StringUtils.format("{}表不存在", tableInfo.getTableName()));
        }
    }


}
