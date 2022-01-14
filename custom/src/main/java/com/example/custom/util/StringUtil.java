package com.example.custom.util;

import java.util.Locale;

public class StringUtil {

    //保留小数点后面多少位
    public static String formatWithString(double value, int digit) {
        String format = String.format(Locale.getDefault(), "%%.%df", digit);
        return String.format(Locale.getDefault(), format, value);
    }

    //格式化流量数据/文件大小。输入以字节为单位的长整数,输出带具体单位的字符串
    public static String formatData(long data) {
        String result = "";
        if (data > 1024 * 1024) {
            result = String.format(Locale.getDefault(), "%sM", formatWithString(data / 1024.0 / 1024.0, 1));
        } else if (data > 1024) {
            result = String.format(Locale.getDefault(), "%sK", formatWithString(data / 1024.0, 1));
        } else {
            result = String.format(Locale.getDefault(), "%sB", "" + data);
        }
        return result;
    }

}
