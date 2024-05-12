package com.dullfan.generate.entity.do_;

import lombok.Data;

@Data
public class TableStructure {
    private String field;
    private String type;
    private String collation;
    private String nullz;
    private String key;
    private String defaultz;
    private String extra;
    private String privileges;
    private String comment;
}
