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
     * 是否忽略表前缀
     */
    @Getter
    private static Boolean tablePrefix;
    /**
     * 搜索后缀
     */
    @Getter
    private static String beanQuery;

    /**
     * 服务后缀
     */
    @Getter
    private static String beanService = "Service";

    /**
     * 服务后缀
     */
    @Getter
    private static String beanMapper = "Mapper";

    /**
     * 服务后缀
     */
    @Getter
    private static String beanServiceImpl = "ServiceImpl";

    /**
     * 服务后缀
     */
    @Getter
    private static String beanController = "Controller";
    /**
     * 文件存放路径
     */
    @Getter
    private static String pathBase;
    /**
     * 文件存放路径--临时
     */
    @Getter
    private static String pathBaseTemporary;
    /**
     * 文件UUID
     */
    @Getter
    private static String fileUUID;
    /**
     * 包路径
     */
    @Getter
    private static String packageBase;
    /**
     * po包路径 如: com.dullfan.CodeTest.entity.po
     */
    @Getter
    private static String packagePo = "entity.po";
    /**
     * query包路径
     */
    @Getter
    private static String packageQuery = "entity.query";
    /**
     * 作者
     */
    @Getter
    private static String author;
    /**
     * 忽略字段
     */
    @Getter
    private static HashSet<String> fieldIgnoreHashSet = new HashSet<>();
    /**
     * 日期是否格式化
     */
    @Getter
    private static Boolean isFieldTimeFormatted;

    /**
     * 如: D:/AStudyData/IdeaProjects/CodeTest/src/main/java/com/dullfan/CodeTest/entity/po
     */
    @Getter
    private static String pathPackagePo;

    /**
     * 如: D:/AStudyData/IdeaProjects/CodeTest/src/main/java/com/dullfan/CodeTest
     */
    @Getter
    private static String pathPackageBase;

    /**
     * Query的包路径
     */
    @Getter
    private static String pathPackageQuery;

    /**
     * 模糊查询 - String模糊搜索
     */
    @Getter
    private static String queryFuzzy;

    /**
     * 模糊查询 - 开始日期
     */
    @Getter
    private static String queryTimeStart;

    /**
     * 模糊查询 - 结束日期
     */
    @Getter
    private static String queryTimeEnd;

    /**
     * mapper包路径
     */
    @Getter
    private static String packageMapper = "mappers";

    /**
     * mapper路径
     */
    @Getter
    private static String pathMapper = "mappers";

    /**
     * service包路径
     */
    @Getter
    private static String packageService = "service";

    /**
     * service路径
     */
    @Getter
    private static String pathService = "service";

    /**
     * serviceImpl包路径
     */
    @Getter
    private static String packageServiceImpl = "impl";

    /**
     * controller路径
     */
    @Getter
    private static String pathController = "controller";

    /**
     * controller包路径
     */
    @Getter
    private static String packageController = "controller";

    /**
     * serviceImpl路径
     */
    @Getter
    private static String pathServiceImpl = "impl";

    /**
     * vo路径
     */
    @Getter
    private static String pathVo = "vo";

    /**
     * vo包路径
     */
    @Getter
    private static String packageVo = "vo";

    /**
     * Constant路径
     */
    @Getter
    private static String pathUtilsConstant = "utils/constant";

    /**
     * Constant包路径
     */
    @Getter
    private static String packageUtilsConstant = ".utils.constant";

    /**
     * Enum路径
     */
    @Getter
    private static String pathUtilsEnum = "utils/enums";

    /**
     * Enum包路径
     */
    @Getter
    private static String packageUtilsEnum = ".utils.enums";

    /**
     * Utils路径
     */
    @Getter
    private static String pathUtils = "utils";
    /**
     * Utils包路径
     */
    @Getter
    private static String packageUtils = ".utils";

    /**
     * SpringBoot3 And SpringBoot2 配置
     */
    @Getter
    private static HashMap<String, String> importSpringBootHashMap = new HashMap<>();

    @Value("${dull.table-prefix}")
    public void setTablePrefix(Boolean tablePrefix) {
        DullJavaConfig.tablePrefix = tablePrefix;
    }

    @Value("${dull.is-field-time-formatted}")
    public void setFieldTimeFormatted(Boolean isFieldTimeFormatted) {
        DullJavaConfig.isFieldTimeFormatted = isFieldTimeFormatted;
    }

    @Value("${dull.field-ignore}")
    public void setFieldTimeFormatted(String fieldIgnore) {
        fieldIgnoreHashSet.addAll(Arrays.asList(StringUtils.trim(fieldIgnore).split(",")));
    }

    @Value("${dull.author}")
    public void setAuthor(String author) {
        DullJavaConfig.author = author;
    }

    @Value("${dull.bean-query}")
    public void setTablePrefix(String beanQuery) {
        DullJavaConfig.beanQuery = beanQuery;
    }

    @Value("${dull.path-base}")
    public void setPathBase(String pathBase) {
        DullJavaConfig.pathBase = pathBase;
    }

    @Value("${dull.path-base}")
    public void setPathBaseTemporary(String pathBase) {
        DullJavaConfig.pathBaseTemporary = pathBase;
    }

    @Value("${dull.package-base}")
    public void setPackageBase(String packageBase) {
        DullJavaConfig.packageBase = packageBase;
    }

    @Value("${dull.query-fuzzy}")
    public void setQueryFuzzy(String queryFuzzy) {
        DullJavaConfig.queryFuzzy = queryFuzzy;
    }

    @Value("${dull.query-time-start}")
    public void setQueryTimeStart(String queryTimeStart) {
        DullJavaConfig.queryTimeStart = queryTimeStart;
    }

    @Value("${dull.query-time-end}")
    public void setQueryTimeEnd(String queryTimeEnd) {
        DullJavaConfig.queryTimeEnd = queryTimeEnd;
    }

    @PostConstruct
    private void init() {
        settingPathBase();
        pathPackageBase = pathBase + Constants.PATH_JAVA + "/" + packageBase.replace(".", "/");
        pathPackagePo = pathPackageBase + "/" + packagePo.replace(".", "/");
        packagePo = packageBase + "." + packagePo;
        pathPackageQuery = (pathPackageBase + "/" + packageQuery).replace(".", "/");
        packageQuery = packageBase + "." + packageQuery;

        pathMapper = pathPackageBase + "/" + pathMapper;
        packageMapper = (packageBase + "." + packageMapper);

        pathService = pathPackageBase + "/" + pathService;
        packageService = (packageBase + "." + packageService);

        pathServiceImpl = pathService + "/" + pathServiceImpl;
        packageServiceImpl = (packageService + "." + packageServiceImpl);

        pathController = pathPackageBase + "/" + pathController;
        packageController = (packageBase + "." + packageController);

        pathVo = pathPackageBase + "/entity/" + pathVo;
        packageVo = (packageBase + ".entity." + packageVo);

        packageUtilsConstant = packageBase + packageUtilsConstant;
        pathUtilsConstant = pathPackageBase + "/" + pathUtilsConstant;

        packageUtilsEnum = packageBase + packageUtilsEnum;
        pathUtilsEnum = pathPackageBase + "/" + pathUtilsEnum;

        packageUtils = packageBase + packageUtils;
        pathUtils = pathPackageBase + "/" + pathUtils;
    }

    private static void initData() {
        settingPathBase();
        // 保留初始化的默认值或设置为预定义的默认值
        pathMapper = "mappers";
        packageMapper = "mappers";
        pathService = "service";
        packageService = "service";
        pathServiceImpl = "impl";
        packageServiceImpl = "impl";
        pathController = "controller";
        packageController = "controller";
        pathVo = "vo";
        packageVo = "vo";
        pathUtilsConstant = "utils/constant";
        packageUtilsConstant = ".utils.constant";
        pathUtilsEnum = "utils/enums";
        packageUtilsEnum = ".utils.enums";
        pathUtils = "utils";
        packageUtils = ".utils";
        pathPackagePo = "entity/po";
        packagePo = "entity.po";
        packageQuery = "entity.query";
        pathPackageQuery = "entity/query";

        // 使用当前配置重新初始化路径和包名
        pathBase = pathBaseTemporary + (fileUUID != null ? fileUUID + "/src/" : "/src/");
        pathPackageBase = pathBase + Constants.PATH_JAVA + "/" + (packageBase != null ? packageBase.replace(".", "/") : "");

        pathPackagePo = pathPackageBase + "/" + packagePo.replace(".", "/");
        packagePo = packageBase + "." + packagePo;

        pathPackageQuery = pathPackageBase + "/" + packageQuery.replace(".", "/");
        packageQuery = packageBase + "." + packageQuery;

        pathMapper = pathPackageBase + "/" + pathMapper;
        packageMapper = packageBase + "." + packageMapper;

        pathService = pathPackageBase + "/" + pathService;
        packageService = packageBase + "." + packageService;

        pathServiceImpl = pathService + "/" + pathServiceImpl;
        packageServiceImpl = packageService + "." + packageServiceImpl;

        pathController = pathPackageBase + "/" + pathController;
        packageController = packageBase + "." + packageController;

        pathVo = pathPackageBase + "/entity/" + pathVo;
        packageVo = packageBase + ".entity." + packageVo;

        pathUtilsConstant = pathPackageBase + "/" + pathUtilsConstant;
        packageUtilsConstant = packageBase + packageUtilsConstant;

        pathUtilsEnum = pathPackageBase + "/" + pathUtilsEnum;
        packageUtilsEnum = packageBase + packageUtilsEnum;

        pathUtils = pathPackageBase + "/" + pathUtils;
        packageUtils = packageBase + packageUtils;
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

    /**
     * 重置PathBase
     */
    public static void resetPathBase() {
        pathBase = pathBaseTemporary;
    }

    /**
     * 设置PathBase
     */
    public static void settingPathBase() {
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        fileUUID = uuid;
        pathBase = pathBaseTemporary + uuid + "/src/";
    }

    /**
     * 手动设置PathBase
     */
    public static void manualSettingPathBase(String uuid) {
        pathBase = pathBaseTemporary + uuid + "/src/";
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
        initData();
    }
}
