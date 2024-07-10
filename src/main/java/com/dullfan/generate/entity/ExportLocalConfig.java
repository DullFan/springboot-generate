package com.dullfan.generate.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ExportLocalConfig extends BaseConfig{
    /**
     * 导出地址
     */
    private String exportLocal = "";
}
