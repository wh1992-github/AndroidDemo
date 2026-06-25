package com.example.group.windowdialog;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.group.R;
import com.example.group.databinding.ThridDialogLoadingBinding;
/**
 * 提供 Dialog Third 相关工具方法的工具类。
 */

public class DialogThirdUtils {

    public static Dialog showWaitDialog(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        ThridDialogLoadingBinding binding = ThridDialogLoadingBinding.inflate(inflater);
        LinearLayout layout = (LinearLayout) binding.getRoot();

        ImageView spaceshipImage = binding.img;
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.rotate_animation);
        spaceshipImage.startAnimation(animation);
        Dialog loadingDialog = new Dialog(context, R.style.MyDialogStyle);
        loadingDialog.setContentView(layout);
        loadingDialog.setCancelable(true);
        loadingDialog.setCanceledOnTouchOutside(false);

        Window window = loadingDialog.getWindow();
        window.setWindowAnimations(R.style.PopWindowAnimStyle);
        loadingDialog.show();

        return loadingDialog;
    }

    public static void closeDialog(Dialog dialog) {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }
}
