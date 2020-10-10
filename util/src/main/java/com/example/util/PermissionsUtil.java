package com.example.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;

import java.util.ArrayList;
import java.util.List;

public class PermissionsUtil {
    private static final int REQUEST_CODE = 100;
    private static AlertDialog mPermissionDialog;

    public static void checkPermissions(Activity activity, String[] permissions) {
        List<String> permissionList = new ArrayList<>();
        for (String permission : permissions) {
            if (ActivityCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                //添加还未授予的权限
                permissionList.add(permission);
            }
        }
        if (permissionList.size() > 0) {
            //权限没有通过,需要申请
            ActivityCompat.requestPermissions(activity, permissions, REQUEST_CODE);
        }
    }

    //请求权限后回调的方法
    //参数:requestCode   是我们自己定义的权限请求码
    //参数:permissions   是我们请求的权限名称数组
    //参数:grantResults  是我们在弹出页面后是否允许权限的标识数组
    public static void onRequestPermissionsResult(Activity activity, int requestCode, String[] permissions, int[] grantResults) {
        boolean hasPermissionDismiss = false;
        if (REQUEST_CODE == requestCode) {
            for (int grantResult : grantResults) {
                if (grantResult != PackageManager.PERMISSION_GRANTED) {
                    hasPermissionDismiss = true;
                    break;
                }
            }
            //如果有权限没有被允许
            if (hasPermissionDismiss) {
                showDialog(activity);
            }
        }
    }

    private static void showDialog(Activity activity) {
        if (mPermissionDialog == null) {
            mPermissionDialog = new AlertDialog.Builder(activity)
                    .setTitle("温馨提示")
                    .setMessage("缺少必要权限,请打开设置手动授予")

                    .setPositiveButton("打开设置", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            intent.setData(Uri.parse("package:" + activity.getPackageName()));
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            activity.startActivity(intent);
                        }
                    }).setNegativeButton("关闭应用", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //关闭页面或者做其他操作
                            //closeApp())
                        }
                    })
                    .setCancelable(false)
                    .create();
        }
        mPermissionDialog.show();
    }

    public static boolean grantAllPermission(Context context, String[] permissions) {
        for (String permission : permissions) {
            if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }
}