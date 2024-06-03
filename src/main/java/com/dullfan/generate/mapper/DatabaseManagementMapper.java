package com.dullfan.generate.mapper;

import com.dullfan.generate.entity.do_.Table;
import com.dullfan.generate.entity.do_.TablePrimaryKey;
import com.dullfan.generate.entity.do_.TableStructure;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DatabaseManagementMapper {
    List<Table> selectListTables();

    List<TableStructure> selectTableStructure(String tableName);

    List<TablePrimaryKey> selectPrimaryKey(String tableName);

    Integer SELECT1();
}
