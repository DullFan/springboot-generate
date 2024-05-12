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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
public class BuildMapperXML {

    private static final String BASE_COLUMN_LIST = "base_column_list";
    private static final String BASE_QUERY_CONDITION = "base_query_condition";
    private static final String BASE_QUERY_CONDITION_EXTEND = "base_query_condition_extend";
    private static final String QUERY_CONDITION = "query_condition";

    private static OutputStream out;
    private static OutputStreamWriter outputStreamWriter;
    private static BufferedWriter bw;

    public static void execute(TableInfo tableInfo) {
        File folder = new File(DullJavaConfig.getPathBase() + "/" + Constants.PATH_RESOURCES + "/mapper");
        if (!folder.exists()) if (!folder.mkdirs()) log.error(DullJavaConfig.getPathPackageQuery() + "创建失败");
        String className = tableInfo.getBeanName() + "Mapper";
        File mappperFile = new File(folder, className + ".xml");
        out = null;
        outputStreamWriter = null;
        bw = null;
        try {
            out = new FileOutputStream(mappperFile);
            outputStreamWriter = new OutputStreamWriter(out, StandardCharsets.UTF_8);
            bw = new BufferedWriter(outputStreamWriter);
            writeText("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
            writeText("<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\"");
            writeText("        \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">");
            writeText("<mapper namespace=\"" + DullJavaConfig.getPackageMapper() + "." + className + "\">");
            writeText("\t<!--实体映射-->");
            String poClass = DullJavaConfig.getPackagePo() + "." + tableInfo.getBeanName();
            writeText("\t<resultMap id=\"base_result_map\" type=\"" + poClass + "\">");
            FieldInfo idField = null;
            Set<Map.Entry<String, List<FieldInfo>>> keyIndexMap = tableInfo.getKeyIndexMap().entrySet();
            for (Map.Entry<String, List<FieldInfo>> entry : keyIndexMap) {
                if ("PRIMARY".equalsIgnoreCase(entry.getKey())) {
                    List<FieldInfo> value = entry.getValue();
                    if (value.size() == 1) {
                        idField = value.get(0);
                        break;
                    }
                }
            }
            for (FieldInfo fieldInfo : tableInfo.getFieldInfoList()) {
                writeText("\t\t<!--" + fieldInfo.getComment() + "-->");
                String key;
                if (idField != null && fieldInfo.getPropertyName().equalsIgnoreCase(idField.getPropertyName())) {
                    key = "id";
                } else {
                    key = "result";
                }
                writeText("\t\t<" + key + " column=\"" + fieldInfo.getFieldName() + "\" property=\"" + fieldInfo.getPropertyName() + "\"/>");
            }
            writeText("\t</resultMap>");


            //通用查询列
            writeText("\t<!--通用查询列-->");
            writeText("\t<sql id=\"" + BASE_COLUMN_LIST + "\">");
            StringBuilder columnBuilder = new StringBuilder();
            for (FieldInfo fieldInfo : tableInfo.getFieldInfoList()) {
                columnBuilder.append(fieldInfo.getFieldName()).append(",");
            }
            String columnBuilderStr = columnBuilder.substring(0, columnBuilder.lastIndexOf(","));
            writeText("\t\t" + columnBuilderStr);
            writeText("\t</sql>");

            // 基础查询条件
            writeText("\t<!--基础查询条件-->");
            writeText("\t<sql id=\"" + BASE_QUERY_CONDITION + "\">");
            for (FieldInfo fieldInfo : tableInfo.getFieldInfoList()) {
                String stringQuery = "";
                if (ArrayUtils.contains(Constants.SQL_STRING_TYPE, fieldInfo.getSqlType())) {
                    stringQuery = " and query." + fieldInfo.getPropertyName() + " != ''";
                }

                writeText("\t\t<if test=\"query." + fieldInfo.getPropertyName() + " != null" + stringQuery + "\">");
                writeText("\t\t\tand " + fieldInfo.getFieldName() + " = #{query." + fieldInfo.getPropertyName() + "}");
                writeText("\t\t</if>");
            }
            writeText("\t</sql>");


            //扩展查询条件
            writeText("\t<!--扩展查询条件-->");
            writeText("\t<sql id=\"" + BASE_QUERY_CONDITION_EXTEND + "\">");
            for (FieldInfo fieldInfo : tableInfo.getExtendFieldInfoList()) {
                String andWhere = "";
                if (ArrayUtils.contains(Constants.SQL_STRING_TYPE, fieldInfo.getSqlType())) {
                    andWhere = "and " + fieldInfo.getFieldName() + " like concat('%', #{query." + fieldInfo.getPropertyName() + "},'%')" + " != ''";
                } else if (ArrayUtils.contains(Constants.SQL_DATE_TYPES, fieldInfo.getSqlType()) || ArrayUtils.contains(Constants.SQL_DATE_TIME_TYPES, fieldInfo.getSqlType())) {
                    if (fieldInfo.getPropertyName().endsWith(DullJavaConfig.getQueryTimeStart())) {
                        andWhere = "<![CDATA[ and " + fieldInfo.getFieldName() + " >= str_to_date(#{query." + fieldInfo.getPropertyName() + "}, '%Y-%m-%d') ]]>";
                    } else if (fieldInfo.getPropertyName().endsWith(DullJavaConfig.getQueryTimeEnd())) {
                        andWhere = "<![CDATA[ and " + fieldInfo.getFieldName() + " < date_sub(str_to_date(#{query." + fieldInfo.getPropertyName() + "}, '%Y-%m-%d'), interval -1 day) ]]>";
                    }
                }

                writeText("\t\t<if test=\"query." + fieldInfo.getPropertyName() + " != null and query." + fieldInfo.getPropertyName() + " != ''\">");
                writeText("\t\t\t" + andWhere);
                writeText("\t\t</if>");
            }
            writeText("\t</sql>");


            //通用条件查询列
            writeText("\t<!--通用查询条件列-->");
            writeText("\t<sql id=\"" + QUERY_CONDITION + "\">");
            writeText("\t\t<where>");
            writeText("\t\t\t<include refid=\"" + BASE_QUERY_CONDITION + "\"/>");
            writeText("\t\t\t<include refid=\"" + BASE_QUERY_CONDITION_EXTEND + "\"/>");
            writeText("\t\t</where>");
            writeText("\t</sql>");


            //查询列表
            writeText("\t<!--查询列表-->");
            writeText("\t<select id=\"selectList\" resultMap=\"base_result_map\">");
            writeText("\t\tSELECT <include refid=\"" + BASE_COLUMN_LIST + "\"/> FROM " + tableInfo.getTableName() + " <include refid=\"" + QUERY_CONDITION + "\"/>");
            writeText("\t\t<if test=\"query.orderBy!=null\"> order by ${query.orderBy} </if>");
            writeText("\t\t<if test=\"query.simplePage!=null\"> limit ${query.simplePage.start},${query.simplePage.end} </if>");
            writeText("\t</select>");


            //查询数量
            writeText("\t<!--查询数量-->");
            writeText("\t<select id=\"selectCount\" resultType=\"java.lang.Integer\">");
            writeText("\t\tselect count(1) FROM " + tableInfo.getTableName());
            writeText("\t\t<include refid=\"" + QUERY_CONDITION + "\"/>");
            writeText("\t</select>");


            //插入，匹配有值的字段
            writeText("\t<!--插入，匹配有值的字段-->");
            writeText("\t<insert id=\"insert\" parameterType=\"" + poClass + "\">");
            FieldInfo autoIncrementField = null;
            for (FieldInfo fieldInfo : tableInfo.getFieldInfoList()) {
                if (fieldInfo.getAutoIncrementFlag()) {
                    autoIncrementField = fieldInfo;
                    break;
                }
            }
            if (autoIncrementField != null) {
                writeText("\t\t<selectKey keyProperty=\"bean." + autoIncrementField.getFieldName() + "\" resultType=\"" + autoIncrementField.getJavaType() + "\" order=\"AFTER\">");
                writeText("\t\t\tSELECT LAST_INSERT_ID()");
                writeText("\t\t</selectKey>");
            }
            writeText("\t\tINSERT INTO " + tableInfo.getTableName());
            writeText("\t\t<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\">");

            for (FieldInfo fieldInfo : tableInfo.getFieldInfoList()) {
                writeText("\t\t\t<if test=\"bean." + fieldInfo.getPropertyName() + " != null\">");
                writeText("\t\t\t\t" + fieldInfo.getFieldName() + ",");
                writeText("\t\t\t</if>");
            }
            writeText("\t\t</trim>");
            writeText("\t\t<trim prefix=\"values (\" suffix=\")\" suffixOverrides=\",\">");
            for (FieldInfo fieldInfo : tableInfo.getFieldInfoList()) {
                writeText("\t\t\t<if test=\"bean." + fieldInfo.getPropertyName() + " != null\">");
                writeText("\t\t\t\t#{bean." + fieldInfo.getPropertyName() + "},");
                writeText("\t\t\t</if>");
            }
            writeText("\t\t</trim>");
            writeText("\t</insert>");


            //插入或者更新
            writeText("\t<!--插入或者更新-->");
            writeText("\t<insert id=\"insertOrUpdate\" parameterType=\"" + poClass + "\">");
            writeText("\t\tINSERT INTO " + tableInfo.getTableName());
            writeText("\t\t<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\">");
            for (FieldInfo fieldInfo : tableInfo.getFieldInfoList()) {
                writeText("\t\t\t<if test=\"bean." + fieldInfo.getPropertyName() + " != null\">");
                writeText("\t\t\t\t" + fieldInfo.getFieldName() + ",");
                writeText("\t\t\t</if>");
            }
            writeText("\t\t</trim>");
            writeText("\t\t<trim prefix=\"values (\" suffix=\")\" suffixOverrides=\",\">");
            for (FieldInfo fieldInfo : tableInfo.getFieldInfoList()) {
                writeText("\t\t\t<if test=\"bean." + fieldInfo.getPropertyName() + " != null\">");
                writeText("\t\t\t\t#{bean." + fieldInfo.getPropertyName() + "},");
                writeText("\t\t\t</if>");
            }
            writeText("\t\t</trim>");
            writeText("\t\ton DUPLICATE key update");
            Map<String, String> keyTempMap = new HashMap<>();
            for (Map.Entry<String, List<FieldInfo>> entry : keyIndexMap) {
                List<FieldInfo> fieldInfoList = entry.getValue();
                for (FieldInfo item : fieldInfoList) {
                    keyTempMap.put(item.getFieldName(), item.getFieldName());
                }
            }
            writeText("\t\t<trim prefix=\"\" suffix=\"\" suffixOverrides=\",\">");
            for (FieldInfo fieldInfo : tableInfo.getFieldInfoList()) {
                if (keyTempMap.get(fieldInfo.getFieldName()) != null) {
                    continue;
                }
                writeText("\t\t\t<if test=\"bean." + fieldInfo.getPropertyName() + " != null\">");
                writeText("\t\t\t\t" + fieldInfo.getFieldName() + " = VALUES(" + fieldInfo.getFieldName() + "),");
                writeText("\t\t\t</if>");
            }
            writeText("\t\t</trim>");

            writeText("\t</insert>");


            //批量插入
            writeText("\t<!--批量插入-->");
            writeText("\t<insert id=\"insertBatch\" parameterType=\"" + poClass + "\">");
            StringBuffer insertField = new StringBuffer();
            for (FieldInfo fieldInfo : tableInfo.getFieldInfoList()) {
                if (fieldInfo.getAutoIncrementFlag()) {
                    continue;
                }
                insertField.append(fieldInfo.getFieldName()).append(",");
            }
            String insertFieldStr = insertField.substring(0, insertField.lastIndexOf(","));
            writeText("\t\tINSERT INTO " + tableInfo.getTableName() + "(" + insertFieldStr + ")Values");
            writeText("\t\t<foreach collection=\"list\" item=\"item\" separator=\",\">");
            StringBuffer insertProperty = new StringBuffer();
            for (FieldInfo fieldInfo : tableInfo.getFieldInfoList()) {
                if (fieldInfo.getAutoIncrementFlag()) {
                    continue;
                }
                insertProperty.append("#{item." + fieldInfo.getPropertyName() + "}").append(",");
            }
            String insertPropertyStr = insertProperty.substring(0, insertProperty.lastIndexOf(","));
            writeText("\t\t\t" + "(" + insertPropertyStr + ")");
            writeText("\t\t</foreach>");
            writeText("\t</insert>");

            //批量插入或者更新
            writeText("\t<!--批量插入或更新-->");
            writeText("\t<insert id=\"insertOrUpdateBatch\" parameterType=\"" + poClass + "\">");
            writeText("\t\tINSERT INTO " + tableInfo.getTableName() + "(" + insertFieldStr + ")Values");
            writeText("\t\t<foreach collection=\"list\" item=\"item\" separator=\",\">");
            writeText("\t\t\t" + "(" + insertPropertyStr + ")");
            writeText("\t\t</foreach>");
            writeText("\t\ton DUPLICATE key update");
            StringBuffer insertBatchUpdate = new StringBuffer();
            for (FieldInfo fieldInfo : tableInfo.getFieldInfoList()) {
                if (fieldInfo.getAutoIncrementFlag()) {
                    continue;
                }
                insertBatchUpdate.append(fieldInfo.getFieldName() + " = VALUES(" + fieldInfo.getFieldName() + ")").append(",");
            }
            String insertBatchUpdateStr = insertBatchUpdate.substring(0, insertBatchUpdate.lastIndexOf(","));
//            writeText("\t\t" + "("+insertBatchUpdateStr+")");
            writeText("\t\t" + insertBatchUpdateStr);
            writeText("\t</insert>");


            //多条件更新
            writeText("\t<!--多条件修改-->");

            writeText("\t<update id=\"updateByParam\" parameterType=\"" + poClass + "\">");

            writeText("\t\t UPDATE " + tableInfo.getTableName());

            writeText(" \t\t <set> ");

            for (FieldInfo FieldInfo : tableInfo.getFieldInfoList()) {
                if ((null != FieldInfo.getAutoIncrementFlag() && FieldInfo.getAutoIncrementFlag())) {
                    continue;
                }
                writeText("\t\t\t<if test=\"bean." + FieldInfo.getPropertyName() + " != null\">");

                writeText("\t\t\t\t " + FieldInfo.getFieldName() + " = #{bean." + FieldInfo.getPropertyName() + "},");

                writeText("\t\t\t</if>");

            }
            writeText(" \t\t </set>");

            writeText(" \t\t <include refid=\"query_condition\" />");

            writeText("\t</update>");


            //多条件删除
            writeText("\t<!--多条件删除-->");

            writeText("\t<delete id=\"deleteByParam\">");

            writeText("\t\t delete from " + tableInfo.getTableName());

            writeText(" \t\t <include refid=\"query_condition\" />");

            writeText("\t</delete>");


            //根据主键更新
            writeText("\t<!--根据主键更新-->");

            for (Map.Entry<String, List<FieldInfo>> entry : keyIndexMap) {
                List<FieldInfo> keyIndexInfoList = entry.getValue();
                StringBuilder methodName = new StringBuilder();
                StringBuilder methodParams = new StringBuilder();
                Integer index = 0;
                for (FieldInfo fieldInfo : keyIndexInfoList) {
                    index++;
                    methodParams.append(fieldInfo.getFieldName() + " = #{" + fieldInfo.getFieldName() + "}");
                    methodName.append(StringUtils.convertToCamelCase(fieldInfo.getPropertyName()));
                    if (index < keyIndexInfoList.size()) {
                        methodName.append("And");
                        methodParams.append(" and ");
                    }
                }
                writeText("\t<!--根据" + methodName + "查询-->");
                writeText("\t<select id=\"selectBy" + methodName + "\" resultMap=\"base_result_map\">");
                writeText("\t\tSELECT <include refid=\"base_column_list\"/> FROM " + tableInfo.getTableName() + " where " + methodParams);
                writeText("\t</select>");


                writeText("\t<!--根据" + methodName + "删除-->");
                writeText("\t<delete id=\"deleteBy" + methodName + "\">");
                writeText("\t\tDELETE FROM " + tableInfo.getTableName() + " where " + methodParams);
                writeText("\t</delete>");


                if (entry.getValue().get(0).getAutoIncrementFlag()) {
                    FieldInfo fieldInfo = entry.getValue().get(0);
                    writeText("\t<!--根据" + methodName + "批量删除-->");
                    String deleteByIds = StringUtils.format("\t<delete id=\"{}\">\n" + "\t    delete from {} where {}\n" + "\t    in\n" + "\t    <foreach collection=\"list\" item=\"id\" separator=\",\" open=\"(\" close=\")\">\n" + "\t        #{id}\n" + "\t    </foreach>\n" + "\t</delete>", "deleteBy" + methodName + "Batch", tableInfo.getTableName(), fieldInfo.getFieldName());
                    writeText(deleteByIds);
                }

                writeText("\t<!--根据" + methodName + "更新-->");
                writeText("\t<update id=\"updateBy" + methodName + "\" parameterType=\"" + poClass + "\">");
                writeText("\t\tUPDATE " + tableInfo.getTableName());
                writeText("\t\t<set>");
                for (FieldInfo fieldInfo : tableInfo.getFieldInfoList()) {
                    if (fieldInfo.getAutoIncrementFlag()) {
                        continue;
                    }
                    writeText("\t\t\t<if test=\"bean." + fieldInfo.getPropertyName() + " != null\">");

                    writeText("\t\t\t\t" + fieldInfo.getFieldName() + " = #{bean." + fieldInfo.getPropertyName() + "},");

                    writeText("\t\t\t</if>");

                }
                writeText("\t\t</set>");
                writeText("\t</update>");

            }
            writeText("</mapper>");
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
