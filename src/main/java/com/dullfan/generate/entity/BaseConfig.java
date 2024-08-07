package com.dullfan.generate.entity;

import com.dullfan.generate.config.DullJavaConfig;
import lombok.Data;

@Data
public class BaseConfig {
    /**
     * 是否忽略表前缀
     */
    private boolean tablePrefix = false;
    /**
     * 是否使用Lombok插件
     */
    private boolean enabledLombok = false;
    /**
     * 包路径
     */
    private String packageBase = DullJavaConfig.getPackageBase();
    /**
     * 需要忽略的字段
     */
    private String fieldIgnoreList;

    /**
     * 作者
     */
    private String author = DullJavaConfig.getAuthor();

    /**
     * 逻辑删除字段
     */
    private String delFlagFields = DullJavaConfig.getDelFlagFields();

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
     * 生成路径
     */
    private String generatePath = "";
}
