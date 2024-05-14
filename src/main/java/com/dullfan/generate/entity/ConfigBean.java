package com.dullfan.generate.entity;

import lombok.Data;

@Data
public class ConfigBean {
    /**
     * 是否忽略表前缀
     */
    private boolean tablePrefix;
    /**
     * 包路径
     */
    private String packageBase;
    /**
     * 需要忽略的字段
     */
    private String fieldIgnoreList;

    /**
     * 作者
     */
    private String author;

    /**
     * 数据库IP
     */
    private String sqlIp;

    /**
     * 数据库端口
     */
    private String ipPort;

    /**
     * 数据库名称
     */
    private String sqlName;

    /**
     * 数据库用户名
     */
    private String sqlUsername;

    /**
     * 数据库密码
     */
    private String sqlPassword;

    /**
     * SpringBoot 版本
     */
    private Integer springBootVersion = 3;

    /**
     * 单表名称列表
     */
    private String databaseName = "";

    /**
     * SQL语句
     */
    private String sqlStatement = "";
}
