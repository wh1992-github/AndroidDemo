package com.example.device.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.example.device.R;
import com.example.device.widget.CameraView;

/**
 * Created by test on 2017/11/4.
 */
public class TakePictureActivity extends AppCompatActivity implements OnClickListener {
    private static final String TAG = "TakePictureActivity";
    private CameraView camera_view; //声明一个相机视图对象
    private int mTakeType = 0; //拍照类型。0为单拍,1为连拍

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_picture);
        //获取前一个页面传来的摄像头类型
        int camera_type = getIntent().getIntExtra("type", CameraView.CAMERA_BEHIND);
        //从布局文件中获取名叫camera_view的相机视图
        camera_view = findViewById(R.id.camera_view);
        //设置相机视图的摄像头类型
        camera_view.setCameraType(camera_type);
        findViewById(R.id.btn_shutter).setOnClickListener(this);
        findViewById(R.id.btn_shooting).setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(); //创建一个新意图
        Bundle bundle = new Bundle(); //创建一个新包裹
        String photo_path = camera_view.getPhotoPath(); //获取照片的保存路径
        bundle.putInt("type", mTakeType);
        if (photo_path == null && mTakeType == 0) { //未发生拍照动作
            bundle.putString("is_null", "yes");
        } else { //有发生拍照动作
            bundle.putString("is_null", "no");
            if (mTakeType == 0) { //单拍。一次只拍一张
                bundle.putString("path", photo_path);
            } else if (mTakeType == 1) { //连拍。一次连续拍了好几张
                bundle.putStringArrayList("path_list", camera_view.getShootingList());
            }
        }
        intent.putExtras(bundle); //往意图中存入包裹
        setResult(Activity.RESULT_OK, intent); //携带意图返回前一个页面
        finish(); //关闭当前页面
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_shutter) { //点击了单拍按钮
            mTakeType = 0;
            //命令相机视图执行单拍操作
            camera_view.doTakePicture();
            //拍照需要完成对焦、图像捕获、图片保存等一系列动作,因而要留足时间给系统处理
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(TakePictureActivity.this, "已完成拍照,按返回键回到上页查看照片。", Toast.LENGTH_SHORT).show();
                }
            }, 1500);
        } else if (v.getId() == R.id.btn_shooting) { //点击了连拍按钮
            mTakeType = 1;
            //命令相机视图执行连拍操作
            camera_view.doTakeShooting();
        }
    }

}
