package com.dullfan.generate.mapper;

import com.dullfan.generate.entity.do_.Table;
import com.dullfan.generate.entity.do_.TablePrimaryKey;
import com.dullfan.generate.entity.do_.TableStructure;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DatabaseManagementMapper {
    List<Table> findListTables();

    List<TableStructure> findTableStructure(String tableName);

    List<TablePrimaryKey> findPrimaryKey(String tableName);

    Integer SELECT1();
}
