package com.dullfan.generate.utils;

import com.dullfan.generate.config.DullJavaConfig;
import jakarta.servlet.http.HttpServletResponse;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class FileUtils {

    /**
     * 读取文件压缩成sip
     * @param folder 需要压缩的文件路径
     * @param zipFilePath zip文件输出路径
     */
    public static void zipDirectory(File folder, String zipFilePath) {
        if (!folder.exists()) {
            System.out.println("目录不存在: " + folder.getPath());
            return;
        }
        if (!folder.isDirectory()) {
            System.out.println("指定的路径不是目录: " + folder.getPath());
            return;
        }

        try {
            FileOutputStream fos = new FileOutputStream(zipFilePath);
            ZipOutputStream zos = new ZipOutputStream(fos);
            zipFile(folder, folder.getName(), zos);
            zos.close();
            fos.close();
            System.out.println("已创建Zip文件: " + zipFilePath);
        } catch (IOException ioe) {
            System.out.println("创建zip文件出错: " + ioe);
        }
    }

    /**
     * 递归判断是否还有子文件夹
     * @param file 文件
     * @param fileName 文件名称
     * @param zos 输出流
     * @throws IOException 错误
     */
    private static void zipFile(File file, String fileName, ZipOutputStream zos) throws IOException {
        if (file.isHidden()) {
            return;
        }
        if (file.isDirectory()) {
            File[] children = file.listFiles();
            if (children.length == 0) {
                // 如果该目录为空，则将其添加为文件夹条目
                zos.putNextEntry(new ZipEntry(fileName + '/'));
                zos.closeEntry();
            } else {
                for (File childFile : children) {
                    System.out.println(DullJavaConfig.getPathBase() + fileName + "/" + childFile.getName());
                    zipFile(childFile, fileName + "/" + childFile.getName(), zos);
                }
            }
        } else {
            FileInputStream fis = new FileInputStream(file);
            ZipEntry zipEntry = new ZipEntry(fileName);
            zos.putNextEntry(zipEntry);
            byte[] bytes = new byte[1024];
            int length;
            while ((length = fis.read(bytes)) >= 0) {
                zos.write(bytes, 0, length);
            }
            fis.close();
        }
    }

    public static void deleteFile(String filePath) {
        File file = new File(filePath);
        File fileZip = new File(filePath + ".zip");
        deleteFileOrDirectory(file);
        deleteFileOrDirectory(fileZip);
    }

    // 删除文件或目录
    public static boolean deleteFileOrDirectory(File file) {
        // 如果是目录，先递归删除其子文件和子目录
        if (file.isDirectory()) {
            File[] children = file.listFiles();
            if (children != null) {
                for (File child : children) {
                    // 递归删除子文件或子目录
                    deleteFileOrDirectory(child);
                }
            }
        }
        // 删除文件或空目录
        return file.delete();
    }

    /**
     * 解压zip压缩包
     * @param byteArray 压缩包字节
     * @param targetDir 导出的路径
     * @throws IOException 异常
     */
    public static void unzip(byte[] byteArray, String targetDir) throws IOException {
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);
             ZipInputStream zipInputStream = new ZipInputStream(byteArrayInputStream)) {

            ZipEntry entry;
            while ((entry = zipInputStream.getNextEntry()) != null) {
                File file = new File(targetDir, entry.getName());

                if (entry.isDirectory()) {
                    file.mkdirs();
                } else {
                    File parent = file.getParentFile();
                    if (!parent.exists()) {
                        parent.mkdirs();
                    }

                    try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
                        byte[] buffer = new byte[1024];
                        int len;
                        while ((len = zipInputStream.read(buffer)) > 0) {
                            fileOutputStream.write(buffer, 0, len);
                        }
                    }
                }
                zipInputStream.closeEntry();
            }
        }
    }
}