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
public class BuildMapper {
    private static OutputStream out;
    private static OutputStreamWriter outputStreamWriter;
    private static BufferedWriter bw;

    public static void execute(TableInfo tableInfo) {
        File folder = new File(DullJavaConfig.getPathMapper());
        if (!folder.exists()) if (!folder.mkdirs()) log.error(DullJavaConfig.getPathPackageQuery() + "创建失败");
        String className = tableInfo.getBeanName() + "Mapper";
        File mappperFile = new File(folder, className + ".java");
        out = null;
        outputStreamWriter = null;
        bw = null;
        try {
            out = new FileOutputStream(mappperFile);
            outputStreamWriter = new OutputStreamWriter(out, StandardCharsets.UTF_8);
            bw = new BufferedWriter(outputStreamWriter);

            writeText("package " + DullJavaConfig.getPackageMapper() + ";");
            bw.newLine();
            writeText("import " + DullJavaConfig.getPackageMapper() + ".ABaseMapper;");
            writeText("import " + DullJavaConfig.getPackagePo() + "." + tableInfo.getBeanName() + ";");
            writeText("import " + DullJavaConfig.getPackageQuery() + "." + tableInfo.getBeanName() + DullJavaConfig.getBeanQuery() + ";");
            writeText("import org.apache.ibatis.annotations.Param;");
            writeText("import org.apache.ibatis.annotations.Mapper;");
            writeText("import java.math.BigDecimal;");
            writeText("import java.util.List;");

            bw.newLine();
            writeText("@Mapper");
            writeText("public interface " + className + " extends ABaseMapper<" + tableInfo.getBeanName() + "," + tableInfo.getBeanName() + DullJavaConfig.getBeanQuery() + "> " + "{");

            Map<String, List<FieldInfo>> keyIndexMap = tableInfo.getKeyIndexMap();

            for (Map.Entry<String, List<FieldInfo>> entry : keyIndexMap.entrySet()) {
                int index = 0;
                List<FieldInfo> keyFieldInfos = entry.getValue();
                StringBuilder methodName = new StringBuilder();
                StringBuilder methodParams = new StringBuilder();
                for (FieldInfo fieldInfo : keyFieldInfos) {
                    methodName.append(StringUtils.convertToCamelCase(fieldInfo.getPropertyName()));

                    methodParams.append("@Param(\"").append(fieldInfo.getPropertyName()).append("\") ").append(fieldInfo.getJavaType()).append(" ").append(fieldInfo.getPropertyName());
                    if (index < keyFieldInfos.size()) {
                        methodName.append("And");
                        methodParams.append(", ");
                    }
                }
                BuildComment.createFieldComment(bw, "根据" + methodName + "查询");
                writeText("\t" + tableInfo.getBeanName() + " selectBy" + methodName + "(" + methodParams + ");");
                bw.newLine();

                BuildComment.createFieldComment(bw, "根据" + methodName + "删除");
                writeText("\tInteger deleteBy" + methodName + "(" + methodParams + ");");
                bw.newLine();


                if(entry.getValue().get(0).getAutoIncrementFlag()){
                    BuildComment.createFieldComment(bw, "根据" + methodName + "批量删除");
                    writeText("\tInteger deleteBy" + methodName + "Batch(@Param(\"list\") List<Integer> list);");
                    bw.newLine();
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
