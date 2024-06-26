package com.dullfan.generate.build;

import com.dullfan.generate.config.DullJavaConfig;
import com.dullfan.generate.entity.FieldInfo;
import com.dullfan.generate.entity.TableInfo;
import com.dullfan.generate.utils.StringUtils;
import com.dullfan.generate.utils.extremely.ServiceException;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@Slf4j
public class BuildServiceImpl {
    private static OutputStream out;
    private static OutputStreamWriter outputStreamWriter;
    private static BufferedWriter bw;


    public static void execute(TableInfo tableInfo) {
        File folder = new File(DullJavaConfig.getPathServiceImpl());
        if (!folder.exists()) if (!folder.mkdirs()) log.error(DullJavaConfig.getPathServiceImpl() + "创建失败");
        String className = tableInfo.getBeanName() + "ServiceImpl";
        File mappperFile = new File(folder, className + ".java");
        out = null;
        outputStreamWriter = null;
        bw = null;
        try {
            out = new FileOutputStream(mappperFile);
            outputStreamWriter = new OutputStreamWriter(out, StandardCharsets.UTF_8);
            bw = new BufferedWriter(outputStreamWriter);
            String beanQuery = tableInfo.getBeanName() + DullJavaConfig.getBeanQuery();
            writeText("package " + DullJavaConfig.getPackageServiceImpl() + ";");
            writeText("import java.util.List;");
            writeText(DullJavaConfig.getImportSpringBootHashMap().get("Resource"));
            writeText("import org.springframework.stereotype.Service;");
            writeText("import java.math.BigDecimal;");
            writeText("import " + DullJavaConfig.getPackageUtilsEnum() + ".PageSizeEnum;");
            writeText("import " + DullJavaConfig.getPackageQuery() + "." + beanQuery + ";");
            writeText("import " + DullJavaConfig.getPackagePo() + "." + tableInfo.getBeanName() + ";");
            writeText("import " + DullJavaConfig.getPackageVo() + ".PaginationResultVo;");
            writeText("import " + DullJavaConfig.getPackageQuery() + ".SimplePage;");

            writeText("import " + DullJavaConfig.getPackageMapper() + "." + tableInfo.getBeanName() + DullJavaConfig.getBeanMapper()
                    + ";");
            writeText("import " + DullJavaConfig.getPackageService() + "." + tableInfo.getBeanName() + DullJavaConfig.getBeanService()
                    + ";");
            writeText("import " + DullJavaConfig.getPackageUtils() + ".StringUtils;");

            BuildComment.createClassComment(bw, tableInfo.getComment() + " 业务接口实现");

            String anServiceBean = tableInfo.getBeanName() + DullJavaConfig.getBeanService();
            anServiceBean = anServiceBean.substring(0, 1).toLowerCase() + anServiceBean.substring(1);
            writeText("@Service(\"" + anServiceBean + "\")");
            writeText("public class " + tableInfo.getBeanName() + DullJavaConfig.getBeanServiceImpl() + " implements "
                    + tableInfo.getBeanName() + DullJavaConfig.getBeanService() + " {");
            writeText("\t@Resource");
            String beanName = tableInfo.getBeanName();
            String paramMapper = beanName + DullJavaConfig.getBeanMapper();
            paramMapper = paramMapper.substring(0, 1).toLowerCase() + paramMapper.substring(1);
            writeText("\tprivate " + tableInfo.getBeanName() + DullJavaConfig.getBeanMapper() + " " + paramMapper + ";");
            //根据条件查询列表
            BuildComment.createMethodComment(bw, "根据条件查询列表");
            writeText("\t@Override");
            writeText("\tpublic List<" + beanName + "> selectListByParam(" + beanQuery + " param) {");
            writeText("\t\treturn this." + paramMapper + ".selectList(param);");
            writeText("\t}");
            //根据条件查询记录数
            BuildComment.createMethodComment(bw, "根据条件查询列表");
            writeText("\t@Override");
            writeText("\tpublic Integer selectCountByParam(" + beanQuery + " param) {");
            writeText("\t\treturn this." + paramMapper + ".selectCount(param);");
            writeText("\t}");
            //分页查询的方法
            BuildComment.createMethodComment(bw, "分页查询方法");
            writeText("\t@Override");
            writeText("\tpublic PaginationResultVo<" + beanName + "> selectListByPage(" + beanQuery
                    + " param) {");
            writeText("\t\tint count = this.selectCountByParam(param);");
            writeText("\t\tint pageSize = param.getPageSize() == null ? PageSizeEnum.SIZE15.getSize() : param.getPageSize();");
            writeText("\t\tSimplePage page = new SimplePage(param.getPageNum(), count, pageSize);");
            writeText("\t\tparam.setSimplePage(page);");
            writeText("\t\tList<" + beanName + "> list = this.selectListByParam(param);");
            writeText("\t\tPaginationResultVo<" + beanName + "> result = new PaginationResultVo(count, page.getPageSize(), page.getPageNum(), page.getPageTotal(), list);");
            writeText("\t\treturn result;");
            writeText("\t}");
            //新增
            BuildComment.createMethodComment(bw, "新增");
            writeText("\t@Override");
            writeText("\tpublic Integer add(" + tableInfo.getBeanName() + " bean) {");
            writeText("\t\treturn this." + paramMapper + ".insert(bean);");
            writeText("\t}");
            //批量新增
            BuildComment.createMethodComment(bw, "批量新增");
            writeText("\t@Override");
            writeText("\tpublic Integer addBatch(List<" + tableInfo.getBeanName() + "> listBean) {");
            writeText("\t\tif (listBean == null || listBean.isEmpty()) {");
            writeText("\t\t\treturn 0;");
            writeText("\t\t}");
            writeText("\t\treturn this." + paramMapper + ".insertBatch(listBean);");
            writeText("\t}");

            Map<String, List<FieldInfo>> keyMap = tableInfo.getKeyIndexMap();
            for (Map.Entry<String, List<FieldInfo>> entry : keyMap.entrySet()) {
                List<FieldInfo> keyColumnList = entry.getValue();
                StringBuilder paramStr = new StringBuilder();
                StringBuilder paramValueStr = new StringBuilder();
                StringBuilder methodName = new StringBuilder();
                int index = 0;
                for (FieldInfo column : keyColumnList) {
                    if (index > 0) {
                        paramStr.append(", ");
                        methodName.append("And");
                        paramValueStr.append(", ");
                    }
                    paramStr.append(column.getJavaType()).append(" ").append(column.getPropertyName());
                    paramValueStr.append(column.getPropertyName());
                    methodName.append(StringUtils.upperCaseFirstLetter(column.getPropertyName()));
                    index++;
                }
                if (!paramStr.isEmpty()) {
                    //根据主键查询
                    BuildComment.createMethodComment(bw, "根据" + methodName + "获取对象");
                    writeText("\t@Override");
                    writeText("\tpublic " + tableInfo.getBeanName() + " select" + tableInfo.getBeanName() + "By" + methodName + "("
                            + paramStr + ") {");
                    writeText("\t\treturn this." + paramMapper + ".selectBy" + methodName + "(" + paramValueStr + ");");
                    writeText("\t}");
                    //根据主键修改
                    BuildComment.createMethodComment(bw, "根据" + methodName + "修改");
                    writeText("\t@Override");
                    String lowerCaseFirstLetter = StringUtils.lowerCaseFirstLetter(beanQuery);
                    writeText("\tpublic Integer update" + tableInfo.getBeanName() + "By" + methodName + "(" + tableInfo.getBeanName() + " bean, "
                            + paramStr + ") {");
                    writeText("\t\t" + beanQuery + " " + lowerCaseFirstLetter + " = "
                            + "new " + beanQuery + "();");
                    StringBuilder updateSetBuilder = new StringBuilder();
                    for (FieldInfo column : keyColumnList) {
                        updateSetBuilder.append("\t\t ").append(lowerCaseFirstLetter).append(".set")
                                .append(StringUtils.convertToCamelCase(column.getPropertyName()))
                                .append("(")
                                .append(column.getPropertyName())
                                .append(");\n");
                    }
                    writeText(updateSetBuilder.toString());

                    writeText("\t\treturn this." + paramMapper + ".updateByParam" + "(bean, " + lowerCaseFirstLetter + ");");
                    writeText("\t}");
                    //根据主键删除
                    BuildComment.createMethodComment(bw, "根据" + methodName + "删除");
                    writeText("\t@Override");
                    writeText("\tpublic Integer delete" + tableInfo.getBeanName() + "By" + methodName + "(" + paramStr + ") {");
                    writeText("\t\treturn this." + paramMapper + ".deleteBy" + methodName + "(" + paramValueStr + ");");

                    writeText("\t}");

                    if(keyColumnList.get(0).getAutoIncrementFlag()){
                        // 根据ID批量删除
                        BuildComment.createMethodComment(bw, "根据ID批量删除");
                        writeText("\t@Override");
                        writeText("\tpublic Integer delete" + tableInfo.getBeanName() + "By" + methodName + "Batch(List<Integer> list) {");
                        writeText("\t\tif (list == null || list.isEmpty()) {");
                        writeText("\t\t\treturn 0;");
                        writeText("\t\t}");
                        writeText("\t\treturn this." + paramMapper + ".deleteBy" + methodName + "Batch(list);");
                        writeText("\t}");
                    }
                }
            }
            writeText("}");
            bw.flush();
        } catch (Exception e) {
            throw new ServiceException(e);
        } finally {
            closeStream();
        }
    }


    private static void closeStream() {
        try {
            bw.close();
            outputStreamWriter.close();
            out.close();
        } catch (IOException e) {
            throw new ServiceException("Mapper流关闭异常");
        }
    }

    private static void writeText(String text) {
        if (StringUtils.isEmpty(text)) return;
        try {
            bw.write(text);
            bw.newLine();
        } catch (IOException e) {
            throw new ServiceException(text + "写入失败");
        }
    }
}
