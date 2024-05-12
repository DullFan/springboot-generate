package com.dullfan.generate.entity.do_;

import lombok.Data;

import java.util.Date;

@Data
public class Table {
    private String name;
    private String engine;
    private String version;
    private String rowFormat;
    private String rows;
    private String avgRowLength;
    private String DataLength;
    private String maxDataLength;
    private String indexLength;
    private String dataFree;
    private String autoIncrement;
    private Date createTime;
    private Date updateTime;
    private Date checkTime;
    private String collation;
    private String checksum;
    private String createOptions;
    private String comment;
}
