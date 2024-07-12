package com.dullfan.generate.entity;

import lombok.Data;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Data
public class TableInfo {
    /**
     * 表名
     */
    private String tableName;
    /**
     * bean名称
     */
    private String beanName;
    /**
     * 参数名称
     */
    private String beanParamName;
    /**
     * 表注释
     */
    private String comment;
    /**
     * 字段信息
     */
    private List<FieldInfo> fieldInfoList;
    /**
     * 唯一索引集合
     */
    private Map<String,List<FieldInfo>> keyIndexMap = new LinkedHashMap<>();
    /**
     * 是否有date类型
     */
    private Boolean haveDate = false;
    /**
     * 是否有事件类型
     */
    private Boolean haveDateTime = false;
    /**
     * 是否有BigDecimal类型
     */
    private Boolean haveBigDecimal = false;
    /**
     * 是否有要被忽略的对象
     */
    private Boolean haveJsonIgnore = false;
    /**
     * 是否有逻辑删除字段
     */
    private Boolean haveDelFlag = false;
    /**
     * 逻辑删除类型
     */
    private String haveDelFlagType = "String";

    /**
     * 扩展字段信息
     */
    private List<FieldInfo> extendFieldInfoList;
}
