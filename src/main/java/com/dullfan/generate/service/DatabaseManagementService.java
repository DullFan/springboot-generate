package com.dullfan.generate.service;


import com.dullfan.generate.entity.*;

import java.util.List;

public interface DatabaseManagementService {

    /**
     * 切换数据库
     *
     * @param sqlIP   数据库IP
     * @param sqlPort 数据库端口号
     * @param sqlName 数据库名称
     * @param sqlUserName 数据库用户名
     * @param sqlPassword 数据库密码
     */
    void switchDatabase(String sqlIP, String sqlPort, String sqlName, String sqlUserName, String sqlPassword);

    /**
     * 获取数据库中所有表
     *
     * @param tableStructureFlag 是否获取表结构
     * @return 表列表
     */
    List<TableInfo> selectListTables(Boolean tableStructureFlag);

    /**
     * 根据SQL语句生成代码
     *
     * @return 表列表
     */
    List<TableInfo> selectListSQLTables(String sql);

    /**
     * 设置配置
     * @param configBean
     */
    void updateConfig(ConfigBean configBean);

    /**
     * 验证数据库是否连接
     */
    Integer SELECT1();
}
