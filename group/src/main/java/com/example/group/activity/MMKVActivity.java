package com.example.group.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;

import com.example.group.R;
import com.example.group.bean.Person;
import com.tencent.mmkv.MMKV;

/**
 * 为什么要替代SharedPreferences？
 * 1.数据加密. 在 Android 环境里,数据加密是非常必须的,SP实际上是把键值对放到本地文件中进行存储.如果要保证数据安全需要自己加密,MMKV 使用了 AES CFB-128 算法来加密/解密.
 * 2.多进程共享.系统自带的 SharedPreferences 对多进程的支持不好.现有基于 ContentProvider 封装的实现,虽然多进程是支持了,但是性能低下,经常导致 ANR.
 * 考虑到 mmap 共享内存本质上是多进程共享的,MMKV 在这个基础上,深入挖掘了Android 系统的能力,提供了可能是业界最高效的多进程数据共享组件.
 * 3.匿名内存. 在多进程共享的基础上,考虑到某些敏感数据(例如密码)需要进程间共享,但是不方便落地存储到文件上,直接用 mmap 不合适.
 * 而Android 系统提供了 Ashmem 匿名共享内存的能力,它在 进程退出后就会消失,不会落地到文件上,非常适合这个场景.MMKV 基于此也提供了 Ashmem(匿名共享内存) MMKV 的功能.
 * 4.效率更高.MMKV 使用protobuf进行序列化和反序列化,比起SP的xml存放方式,更加高效.
 * 5.支持从SP迁移,如果你之前项目里面都是使用SP,现在想改为使用MMKV,只需几行代码即可将之前的SP实现迁移到MMKV.
 * <p>
 * 支持的数据类型
 * 1.支持以下 Java 语言基础类型：
 * boolean、int、long、float、double、byte[]
 * 2.支持以下 Java 类和容器：
 * String、Set<String>
 * 任何实现了Parcelable的类型
 * <p>
 * 自定义根目录
 * MMKV 默认把文件存放在$(FilesDir)/mmkv/目录.你可以在 App 启动时自定义根目录：
 * String dir = getFilesDir().getAbsolutePath() + "/mmkv_custom";
 * String rootDir = MMKV.initialize(dir);
 * Log.i(TAG, "mmkv root: " + rootDir);
 * <p>
 * MMKV 甚至支持自定义某个文件的目录：
 * String relativePath = getFilesDir().getAbsolutePath() + "/mmkv_custom";
 * MMKV kv = MMKV.mmkvWithID("testCustomDir", relativePath);
 */
@SuppressLint("LogNotTimber")
public class MMKVActivity extends AppCompatActivity {
    private static final String TAG = "MMKVActivity";
    //默认支持单进程
    private static final MMKV mMMKV = MMKV.defaultMMKV();
    //设置单进程
    //private static final MMKV mMMKV = MMKV.mmkvWithID("mapId", MMKV.SINGLE_PROCESS_MODE);
    //设置多进程
    //private static final MMKV mMMKV = MMKV.mmkvWithID("mapId", MMKV.MULTI_PROCESS_MODE);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mmkv);

        Button button1 = findViewById(R.id.btn_write_int);
        button1.setOnClickListener(v -> mMMKV.encode("testInt", 2));

        Button button2 = findViewById(R.id.btn_read_int);
        button2.setOnClickListener(v -> {
            int value = mMMKV.decodeInt("testInt", 0);
            Log.i(TAG, "testInt = " + value);
        });

        Button button3 = findViewById(R.id.btn_delete_int);
        button3.setOnClickListener(v -> mMMKV.removeValueForKey("testInt"));

        Button button4 = findViewById(R.id.btn_write_string);
        button4.setOnClickListener(v -> mMMKV.encode("testString", "abc"));

        Button button5 = findViewById(R.id.btn_read_string);
        button5.setOnClickListener(v -> {
            String value = mMMKV.decodeString("testString");
            Log.i(TAG, "testString = " + value);
        });

        Button button6 = findViewById(R.id.btn_delete_string);
        button6.setOnClickListener(v -> mMMKV.removeValueForKey("testString"));

        Button button7 = findViewById(R.id.btn_write_object);
        button7.setOnClickListener(v -> {
            long time = System.currentTimeMillis();
            Person person = new Person("name", 28);
            mMMKV.encode("person", person);
            Log.i(TAG, "time = " + (System.currentTimeMillis() - time));
        });

        Button button8 = findViewById(R.id.btn_read_object);
        button8.setOnClickListener(v -> {
            Person person = mMMKV.decodeParcelable("person", Person.class);
            Log.i(TAG, "person = " + person);
        });

        Button button9 = findViewById(R.id.btn_delete_object);
        button9.setOnClickListener(v -> mMMKV.removeValueForKey("person"));
    }
}
