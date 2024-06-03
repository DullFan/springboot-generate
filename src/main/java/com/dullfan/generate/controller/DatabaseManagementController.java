package com.dullfan.generate.controller;


import com.alibaba.fastjson2.JSONObject;
import com.dullfan.generate.build.*;
import com.dullfan.generate.config.DullJavaConfig;
import com.dullfan.generate.entity.ConfigBean;
import com.dullfan.generate.entity.TableInfo;
import com.dullfan.generate.service.DatabaseManagementService;
import com.dullfan.generate.utils.AjaxResult;
import com.dullfan.generate.utils.FileUtils;
import com.dullfan.generate.utils.StringUtils;
import com.dullfan.generate.utils.extremely.ServiceException;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.util.*;

@RestController("DatabaseManagementController")
@RequestMapping("/api")
public class DatabaseManagementController {

    @Resource
    DatabaseManagementService service;

    /**
     * 获取当前数据库中所有表
     */
    @PostMapping("/findAllSQLResource")
    public void findAllSQLResource(HttpServletResponse response, @RequestBody ConfigBean configBean) {
        createFile(response, configBean, true);
    }

    /**
     * 获取单表数据
     */
    @PostMapping("/findSQLResource")
    public void findSQLResource(HttpServletResponse response, @RequestBody ConfigBean configBean) {
        if (StringUtils.isEmpty(configBean.getDatabaseName())) {
            throw new ServiceException("databaseName为空");
        }
        createFile(response, configBean, false);
    }

    public void createFile(HttpServletResponse response, ConfigBean configBean, Boolean isAll) {
        service.updateConfig(configBean);
        HashSet<String> databaseHashSet = new HashSet<>(Arrays.asList(StringUtils.trim(configBean.getDatabaseName()).split(",")));
        List<TableInfo> listTables;
        if (StringUtils.isEmpty(configBean.getSqlStatement())) {
            listTables = service.selectListTables(true);
        } else {
            listTables = service.selectListSQLTables(configBean.getSqlStatement());
        }
        System.out.println(JSONObject.toJSONString(listTables));
        for (TableInfo tableInfo : listTables) {
            if (isAll || databaseHashSet.contains(tableInfo.getTableName())) {
                BuildFile.execute(tableInfo);
            }
        }
        String fileName = DullJavaConfig.getFileUUID() + ".zip";
        String fullPath = DullJavaConfig.getPathBaseTemporary() + fileName;
        BuildBase.execute();
        // 将生成的文件压缩成zip
        File directoryToZip = new File(DullJavaConfig.getPathBase());
        FileUtils.zipDirectory(directoryToZip, fullPath);
        // 下载文件
        FileUtils.downloadFile(response);
        // 删除生成的文件
        FileUtils.deleteFile(DullJavaConfig.getPathBaseTemporary() + DullJavaConfig.getFileUUID());
    }
    @PostMapping("/updateConfig")
    public AjaxResult updateConfig(@RequestBody ConfigBean configBean) {
        service.updateConfig(configBean);
        return AjaxResult.success();
    }

    /**
     * 获取当前数据库中所有字段
     */
    @PostMapping("/findAllSQLStructure")
    public AjaxResult findAllSQLStructure(@RequestBody ConfigBean configBean) {
        service.updateConfig(configBean);
        System.out.println(configBean.toString());
        List<TableInfo> listTables = service.selectListTables(false);
        return AjaxResult.success(listTables);
    }
}
