package com.dullfan.generate.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class SQLStatementConfig extends BaseConfig{
    /**
     * SQL语句
     */
    private String sqlStatement = "";
}
