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
public class BuildController {
    private static OutputStream out;
    private static OutputStreamWriter outputStreamWriter;
    private static BufferedWriter bw;


    public static void execute(TableInfo tableInfo) {
        File folder = new File(DullJavaConfig.getPathController());
        if (!folder.exists()) if (!folder.mkdirs()) log.error(DullJavaConfig.getPathController() + "创建失败");
        String className = tableInfo.getBeanName() + "Controller";
        File mappperFile = new File(folder, className + ".java");
        out = null;
        outputStreamWriter = null;
        bw = null;
        try {
            out = new FileOutputStream(mappperFile);
            outputStreamWriter = new OutputStreamWriter(out, StandardCharsets.UTF_8);
            bw = new BufferedWriter(outputStreamWriter);

            writeText("package " + DullJavaConfig.getPackageController() + ";");
            writeText("import " + DullJavaConfig.getPackageQuery() + "." + tableInfo.getBeanName() + DullJavaConfig.getBeanQuery() + ";");
            writeText("import " + DullJavaConfig.getPackagePo() + "." + tableInfo.getBeanName() + ";");
            writeText("import " + DullJavaConfig.getPackageVo() + ".AjaxResult;");
            writeText("import " + DullJavaConfig.getPackageService() + "." + tableInfo.getBeanName() + "Service;");
            writeText("import " + DullJavaConfig.getPackageController() + ".ABaseController;");
            writeText("import org.apache.ibatis.annotations.Param;");
            writeText("import java.util.List;");
            writeText("import org.springframework.web.bind.annotation.RequestBody;");
            writeText("import org.springframework.web.bind.annotation.RequestMapping;");
            writeText("import org.springframework.web.bind.annotation.RestController;");
            writeText("import org.springframework.web.bind.annotation.*;");
            writeText(DullJavaConfig.getImportSpringBootHashMap().get("Resource"));
            if (tableInfo.getHaveBigDecimal()) {
                writeText("import java.math.BigDecimal;");
            }

            writeText("@RestController(\"" + tableInfo.getBeanName() + "Controller\")");
            writeText("@RequestMapping(\"/" + StringUtils.lowerCaseFirstLetter(tableInfo.getBeanName()) + "\")");
            writeText("public class " + className + " extends ABaseController {");

            writeText("\t@Resource");
            String serviceBean = StringUtils.lowerCaseFirstLetter(tableInfo.getBeanName()) + "Service";
            writeText("\tprivate " + tableInfo.getBeanName() + "Service " + serviceBean + ";");

            //根据条件查询列表
            BuildComment.createMethodComment(bw, "根据条件分页查询");
            writeText("\t@GetMapping(\"/loadDataList\")");
            String paramName = "param";
            writeText("\tpublic AjaxResult loadDataList(" + tableInfo.getBeanName() + DullJavaConfig.getBeanQuery() + " " + paramName + "){");

            writeText("\t\treturn getSuccessAjaxResult(" + serviceBean + ".findListByPage(" + paramName + "));");
            writeText("\t}");

            //新增
            BuildComment.createMethodComment(bw, "新增");
            writeText("\t@PostMapping(\"/add\")");
            writeText("\tpublic AjaxResult add(@RequestBody " + tableInfo.getBeanName() + " bean) {");
            writeText("\t\t" + serviceBean + ".add(bean);");
            writeText("\t\treturn getSuccessAjaxResult(null);");
            writeText("\t}");

            //批量新增的方法
            BuildComment.createMethodComment(bw, "批量新增");
            writeText("\t@PostMapping(\"/addBatch\")");
            writeText("\tpublic AjaxResult addBatch(@RequestBody List<" + tableInfo.getBeanName() + "> listBean) {");
            writeText("\t\t" + serviceBean + ".addBatch(listBean);");
            writeText("\t\treturn getSuccessAjaxResult(null);");
            writeText("\t}");

            //批量新增的方法
            BuildComment.createMethodComment(bw, "批量新增/修改");
            writeText("\t@PostMapping(\"/addOrUpdateBatch\")");
            writeText("\tpublic AjaxResult addOrUpdateBatch(@RequestBody List<" + tableInfo.getBeanName() + "> listBean) {");
            writeText("\t\t" + serviceBean + ".addBatch(listBean);");
            writeText("\t\treturn getSuccessAjaxResult(null);");
            writeText("\t}");

            Map<String, List<FieldInfo>> keyMap = tableInfo.getKeyIndexMap();
            for (Map.Entry<String, List<FieldInfo>> entry : keyMap.entrySet()) {
                List<FieldInfo> keyfieldInfoList = entry.getValue();
                StringBuilder paramStr = new StringBuilder();
                StringBuilder paramNameStr = new StringBuilder();
                StringBuilder methodName = new StringBuilder();
                int index = 0;
                for (FieldInfo column : keyfieldInfoList) {
                    if (index > 0) {
                        paramStr.append(",");
                        paramNameStr.append(",");
                        methodName.append("And");
                    }
                    paramStr.append(column.getJavaType()).append(" ").append(column.getPropertyName());
                    paramNameStr.append(column.getPropertyName());
                    methodName.append(StringUtils.convertToCamelCase(column.getPropertyName()));
                    index++;
                }
                if (!paramStr.isEmpty()) {
                    //根据主键查询
                    BuildComment.createMethodComment(bw, "根据" + methodName + "查询对象");
                    String methodNameStr = "get" + tableInfo.getBeanName() + "By" + methodName;
                    writeText("\t@GetMapping(\"/" + methodNameStr + "\")");
                    writeText("\tpublic AjaxResult " + methodNameStr + "(" + paramStr + ") {");
                    writeText("\t\treturn getSuccessAjaxResult(" + serviceBean + "." + methodNameStr + "(" + paramNameStr + "));");
                    writeText("\t}");

                    BuildComment.createMethodComment(bw, "根据" + methodName + "修改对象");
                    methodNameStr = "update" + tableInfo.getBeanName() + "By" + methodName;
                    writeText("\t@PutMapping(\"/" + methodNameStr + "\")");
                    writeText("\tpublic AjaxResult " + methodNameStr + "(" + tableInfo.getBeanName() + " bean," + paramStr + ") {");
                    writeText("\t\t" + serviceBean + "." + methodNameStr + "(bean," + paramNameStr + ");");
                    writeText("\t\treturn getSuccessAjaxResult(null);");
                    writeText("\t}");

                    //根据主键删除
                    BuildComment.createMethodComment(bw, "根据" + methodName + "删除");
                    methodNameStr = "delete" + tableInfo.getBeanName() + "By" + methodName;
                    writeText("\t@DeleteMapping(\"/" + methodNameStr + "\")");
                    writeText("\tpublic AjaxResult " + methodNameStr + "(" + paramStr + ") {");
                    writeText("\t\t" + serviceBean + "." + methodNameStr + "(" + paramNameStr + ");");
                    writeText("\t\treturn getSuccessAjaxResult(null);");
                    writeText("\t}");

                    if(keyfieldInfoList.get(0).getAutoIncrementFlag()){
                        //根据主键批量删除
                        BuildComment.createMethodComment(bw, "根据" + methodName + "批量删除");
                        methodNameStr = "delete" + tableInfo.getBeanName() + "By" + methodName+"Batch";
                        writeText("\t@DeleteMapping(\"/" + methodNameStr + "\")");
                        writeText("\tpublic AjaxResult " + methodNameStr + "(@RequestParam List<Integer> list) {");
                        writeText("\t\t" + serviceBean + "." + methodNameStr + "(list);");
                        writeText("\t\treturn getSuccessAjaxResult(null);");
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
