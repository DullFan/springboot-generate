package com.dullfan.generate.utils;

import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

import java.util.Properties;

public class VelocityInitializer {

    public static void initVelocity() {
        Properties p = new Properties();
        // 加载classpath目录下的vm文件
        p.setProperty("resource.loader.file.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        // 定义字符集
        p.setProperty(Velocity.INPUT_ENCODING, "UTF-8");
        Velocity.init(p);
    }

}
