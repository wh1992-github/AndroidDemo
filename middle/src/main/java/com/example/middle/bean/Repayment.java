package com.example.middle.bean;
/**
 * 封装 Repayment 相关逻辑的类。
 */

public class Repayment {
    public double mTotal;
    public double mMonthRepayment;
    public double mMonthMinus;
    public double mTotalInterest;

    public Repayment() {
        mTotal = 0;
        mMonthRepayment = 0;
        mMonthMinus = 0;
        mTotalInterest = 0;
    }
}
