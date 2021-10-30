package com.example.device.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.os.Process;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@SuppressLint({"StaticFieldLeak", "SimpleDateFormat"})
public class CrashHandler implements Thread.UncaughtExceptionHandler {
    public static final String TAG = "CrashHandler";
    //系统默认的UncaughtExceptionHandler处理类
    private Thread.UncaughtExceptionHandler mDefaultHandler;
    //CrashHandler实例
    private static volatile CrashHandler sInstance;
    //程序的Context对象
    private Context mContext;
    //用来存储设备信息和异常信息
    private Map<String, Object> mInfos = new HashMap<>();
    //用于格式化日期,作为日志文件名的一部分
    private SimpleDateFormat mFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");

    //获取CrashHandler实例,单例模式
    public static CrashHandler getInstance() {
        if (sInstance == null) {
            synchronized (CrashHandler.class) {
                if (sInstance == null) {
                    sInstance = new CrashHandler();
                }
            }
        }
        return sInstance;
    }

    private CrashHandler() {
    }

    //初始化
    public void init(Context context) {
        mContext = context;
        //获取系统默认的UncaughtException处理器
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        //设置该CrashHandler为程序的默认处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    //当UncaughtException发生时会转入该函数来处理
    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        if (throwable == null) {
            if (mDefaultHandler != null) {
                //如果没有处理则让系统默认的异常处理器来处理
                mDefaultHandler.uncaughtException(thread, throwable);
            }
        } else {
            showToast();
            //收集设备参数信息
            collectDeviceInfo(mContext);
            //保存日志文件
            saveCrashInfo2File(throwable);

            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                Log.i(TAG, "uncaughtException: e = " + e.getMessage());
            }
            //退出程序
            Process.killProcess(Process.myPid());
            System.exit(1);//非正常退出 1.
        }
    }

    //收集设备参数信息
    public void collectDeviceInfo(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES);
            if (packageInfo != null) {
                String versionName = packageInfo.versionName;
                int versionCode = packageInfo.versionCode;
                mInfos.put("versionName", versionName);
                mInfos.put("versionCode", versionCode);
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "collectDeviceInfo: e1 = " + e.getMessage());
        }
        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                mInfos.put(field.getName(), field.get(null).toString());
                Log.i(TAG, "field = " + field.getName() + " : " + field.get(null));
            } catch (Exception e) {
                Log.e(TAG, "collectDeviceInfo: e2 = " + e.getMessage());
            }
        }
    }

    //保存错误信息到文件中
    //返回文件名称, 便于将文件传送到服务器
    private void saveCrashInfo2File(Throwable throwable) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<String, Object> entry : mInfos.entrySet()) {
            String key = entry.getKey();
            String value = String.valueOf(entry.getValue());
            stringBuilder.append(key).append("=").append(value).append("\n");
        }
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        throwable.printStackTrace(printWriter);
        Throwable cause = throwable.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        String result = writer.toString();
        stringBuilder.append(result);
        try {
            String time = mFormatter.format(new Date());
            String fileName = time + ".txt";
            if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) || !Environment.isExternalStorageRemovable()) {
                String path = Environment.getExternalStorageDirectory().toString() + "/crash_log/";
                File dir = new File(path);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                FileOutputStream fos = new FileOutputStream(path + fileName);
                fos.write(stringBuilder.toString().getBytes());
                fos.close();
            }
        } catch (Exception e) {
            Log.i(TAG, "saveCrashInfo2File: e = " + e);
        }
    }

    private void showToast() {
        //使用Toast来显示异常信息
        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                Toast.makeText(mContext, "程序出现异常,正在收集日志,即将退出。", Toast.LENGTH_LONG).show();
                Looper.loop();
            }
        }.start();
    }
}
