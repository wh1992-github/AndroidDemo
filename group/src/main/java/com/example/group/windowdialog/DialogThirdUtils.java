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

public class DialogThirdUtils {

    public static Dialog showWaitDialog(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.thrid_dialog_loading, null);

        ImageView spaceshipImage = layout.findViewById(R.id.img);
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
