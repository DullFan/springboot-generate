package com.dullfan.generate.build;

import com.dullfan.generate.config.DullJavaConfig;

import java.io.BufferedWriter;
import java.io.IOException;


public class BuildComment {
    /**
     * 类注释
     */
    public static void createClassComment(BufferedWriter bufferedWriter,String classComment) throws IOException {
        bufferedWriter.write("/**");
        bufferedWriter.newLine();
        bufferedWriter.write(" * "+classComment);
        bufferedWriter.newLine();
        bufferedWriter.write(" * ");
        bufferedWriter.newLine();
        bufferedWriter.write(" * @author "+ DullJavaConfig.getAuthor());
        bufferedWriter.newLine();
        bufferedWriter.write(" */");
        bufferedWriter.newLine();
    }

    /**
     * 字段注释
     */
    public static void createFieldComment(BufferedWriter bufferedWriter,String fieldComment) throws IOException {
        bufferedWriter.write("\t/**");
        bufferedWriter.newLine();
        bufferedWriter.write("\t * "+ fieldComment);
        bufferedWriter.newLine();
        bufferedWriter.write("\t */");
        bufferedWriter.newLine();
    }

    /**
     * 方法注释
     */
    public static void createMethodComment(BufferedWriter bufferedWriter,String fieldComment) throws IOException {
        bufferedWriter.write("\t/**");
        bufferedWriter.newLine();
        bufferedWriter.write("\t * "+ fieldComment);
        bufferedWriter.newLine();
        bufferedWriter.write("\t */");
        bufferedWriter.newLine();
    }
}
