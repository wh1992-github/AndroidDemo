package com.example.junior.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by test on 2017/9/14.
 */
public class DateUtil {
    public static String getNowDateTime(Long fdf) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        return sdf.format(new Date(fdf));
    }

    public static String getNowTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        return sdf.format(new Date());
    }

}
