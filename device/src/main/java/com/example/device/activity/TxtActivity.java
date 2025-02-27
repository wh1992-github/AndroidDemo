package com.example.device.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.example.device.R;
import com.example.device.databinding.ActivityTxtBinding;
import com.example.device.util.GenerateValueFiles;
import com.example.device.util.WordsTypeUtil;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@SuppressLint({"NonConstantResourceId", "SdCardPath"})
@RequiresApi(api = Build.VERSION_CODES.R)
public class TxtActivity extends AppCompatActivity {
    private static final String TAG = "TxtActivity---";
    private ActivityTxtBinding mViewBinding;
    private static final String[] PERMISSIONS = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CALL_PHONE, Manifest.permission.SEND_SMS};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate: ");
        mViewBinding = ActivityTxtBinding.inflate(getLayoutInflater());
        setContentView(mViewBinding.getRoot());
        mViewBinding.txtCreate1.setOnClickListener(this::onClick);
        mViewBinding.txtCreate2.setOnClickListener(this::onClick);
        mViewBinding.txtCreate3.setOnClickListener(this::onClick);
        mViewBinding.txtCreate4.setOnClickListener(this::onClick);

        requestPermissions(PERMISSIONS, 10002);

        Log.i(TAG, "onCreate: " + (ContextCompat.checkSelfPermission(this, Manifest.permission.MANAGE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED));
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.MANAGE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            //如果没有权限，获取权限
            Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
            intent.setData(Uri.parse("package:" + getPackageName()));
            startActivity(intent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (int i = 0; i < permissions.length; i++) {
            Log.i(TAG, "onRequestPermissionsResult: grantResults = " + grantResults[i] + ", permission = " + permissions[i]);
        }
    }

    private void onClick(View view) {
        switch (view.getId()) {
            case R.id.txt_create1:
                GenerateValueFiles.createXML();
                createTXT1();
                break;
            case R.id.txt_create2:
                createTXT2();
                break;
            case R.id.txt_create3:
                createTXT3();
                break;
            case R.id.txt_create4:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        convert();
                    }
                }).start();
                break;
            default:
                break;
        }
    }

    public void convert() {
        File dir = new File("/sdcard/AAA/");
        StringBuilder all = new StringBuilder();
        File file = new File(dir.getAbsolutePath(), "AAA.txt");
        Log.i(TAG, "createTXT: " + file.getAbsolutePath());
        int index = 0;
        try {
            file.createNewFile();
            Log.i(TAG, "createTXT: " + file.getAbsolutePath());
            InputStream inputStream = getResources().getAssets().open("百战系列.txt");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));

            String lines;
            while ((lines = bufferedReader.readLine()) != null) {
                for (int i = 0; i < lines.length(); i++) {
                    String charAt = String.valueOf(lines.charAt(i));
                    if (all.toString().isEmpty() || !all.toString().contains(charAt)) {
                        all.append(charAt);
                        Log.i(TAG, "charAt: " + charAt + ", " + index++);
                    }
                }
            }
            Log.i(TAG, "convert: " + lines);
            bufferedWriter.write((String) null);
            bufferedWriter.newLine();
            bufferedWriter.flush();
            bufferedWriter.close();
            bufferedReader.close();
        } catch (IOException e) {
            Log.i(TAG, "createTXT: e = " + e.getMessage());
        }
    }

    public void createTXT3() {
        File dir = new File("/sdcard/AAA/AAA/");
        File[] files = dir.listFiles();
        Log.i(TAG, "createTXT3: " + files.length);

        String name;
        for (int i = 0; i < files.length; i++) {
            Log.i(TAG, "createTXT3: " + files[i]);
            if (i < 10) {
                name = "/sdcard/AAA/AAA/00" + i + ".jpg";
            } else {
                name = "/sdcard/AAA/AAA/0" + i + ".jpg";
            }
            files[i].renameTo(new File(name));
        }
    }

    public void createTXT2() {
        Log.i(TAG, "createTXT: ");
        File dir = new File("/sdcard/AAA/BBB/CCC/DDD");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File file = new File(dir.getAbsolutePath(), "百战系列.txt");
        Log.i(TAG, "createTXT: " + file.getAbsolutePath());
        try {
            file.createNewFile();
            Log.i(TAG, "createTXT: " + file.getAbsolutePath());
            InputStream inputStream = getResources().getAssets().open("abcd.txt");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));

            String lines;
            while ((lines = bufferedReader.readLine()) != null) {
                if (lines.equals("一") || lines.equals("二") || lines.equals("三") || lines.equals("四") || lines.equals("五") || lines.equals("六") || lines.equals("七") || lines.equals("八") || lines.equals("九") || lines.equals("十")) {
                    lines = lines + "、";
                    Log.i(TAG, "createTXT: content = " + lines);
                    bufferedWriter.write(lines);
                } else {
                    bufferedWriter.write(lines);
                    bufferedWriter.newLine();
                }
            }
            bufferedWriter.flush();
            bufferedWriter.close();
            bufferedReader.close();
        } catch (IOException e) {
            Log.i(TAG, "createTXT: e = " + e.getMessage());
        }
    }

    public void createTXT1() {
        Log.i(TAG, "createTXT: ");
        File dir = new File("/sdcard/AAA/BBB/CCC/DDD");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File file = new File(dir.getAbsolutePath(), "一眼万年.txt");
        Log.i(TAG, "createTXT: " + file.getAbsolutePath());
        try {
            file.createNewFile();
            Log.i(TAG, "createTXT: " + file.getAbsolutePath());
            InputStream inputStream = getResources().getAssets().open("一眼万年.txt");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));

            String lines;
            StringBuilder builder = new StringBuilder();
            while ((lines = bufferedReader.readLine()) != null) {
                lines = WordsTypeUtil.changeT_S(lines);
                builder.append(lines);
                if (builder.toString().endsWith("。") || builder.toString().endsWith("！") || builder.toString().endsWith("？")) {
                    Log.i(TAG, "createTXT: content = " + builder.toString());
                    bufferedWriter.write(builder.toString());
                    bufferedWriter.newLine();
                    builder = new StringBuilder();
                }
            }
            bufferedWriter.flush();
            bufferedWriter.close();
            bufferedReader.close();
        } catch (IOException e) {
            Log.i(TAG, "createTXT: e = " + e.getMessage());
        }
    }

}