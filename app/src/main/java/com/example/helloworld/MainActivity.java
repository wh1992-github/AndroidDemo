package com.example.helloworld;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
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
import android.widget.TextView;

import com.example.helloworld.databinding.ActivityMainBinding;

@SuppressLint({"NonConstantResourceId", "SdCardPath"})
@RequiresApi(api = Build.VERSION_CODES.R)
public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity---";
    private ActivityMainBinding mViewBinding;
    private static final int lists[] = {777, 779, 770, 768, 753, 740, 734, 741, 738, 738, 745, 730, 728, 722, 695, 702, 733, 750, 755, 745, 725, 717, 708, 689, 660, 722, 744,
            747, 732, 737, 740, 744, 753, 749, 763, 753, 777, 778, 770, 760, 756, 746, 750, 763, 772, 764, 758, 766, 777, 763, 765, 766, 750, 727, 732, 716, 720, 729, 740, 732,
            728, 713, 717, 715, 710, 696, 713, 694, 709, 719, 716, 714, 707, 711, 708, 747, 762, 751, 751, 746, 733, 741, 743, 742, 746, 728, 727, 738, 736, 731, 733, 718, 710,
            714, 705, 707, 708, 710, 703, 708, 699, 686, 683, 682, 683, 676, 691, 694, 697, 693, 684, 686, 674, 666, 674, 666, 655, 659, 657, 660, 643, 646, 625, 641, 641, 649,
            647, 640, 644, 646, 651, 655, 651, 641, 636, 645, 655, 652, 655, 688, 686, 669, 666, 669, 667, 678, 671, 667, 674, 673, 680, 671, 675, 665, 665, 647, 651, 651, 645,
            646, 651, 671, 652, 656, 659, 660, 663, 666, 659, 656, 688, 683, 680, 686, 683, 678, 721, 727, 760, 831, 915, 1007, 906, 856, 835, 838, 814, 819, 806, 841, 827, 831,
            830, 832, 853, 852, 838, 838, 851, 855, 878, 910, 896, 943, 922, 917, 897, 898, 884, 857, 850, 858, 856, 856, 817, 821, 819, 834, 860, 869, 870, 865, 853, 859, 869,
            860, 866, 867, 881, 859, 855, 848, 846, 843, 848, 833, 852, 845, 846, 850, 857, 820};
    private static final String[] PERMISSIONS = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CALL_PHONE, Manifest.permission.SEND_SMS};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate: ");
        mViewBinding = ActivityMainBinding.inflate(getLayoutInflater());

        setContentView(mViewBinding.getRoot());
        //获取名叫tv_hello的TextView控件
        TextView tv_hello = findViewById(R.id.tv_hello);
        //设置TextView控件的文字内容
        tv_hello.setText("今天天气真热啊");
        //设置TextView控件的文字颜色
        tv_hello.setTextColor(Color.RED);
        //设置TextView控件的文字大小
        tv_hello.setTextSize(20);
        mViewBinding.btn1.setOnClickListener(this::onClick);
        mViewBinding.btn2.setOnClickListener(this::onClick);
        mViewBinding.btn3.setOnClickListener(this::onClick);

        requestPermissions(PERMISSIONS, 10002);

        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.MANAGE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
            //如果没有权限，获取权限
            Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
            intent.setData(Uri.parse("package:" + getPackageName()));
            startActivity(intent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.i(TAG, "onRequestPermissionsResult: READ_EXTERNAL_STORAGE = " + ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE));
        Log.i(TAG, "onRequestPermissionsResult: WRITE_EXTERNAL_STORAGE = " + ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE));
        Log.i(TAG, "onRequestPermissionsResult: CALL_PHONE = " + ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE));
        Log.i(TAG, "onRequestPermissionsResult: SEND_SMS = " + ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS));
        for (int i = 0; i < permissions.length; i++) {
            Log.i(TAG, "onRequestPermissionsResult: permission = " + permissions[i] + ", " + grantResults[i]);
        }
    }

    private void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn1:
                break;
            case R.id.btn2:
                break;
            default:
                break;
        }
    }
}