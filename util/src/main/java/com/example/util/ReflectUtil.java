package com.example.util;

import android.annotation.SuppressLint;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * 反射机制
 * 第一种方式获取Class对象
 * Person p = new Person();
 * Class<?> clazz = (Class<Person>) p.getClass();
 * 第二种方式获取Class对象
 * Class<?> clazz = Person.class;
 * 第三种方式获取Class对象
 * Class<?> clazz = Class.forName("com.wh.example.Person");
 */
@SuppressLint("PrivateApi")
@RequiresApi(api = Build.VERSION_CODES.O)
public class ReflectUtil {
    private static final String TAG = "ReflectUtil";


    public static void reflect() {
        reflectField();
        reflectMethod();
        reflectAllField();
        reflectAllMethod();
    }

    public static void reflectField() {
        try {
            Class<?> systemProperties = Class.forName("android.os.SystemProperties");
            Field field = systemProperties.getDeclaredField("PROP_VALUE_MAX");
            field.setAccessible(true);
            String name = field.getName();
            Class<?> type = field.getType();
            int value = field.getInt(systemProperties);
            Log.i(TAG, "reflectField: name = " + name + ", type = " + type + ", value = " + value);
        } catch (ClassNotFoundException | NoSuchFieldException | IllegalAccessException e) {
            Log.i(TAG, "reflectField: e = " + e.getMessage());
        }
    }

    public static void reflectMethod() {
        try {
            Class<?> systemProperties = Class.forName("android.os.SystemProperties");
            Method method = systemProperties.getDeclaredMethod("get", String.class);
            method.setAccessible(true);
            String name = method.getName();
            Class<?> returnType = method.getReturnType();
            int paramsCount = method.getParameterCount();
            Class<?>[] params = method.getParameterTypes();
            String value = (String) method.invoke(systemProperties, "persist.vinprop");
            Log.i(TAG, "reflectMethod: name = " + name + ", returnType = " + returnType + ", value = " + value
                    + ", paramsCount = " + paramsCount + ", params = " + Arrays.asList(params));
        } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            LogUtil.i(TAG, "reflectMethod: e = " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void reflectAllField() {
        try {
            Class<?> systemProperties = Class.forName("android.os.SystemProperties");
            Field[] fields = systemProperties.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                //属性名字
                String name = field.getName();
                //属性类型
                Class<?> type = field.getType();
                Log.i(TAG, "reflectAllField: name = " + name + ", type = " + type);
            }
        } catch (ClassNotFoundException e) {
            Log.i(TAG, "reflectAllField: e = " + e.getMessage());
        }
    }

    public static void reflectAllMethod() {
        try {
            Class<?> systemProperties = Class.forName("android.os.SystemProperties");
            Method[] methods = systemProperties.getDeclaredMethods();
            for (Method method : methods) {
                method.setAccessible(true);
                //方法名字
                String name = method.getName();
                //返回值类型
                Class<?> returnType = method.getReturnType();
                //参数个数
                int paramsCount = method.getParameterCount();
                //参数类型
                Class<?>[] params = method.getParameterTypes();
                Log.i(TAG, "reflectAllMethod: name = " + name + ", returnType = " + returnType
                        + ", paramsCount = " + paramsCount + ", params = " + Arrays.asList(params));
            }
        } catch (ClassNotFoundException e) {
            Log.i(TAG, "reflectAllMethod: e = " + e.getMessage());
        }
    }
}