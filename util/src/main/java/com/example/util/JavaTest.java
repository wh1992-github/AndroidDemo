package com.example.util;

import android.util.Log;

import java.util.Arrays;

public class JavaTest {
    private static final String TAG = "Test";

    /**
     * 问题：
     * 说有一批编号为1～100的灯,每个灯下面都有一个开关,按一下就开,再按一下就关,一开始灯都是灭的.
     * 1. 凡是编号为1的倍数的,按一次开关
     * 2. 凡是编号为2的倍数的,再按一次开关
     * 3. 凡是编号为3的倍数的,再按一次开关
     * 4. ....
     * 5. 凡是编号为100的倍数的,再按一次开关
     * 问：最后哪些灯还亮着？
     */
    public static void switchLight() {
        boolean[] array = new boolean[101];
        Arrays.fill(array, false);

        for (int i = 1; i <= 100; i++) {//人的编号
            int j;
            for (j = 1; j <= 100; j++) {//灯的编号
                if (j % i == 0) {
                    array[j] = !array[j];
                }
            }

        }

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("亮着的灯:");
        for (int i = 0; i < array.length; i++) {
            if (array[i]) {
                stringBuilder.append(",第" + i + "个");
            }
        }
        Log.i(TAG, "switchLight: " + stringBuilder.toString());
    }

    /**
     * 斐波那契数列：
     * 技术点：递归和循环
     * 思路：斐波那契数列的定义
     * 参考回答：斐波那契数列指的是这样的数列1, 1, 2, 3, 5, 8, 13, 21, 34, 55, 89, 144
     * 即这个数列从第3项开始,每一项都等于前两项之和,数学表示F(1)=1,F(2)=1, F(3)=2,F(n)=F(n-1)+F(n-2)（n>=4,n∈N*）
     */
    public static void fbnq() {
        int[] array = new int[20];
        array[0] = 1;
        array[1] = 1;
        for (int i = 2; i < 20; i++) {
            array[i] = array[i - 2] + array[i - 1];
            Log.i(TAG, "fbnq: i = " + array[i]);
        }
    }

    public static void test() {
        test01();
        test02();
        test03();
        test04();
        test05();
        test06();
    }

    /**
     * 古典问题：
     * 有一对兔子,从出生后第3个月起每个月都生一对兔子,小兔子长到第三个月后每个月又生一对兔子,
     * 假如兔子都不死,问每个月的兔子总数为多少？
     * 这是一个斐波那契数列问题
     */
    public static void test01() {
        int f1 = 1, f2 = 1, f;
        int M = 30;
        Log.i(TAG, "test01: " + f1);
        Log.i(TAG, "test01: " + f2);
        for (int i = 3; i < M; i++) {
            f = f2;
            f2 = f1 + f2;
            f1 = f;
            Log.i(TAG, "test01: " + f2);
        }
    }

    /**
     * 题目：判断101-200之间有多少个素数,并输出所有素数.
     */
    public static void test02() {
        int count = 0;
        for (int i = 101; i < 200; i++) {
            boolean flag = true;
            for (int j = 2; j <= Math.sqrt(i); j++) {
                if (i % j == 0) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                count++;
                Log.i(TAG, "test02: i = " + i);
            }
        }
        Log.i(TAG, "test02: count = " + count);
    }

    /**
     * 题目：打印出所有的 "水仙花数 ",所谓 "水仙花数 "是指一个三位数,其各位数字立方和等于该数本身.
     * 例如：153是一个 "水仙花数 ",因为153=1的三次方＋5的三次方＋3的三次方.
     */
    public static void test03() {
        int a, b, c;
        for (int i = 101; i < 1000; i++) {
            a = i % 10;
            b = i / 10 % 10;
            c = i / 100;
            if (a * a * a + b * b * b + c * c * c == i)
                Log.i(TAG, "test03: i = " + i);
        }
    }

    /**
     * 题目：一球从100米高度自由落下,每次落地后反跳回原高度的一半,再落下.
     * 求它在 第10次落地时,共经过多少米？第10次反弹多高？
     */
    public static void test04() {
        double h = 100;
        double s = 100;
        for (int i = 1; i <= 10; i++) {
            h = h / 2;
            s = s + 2 * h;
        }
        Log.i(TAG, "test04: s = " + s + ", h = " + h);
    }

    /**
     * 题目：一个整数,它加上100后是一个完全平方数,再加上168又是一个完全平方数,请问该数是多少？
     */
    public static void test05() {
        for (int i = -100; i < 10000; i++) {
            if (Math.sqrt(i + 100) % 1 == 0 && Math.sqrt(i + 268) % 1 == 0) {
                Log.i(TAG, "test05: i = " + i);
            }
        }
    }

    /**
     * 题目：猴子吃桃问题：猴子第一天摘下若干个桃子,当即吃了一半,还不瘾,又多吃了一个
     * 第二天早上又将剩下的桃子吃掉一半,又多吃了一个.
     * 以后每天早上都吃了前一天剩下的一半零一个.
     * 到第10天早上想再吃时,见只剩下一个桃子了.
     * 求第一天共摘了多少？
     */
    public static void test06() {
        int total = 1;
        for (int i = 0; i < 10; i++) {
            total = (total + 1) * 2;
        }
        Log.i(TAG, "test06: total = " + total);
    }
}