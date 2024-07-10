package com.dullfan.generate.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class SingleTableConfig extends BaseConfig {
    /**
     * 单表名称列表
     */
    private String databaseName = "";
}
