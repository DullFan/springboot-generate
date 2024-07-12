package com.dullfan.generate.utils;

import com.dullfan.generate.config.DullJavaConfig;

import java.util.ArrayList;
import java.util.List;

public class VelocityUtils {

    /**
     * 获取模板信息
     * @return 模板列表
     */
    public static List<String> getTemplateList() {
        List<String> templates = new ArrayList<>();
        templates.add("vm/po.java.vm");
        templates.add("vm/query.java.vm");
        templates.add("vm/mapper.java.vm");
        templates.add("vm/mapperXML.xml.vm");
        templates.add("vm/service.java.vm");
        templates.add("vm/serviceImpl.java.vm");
        templates.add("vm/controller.java.vm");
        return templates;
    }

    /**
     * 获取通用类模板信息
     * @return 模板列表
     */
    public static List<String> getBaseTemplateList() {
        List<String> templates = new ArrayList<>();
        templates.add("vm_base/ABaseController.java.vm");
        templates.add("vm_base/ABaseMapper.java.vm");
        templates.add("vm_base/ABaseParam.java.vm");
        templates.add("vm_base/HttpStatus.java.vm");
        templates.add("vm_base/PageSizeEnum.java.vm");
        templates.add("vm_base/PaginationResultVo.java.vm");
        templates.add("vm_base/Result.java.vm");
        templates.add("vm_base/SimplePage.java.vm");
        templates.add("vm_base/StringUtils.java.vm");
        return templates;
    }

    /**
     * 获取文件名
     */
    public static String getFileName(String template, String beanName) {
        // 文件名称
        String fileName = "";
        String pathPackageBase = DullJavaConfig.PROJECT_PATH + "/" + DullJavaConfig.getPackageBase().replace(".","/");
        String pathPackageBaseXML = DullJavaConfig.MYBATIS_PATH;
        // 包路径
//        String pathPackageBase = DullJavaConfig.getPathPackageBase();
        if (template.contains("po.java.vm")) {
            fileName = StringUtils.format("{}/entity/po/{}.java", pathPackageBase, beanName);
        } else if (template.contains("query.java.vm")) {
            fileName = StringUtils.format("{}/entity/query/{}Query.java", pathPackageBase, beanName);
        } else if (template.contains("mapper.java.vm")) {
            fileName = StringUtils.format("{}/mappers/{}Mapper.java", pathPackageBase, beanName);
        } else if (template.contains("mapperXML.xml.vm")) {
            fileName = StringUtils.format("{}/{}Mapper.xml", pathPackageBaseXML, beanName);
        } else if (template.contains("service.java.vm")) {
            fileName = StringUtils.format("{}/service/{}Service.java", pathPackageBase, beanName);
        } else if (template.contains("serviceImpl.java.vm")) {
            fileName = StringUtils.format("{}/service/impl/{}ServiceImpl.java", pathPackageBase, beanName);
        } else if (template.contains("controller.java.vm")) {
            fileName = StringUtils.format("{}/controller/{}Controller.java", pathPackageBase, beanName);
        }
        System.out.println(fileName);
        return fileName;
    }

    /**
     * 获取Base文件名
     */
    public static String getBaseFileName(String template) {
        // 文件名称
        String fileName = "";
        String pathPackageBase = DullJavaConfig.PROJECT_PATH + "/" + DullJavaConfig.getPackageBase().replace(".","/");
        // 包路径
//        String pathPackageBase = DullJavaConfig.getPathPackageBase();
        if (template.contains("ABaseController.java.vm")) {
            fileName = StringUtils.format("{}/controller/ABaseController.java", pathPackageBase);
        } else if (template.contains("ABaseMapper.java.vm")) {
            fileName = StringUtils.format("{}/mappers/ABaseMapper.java", pathPackageBase);
        } else if (template.contains("ABaseParam.java.vm")) {
            fileName = StringUtils.format("{}/entity/query/ABaseParam.java", pathPackageBase);
        } else if (template.contains("HttpStatus.java.vm")) {
            fileName = StringUtils.format("{}/utils/constant/HttpStatus.java", pathPackageBase);
        } else if (template.contains("PageSizeEnum.java.vm")) {
            fileName = StringUtils.format("{}/utils/enums/PageSizeEnum.java", pathPackageBase);
        } else if (template.contains("PaginationResultVo.java.vm")) {
            fileName = StringUtils.format("{}/entity/vo/PaginationResultVo.java", pathPackageBase);
        } else if (template.contains("Result.java.vm")) {
            fileName = StringUtils.format("{}/entity/vo/Result.java", pathPackageBase);
        } else if (template.contains("SimplePage.java.vm")) {
            fileName = StringUtils.format("{}/entity/query/SimplePage.java", pathPackageBase);
        } else if (template.contains("StringUtils.java.vm")) {
            fileName = StringUtils.format("{}/utils/StringUtils.java", pathPackageBase);
        }
        return fileName;
    }
}
