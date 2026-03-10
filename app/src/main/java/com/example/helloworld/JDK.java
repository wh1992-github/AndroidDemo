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

    static class MonthStats {
        float average;
        float minPrice;
        String minLine;
        float maxPrice;
        String maxLine;
    }

    /**
     * 滑动窗口极值追踪：在给定天数窗口内更新追踪的极值价格和索引。
     * @param list 价格列表
     * @param i 当前索引
     * @param trackedPrice 当前追踪的极值价格
     * @param trackedIndex 当前追踪的极值索引
     * @param day 窗口天数
     * @param isHolding true=追踪最高点, false=追踪最低点
     * @return [updatedTrackedPrice, updatedTrackedIndex]
     */
    private float[] updateTrackedPrice(List<Float> list, int i, float trackedPrice, int trackedIndex, int day, boolean isHolding) {
        if (!isHolding) {
            if (trackedPrice >= list.get(i)) {
                trackedPrice = list.get(i);
                trackedIndex = i;
            }
            if ((i - trackedIndex) > day) {
                trackedIndex++;
                trackedPrice = list.get(trackedIndex);
            }
        } else {
            if (trackedPrice <= list.get(i)) {
                trackedPrice = list.get(i);
                trackedIndex = i;
            }
            if ((i - trackedIndex) > day) {
                trackedIndex++;
                trackedPrice = list.get(trackedIndex);
            }
        }
        return new float[]{trackedPrice, trackedIndex};
    }

    //收益超过该数
    private static final int MAX_MONEY = 1000;

    public void start(Context context) {
        Log.i(TAG, "---------------------------------------------------------------- ALL START ------------------------------------------------------------------ ");

        printTest("TEST", Constants.getData());

//        printMonth(context);

        Log.i(TAG, "----------------------------------------------------------------- ALL END ------------------------------------------------------------------- ");
    }

    @SuppressLint("DefaultLocale")
    public void printTest(String gupiao, Float[] array) {
        mList.clear();
        mList.addAll(Arrays.asList(array));
        Log.i(TAG, gupiao + ": ------ START ------ name = " + gupiao + ", size = " + mList.size());
        float capital;
        float sell_money = 0;
        float buy_price = 0;
        float sell_price;
        boolean isHolding;
        float buy_min = 100f, sell_min = 100f;
        float buy_max = 0f, sell_max = 0f;
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
            for (int buyThreshold = 200; buyThreshold <= 600; buyThreshold += 20) {
                for (int sellThreshold = 200; sellThreshold <= 600; sellThreshold += 20) {
                    isHolding = false;
                    float trackedPrice = list.get(0);
                    int trackedIndex = 0;
                    capital = 10000;
                    for (int i = 0; i < list.size(); i++) {
                        float[] tracked = updateTrackedPrice(list, i, trackedPrice, trackedIndex, day, isHolding);
                        trackedPrice = tracked[0];
                        trackedIndex = (int) tracked[1];
                        //B
                        if (!isHolding && ((list.get(i) - trackedPrice) / trackedPrice > (float) buyThreshold / 10000)) {
                            isHolding = true;
                            buy_price = list.get(i);
                            trackedPrice = list.get(i);
                            trackedIndex = i;
                        }
                        //S
                        if (isHolding && ((list.get(i) - trackedPrice) / trackedPrice < (float) -sellThreshold / 10000 || i == list.size() - 1)) {
                            isHolding = false;
                            sell_price = list.get(i);
                            trackedPrice = list.get(i);
                            trackedIndex = i;
                            capital = capital * ((sell_price - buy_price) / buy_price) + capital - 15;
                        }
                    }
                    if (capital > sell_money) {
                        sell_money = capital;
                        buy_min = (float) buyThreshold / 10000;
                        sell_min = (float) -sellThreshold / 10000;
                        buy_max = (float) buyThreshold / 10000;
                        sell_max = (float) -sellThreshold / 10000;
                    } else if (capital == sell_money) {
                        buy_max = (float) buyThreshold / 10000;
                        sell_max = (float) -sellThreshold / 10000;
                    }
                }
            }
            winRate += (sell_money - 10000) > 1600 ? 1.0f : 0.0f;
            sumMoney += (sell_money - 10000);
            multiple = multiple * (sell_money / 10000);
            String dayRange = days - cycle_day == 0 ? "000" : String.valueOf(days - cycle_day);
            stringBuilder.append(String.format("%s: 第%d - %s天 收益 = %s, 第%d - %s天 总收益 = %s, buy = (%s%%~%s%%), sell = (%s%%~%s%%)%s",
                    gupiao, days, dayRange, formatNumTwo(sell_money - 10000),
                    total_day, dayRange, formatNumTwo(sumMoney),
                    formatNumTwo(buy_min * 100), formatNumTwo(buy_max * 100),
                    formatNumTwo(sell_min * 100), formatNumTwo(sell_max * 100),
                    (sell_money - 10000) > 1600 ? "" : ", ×××××× ")).append("\n");
            printMoney(gupiao, list, day, buy_min, sell_min);
            if (days == cycle_day) {
                Log.i(TAG, stringBuilder.toString());
                Log.i(TAG, String.format("%s: 最近%d天复利总收益 = %s, 总收益 = %s, 盈利率 = %s%%",
                        gupiao, total_day, formatNumTwo(multiple * 10000 - 10000),
                        formatNumTwo(sumMoney), formatNumTwo(winRate * 100 / (total_day / cycle_day))));
                stringBuilder.delete(0, stringBuilder.length());
            }
            sell_money = 0;
        }
        Log.i(TAG, gupiao + ": ------- END ------- name = " + gupiao + ", size = " + mList.size());
    }

    public void printMoney(String gupiao, List<Float> list, int day, float buy, float sell) {
        float trackedPrice = list.get(0);
        float capital = 10000;
        float buy_price = 0;
        float sell_price = 0;
        boolean isHolding = false;
        float sum = 0;
        float high_price = 0;
        int trackedIndex = 0;
        int flagDay = 0;
        float buy_zf = 100;
        float sell_df = 100;
        for (int i = 0; i < list.size(); i++) {
            float[] tracked = updateTrackedPrice(list, i, trackedPrice, trackedIndex, day, isHolding);
            trackedPrice = tracked[0];
            trackedIndex = (int) tracked[1];
            //B
            if (!isHolding && ((list.get(i) - trackedPrice) / trackedPrice > buy)) {
                isHolding = true;
                buy_price = list.get(i);
                buy_zf = (list.get(i) - trackedPrice) / trackedPrice;
                trackedPrice = list.get(i);
                trackedIndex = i;
                flagDay = i;
                Log.i(TAG, String.format("%s: 第%d天买入, 价格 = %s, 总价 = %s, 涨幅 = %s%%",
                        gupiao, flagDay + 1, formatNumTwo(buy_price),
                        formatNumTwo(capital), formatNumTwo(buy_zf * 100)));
            }
            //应该买入
            if (!isHolding && (i == list.size() - 1)) {
                float buy_ = buy * trackedPrice + trackedPrice;
                Log.i(TAG, gupiao + ": 第" + (i + 2) + "天买入, 价格应该大于 " + formatNumTwo(buy_));
            }
            //应该卖出
            if (isHolding && (i == list.size() - 1)) {
                float sell_ = sell * trackedPrice + trackedPrice;
                Log.i(TAG, gupiao + ": 第" + (i + 2) + "天卖出, 价格应该小于 " + formatNumTwo(sell_));
            }
            //S
            if (isHolding && ((list.get(i) - trackedPrice) / trackedPrice < sell || i == list.size() - 1)) {
                boolean isTrue = ((list.get(i) - trackedPrice) / trackedPrice < sell && i == list.size() - 1);
                isHolding = false;
                sell_price = list.get(i);
                sell_df = (list.get(i) - trackedPrice) / trackedPrice;
                high_price = list.get(i);
                trackedPrice = list.get(i);
                trackedIndex = i;
                float profit = capital * ((sell_price - buy_price) / buy_price) - 15;
                String sellType = (i == list.size() - 1) ? (isTrue ? "真的" : "假的") : "";
                Log.i(TAG, String.format("%s: 第%d天%s卖出, 价格 = %s, 总价 = %s, 跌幅 = %s%%, 持有 = %d 天, 收益 = %s, 盈利 = %s%%%s",
                        gupiao, i + 1, sellType, formatNumTwo(sell_price),
                        formatNumTwo(profit + capital), formatNumTwo(sell_df * 100),
                        i - flagDay, formatNumTwo(profit),
                        formatNumTwo(profit * 100 / capital),
                        profit < 0 ? ", ******" : ""));
                sum += profit;
                capital = profit + capital;
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
                    MonthStats buyStats = getMonthStats(context, year, name, formatNumTwo(month));
                    MonthStats sellStats;
                    if (month < 12) {
                        sellStats = getMonthStats(context, year, name, formatNumTwo(month + 1));
                    } else {
                        sellStats = getMonthStats(context, year + 1, name, formatNumTwo(1));
                    }
                    if (!TextUtils.isEmpty(buyStats.minLine) && !TextUtils.isEmpty(sellStats.maxLine)) {
                        float average = buyStats.average;
                        float max = sellStats.maxPrice;
                        Log.i(TAG, "printMonth: time = " + buyStats.minLine.substring(0, 7)
                                + ", average = " + formatNumTwo(average)
                                + " ~ " + sellStats.maxLine.substring(0, 10)
                                + ", max = " + formatNumTwo(max)
                                + ", m = " + formatNumTwo((max - average) / average * 100) + "%"
                                + (((max - average) / average > 0.10f) ? " ******" : ""));
                        sum += (max - average) / average;
                    } else {
                        float average = buyStats.average;
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

    public MonthStats getMonthStats(Context context, int year, String name, String month) {
        MonthStats stats = new MonthStats();
        float sum = 0;
        int days = 0;
        stats.minPrice = 10000;
        stats.maxPrice = 0;
        stats.minLine = "";
        stats.maxLine = "";
        try {
            InputStream inputStream = context.getResources().getAssets().open(name);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String lines;
            while ((lines = bufferedReader.readLine()) != null) {
                if (lines.contains(String.valueOf(year)) && (lines.contains("-" + month + "-"))) {
                    float price = Float.parseFloat(lines.substring(lines.indexOf("------") + 6));
                    days++;
                    sum += price;
                    if (price < stats.minPrice) {
                        stats.minPrice = price;
                        stats.minLine = lines;
                    }
                    if (price > stats.maxPrice) {
                        stats.maxPrice = price;
                        stats.maxLine = lines;
                    }
                }
            }
            bufferedReader.close();
        } catch (IOException e) {
            Log.i(TAG, "printMonth: e = " + e.getMessage());
        }
        stats.average = days > 0 ? sum / days : 0;
        return stats;
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

}
