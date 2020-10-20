package com.example.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SystemFileUtils {

    public static void deleteFile(File file) {
        if (file.isFile()) {
            //如果是文件,可以直接删除。
            //用.delete()实现。
            file.delete();
            return;
        }

        if (file.isDirectory()) {
            //如果是文件夹目录的话,必须遍历他的每一个子项来逐个删除。
            //原因在于.delete()只能删除文件和空文件夹。
            File[] childFiles = file.listFiles();
            if (childFiles == null || childFiles.length == 0) {
                file.delete();
                return;
            }
            //执行到此处,说明childFiles.length>0。
            //所以回调deleteFile()来继续完成删除任务。
            for (int i = 0; i < childFiles.length; i++) {
                deleteFile(childFiles[i]);
            }
            //此处是将空文件夹删除掉
            file.delete();
        }
    }

    private static final List<String> list = new ArrayList<>();

    //遍历所有文件 并将路径名存入集合中 参数需要 路径和集合
    public static List<String> recursionFile(File dir) {
        //得到某个文件夹下所有的文件
        File[] files = dir.listFiles();
        //文件为空
        if (files == null) {
            return list;
        }
        //遍历当前文件下的所有文件
        for (File file : files) {
            //如果是文件夹
            if (file.isDirectory()) {
                //则递归(方法自己调用自己)继续遍历该文件夹
                recursionFile(file);
            } else { //如果不是文件夹 则是文件
                //如果文件名以 .mp3结尾则是mp3文件
                if (file.getName().endsWith(".jpg")) {
                    //往图片集合中 添加图片的路径
                    list.add(file.getAbsolutePath());
                }
            }
        }
        return list;
    }
}
