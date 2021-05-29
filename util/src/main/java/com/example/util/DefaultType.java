package com.example.util;

import android.support.annotation.IntDef;
import android.support.annotation.StringDef;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Target:注解的作用目标
 * @Target(ElementType.TYPE)——接口、类、枚举、注解
 * @Target(ElementType.FIELD)——字段、枚举的常量
 * @Target(ElementType.METHOD)——方法
 * @Target(ElementType.PARAMETER)——方法参数
 * @Target(ElementType.CONSTRUCTOR) ——构造函数
 * @Target(ElementType.LOCAL_VARIABLE)——局部变量
 * @Target(ElementType.ANNOTATION_TYPE)——注解
 * @Target(ElementType.PACKAGE)——包
 * @Retention：注解的保留位置 RetentionPolicy.SOURCE:这种类型的Annotations只在源代码级别保留,编译时就会被忽略,在class字节码文件中不包含。
 * RetentionPolicy.CLASS:这种类型的Annotations编译时被保留,默认的保留策略,在class文件中存在,但JVM将会忽略,运行时无法获得。
 * RetentionPolicy.RUNTIME:这种类型的Annotations将被JVM保留,所以他们能在运行时被JVM或其他使用反射机制的代码所读取和使用。
 * @Document：说明该注解将被包含在javadoc中
 * @Inherited：说明子类可以继承父类中的该注解
 */
public class DefaultType {
    private static final String TAG = "DefaultType";

    @IntDef({Sex.MAN, Sex.WOMEN}) //限定为MAN,WOMEN
    @Target(ElementType.PARAMETER) //表示注解作用范围,参数注解
    @Retention(RetentionPolicy.SOURCE) //表示注解所存活的时间,在运行时,而不会存在.class文件.
    public @interface Sex { //接口,定义新的注解类型
        int MAN = 1;
        int WOMEN = 2;
    }

    public void setSexType(@Sex int sex) {
        LogUtil.i(TAG, "setSex: sex = " + sex);
    }

    @StringDef({Person.male, Person.female})
    @Target(ElementType.PARAMETER)
    @Retention(RetentionPolicy.SOURCE)
    public @interface Person {
        String male = "man";
        String female = "woman";
    }

    public void setSexName(@Person String sex) {
        LogUtil.i(TAG, "setSex: sex = " + sex);
    }
}
