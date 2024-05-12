package com.dullfan.generate.build;

import com.dullfan.generate.entity.TableInfo;

public class BuildFile {

    public static void execute(TableInfo tableInfo) {
        BuildPo.execute(tableInfo);
        BuildQuery.execute(tableInfo);
        BuildMapper.execute(tableInfo);
        BuildMapperXML.execute(tableInfo);
        BuildService.execute(tableInfo);
        BuildServiceImpl.execute(tableInfo);
        BuildController.execute(tableInfo);
    }
}
