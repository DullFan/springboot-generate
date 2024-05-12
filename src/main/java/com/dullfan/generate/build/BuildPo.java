package com.dullfan.generate.build;

import com.dullfan.generate.config.DullJavaConfig;
import com.dullfan.generate.entity.FieldInfo;
import com.dullfan.generate.entity.TableInfo;
import com.dullfan.generate.utils.StringUtils;
import com.dullfan.generate.utils.constant.Constants;
import com.dullfan.generate.utils.extremely.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;

@Slf4j
public class BuildPo {
    private static OutputStream out;
    private static OutputStreamWriter outputStreamWriter;
    private static BufferedWriter bw;

    public static void execute(TableInfo tableInfo) {
        File folder = new File(DullJavaConfig.getPathPackagePo());
        if (!folder.exists()) if(!folder.mkdirs()) log.error(DullJavaConfig.getPathPackagePo()+"创建失败");
        File poFile = new File(folder, tableInfo.getBeanName() + ".java");
        out = null;
        outputStreamWriter = null;
        bw = null;
        try {
            out = new FileOutputStream(poFile);
            outputStreamWriter = new OutputStreamWriter(out, StandardCharsets.UTF_8);
            bw = new BufferedWriter(outputStreamWriter);
            // 包名
            writeText("package " + DullJavaConfig.getPackagePo() + ";");
            bw.newLine();
            // 导包
            writeText("import java.io.Serializable;");
            if (tableInfo.getHaveDateTime() || tableInfo.getHaveDate()) writeText("import java.util.Date;");
            if (tableInfo.getHaveBigDecimal()) writeText("import java.math.BigDecimal;");
            if (tableInfo.getHaveDate() || tableInfo.getHaveDateTime()) {
                writeText("import com.fasterxml.jackson.annotation.JsonFormat;");
                writeText("import org.springframework.format.annotation.DateTimeFormat;");
            }
            if (tableInfo.getHaveJsonIgnore()) writeText("import com.fasterxml.jackson.annotation.JsonIgnore;");
            bw.newLine();
            // 构建类注释
            BuildComment.createClassComment(bw, tableInfo.getComment());
            writeText("public class " + tableInfo.getBeanName() + " implements Serializable {");

            // 生成变量
            for (FieldInfo fieldInfo : tableInfo.getFieldInfoList()) {
                BuildComment.createFieldComment(bw, fieldInfo.getComment());
                if (DullJavaConfig.getFieldIgnoreHashSet().contains(fieldInfo.getFieldName()))
                    writeText("\t@JsonIgnore");
                if (ArrayUtils.contains(Constants.SQL_DATE_TIME_TYPES, fieldInfo.getSqlType())) {
                    writeText("\t@JsonFormat(pattern = \"yyyy-MM-dd HH:mm:ss\", timezone = \"GMT-8\")");
                    writeText("\t@DateTimeFormat(pattern = \"yyyy-MM-dd HH:mm:ss\")");
                }
                if (ArrayUtils.contains(Constants.SQL_DATE_TYPES, fieldInfo.getSqlType())) {
                    writeText("\t@JsonFormat(pattern = \"yyyy-MM-dd\", timezone = \"GMT-8\")");
                    writeText("\t@DateTimeFormat(pattern = \"yyyy-MM-dd\")");
                }
                writeText("\tprivate " + fieldInfo.getJavaType() + " " + fieldInfo.getPropertyName() + ";");
            }

            // 生成get set方法
            for (FieldInfo fieldInfo : tableInfo.getFieldInfoList()) {
                bw.newLine();
                writeText("\tpublic void set" + StringUtils.toUpperCase(fieldInfo.getPropertyName()) + "(" + fieldInfo.getJavaType() + " " + fieldInfo.getPropertyName() + ")" + " {");
                writeText("\t\tthis." + fieldInfo.getPropertyName() + " = " + fieldInfo.getPropertyName() + ";");
                writeText("\t}");

                bw.newLine();
                writeText("\tpublic " + fieldInfo.getJavaType() + " get" + StringUtils.toUpperCase(fieldInfo.getPropertyName()) + "(" + ")" + " {");
                writeText("\t\treturn " + fieldInfo.getPropertyName() + ";");
                writeText("\t}");
            }

            // 重写toString
            bw.newLine();
            writeText("\t@Override");
            writeText("\tpublic String toString(){");
            StringBuilder toStringText = new StringBuilder();
            for (FieldInfo fieldInfo : tableInfo.getFieldInfoList()) {
                toStringText.append("\"").append(fieldInfo.getPropertyName()).append("= \"")
                        .append("+")
                        .append(fieldInfo.getPropertyName())
                        .append("+");
            }
            writeText("\t\treturn " + toStringText.substring(0, toStringText.lastIndexOf("+")) + ";");
            writeText("\t}");
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
