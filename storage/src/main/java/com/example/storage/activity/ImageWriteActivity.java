package com.example.storage.activity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.storage.R;
import com.example.storage.util.DateUtil;
import com.example.storage.util.FileUtil;

/**
 * Created by test on 2017/10/1.
 */
@SuppressLint("SetTextI18n")
public class ImageWriteActivity extends AppCompatActivity implements OnClickListener {
    private LinearLayout ll_info;
    private EditText et_name;
    private EditText et_age;
    private EditText et_height;
    private EditText et_weight;
    private boolean bMarried = false;

    private String mPath;
    private TextView tv_path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_write);
        ll_info = findViewById(R.id.ll_info);
        et_name = findViewById(R.id.et_name);
        et_age = findViewById(R.id.et_age);
        et_height = findViewById(R.id.et_height);
        et_weight = findViewById(R.id.et_weight);
        tv_path = findViewById(R.id.tv_path);
        findViewById(R.id.btn_save).setOnClickListener(this);
        //获取当前App的私有存储目录
        mPath = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).toString() + "/";
        initTypeSpinner();
    }

    //初始化婚姻状况的下拉框
    private void initTypeSpinner() {
        ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(this,
                R.layout.item_select, typeArray);
        typeAdapter.setDropDownViewResource(R.layout.item_dropdown);
        Spinner sp_married = findViewById(R.id.sp_married);
        sp_married.setPrompt("请选择婚姻状况");
        sp_married.setAdapter(typeAdapter);
        sp_married.setSelection(0);
        sp_married.setOnItemSelectedListener(new TypeSelectedListener());
    }

    private String[] typeArray = {"未婚", "已婚"};

    class TypeSelectedListener implements OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
            bMarried = arg2 != 0;
        }

        public void onNothingSelected(AdapterView<?> arg0) {
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_save) {
            String name = et_name.getText().toString();
            String age = et_age.getText().toString();
            String height = et_height.getText().toString();
            String weight = et_weight.getText().toString();
            if (TextUtils.isEmpty(name)) {
                showToast("请先填写姓名");
                return;
            } else if (TextUtils.isEmpty(age)) {
                showToast("请先填写年龄");
                return;
            } else if (TextUtils.isEmpty(height)) {
                showToast("请先填写身高");
                return;
            } else if (TextUtils.isEmpty(weight)) {
                showToast("请先填写体重");
                return;
            }

            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                //获取线性布局ll_info绘图缓存中的位图数据
                Bitmap bitmap = ll_info.getDrawingCache();
                String file_path = mPath + DateUtil.getNowDateTime("") + ".png";
                //把位图数据保存为图片文件
                FileUtil.saveImage(file_path, bitmap);
                //回收位图对象
                bitmap.recycle();
                tv_path.setText("用户注册信息图片的保存路径为：\n" + file_path);
                showToast("图片已存入SD卡文件");
            } else {
                showToast("未发现已挂载的SD卡,请检查");
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        //开启线性布局ll_info的绘图缓存
        ll_info.setDrawingCacheEnabled(true);
    }

    @Override
    protected void onStop() {
        super.onStop();
        //关闭线性布局ll_info的绘图缓存
        ll_info.setDrawingCacheEnabled(false);
    }

    private void showToast(String desc) {
        Toast.makeText(this, desc, Toast.LENGTH_SHORT).show();
    }

}
