
package com.example.util;

import android.content.Context;
import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class StorageUtils {
    private static final String TAG = "StorageUtils";
    /**
     * 应用文件根目录,更新为缓存目录
     */
    private static String FILE_DIR_APP;
    /**
     * 应用Log保存目录
     */
    private static String FILE_DIR_APP_LOG;
    /**
     * 应用crash保存目录
     */
    private static String FILE_DIR_APP_CRASH;
    private static String FILE_DIR_APP_APK;

    private static boolean sIsInit = false;

    public static void initExtDir(Context context) {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            File fileDir = context.getExternalFilesDir(null);
            if (null != fileDir) {
                FILE_DIR_APP = fileDir.getAbsolutePath() + File.separator;
            }
            File logDir = context.getExternalFilesDir("log");
            if (null != logDir) {
                FILE_DIR_APP_LOG = logDir.getAbsolutePath() + File.separator;
            }
            File crashDir = context.getExternalFilesDir("crash");
            if (null != crashDir) {
                FILE_DIR_APP_CRASH = crashDir.getAbsolutePath() + File.separator;
            }
            File apkDir = context.getExternalFilesDir("apk");
            if (null != apkDir) {
                FILE_DIR_APP_APK = apkDir.getAbsolutePath() + File.separator;
            }
            sIsInit = true;
        } else {
            LogUtil.w(TAG, "The external storage state is not MEDIA_MOUNTED.");
        }
    }

    /**
     * app 文件存储根目录
     */
    public static String getAppDir(Context context) {
        if (null == context) {
            return null;
        }
        if (!sIsInit) {
            initExtDir(context);
        }
        if (!sIsInit) {
            return null;
        }
        return FILE_DIR_APP;
    }

    /**
     * Log dir
     */
    public static String getLogDir(Context context) {
        if (null == context) {
            return null;
        }
        if (!sIsInit) {
            initExtDir(context);
        }
        if (!sIsInit) {
            return null;
        }
        return FILE_DIR_APP_LOG;
    }

    /**
     * Crash dir
     */
    public static String getCrashDir(Context context) {
        if (null == context) {
            return null;
        }
        if (!sIsInit) {
            initExtDir(context);
        }
        if (!sIsInit) {
            return null;
        }
        return FILE_DIR_APP_CRASH;
    }

    /**
     * 更新包存放位置
     */
    public static String getApkDir(Context context) {
        if (null == context) {
            return null;
        }
        if (!sIsInit) {
            initExtDir(context);
        }
        if (!sIsInit) {
            return null;
        }
        return FILE_DIR_APP_APK;
    }

    public static File getInternalFileDir(Context context) {
        if (null == context) {
            return null;
        }
        if (!sIsInit) {
            initExtDir(context);
        }
        if (!sIsInit) {
            return null;
        }
        return context.getFilesDir();
    }

    public static List<String> getAllExternalSdcardPath() {
        List<String> pathList = new ArrayList<>();
        String firstPath = Environment.getExternalStorageDirectory().getPath();
        LogUtil.d(TAG, "getAllExternalSdcardPath , firstPath = " + firstPath);
        try {
            //运行mount命令,获取命令的输出,得到系统中挂载的所有目录
            Runtime runtime = Runtime.getRuntime();
            Process proc = runtime.exec("mount");
            InputStream is = proc.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            String line;
            BufferedReader br = new BufferedReader(isr);
            while ((line = br.readLine()) != null) {
                //将常见的linux分区过滤掉
                if (line.contains("proc") || line.contains("tmpfs") || line.contains("media")
                        || line.contains("asec") || line.contains("secure") || line.contains("system")
                        || line.contains("cache") || line.contains("sys") || line.contains("data")
                        || line.contains("shell") || line.contains("root") || line.contains("acct")
                        || line.contains("misc") || line.contains("obb")) {
                    continue;
                }
                //下面这些分区是我们需要的
                if (line.contains("fat") || line.contains("fuse") || (line.contains("ntfs"))) {
                    //将mount命令获取的列表分割,items[0]为设备名,items[1]为挂载路径
                    String[] items = line.split(" ");
                    if (items != null && items.length > 1) {
                        String path = items[1].toLowerCase(Locale.getDefault());
                        //添加一些判断,确保是sd卡,如果是otg等挂载方式,可以具体分析并添加判断条件
                        if (path != null && !pathList.contains(path) && path.contains("sd")) {
                            pathList.add(items[1]);
                        } else if (path != null && !pathList.contains(path) && path.contains("usb")) {
                            pathList.add(items[1]);
                        } else if (path != null && !pathList.contains(path) && path.contains("storage")) {
                            pathList.add(items[1]);
                        }
                    }
                }
            }
        } catch (Exception e) {
            LogUtil.e(TAG, "Exception: " + e.getMessage());
        }

        if (!pathList.contains(firstPath)) {
            pathList.add(firstPath);
        }

        return pathList;
    }
}
