package com.example.util;

import android.util.Log;

import java.text.DecimalFormat;

//工资个税计算方法
public class GongZi {
    private static final String TAG = "GongZi";
    //每个月的基本工资
    private static final float[] gongzi = {20000, 20000, 20000, 20000, 20000, 20000, 20000, 20000, 20000, 20000, 20000, 20000, 20000, 20000, 20000};
    //每个月的社保公积金扣除额
    private static final float[] wuxian = {5000, 5000, 5000, 5000, 5000, 5000, 5000, 5000, 5000, 5000, 5000, 5000};
    //工资合计
    private static float gongziheji = 0;
    //社保公积金缴纳合计
    private static float wuxianheji = 0;
    //已扣税总额
    private static float prekoushui = 0;
    //应扣税总额
    private static float koushuiheji = 0;

    public static void shouru() {
        for (int i = 0; i < 12; i++) {
            gongziheji += gongzi[i];
            wuxianheji += wuxian[i];
            float nashui = gongziheji - wuxianheji - (i + 1) * 6500;
            if (nashui <= 36000) {
                koushuiheji = nashui * 0.03f;
            } else if (nashui > 36000 && nashui <= 144000) {
                koushuiheji = nashui * 0.1f - 2520;
            } else if (nashui > 144000) {
                koushuiheji = nashui * 0.2f - 16920;
            }
            Log.i(TAG, (i + 1) + "月到手工资：" + roundTwo(gongzi[i] - wuxian[i] - (koushuiheji - prekoushui)) + ", 工资总额：" + roundTwo(gongziheji)
                    + ", 扣稅：" + roundTwo(koushuiheji - prekoushui) + ", 扣税总额：" + roundTwo(koushuiheji));
            prekoushui = koushuiheji;
        }
    }

    public static String roundTwo(float originNum) {
        DecimalFormat format = new DecimalFormat("0.00");
        return format.format(originNum);
    }
}
