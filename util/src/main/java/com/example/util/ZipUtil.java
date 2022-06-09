package com.example.util;


import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

@RequiresApi(api = Build.VERSION_CODES.N)
public class ZipUtil {
    private static final String TAG = "ZipUtil";

    /**
     * @param srcPath 要压缩的文件或文件夹
     * @param zipPath 压缩完成的Zip路径
     */
    public static void zipFolder(String srcPath, String zipPath) {
        try {
            ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(zipPath));
            //压缩
            File file = new File(srcPath);
            zipFile(srcPath, zipOutputStream, file.getParent().length() + 1);
            //完成和关闭
            zipOutputStream.finish();
            zipOutputStream.close();
        } catch (IOException e) {
            Log.i(TAG, "zipFolder: e = " + e.getMessage());
        }
    }

    /**
     * @param prePath 多余的路径去掉
     */
    private static void zipFile(String srcPath, ZipOutputStream zipOutputSteam, int prePath) throws IOException {
        File file = new File(srcPath);
        if (file.isDirectory()) {
            //文件夹
            String[] fileList = file.list();
            //没有子文件和压缩
            if (fileList.length <= 0) {
                ZipEntry zipEntry = new ZipEntry(file.getAbsolutePath().substring(prePath) + File.separator);
                Log.i(TAG, "zipFile: zipFolder = " + (file.getAbsolutePath().substring(prePath) + File.separator));
                zipOutputSteam.putNextEntry(zipEntry);
                zipOutputSteam.closeEntry();
            }
            //子文件和递归
            for (int i = 0; i < fileList.length; i++) {
                zipFile(file.getAbsolutePath() + File.separator + fileList[i], zipOutputSteam, prePath);
            }
        } else {
            ZipEntry zipEntry = new ZipEntry(file.getAbsolutePath().substring(prePath));
            Log.i(TAG, "zipFile: zipFile = " + (file.getAbsolutePath().substring(prePath)));
            FileInputStream inputStream = new FileInputStream(file);
            zipOutputSteam.putNextEntry(zipEntry);
            int len;
            byte[] buffer = new byte[8 * 1024];
            while ((len = inputStream.read(buffer)) != -1) {
                zipOutputSteam.write(buffer, 0, len);
            }
            zipOutputSteam.closeEntry();
        }
    }

    /**
     * @param zipPath 要解压的Zip路径
     * @param outPath 解压到的路径
     */
    public static void unZipFolder(String zipPath, String outPath) {
        Log.i(TAG, "unZipFolder: 解压的文件 = " + zipPath + ", 解压的目标路径 = " + outPath);
        try {
            ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(zipPath), Charset.forName("GBK"));
            ZipEntry zipEntry;
            String fileName;
            File root = new File(outPath);
            if (!root.exists()) {
                root.mkdirs();
            }
            while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                fileName = zipEntry.getName();
                Log.i(TAG, "unZipFolder: fileName = " + fileName);
                if (zipEntry.isDirectory()) {
                    //获取部件的文件夹名
                    fileName = fileName.substring(0, fileName.length() - 1);
                    File folder = new File(outPath + File.separator + fileName);
                    if (!folder.exists()) {
                        Log.i(TAG, "unZipFolder: Create the folder = " + outPath + File.separator + fileName);
                        folder.mkdirs();
                    }
                } else {
                    File file = new File(outPath + File.separator + fileName);
                    if (!file.exists()) {
                        Log.i(TAG, "unZipFolder: Create the file = " + outPath + File.separator + fileName);
                        file.getParentFile().mkdirs();
                        file.createNewFile();
                    }
                    //获取文件的输出流
                    FileOutputStream out = new FileOutputStream(file);
                    int len;
                    byte[] buffer = new byte[8 * 1024];
                    //读取字节到缓冲区
                    while ((len = zipInputStream.read(buffer)) != -1) {
                        //从缓冲区（0）位置写入字节
                        out.write(buffer, 0, len);
                        out.flush();
                    }
                    out.close();
                }
            }
            zipInputStream.close();
        } catch (IOException e) {
            Log.i(TAG, "unZipFolder: e = " + e.getMessage());
        }
        Log.i(TAG, "UnZipFolder: 解压完成");
    }
}