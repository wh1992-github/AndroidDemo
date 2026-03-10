package com.example.helloworld;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class JDK {
    private static final String TAG = "Constants---";
    private static final List<Float> mList = new ArrayList<>();

    private static float buy_min = 100f, sell_min = 100f;
    private static float buy_max = 0f, sell_max = 0f;

    //收益超过该数
    private static final int MAX_MONEY = 1000;

    public void start(Context context) {
        Log.i(TAG, "---------------------------------------------------------------- ALL START ------------------------------------------------------------------ ");

        printTest("TEST", Constants.TEST());

//        printMonth(context);

        Log.i(TAG, "----------------------------------------------------------------- ALL END ------------------------------------------------------------------- ");
    }

    public void printTest(String gupiao, Float[] array) {
        mList.clear();
        mList.addAll(Arrays.asList(array));
        Log.i(TAG, gupiao + ": ------ START ------ name = " + gupiao + ", size = " + mList.size());
        float base_money;
        float sell_money = 0;
        float buy_price = 0;
        float sell_price;
        boolean flag;
        int day = 10;
        //每240天是一个周期
        int cycle_day = 240;
        int total_day = 480;
        float winRate = 0f;
        float sumMoney = 0f;
        float multiple = 1.0f;
        StringBuilder stringBuilder = new StringBuilder();
        for (int days = Math.min(mList.size(), total_day); days >= cycle_day; days -= cycle_day) {
            List<Float> list = mList.subList(Math.max(mList.size() - days, 0), Math.max(mList.size() - days + cycle_day, 1));
            //盈亏百分比: 在 2% - 6% 之间 选择最优
            for (int b = 200; b <= 600; b += 20) {
                for (int s = 200; s <= 600; s += 20) {
                    flag = false;
                    float temp = list.get(0);
                    int tempIndex = 0;
                    base_money = 10000;
                    for (int i = 0; i < list.size(); i++) {
                        if (!flag) {
                            if (temp >= list.get(i)) {
                                temp = list.get(i);
                                tempIndex = i;
                            }
                            if ((i - tempIndex) > day) {
                                temp = list.get(++tempIndex);
                            }
                        }
                        if (flag) {
                            if (temp <= list.get(i)) {
                                temp = list.get(i);
                                tempIndex = i;
                            }
                            if ((i - tempIndex) > day) {
                                temp = list.get(++tempIndex);
                            }
                        }
                        //B
                        if (!flag && ((list.get(i) - temp) / temp > (float) b / 10000)) {
                            flag = true;
                            buy_price = list.get(i);
                            temp = list.get(i);
                            tempIndex = i;
                        }
                        //S
                        if (flag && ((list.get(i) - temp) / temp < (float) -s / 10000 || i == list.size() - 1)) {
                            flag = false;
                            sell_price = list.get(i);
                            temp = list.get(i);
                            tempIndex = i;
                            base_money = base_money * ((sell_price - buy_price) / buy_price) + base_money - 15;
                        }
                    }
                    if (base_money > sell_money) {
                        sell_money = base_money;
                        buy_min = (float) b / 10000;
                        sell_min = (float) -s / 10000;
                        buy_max = (float) b / 10000;
                        sell_max = (float) -s / 10000;
                    } else if (base_money == sell_money) {
                        buy_max = (float) b / 10000;
                        sell_max = (float) -s / 10000;
                    }
                }
            }
            winRate += (sell_money - 10000) > 1600 ? 1.0f : 0.0f;
            sumMoney += (sell_money - 10000);
            multiple = multiple * (sell_money / 10000);
            stringBuilder.append(gupiao + ": 第" + days + " - " + (days - cycle_day == 0 ? "000" : days - cycle_day) + "天 收益 = " + formatNumTwo(sell_money - 10000) + ", 第" + total_day + " - " + (days - cycle_day == 0 ? "000" : days - cycle_day) + "天 总收益 = " + formatNumTwo(sumMoney) + ", buy = (" + formatNumTwo(buy_min * 100) + "%~" + formatNumTwo(buy_max * 100) + "%), sell = (" + formatNumTwo(sell_min * 100) + "%~" + formatNumTwo(sell_max * 100) + "%)" + ((sell_money - 10000) > 1600 ? "" : ", ×××××× ")).append("\n");
            printMoney(gupiao, list, day, buy_min, sell_min);
            if (days == cycle_day) {
                Log.i(TAG, stringBuilder.toString());
                Log.i(TAG, gupiao + ": 最近" + total_day + "天复利总收益 = " + formatNumTwo(multiple * 10000 - 10000) + ", 总收益 = " + formatNumTwo(sumMoney) + ", 盈利率 = " + formatNumTwo(winRate * 100 / (total_day / cycle_day)) + "%");
                stringBuilder.delete(0, stringBuilder.length());
            }
            sell_money = 0;
        }
        Log.i(TAG, gupiao + ": ------- END ------- name = " + gupiao + ", size = " + mList.size());
    }

    public void printMoney(String gupiao, List<Float> list, int day, float buy, float sell) {
        float temp = list.get(0);
        float base_money = 10000;
        float sell_money = 0;
        float buy_price = 0;
        float sell_price = 0;
        boolean flag = false;
        float sum = 0;
        float high_price = 0;
        int tempIndex = 0;
        int flagDay = 0;
        float buy_zf = 100;
        float sell_df = 100;
        for (int i = 0; i < list.size(); i++) {
            if (!flag) {
                if (temp >= list.get(i)) {
                    temp = list.get(i);
                    tempIndex = i;
                }
                if ((i - tempIndex) > day) {
                    temp = list.get(++tempIndex);
                }
            }
            if (flag) {
                if (temp <= list.get(i)) {
                    temp = list.get(i);
                    tempIndex = i;
                }
                if ((i - tempIndex) > day) {
                    temp = list.get(++tempIndex);
                }
            }
            //B
            if (!flag && ((list.get(i) - temp) / temp > buy)) {
                flag = true;
                buy_price = list.get(i);
                buy_zf = (list.get(i) - temp) / temp;
                temp = list.get(i);
                tempIndex = i;
                flagDay = i;
                Log.i(TAG, gupiao + ": 第" + (flagDay + 1) + "天买入, 价格 = " + formatNumTwo(buy_price) + ", 总价 = " + formatNumTwo(base_money) + ", 涨幅 = " + formatNumTwo(buy_zf * 100) + "%");
            }
            //应该买入
            if (!flag && (i == list.size() - 1)) {
                float buy_ = buy * temp + temp;
                Log.i(TAG, gupiao + ": 第" + (i + 2) + "天买入, 价格应该大于 " + formatNumTwo(buy_));
            }
            //应该卖出
            if (flag && (i == list.size() - 1)) {
                float sell_ = sell * temp + temp;
                Log.i(TAG, gupiao + ": 第" + (i + 2) + "天卖出, 价格应该小于 " + formatNumTwo(sell_));
            }
            //S
            if (flag && ((list.get(i) - temp) / temp < sell || i == list.size() - 1)) {
                boolean isTrue = ((list.get(i) - temp) / temp < sell && i == list.size() - 1);
                flag = false;
                sell_price = list.get(i);
                sell_df = (list.get(i) - temp) / temp;
                high_price = list.get(i);
                temp = list.get(i);
                tempIndex = i;
                Log.i(TAG, gupiao + ": 第" + (i + 1) + "天" + ((i == list.size() - 1) ? (isTrue ? "真的" : "假的") : "") + "卖出, 价格 = " + formatNumTwo(sell_price) + ", 总价 = " + formatNumTwo(base_money * ((sell_price - buy_price) / buy_price) - 15 + base_money) + ", 跌幅 = " + formatNumTwo(sell_df * 100) + "%, 持有 = " + (i - flagDay) + " 天, 收益 = " + formatNumTwo(base_money * ((sell_price - buy_price) / buy_price) - 15) + ", 盈利 = " + formatNumTwo((base_money * ((sell_price - buy_price) / buy_price) - 15) * 100 / base_money) + "%" + (((base_money * ((sell_price - buy_price) / buy_price) - 15) < 0) ? ", ******" : ""));
                sum += (base_money * ((sell_price - buy_price) / buy_price) - 15);
                base_money = base_money * ((sell_price - buy_price) / buy_price) - 15 + base_money;
                flagDay = i;
            }

            high_price = Math.max(high_price, list.get(i));
        }
        Log.i(TAG, gupiao + ": 实操盈利 = " + formatNumTwo(sum) + ", buy = " + buy + ", sell = " + sell);
    }

    public void printMonth(Context context) {
        String[] names = {
//                "通富微电", "晶方科技", "浪潮信息", "曲江文旅", "三特索道",
                "长白山"};
        String name;
        for (String s : names) {
            name = s + ".txt";
            for (int year = 2016; year <= 2025; year++) {
                Log.i(TAG, "printMonth: year -------------------------- " + year + ", " + s + " -------------------------- ");
                float sum = 0;
                for (int month = 1; month <= 12; month++) {
                    float average;
                    float max;
                    float min;
                    String minTime;
                    String maxTime;
                    if (month < 12) {
                        minTime = getMonthMin(context, year, name, formatNumTwo(month));
                        maxTime = getMonthMax(context, year, name, formatNumTwo(month + 1));
                    } else {
                        minTime = getMonthMin(context, year, name, formatNumTwo(month));
                        maxTime = getMonthMax(context, year + 1, name, formatNumTwo(1));
                    }
                    if (!TextUtils.isEmpty(minTime) && !TextUtils.isEmpty(maxTime)) {
                        average = getMonthAverage(context, year, name, formatNumTwo(month));
                        max = Float.parseFloat(maxTime.substring(maxTime.indexOf("------") + 6));
                        min = Float.parseFloat(minTime.substring(minTime.indexOf("------") + 6));
//                        Log.i(TAG, "printMonth: time = " + minTime.substring(0, 10)
//                                + ", min = " + formatNumTwo(min) + " ~ " + maxTime.substring(0, 10) + ", max = " + formatNumTwo(max)
//                                + ", m = " + formatNumTwo((max - min) / min * 100) + "%" + (((max - min) / min > 0.05f) ? "" : " ******"));
                        Log.i(TAG, "printMonth: time = " + minTime.substring(0, 7) + ", average = " + formatNumTwo(average) + " ~ " + maxTime.substring(0, 10) + ", max = " + formatNumTwo(max) + ", m = " + formatNumTwo((max - average) / average * 100) + "%" + (((max - average) / average > 0.10f) ? " ******" : ""));
                        sum += (max - average) / average;
                    } else {
                        average = getMonthAverage(context, year, name, formatNumTwo(month));
                        Log.i(TAG, "printMonth: time = " + year + "-" + formatNumTwo(month) + ", average = " + formatNumTwo(average));
                        break;
                    }
                }
                Log.i(TAG, "printMonth: year = " + year + ", 最理想: " + formatNumTwo(sum * 100) + "%");
            }
        }
    }

    public String formatNumTwo(double money) {
        DecimalFormat format = new DecimalFormat("0.00");
        return format.format(money);
    }

    @SuppressLint("DefaultLocale")
    public String formatNumTwo(int money) {
        return String.format(Locale.getDefault(), "%02d", money);
    }

    public float getMonthAverage(Context context, int year, String name, String month) {
        float sum = 0;
        int days = 0;
        try {
            InputStream inputStream = context.getResources().getAssets().open(name);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String lines;
            while ((lines = bufferedReader.readLine()) != null) {
                if (lines.contains(String.valueOf(year)) && (lines.contains("-" + month + "-"))) {
                    days++;
                    sum += Float.parseFloat(lines.substring(lines.indexOf("------") + 6));
                }
            }
            bufferedReader.close();
        } catch (IOException e) {
            Log.i(TAG, "printMonth: e = " + e.getMessage());
        }
        return sum / days;
    }

    public String getMonthMin(Context context, int year, String name, String month) {
        float min = 10000;
        String time = "";
        try {
            InputStream inputStream = context.getResources().getAssets().open(name);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String lines;
            while ((lines = bufferedReader.readLine()) != null) {
                if (lines.contains(String.valueOf(year)) && (lines.contains("-" + month + "-"))) {
                    if (min > Float.parseFloat(lines.substring(lines.indexOf("------") + 6))) {
                        min = Float.parseFloat(lines.substring(lines.indexOf("------") + 6));
                        time = lines;
                    }
                }
            }
            bufferedReader.close();
        } catch (IOException e) {
            Log.i(TAG, "printMonth: e = " + e.getMessage());
        }
        return time;
    }

    public String getMonthMax(Context context, int year, String name, String month) {
        float max = 0;
        String time = "";
        try {
            InputStream inputStream = context.getResources().getAssets().open(name);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String lines;
            while ((lines = bufferedReader.readLine()) != null) {
                if (lines.contains(String.valueOf(year)) && (lines.contains("-" + month + "-"))) {
                    if (max < Float.parseFloat(lines.substring(lines.indexOf("------") + 6))) {
                        max = Float.parseFloat(lines.substring(lines.indexOf("------") + 6));
                        time = lines;
                    }
                }
            }
            bufferedReader.close();
        } catch (IOException e) {
            Log.i(TAG, "printMonth: e = " + e.getMessage());
        }
        return time;
    }

    /**
     * 读取股票数据文件，计算每个月最低点买入、下个月最高点卖出的收益
     */
    public void printBestBuySell(Context context, String fileName) {
        // 读取所有数据行
        List<String> allLines = new ArrayList<>();
        try {
            InputStream inputStream = context.getResources().getAssets().open(fileName);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                if (!TextUtils.isEmpty(line)) {
                    allLines.add(line);
                }
            }
            bufferedReader.close();
        } catch (IOException e) {
            Log.e(TAG, "printBestBuySell: e = " + e.getMessage());
            return;
        }

        // 按年月分组: key = "2016-01", value = list of lines
        java.util.LinkedHashMap<String, List<String>> monthMap = new java.util.LinkedHashMap<>();
        for (String line : allLines) {
            String yearMonth = line.substring(0, 7); // "2016-01"
            if (!monthMap.containsKey(yearMonth)) {
                monthMap.put(yearMonth, new ArrayList<>());
            }
            monthMap.get(yearMonth).add(line);
        }

        List<String> monthKeys = new ArrayList<>(monthMap.keySet());
        float totalProfit = 0;
        int profitCount = 0;
        int lossCount = 0;

        Log.i(TAG, "printBestBuySell: ====== " + fileName + " 月度策略：上月最低买入 → 下月最高卖出 ======");

        for (int i = 0; i < monthKeys.size() - 1; i++) {
            String buyMonth = monthKeys.get(i);
            String sellMonth = monthKeys.get(i + 1);
            List<String> buyLines = monthMap.get(buyMonth);
            List<String> sellLines = monthMap.get(sellMonth);

            // 找本月最低点
            String buyDay = "";
            float buyPrice = Float.MAX_VALUE;
            for (String line : buyLines) {
                float price = Float.parseFloat(line.substring(line.indexOf("------") + 6));
                if (price < buyPrice) {
                    buyPrice = price;
                    buyDay = line.substring(0, 10);
                }
            }

            // 找下月最高点
            String sellDay = "";
            float sellPrice = 0;
            for (String line : sellLines) {
                float price = Float.parseFloat(line.substring(line.indexOf("------") + 6));
                if (price > sellPrice) {
                    sellPrice = price;
                    sellDay = line.substring(0, 10);
                }
            }

            float profit = (sellPrice - buyPrice) / buyPrice * 100;
            totalProfit += profit;
            if (profit > 0) profitCount++;
            else lossCount++;

            Log.i(TAG, "printBestBuySell: 买入 " + buyDay + " 价格 " + formatNumTwo(buyPrice)
                    + " → 卖出 " + sellDay + " 价格 " + formatNumTwo(sellPrice)
                    + " | 收益 " + formatNumTwo(profit) + "%"
                    + (profit < 0 ? " ******" : ""));
        }

        Log.i(TAG, "printBestBuySell: ====== 总结 ======");
        Log.i(TAG, "printBestBuySell: 总交易次数 = " + (profitCount + lossCount)
                + ", 盈利次数 = " + profitCount + ", 亏损次数 = " + lossCount
                + ", 胜率 = " + formatNumTwo(profitCount * 100.0 / (profitCount + lossCount)) + "%"
                + ", 累计收益 = " + formatNumTwo(totalProfit) + "%");
    }

    public float getAverage5(List<Float> list, int index, int days) {
        float sum = 0;
        for (int i = 1; i <= days; i++) {
            sum += list.get(Math.max(index - i, 0));
        }
        return sum / days;
    }
}
