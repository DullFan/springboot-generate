package com.dullfan.generate.build;

import com.dullfan.generate.config.DullJavaConfig;
import com.dullfan.generate.entity.FieldInfo;
import com.dullfan.generate.entity.TableInfo;
import com.dullfan.generate.utils.StringUtils;
import com.dullfan.generate.utils.constant.Constants;
import com.dullfan.generate.utils.extremely.ServiceException;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
public class BuildQuery {
    private static OutputStream out;
    private static OutputStreamWriter outputStreamWriter;
    private static BufferedWriter bw;

    public static void execute(TableInfo tableInfo) {
        File folder = new File(DullJavaConfig.getPathPackageQuery());
        if (!folder.exists()) if (!folder.mkdirs()) log.error(DullJavaConfig.getPathPackageQuery() + "创建失败");
        String className = tableInfo.getBeanName() + DullJavaConfig.getBeanQuery();
        File poFile = new File(folder, className + ".java");
        out = null;
        outputStreamWriter = null;
        bw = null;
        try {
            out = new FileOutputStream(poFile);
            outputStreamWriter = new OutputStreamWriter(out, StandardCharsets.UTF_8);
            bw = new BufferedWriter(outputStreamWriter);
            // 包名
            writeText("package " + DullJavaConfig.getPackageQuery() + ";");
            // 导包
            if (tableInfo.getHaveDateTime() || tableInfo.getHaveDate()) writeText("import java.util.Date;");
            if (tableInfo.getHaveBigDecimal()) writeText("import java.math.BigDecimal;");
            writeText("import " + DullJavaConfig.getPackageQuery() + ".BaseParam;");
            // 构建类注释
            BuildComment.createClassComment(bw, tableInfo.getComment());
            writeText("public class " + className + " extends BaseParam {");

            List<FieldInfo> columnList = tableInfo.getFieldInfoList();

            for (FieldInfo columnInfo : columnList) {
                BuildComment.createMethodComment(bw, columnInfo.getComment());
                if (!Constants.TYPE_DATE.equals(columnInfo.getJavaType())) {
                    writeText("\tprivate " + columnInfo.getJavaType() + " " + columnInfo.getPropertyName() + ";");
                }
                //添加模糊搜索条件
                if (Constants.TYPE_STRING.equals(columnInfo.getJavaType())) {
                    writeText("\tprivate " + columnInfo.getJavaType() + " " + columnInfo.getPropertyName() + Constants.SUFFIX_PROPERTY_FUZZY + ";");
                }
                //添加时间日期搜索
                if (Constants.TYPE_DATE.equals(columnInfo.getJavaType())) {
                    writeText("\tprivate " + Constants.TYPE_STRING + " " + columnInfo.getPropertyName() + ";");
                    String start = columnInfo.getPropertyName() + DullJavaConfig.getQueryTimeStart();
                    String end = columnInfo.getPropertyName() + DullJavaConfig.getQueryTimeEnd();
                    writeText("\tprivate " + Constants.TYPE_STRING + " " + start + ";");
                    writeText("\tprivate " + Constants.TYPE_STRING + " " + end + ";");
                }
            }
            // 生成get 和 set方法
            String tempField;
            for (FieldInfo columnInfo : columnList) {
                tempField = StringUtils.upperCaseFirstLetter((columnInfo.getPropertyName()));
                if (!Constants.TYPE_DATE.equals(columnInfo.getJavaType())) {
                    writeText("\tpublic void set" + tempField + "(" + columnInfo.getJavaType() + " "
                            + columnInfo.getPropertyName() + "){");
                    writeText("\t\tthis." + columnInfo.getPropertyName() + " = " + columnInfo.getPropertyName() + ";");
                    writeText("\t}");
                    writeText("\tpublic " + columnInfo.getJavaType() + " get" + tempField + "(){");
                    writeText("\t\treturn this." + columnInfo.getPropertyName() + ";");
                    writeText("\t}");
                } else {
                    writeText("\tpublic void set" + tempField + "(" + Constants.TYPE_STRING + " "
                            + columnInfo.getPropertyName() + "){");
                    writeText("\t\tthis." + columnInfo.getPropertyName() + " = " + columnInfo.getPropertyName() + ";");
                    writeText("\t}");
                    writeText("\tpublic " + Constants.TYPE_STRING + " get" + tempField + "(){");
                    writeText("\t\treturn this." + columnInfo.getPropertyName() + ";");
                    writeText("\t}");
                }

                if (Constants.TYPE_STRING.equals(columnInfo.getJavaType())) {
                    writeText("\tpublic void set" + tempField + Constants.SUFFIX_PROPERTY_FUZZY + "("
                            + columnInfo.getJavaType() + " " + columnInfo.getPropertyName()
                            + Constants.SUFFIX_PROPERTY_FUZZY + "){");
                    writeText("\t\tthis." + columnInfo.getPropertyName() + Constants.SUFFIX_PROPERTY_FUZZY + " = "
                            + columnInfo.getPropertyName() + Constants.SUFFIX_PROPERTY_FUZZY + ";");
                    writeText("\t}");
                    writeText("\tpublic " + columnInfo.getJavaType() + " get" + tempField + Constants.SUFFIX_PROPERTY_FUZZY
                            + "(){");
                    writeText("\t\treturn this." + columnInfo.getPropertyName() + Constants.SUFFIX_PROPERTY_FUZZY + ";");
                    writeText("\t}");
                }

                if (Constants.TYPE_DATE.equals(columnInfo.getJavaType())) {
                    String start = columnInfo.getPropertyName() + DullJavaConfig.getQueryTimeStart();
                    String end = columnInfo.getPropertyName() + DullJavaConfig.getQueryTimeEnd();
                    tempField = start.substring(0, 1).toUpperCase() + start.substring(1);
                    //开始日期
                    writeText("\tpublic void set" + tempField + "(" + Constants.TYPE_STRING + " " + start + "){");
                    writeText("\t\tthis." + start + " = " + start + ";");
                    writeText("\t}");
                    writeText("\tpublic " + Constants.TYPE_STRING + " get" + tempField + "(){");
                    writeText("\t\treturn this." + start + ";");
                    writeText("\t}");

                    //结束日期
                    tempField = end.substring(0, 1).toUpperCase() + end.substring(1);
                    writeText("\tpublic void set" + tempField + "(" + Constants.TYPE_STRING + " " + end + "){");
                    writeText("\t\tthis." + end + " = " + end + ";");
                    writeText("\t}");
                    writeText("\tpublic " + Constants.TYPE_STRING + " get" + tempField + "(){");
                    writeText("\t\treturn this." + end + ";");
                    writeText("\t}");
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
            throw new ServiceException("Po流关闭异常");
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
