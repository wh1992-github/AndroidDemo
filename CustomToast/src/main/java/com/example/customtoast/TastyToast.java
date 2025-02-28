package com.example.customtoast;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.rebound.SimpleSpringListener;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringConfig;
import com.facebook.rebound.SpringSystem;

public class TastyToast {
    private static final String TAG = "TastyToast";

    public static final int SUCCESS = 1;
    public static final int WARNING = 2;
    public static final int ERROR = 3;
    public static final int INFO = 4;
    public static final int DEFAULT = 5;
    public static final int CONFUSING = 6;

    static SuccessToastView successToastView;
    static WarningToastView warningToastView;
    static ErrorToastView errorToastView;
    static InfoToastView infoToastView;
    static DefaultToastView defaultToastView;
    static ConfusingToastView confusingToastView;

    public static Toast makeText(Context context, String msg, int type) {

        Toast toast = new Toast(context);

        switch (type) {
            case SUCCESS: {
                View layout = LayoutInflater.from(context).inflate(R.layout.success_toast_layout, null, false);
                TextView text = layout.findViewById(R.id.toastMessage);
                text.setText(msg);
                successToastView = layout.findViewById(R.id.successView);
                successToastView.startAnim();
                text.setBackgroundResource(R.drawable.success_toast);
                text.setTextColor(Color.parseColor("#FFFFFF"));
                toast.setView(layout);
                break;
            }
            case WARNING: {
                View layout = LayoutInflater.from(context).inflate(R.layout.warning_toast_layout, null, false);
                TextView text = layout.findViewById(R.id.toastMessage);
                text.setText(msg);
                warningToastView = layout.findViewById(R.id.warningView);
                SpringSystem springSystem = SpringSystem.create();
                final Spring spring = springSystem.createSpring();
                spring.setCurrentValue(1.8);
                SpringConfig config = new SpringConfig(40, 5);
                spring.setSpringConfig(config);
                spring.addListener(new SimpleSpringListener() {
                    @Override
                    public void onSpringUpdate(Spring spring) {
                        float value = (float) spring.getCurrentValue();
                        float scale = 0.9f - (value * 0.5f);

                        warningToastView.setScaleX(scale);
                        warningToastView.setScaleY(scale);
                    }
                });
                Thread t = new Thread(() -> {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        Log.i(TAG, "makeText: e = " + e.getMessage());
                    }
                    spring.setEndValue(0.4f);
                });
                t.start();
                text.setBackgroundResource(R.drawable.warning_toast);
                text.setTextColor(Color.parseColor("#FFFFFF"));
                toast.setView(layout);
                break;
            }
            case ERROR: {
                View layout = LayoutInflater.from(context).inflate(R.layout.error_toast_layout, null, false);
                TextView text = layout.findViewById(R.id.toastMessage);
                text.setText(msg);
                errorToastView = layout.findViewById(R.id.errorView);
                errorToastView.startAnim();
                text.setBackgroundResource(R.drawable.error_toast);
                text.setTextColor(Color.parseColor("#FFFFFF"));
                toast.setView(layout);
                break;
            }
            case INFO: {
                View layout = LayoutInflater.from(context).inflate(R.layout.info_toast_layout, null, false);
                TextView text = layout.findViewById(R.id.toastMessage);
                text.setText(msg);
                infoToastView = layout.findViewById(R.id.infoView);
                infoToastView.startAnim();
                text.setBackgroundResource(R.drawable.info_toast);
                text.setTextColor(Color.parseColor("#FFFFFF"));
                toast.setView(layout);
                break;
            }
            case DEFAULT: {
                View layout = LayoutInflater.from(context).inflate(R.layout.default_toast_layout, null, false);
                TextView text = layout.findViewById(R.id.toastMessage);
                text.setText(msg);
                defaultToastView = layout.findViewById(R.id.defaultView);
                defaultToastView.startAnim();
                text.setBackgroundResource(R.drawable.default_toast);
                text.setTextColor(Color.parseColor("#FFFFFF"));
                toast.setView(layout);
                break;
            }
            case CONFUSING: {
                View layout = LayoutInflater.from(context).inflate(R.layout.confusing_toast_layout, null, false);
                TextView text = layout.findViewById(R.id.toastMessage);
                text.setText(msg);
                confusingToastView = layout.findViewById(R.id.confusingView);
                confusingToastView.startAnim();
                text.setBackgroundResource(R.drawable.confusing_toast);
                text.setTextColor(Color.parseColor("#FFFFFF"));
                toast.setView(layout);
                break;
            }
        }
        toast.setDuration(Toast.LENGTH_LONG);
        return toast;
    }
}
