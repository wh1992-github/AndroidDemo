package com.example.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ZhengzeUtils {
    private static final String TAG = "ZhengzeUtils";

    public static void test() {
        boolean matchName = matchName("13655558888");
    }

    //正则表达式-匹配手机号码
    public static boolean matchName(String str) {
        Pattern pattern = Pattern.compile("^(0|86|17951)?(13[0-9]|15[012356789]|17[678]|18[0-9]|14[57])[0-9]{8}$");
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }

    //正则表达式-判断是否为数字
    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }

    //正则表达式-判断是否为IP
    public static boolean isIP(String str) {
        Pattern pattern = Pattern.compile("((2(5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2})(\\.((2(5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2})){3}");
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }
}
