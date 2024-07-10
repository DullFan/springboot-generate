package com.dullfan.generate.config;

import com.dullfan.generate.utils.StringUtils;
import com.dullfan.generate.utils.constant.Constants;

import com.dullfan.generate.utils.extremely.ServiceException;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class DullJavaConfig {
    /**
     * 项目空间路径
     * */
    public static final String PROJECT_PATH = "main/java";

    /**
     * mybatis空间路径
     * */
    public static final String MYBATIS_PATH = "main/resources/mappers";

    /**
     * 文件存放路径
     */
    @Getter
    private static String pathBase;
    /**
     * 包路径
     */
    @Getter
    private static String packageBase;

    /**
     * 作者
     */
    @Getter
    private static String author;

    /**
     * 是否忽略表前缀
     */
    @Getter
    private static Boolean tablePrefix;

    /**
     * 是否导入lombok
     */
    @Getter
    private static Boolean enabledLombok = false;

    /**
     * 忽略字段
     */
    @Getter
    private static HashSet<String> fieldIgnoreHashSet = new HashSet<>();

    /**
     * SpringBoot3 And SpringBoot2 配置
     */
    @Getter
    private static final HashMap<String, String> importSpringBootHashMap = new HashMap<>();

    @Value("${dull.lombok-enable}")
    public void setLombokEnableFlag(Boolean enabledLombok) {
        DullJavaConfig.enabledLombok = enabledLombok;
    }

    @Value("${dull.table-prefix}")
    public void setTablePrefix(Boolean tablePrefix) {
        DullJavaConfig.tablePrefix = tablePrefix;
    }

    @Value("${dull.field-ignore}")
    public void setFieldTimeFormatted(String fieldIgnore) {
        fieldIgnoreHashSet.addAll(Arrays.asList(StringUtils.trim(fieldIgnore).split(",")));
    }

    @Value("${dull.author}")
    public void setAuthor(String author) {
        DullJavaConfig.author = author;
    }

    @Value("${dull.path-base}")
    public void setPathBase(String pathBase) {
        DullJavaConfig.pathBase = pathBase;
    }

    @Value("${dull.package-base}")
    public void setPackageBase(String packageBase) {
        DullJavaConfig.packageBase = packageBase;
    }

    public static void setSpringBoot2() {
        importSpringBootHashMap.clear();
        importSpringBootHashMap.put("Resource", "import javax.annotation.Resource;");
        importSpringBootHashMap.put("HttpServletRequest", "import javax.servlet.http.HttpServletRequest;");
        importSpringBootHashMap.put("PostConstruct", "import javax.annotation.PostConstruct;");
    }

    public static void setSpringBoot3() {
        importSpringBootHashMap.clear();
        importSpringBootHashMap.put("Resource", "import jakarta.annotation.Resource;");
        importSpringBootHashMap.put("HttpServletRequest", "import jakarta.servlet.http.HttpServletRequest;");
        importSpringBootHashMap.put("PostConstruct", "import jakarta.annotation.PostConstruct;");
    }

    public static void setStaticTablePrefix(Boolean tablePrefix) {
        DullJavaConfig.tablePrefix = tablePrefix;
    }

    public static void setStaticAuthor(String author) {
        DullJavaConfig.author = author;
    }

    public static void setStaticFieldIgnore(String fieldIgnore) {
        fieldIgnoreHashSet.clear();
        // 去除输入字符串两端的空格，并检查去除空格后是否为空
        fieldIgnore = StringUtils.trim(fieldIgnore);
        if (StringUtils.isEmpty(fieldIgnore)) {
            return;
        }

        // 使用正则表达式验证只使用逗号作为分隔符
        if (!fieldIgnore.matches("[^,]+(,[^,]+)*")) {
            throw new ServiceException("错误：检测到无效分隔符。只能使用逗号。");
        }

        // 根据逗号分隔并去除每个部分的多余空格
        String[] fieldArray = fieldIgnore.split(",");
        List<String> trimmedFields = Arrays.stream(fieldArray)
                .map(String::trim)
                .toList();

        // 将有效值添加到 HashSet 中
        fieldIgnoreHashSet.addAll(trimmedFields);
    }

    public static void setStaticPackageBase(String packageBase) {
        if (StringUtils.isEmpty(packageBase)) {
            return;
        }
        if (!packageBase.matches(".*\\..+")) {
            throw new ServiceException("包不能为空，并且必须至少包含一个 . 字符后跟非空字符串。");
        }
        DullJavaConfig.packageBase = packageBase;
    }
}
