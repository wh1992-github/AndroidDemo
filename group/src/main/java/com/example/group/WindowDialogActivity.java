package com.example.group;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.example.group.windowdialog.BottomDialog;
import com.example.group.windowdialog.CenterDialog;
import com.example.group.windowdialog.DialogThirdUtils;
import com.example.group.windowdialog.FloatingService;
import com.example.group.windowdialog.OnItemClickListener;
import com.example.group.windowdialog.RightDialog;
import com.example.group.windowdialog.WeiBoDialogUtils;
import com.example.group.windowdialog.WindowDialog;
import com.example.group.windowdialog.WindowService;

@RequiresApi(api = Build.VERSION_CODES.O)
public class WindowDialogActivity extends AppCompatActivity implements OnItemClickListener {
    private static final String TAG = "MainActivity";
    private Button mBtn1, mBtn2, mBtn3, mBtn4, mBtn5, mBtn6, mBtn7, mBtn8, mBtn9, mBtn19, mBtn20;
    private RightDialog mRightDialog;
    private BottomDialog mBottomDialog;
    private WindowDialog mWindowDialog;
    private CenterDialog mCenterDialog;
    private Dialog mSecondDialog;
    private Dialog mWeiboDialog;
    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    DialogThirdUtils.closeDialog(mSecondDialog);
                    WeiBoDialogUtils.closeDialog(mWeiboDialog);
                    break;
                default:
                    break;
            }
            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_window_dialog);

        mBtn1 = findViewById(R.id.btn1);
        mBtn2 = findViewById(R.id.btn2);
        mBtn3 = findViewById(R.id.btn3);
        mBtn4 = findViewById(R.id.btn4);
        mBtn5 = findViewById(R.id.btn5);
        mBtn6 = findViewById(R.id.btn6);
        mBtn7 = findViewById(R.id.btn7);
        mBtn8 = findViewById(R.id.btn8);
        mBtn9 = findViewById(R.id.btn9);
        mBtn19 = findViewById(R.id.btn19);
        mBtn20 = findViewById(R.id.btn20);

        mRightDialog = new RightDialog(this, this);
        mBottomDialog = new BottomDialog(this, this);
        mWindowDialog = new WindowDialog(this);
        mCenterDialog = new CenterDialog(this);

        mBtn1.setOnClickListener(v -> mRightDialog.showDialog());
        mBtn2.setOnClickListener(v -> mBottomDialog.showDialog());
        mBtn3.setOnClickListener(v -> mCenterDialog.showDialog());

        mBtn4.setOnClickListener(v -> mWindowDialog.showDialog());
        mBtn5.setOnClickListener(v -> {
            if (mWindowDialog != null && mWindowDialog.isShowing()) {
                mWindowDialog.dismissDialog();
            }
        });

        mBtn6.setOnClickListener(v -> {
            Intent intent = new Intent(this, WindowService.class);
            startService(intent);
        });
        mBtn7.setOnClickListener(v -> {
            Intent intent = new Intent(this, WindowService.class);
            stopService(intent);
        });

        mBtn8.setOnClickListener(v -> {
            Intent intent = new Intent(this, FloatingService.class);
            startService(intent);
        });
        mBtn9.setOnClickListener(v -> {
            Intent intent = new Intent(this, FloatingService.class);
            stopService(intent);
        });

        mBtn19.setOnClickListener(v -> {
            mWeiboDialog = WeiBoDialogUtils.createLoadingDialog(this);
            mHandler.sendEmptyMessageDelayed(1, 2000);
        });
        mBtn20.setOnClickListener(v -> {
            mSecondDialog = DialogThirdUtils.showWaitDialog(this);
            mHandler.sendEmptyMessageDelayed(1, 2000);
        });

        requestPermission();
    }

    @Override
    public void onItemClick(String string) {
        Toast.makeText(this, "选择了" + string, Toast.LENGTH_SHORT).show();
    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivityForResult(intent, 100);
            } else {
                Log.i(TAG, "requestPermission: ");
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            Log.i(TAG, "onActivityResult: ");
        }
    }
}