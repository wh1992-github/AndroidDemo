package com.example.group.arouter.data;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.annotation.Interceptor;
import com.alibaba.android.arouter.facade.callback.InterceptorCallback;
import com.alibaba.android.arouter.facade.template.IInterceptor;
import com.example.group.ARouterActivity;

@Interceptor(priority = 7, name = "TestInterceptor")
public class InterceptorImpl implements IInterceptor {
    private static final String TAG = "TestInterceptor";

    public static final String INTERCEPTOR = ARouterConstants.COM_ACTIVITY_INTERCEPTOR;

    @Override
    public void process(final Postcard postcard, final InterceptorCallback callback) {
        String path = postcard.getPath();
        Log.i(TAG, "process: path = " + path);

        if (INTERCEPTOR.equals(postcard.getPath())) {
            Activity activity = ARouterActivity.getWeakReference().get();
            if (activity == null) {
                return;
            }
            final AlertDialog.Builder ab = new AlertDialog.Builder(activity);
            ab.setCancelable(false);
            ab.setTitle("温馨提醒");
            ab.setMessage(String.format("想要跳转到ActivityOne吗？(触发了%s拦截器，拦截了本次跳转)", INTERCEPTOR));
            ab.setNegativeButton("继续", (dialog, which) -> callback.onContinue(postcard));
            ab.setNeutralButton("算了", (dialog, which) -> callback.onInterrupt(null));
            ab.setPositiveButton("加点料", (dialog, which) -> {
                postcard.withString("extra", "我是在拦截器中附加的参数");
                callback.onContinue(postcard);
            });

            activity.runOnUiThread(() -> ab.create().show());
        } else {
            callback.onContinue(postcard);
        }
    }

    @Override
    public void init(Context context) {

    }
}
