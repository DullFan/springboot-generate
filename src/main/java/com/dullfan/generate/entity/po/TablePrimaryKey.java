package com.dullfan.generate.entity.po;

import lombok.Data;

@Data
public class TablePrimaryKey {
    private String table;
    private Integer nonUnique;
    private String keyName;
    private String seqInIndex;
    private String columnName;
    private String collation;
    private String cardinality;
    private String subPart;
    private String packed;
    private String nullz;
    private String indexType;
    private String comment;
    private String indexComment;
    private String visible;
    private String expression;
}
