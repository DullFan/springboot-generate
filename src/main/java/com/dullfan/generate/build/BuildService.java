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
public class BuildService {
    private static OutputStream out;
    private static OutputStreamWriter outputStreamWriter;
    private static BufferedWriter bw;


    public static void execute(TableInfo tableInfo) {
        File folder = new File(DullJavaConfig.getPathService());
        if (!folder.exists()) if (!folder.mkdirs()) log.error(DullJavaConfig.getPathService() + "创建失败");
        String className = tableInfo.getBeanName() + "Service";
        File mappperFile = new File(folder, className + ".java");
        out = null;
        outputStreamWriter = null;
        bw = null;
        try {
            out = new FileOutputStream(mappperFile);
            outputStreamWriter = new OutputStreamWriter(out, StandardCharsets.UTF_8);
            bw = new BufferedWriter(outputStreamWriter);

            writeText("package " + DullJavaConfig.getPackageService() + ";");
            writeText("import " + DullJavaConfig.getPackagePo() + "." + tableInfo.getBeanName() + ";");
            writeText("import " + DullJavaConfig.getPackageQuery() + "." + tableInfo.getBeanName() + DullJavaConfig.getBeanQuery() + ";");
            writeText("import " + DullJavaConfig.getPackageVo() + ".PaginationResultVo;");
            writeText("import org.apache.ibatis.annotations.Param;");
            writeText("import java.util.List;");
            if(tableInfo.getHaveBigDecimal()){
                writeText("import java.math.BigDecimal;");
            }
            writeText("public interface " + className + "{");


            String beanName = tableInfo.getBeanName();

            //根据条件查询列表
            BuildComment.createMethodComment(bw, "根据条件查询列表");
            writeText("\tList<" + beanName + "> findListByParam(" + tableInfo.getBeanName()+DullJavaConfig.getBeanQuery()     + " param);");
            //根据条件查询数量
            BuildComment.createMethodComment(bw, "根据条件查询列表");
            writeText("\tInteger findCountByParam(" + tableInfo.getBeanName()+DullJavaConfig.getBeanQuery()   + " param);");
            //分页查询的方法
            BuildComment.createMethodComment(bw, "分页查询");
            writeText("\tPaginationResultVo<" + beanName + "> findListByPage(" + tableInfo.getBeanName()+DullJavaConfig.getBeanQuery()
                    + " param);");


            Map<String, List<FieldInfo>> keyMap = tableInfo.getKeyIndexMap();
            //新增的方法
            BuildComment.createMethodComment(bw, "新增");
            writeText("\tInteger add(" + tableInfo.getBeanName() + " bean);");
            //批量新增的方法
            BuildComment.createMethodComment(bw, "批量新增");
            writeText("\tInteger addBatch(List<" + tableInfo.getBeanName() + "> listBean);");

            BuildComment.createMethodComment(bw, "批量新增/修改");
            writeText("\tInteger addOrUpdateBatch(List<" + tableInfo.getBeanName() + "> listBean);");

            BuildComment.createMethodComment(bw, "多条件更新");
            writeText("\tInteger updateByParam(" + tableInfo.getBeanName() + " bean," + tableInfo.getBeanName()+DullJavaConfig.getBeanQuery()     + " param);");

            BuildComment.createMethodComment(bw, "多条件删除");
            writeText("\tInteger deleteByParam(" + tableInfo.getBeanName()+DullJavaConfig.getBeanQuery()  + " param);");

            for (Map.Entry<String, List<FieldInfo>> entry : keyMap.entrySet()) {
                List<FieldInfo> keyfieldInfoList = entry.getValue();
                StringBuffer paramStr = new StringBuffer();
                StringBuffer methodName = new StringBuffer();
                int index = 0;
                for (FieldInfo column : keyfieldInfoList) {
                    if (index > 0) {
                        paramStr.append(",");
                        methodName.append("And");
                    }
                    paramStr.append(column.getJavaType()).append(" ").append(column.getPropertyName());
                    methodName.append(StringUtils.convertToCamelCase(column.getPropertyName()));
                    index++;
                }
                if (!paramStr.isEmpty()) {
                    //根据主键查询
                    BuildComment.createMethodComment(bw, "根据" + methodName + "查询对象");
                    writeText("\t" + tableInfo.getBeanName() + " find" + tableInfo.getBeanName() + "By" + methodName.toString() + "("
                            + paramStr.toString() + ");");

                    //根据主键方法
                    BuildComment.createMethodComment(bw, "根据" + methodName + "修改");
                    writeText("\tInteger update" + tableInfo.getBeanName() + "By" + methodName.toString() + "(" + tableInfo.getBeanName() + " bean,"
                            + paramStr.toString() + ");");
                    //根据主键删除
                    BuildComment.createMethodComment(bw, "根据" + methodName + "删除");
                    writeText("\tInteger delete" + tableInfo.getBeanName() + "By" + methodName.toString() + "(" + paramStr.toString() + ");");

                    if(keyfieldInfoList.get(0).getAutoIncrementFlag()){
                        //根据主键批量删除
                        BuildComment.createMethodComment(bw, "根据" + methodName + "批量删除");
                        writeText("\tInteger delete" + tableInfo.getBeanName() + "By" + methodName + "Batch(List<Integer> list);");
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
