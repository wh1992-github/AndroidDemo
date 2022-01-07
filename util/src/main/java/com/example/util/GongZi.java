package com.example.util;

import android.util.Log;

//工资个税计算方法
public class GongZi {
    private static final String TAG = "GongZi";
    //每个月的基本工资
    private static final float gongzi = 10000;
    //每个月的餐补
    private static final float[] canbu = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    //每个月的社保公积金扣除额
    private static final float[] wuxian = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    //工会费
    private static final float gonghuifei = 5;
    //餐补合计
    private static float canbuheji = 0;
    //社保公积金缴纳合计
    private static float wuxianheji = 0;
    //已扣税总额
    private static float prekoushui = 0;
    //应扣税总额
    private static float koushui = 0;

    public static void shouru() {
        for (int i = 0; i < 11; i++) {
            canbuheji += canbu[i];
            wuxianheji += wuxian[i];
            float nashui = canbuheji + gongzi * (i + 1) - wuxianheji - (i + 1) * 6500;
            if (nashui <= 36000) {
                koushui = nashui * 0.03f;
            } else if (nashui > 36000 && nashui <= 144000) {
                koushui = nashui * 0.1f - 2520;
            } else if (nashui > 144000) {
                koushui = nashui * 0.2f - 16920;
            }

            Log.i(TAG, "shouru: " + (i + 1) + "月到手工资：" + (canbu[i] + gongzi - gonghuifei - wuxian[i] - (koushui - prekoushui))
                    + ", 扣稅：" + (koushui - prekoushui));
            prekoushui = koushui;
        }
    }
}
