package com.example.storage.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.storage.R;
import com.example.storage.util.FileUtil;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by test on 2017/10/1.
 */
public class ImageReadActivity extends AppCompatActivity implements OnClickListener {
    private static final String TAG = "ImageReadActivity";
    private ImageView iv_image;
    private Spinner sp_file;
    private String mPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_read);
        iv_image = findViewById(R.id.iv_image);
        sp_file = findViewById(R.id.sp_file);
        findViewById(R.id.btn_delete).setOnClickListener(this);
        //获取当前App的私有存储目录
        mPath = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).toString() + "/";
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            refreshSpinner();
        } else {
            showToast("未发现已挂载的SD卡,请检查");
        }
    }

    private void refreshSpinner() {
        //获得指定目录下面的所有图片文件
        ArrayList<File> fileAllList = FileUtil.getFileList(mPath, new String[]{".png", ".jpg"});
        if (fileAllList.size() > 0) {
            fileArray = new String[fileAllList.size()];
            for (int i = 0; i < fileAllList.size(); i++) {
                fileArray[i] = fileAllList.get(i).getName();
            }
            ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(this,
                    R.layout.item_select, fileArray);
            typeAdapter.setDropDownViewResource(R.layout.item_dropdown);
            sp_file.setPrompt("请选择图片文件");
            sp_file.setAdapter(typeAdapter);
            sp_file.setSelection(0);
            sp_file.setOnItemSelectedListener(new FileSelectedListener());
        } else {
            fileArray = null;
            fileArray = new String[1];
            fileArray[0] = "";
            ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(this,
                    R.layout.item_select, fileArray);
            sp_file.setPrompt(null);
            sp_file.setAdapter(typeAdapter);
            sp_file.setOnItemSelectedListener(null);
            iv_image.setImageDrawable(null);
        }
    }

    private String[] fileArray;

    class FileSelectedListener implements OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
            //打开并显示选中的图片文件位图
            String file_path = mPath + fileArray[arg2];
            Bitmap bitmap = FileUtil.openImage(file_path);
            iv_image.setImageBitmap(bitmap);
        }

        public void onNothingSelected(AdapterView<?> arg0) {
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_delete) {
            for (int i = 0; i < fileArray.length; i++) {
                String file_path = mPath + fileArray[i];
                File f = new File(file_path);
                if (!f.delete()) {
                    Log.d(TAG, "file_path=" + file_path + ", delete failed");
                }
            }
            refreshSpinner();
            showToast("已删除临时目录下的所有图片文件");
        }
    }

    private void showToast(String desc) {
        Toast.makeText(this, desc, Toast.LENGTH_SHORT).show();
    }

}
