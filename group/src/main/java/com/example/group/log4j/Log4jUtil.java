package com.example.group.log4j;

import android.os.Environment;
import android.text.TextUtils;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.io.File;

import de.mindpipe.android.logging.log4j.LogConfigurator;

public class Log4jUtil {
    private static final String TAG = "Log4jUtil";

    private static final String DEFAULT_FILE_NAME = Environment.getExternalStorageDirectory() + File.separator + "backup.log";
    private static final int MAX_FILE_SIZE = 10 * 1024 * 1024;

    public static void i(String tag, String message) {
        Logger mLogger = getLogger(tag);
        mLogger.info(message);
    }

    public static void i(String tag, String message, Throwable exception) {
        Logger mLogger = getLogger(tag);
        mLogger.info(message, exception);
    }

    private static Logger getLogger(String tag) {
        Logger logger;
        if (TextUtils.isEmpty(tag)) {
            logger = Logger.getRootLogger();
        } else {
            logger = Logger.getLogger(tag);
        }
        return logger;
    }

    //配置Log4j
    public static void configure() {
        final LogConfigurator logConfigurator = new LogConfigurator();
        try {
            logConfigurator.setFileName(DEFAULT_FILE_NAME);
            //备份数目
            logConfigurator.setMaxBackupSize(3);
            //文件大小
            logConfigurator.setMaxFileSize(MAX_FILE_SIZE);
            //以下为通用配置
            logConfigurator.setImmediateFlush(true);
            logConfigurator.setRootLevel(Level.DEBUG);
            logConfigurator.setFilePattern("%d\t%p/%c:\t%m%n");
            logConfigurator.configure();
            Log4jUtil.i(TAG, "Log4j config finish.");
        } catch (Throwable throwable) {
            logConfigurator.setResetConfiguration(true);
            Log4jUtil.i(TAG, "Log4j config error, use default config. Error = " + throwable);
        }
    }
}