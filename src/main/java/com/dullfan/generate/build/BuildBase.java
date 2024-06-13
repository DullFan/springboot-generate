package com.dullfan.generate.build;

import com.dullfan.generate.config.DullJavaConfig;
import com.dullfan.generate.utils.StringUtils;
import com.dullfan.generate.utils.extremely.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ClassUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 生成基础类
 */
@Slf4j
public class BuildBase {

    private static OutputStream out;
    private static OutputStreamWriter outw;
    private static BufferedWriter bw;
    private static InputStream in;
    private static InputStreamReader inr;
    private static BufferedReader bf;

    public static void execute() {
        List<String> headerInfoList = new ArrayList<>();
        headerInfoList.add("package " + DullJavaConfig.getPackageUtilsConstant() + ";\n");
        build(headerInfoList, "HttpStatus", DullJavaConfig.getPathUtilsConstant());

        headerInfoList.clear();
        headerInfoList.add("package " + DullJavaConfig.getPackageVo()+";");
        headerInfoList.add("import " + DullJavaConfig.getPackageUtilsConstant() + ".HttpStatus;");
        build(headerInfoList, "AjaxResult", DullJavaConfig.getPathVo() + "/");

        headerInfoList.clear();
        headerInfoList.add("package " + DullJavaConfig.getPackageMapper() + ";");
        build(headerInfoList, "ABaseMapper", DullJavaConfig.getPathMapper());

        headerInfoList.clear();
        headerInfoList.add("package " + DullJavaConfig.getPackageVo() + ";");
        headerInfoList.add("import java.util.List;");
        headerInfoList.add("import java.util.ArrayList;");
        build(headerInfoList, "PaginationResultVo", DullJavaConfig.getPathVo() + "/");

        headerInfoList.clear();
        headerInfoList.add("package " + DullJavaConfig.getPackageUtilsEnum() + ";\n");
        build(headerInfoList, "PageSizeEnum", DullJavaConfig.getPathUtilsEnum() + "/");

        headerInfoList.clear();
        headerInfoList.add("package " + DullJavaConfig.getPackageUtils() + ";\n");
        build(headerInfoList, "StringUtils", DullJavaConfig.getPathUtils() + "/");

        headerInfoList.clear();
        headerInfoList.add("package " + DullJavaConfig.getPackageQuery() + ";\n");
        headerInfoList.add("import " + DullJavaConfig.getPackageUtilsEnum() + ".PageSizeEnum;");
        build(headerInfoList, "SimplePage", DullJavaConfig.getPathPackageQuery() + "/");

        headerInfoList.clear();
        headerInfoList.add("package " + DullJavaConfig.getPackageQuery() + ";\n");
        build(headerInfoList, "BaseParam", DullJavaConfig.getPathPackageQuery() + "/");

        headerInfoList.clear();
        headerInfoList.add("package " + DullJavaConfig.getPackageController() + ";\n");
        headerInfoList.add("import " + DullJavaConfig.getPackageVo() + ".AjaxResult;");
        build(headerInfoList, "ABaseController", DullJavaConfig.getPathController() + "/");
    }

    public static void build(List<String> headerInfoList, String fileName, String outPutPath) {
        File folder = new File(outPutPath);
        if (!folder.exists()) if (!folder.mkdirs()) log.error(outPutPath + "创建失败");
        File javaFile = new File(outPutPath, fileName + ".java");
        out = null;
        outw = null;
        bw = null;

        in = null;
        inr = null;
        bf = null;
        try {
            out = new FileOutputStream(javaFile);
            outw = new OutputStreamWriter(out, StandardCharsets.UTF_8);
            bw = new BufferedWriter(outw);
            InputStream in = Objects.requireNonNull(ClassUtils.getDefaultClassLoader()).getResourceAsStream("template/" + fileName + ".txt");
            assert in != null;
            inr = new InputStreamReader(in, StandardCharsets.UTF_8);
            bf = new BufferedReader(inr);
            for (String s : headerInfoList) {
                writeText(s);
            }
            String lineInfo;
            while ((lineInfo = bf.readLine()) != null) {
                writeText(lineInfo);
            }
            bw.flush();
        } catch (Exception e) {
            throw new ServiceException(e);
        } finally {
            closeStream();
        }
    }

    private static void closeStream() {
        try {
            if (bf != null) bf.close();
            if (inr != null) inr.close();
            if (in != null) in.close();
            if (bw != null) bw.close();
            if (outw != null) outw.close();
            if (out != null) out.close();
        } catch (IOException e) {
            throw new ServiceException("Base流异常");
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
