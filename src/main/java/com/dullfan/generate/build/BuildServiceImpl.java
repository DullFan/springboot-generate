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
            writeText("import " + DullJavaConfig.getPackageVo() + ".PaginationResultVO;");
            writeText("import " + DullJavaConfig.getPackageQuery() + ".SimplePage;");

            writeText("import " + DullJavaConfig.getPackageMapper() + "." + tableInfo.getBeanName() + DullJavaConfig.getBeanMapper()
                    + ";");
            writeText("import " + DullJavaConfig.getPackageService() + "." + tableInfo.getBeanName() + DullJavaConfig.getBeanService()
                    + ";");
            writeText("import " + DullJavaConfig.getPackageUtils() + ".StringTools;");

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
            writeText("\tpublic List<" + beanName + "> findListByParam(" + beanQuery + " param) {");
            writeText("\t\treturn this." + paramMapper + ".selectList(param);");
            writeText("\t}");
            //根据条件查询记录数
            BuildComment.createMethodComment(bw, "根据条件查询列表");
            writeText("\t@Override");
            writeText("\tpublic Integer findCountByParam(" + beanQuery + " param) {");
            writeText("\t\treturn this." + paramMapper + ".selectCount(param);");
            writeText("\t}");
            //分页查询的方法
            BuildComment.createMethodComment(bw, "分页查询方法");
            writeText("\t@Override");
            writeText("\tpublic PaginationResultVO<" + beanName + "> findListByPage(" + beanQuery
                    + " param) {");
            writeText("\t\tint count = this.findCountByParam(param);");
            writeText("\t\tint pageSize = param.getPageSize() == null ? PageSizeEnum.SIZE15.getSize() : param.getPageSize();");
            writeText("\t\tSimplePage page = new SimplePage(param.getPageNum(), count, pageSize);");
            writeText("\t\tparam.setSimplePage(page);");
            writeText("\t\tList<" + beanName + "> list = this.findListByParam(param);");
            writeText("\t\tPaginationResultVO<" + beanName + "> result = new PaginationResultVO(count, page.getPageSize(), page.getPageNum(), page.getPageTotal(), list);");
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
            //批量新增
            BuildComment.createMethodComment(bw, "批量新增或者修改");
            writeText("\t@Override");
            writeText("\tpublic Integer addOrUpdateBatch(List<" + tableInfo.getBeanName() + "> listBean) {");
            writeText("\t\tif (listBean == null || listBean.isEmpty()) {");
            writeText("\t\t\treturn 0;");
            writeText("\t\t}");
            writeText("\t\treturn this." + paramMapper + ".insertOrUpdateBatch(listBean);");
            writeText("\t}");
            //多条件更新
            BuildComment.createMethodComment(bw, "多条件更新");
            writeText("\t@Override");
            writeText("\tpublic Integer updateByParam(" + tableInfo.getBeanName() + " bean, " + beanQuery + " param) {");
            writeText("\t\tStringTools.checkParam(param);");
            writeText("\t\treturn this." + paramMapper + ".updateByParam(bean, param);");
            writeText("\t}");
            //多条件删除
            BuildComment.createMethodComment(bw, "多条件删除");
            writeText("\t@Override");
            writeText("\tpublic Integer deleteByParam(" + beanQuery + " param) {");
            writeText("\t\tStringTools.checkParam(param);");
            writeText("\t\treturn this." + paramMapper + ".deleteByParam(param);");
            writeText("\t}");
            Map<String, List<FieldInfo>> keyMap = tableInfo.getKeyIndexMap();
            for (Map.Entry<String, List<FieldInfo>> entry : keyMap.entrySet()) {
                List<FieldInfo> keyColumnList = entry.getValue();
                StringBuffer paramStr = new StringBuffer();
                StringBuffer paramValueStr = new StringBuffer();
                StringBuffer methodName = new StringBuffer();
                int index = 0;
                for (FieldInfo column : keyColumnList) {
                    if (index > 0) {
                        paramStr.append(", ");
                        methodName.append("And");
                        paramValueStr.append(", ");
                    }
                    paramStr.append(column.getJavaType() + " " + column.getPropertyName());
                    paramValueStr.append(column.getPropertyName());
                    methodName.append(StringUtils.upperCaseFirstLetter(column.getPropertyName()));
                    index++;
                }
                if (paramStr.length() > 0) {
                    //根据主键查询
                    BuildComment.createMethodComment(bw, "根据" + methodName + "获取对象");
                    writeText("\t@Override");
                    writeText("\tpublic " + tableInfo.getBeanName() + " get" + tableInfo.getBeanName() + "By" + methodName.toString() + "("
                            + paramStr.toString() + ") {");
                    writeText("\t\treturn this." + paramMapper + ".selectBy" + methodName.toString() + "(" + paramValueStr.toString() + ");");
                    writeText("\t}");
                    //根据主键修改
                    BuildComment.createMethodComment(bw, "根据" + methodName + "修改");
                    writeText("\t@Override");
                    String lowerCaseFirstLetter = StringUtils.lowerCaseFirstLetter(beanQuery);
                    writeText("\tpublic Integer update" + tableInfo.getBeanName() + "By" + methodName.toString() + "(" + tableInfo.getBeanName() + " bean, "
                            + paramStr.toString() + ") {");
                    writeText("\t\t" + beanQuery + " " + lowerCaseFirstLetter + " = "
                            + "new " + beanQuery + "();");
                    StringBuilder updateSetBuilder = new StringBuilder();
                    for (FieldInfo column : keyColumnList) {
                        updateSetBuilder.append("\t\t " + lowerCaseFirstLetter + ".set")
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
                    writeText("\tpublic Integer delete" + tableInfo.getBeanName() + "By" + methodName.toString() + "(" + paramStr.toString() + ") {");
                    writeText("\t\treturn this." + paramMapper + ".deleteBy" + methodName.toString() + "(" + paramValueStr.toString() + ");");

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
