package com.dullfan.generate.controller;

import com.dullfan.generate.config.DullJavaConfig;
import com.dullfan.generate.entity.*;
import com.dullfan.generate.service.DatabaseManagementService;
import com.dullfan.generate.utils.*;
import com.dullfan.generate.utils.extremely.ServiceException;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@RestController("DatabaseManagementController")
@RequestMapping("/api")
public class DatabaseManagementController {

    @Resource
    DatabaseManagementService service;

    /**
     * 将代码生成到指定路径中
     */
    @PostMapping("/exportLocalCode")
    public Result exportLocalCode(@RequestBody ExportLocalConfig config) throws IOException {
        service.updateConfig(config);
        DullJavaConfig.setStaticPathBase(config.getExportLocal());
        List<TableInfo> listTables = service.selectListTables(true);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipOutputStream zip = new ZipOutputStream(outputStream);
        newCreateFile(listTables,zip);
        IOUtils.closeQuietly(zip);
        byte[] byteArray = outputStream.toByteArray();
        FileUtils.unzip(byteArray,DullJavaConfig.getPathBase());
        return Result.success();
    }

    /**
     * 获取当前数据库中所有表
     */
    @PostMapping("/generateCodeAll")
    public void generateCodeAll(HttpServletResponse response,@RequestBody BaseConfig config) throws IOException {
        service.updateConfig(config);
        List<TableInfo> listTables = service.selectListTables(true);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipOutputStream zip = new ZipOutputStream(outputStream);
        newCreateFile(listTables,zip);
        IOUtils.closeQuietly(zip);
        byte[] byteArray = outputStream.toByteArray();
        downloadFile(response,byteArray);
    }

    /**
     * 根据SQL语句生成代码
     */
    @PostMapping("/generateCodeBySQL")
    public void generateCodeBySQL(HttpServletResponse response, @RequestBody SQLStatementConfig config) throws IOException {
        service.updateConfig(config);
        if (StringUtils.isEmpty(config.getSqlStatement())) {
            throw new ServiceException("sqlStatement为空");
        }
        List<TableInfo> listTables = service.selectListSQLTables(config.getSqlStatement());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipOutputStream zip = new ZipOutputStream(outputStream);
        newCreateFile(listTables,zip);
        IOUtils.closeQuietly(zip);
        byte[] byteArray = outputStream.toByteArray();
        downloadFile(response,byteArray);
    }

    /**
     * 根据表名生成代码
     */
    @PostMapping("/generateCodeByTableName")
    public void generateCodeByTableName(HttpServletResponse response, @RequestBody SingleTableConfig config) throws IOException {
        service.updateConfig(config);
        if (StringUtils.isEmpty(config.getDatabaseName())) {
            throw new ServiceException("databaseName为空");
        }
        List<TableInfo> listTables = service.selectListTables(true);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipOutputStream zip = new ZipOutputStream(outputStream);
        HashSet<String> databaseHashSet = new HashSet<>(Arrays.asList(StringUtils.trim(config.getDatabaseName()).split(",")));
        List<TableInfo> newListTables = new ArrayList<>();

        for (TableInfo tableInfo : listTables) {
            if (databaseHashSet.contains(tableInfo.getTableName())) {
                newListTables.add(tableInfo);
            }
        }
        newCreateFile(newListTables,zip);
        IOUtils.closeQuietly(zip);
        byte[] byteArray = outputStream.toByteArray();
        downloadFile(response,byteArray);
    }

    /**
     * 获取当前数据库中所有字段
     */
    @PostMapping("/findAllSQLStructure")
    public Result findAllSQLStructure(@RequestBody BaseConfig configBean) {
        service.updateConfig(configBean);
        System.out.println(configBean.toString());
        List<TableInfo> listTables = service.selectListTables(false);
        return Result.success(listTables);
    }

    /**
     * 生成zip文件
     */
    private void downloadFile(HttpServletResponse response, byte[] data) throws IOException {
        response.reset();
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Expose-Headers", "Content-Disposition");
        response.setHeader("Content-Disposition", "attachment; filename=\"code.zip\"");
        response.addHeader("Content-Length", "" + data.length);
        response.setContentType("application/octet-stream; charset=UTF-8");
        IOUtils.write(data, response.getOutputStream());
    }

    public void newCreateFile(List<TableInfo> listTables, ZipOutputStream zip) {
        VelocityEngine velocityEngine = new VelocityEngine();
        velocityEngine.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
        velocityEngine.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
        velocityEngine.init();
        // 模板变量
        VelocityContext baseContext = new VelocityContext();
        // 包名
        baseContext.put("packageName", DullJavaConfig.getPackageBase());
        for (String templateBaseString : VelocityUtils.getBaseTemplateList()) {
            Template template = velocityEngine.getTemplate(templateBaseString);
            // 输出
            StringWriter sw = new StringWriter();
            template.merge(baseContext, sw);
            try {
                String fileName = VelocityUtils.getBaseFileName(templateBaseString);
                // 添加到zip
                zip.putNextEntry(new ZipEntry(fileName));
                IOUtils.write(sw.toString(), zip, "UTF-8");
                IOUtils.closeQuietly(sw);
                zip.flush();
                zip.closeEntry();
            } catch (IOException e) {
                throw new ServiceException("基类生成错误"+e.getMessage());
            }
        }

        VelocityInitializer.initVelocity();
        for (TableInfo tableInfo : listTables) {
            // 模板变量
            VelocityContext context = new VelocityContext();
            // 包名
            context.put("packageName", DullJavaConfig.getPackageBase());
            // 类名
            context.put("className", tableInfo.getBeanName());
            // 表名
            context.put("tableName",tableInfo.getTableName());
            // 表字段信息列表
            context.put("fieldInfoList", tableInfo.getFieldInfoList());
            // 表扩展字段信息列表
            context.put("extendFieldInfoList", tableInfo.getExtendFieldInfoList());
            // 主键Map
            context.put("keyIndexMap", tableInfo.getKeyIndexMap());
            // 是否使用Lombok
            context.put("enableLombok", DullJavaConfig.getEnabledLombok());
            // 是否有DateTime类型字段
            context.put("hasDateTime", tableInfo.getHaveDateTime());
            // 是否有Date类型字段
            context.put("hasDate", tableInfo.getHaveDate());
            // 是否有BigDecimal类型字段
            context.put("hasBigDecimal", tableInfo.getHaveBigDecimal());
            // 是否有需要JsonIgnore注解的字段
            context.put("hasJsonIgnore", tableInfo.getHaveJsonIgnore());
            // 作者
            context.put("author", DullJavaConfig.getAuthor());
            // 创建时间
            context.put("datetime", DateUtils.getTime());
            // 字符串工具类
            context.put("StringUtils", new StringUtils());
            // 字符串工具类
            context.put("importSpringBootHashMap", DullJavaConfig.getImportSpringBootHashMap());
            // 获取模板文件
            List<String> templates = VelocityUtils.getTemplateList();

            for (String templateString : templates) {
                Template template = velocityEngine.getTemplate(templateString);
                // 输出
                StringWriter sw = new StringWriter();
                template.merge(context, sw);
                try {
                    String fileName = VelocityUtils.getFileName(templateString, tableInfo.getBeanName());
                    // 添加到zip
                    zip.putNextEntry(new ZipEntry(fileName));
                    IOUtils.write(sw.toString(), zip, "UTF-8");
                    IOUtils.closeQuietly(sw);
                    zip.flush();
                    zip.closeEntry();
                } catch (IOException e) {
                    throw new ServiceException("条目重复,可能是表前缀忽略后导致重复");
                }
            }
        }
    }
}
