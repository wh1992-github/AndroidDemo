package com.example.util;

import android.annotation.SuppressLint;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.util.Log;

import java.lang.reflect.Constructor;
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
 * Class<?> clazz = Class.forName("com.example.util.Person");
 */
@SuppressLint("PrivateApi")
@RequiresApi(api = Build.VERSION_CODES.O)
public class ReflectUtil {
    private static final String TAG = "ReflectUtil";

    public static void reflect() {
        reflectConstructor();
        reflectField();
        reflectMethod();
        reflectAllConstructor();
        reflectAllField();
        reflectAllMethod();
    }

    private static void reflectConstructor() {
        //反射获取构造器，首先要获取待创建对象的类的class文件，通过class文件获取构造器，得到构造方法，通过构造方法创建对象
        try {
            Class<?> person = Class.forName("com.example.util.Person");
            //获取构造器对象，此处获取的为带两个参数的构造方法
            Constructor<?> constructor = person.getDeclaredConstructor(String.class, int.class);
            constructor.setAccessible(true);
            //创建对象，创建对象方法为 newInstance()
            Person yuefei = (Person) constructor.newInstance("将军", 39);
            yuefei.name = "岳飞";
            Log.i(TAG, "reflectConstructor: " + yuefei);
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException
                | InstantiationException | InvocationTargetException e) {
            Log.e(TAG, "reflectConstructor: e = " + e.getMessage());
        }
    }

    private static void reflectField() {
        try {
            Class<?> systemProperties = Class.forName("android.os.SystemProperties");
            Field field = systemProperties.getDeclaredField("PROP_VALUE_MAX");
            field.setAccessible(true);
            String name = field.getName();
            Class<?> type = field.getType();
            //int value = field.getInt(null);
            int value = field.getInt(systemProperties);
            Log.i(TAG, "reflectField: name = " + name + ", type = " + type + ", value = " + value);
        } catch (ClassNotFoundException | NoSuchFieldException | IllegalAccessException e) {
            Log.e(TAG, "reflectField: e = " + e.getMessage());
        }
    }

    private static void reflectMethod() {
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
            LogUtil.e(TAG, "reflectMethod: e = " + e.getMessage());
        }
    }

    private static void reflectAllConstructor() {
        //反射获取构造器，首先要获取待创建对象的类的class文件，通过class文件获取构造器，得到构造方法，通过构造方法创建对象
        try {
            Class<?> person = Class.forName("com.example.util.Person");
            //获取构造器对象，此处获取的为带两个参数的构造方法
            Constructor<?>[] constructors = person.getConstructors();
            Log.i(TAG, "reflectAllConstructor: constructors size = " + constructors.length);
            for (Constructor<?> constructor : constructors) {
                constructor.setAccessible(true);
                int paramsCount = constructor.getParameterCount();
                Class<?>[] params = constructor.getParameterTypes();
                Log.i(TAG, "reflectAllConstructor: paramsCount = " + paramsCount + ", params = " + Arrays.asList(params));
            }
        } catch (ClassNotFoundException e) {
            Log.e(TAG, "reflectAllConstructor: e = " + e.getMessage());
        }
    }

    private static void reflectAllField() {
        try {
            Class<?> systemProperties = Class.forName("android.os.SystemProperties");
            Field[] fields = systemProperties.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                //属性名字
                String name = field.getName();
                //属性类型
                Class<?> type = field.getType();
                //属性值
                //Object object = field.get(null);
                Object object = field.get(systemProperties);
                Log.i(TAG, "reflectAllField: name = " + name + ", type = " + type + ", value = " + object);
            }
        } catch (ClassNotFoundException | IllegalAccessException e) {
            Log.e(TAG, "reflectAllField: e = " + e.getMessage());
        }
    }

    private static void reflectAllMethod() {
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
            Log.e(TAG, "reflectAllMethod: e = " + e.getMessage());
        }
    }
}

class Person {
    String name;
    String work;
    int age;

    private Person(String name) {
        this.name = name;
    }

    public Person(String work, int age) {
        this.work = work;
        this.age = age;
    }

    @NonNull
    @Override
    public String toString() {
        return "name = " + name + ", work = " + work + ", age = " + age;
    }
}