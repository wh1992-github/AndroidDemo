package com.example.group.livedata;

import androidx.lifecycle.LiveData;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.group.util.LogUtil;

/**
 * Created by test on 19-1-22.
 */
public class NetworkLiveData extends LiveData<NetworkInfo> {
    private static final String TAG = "NetworkLiveData";

    private static NetworkLiveData mNetworkLiveData;
    private final Context mContext;
    private final IntentFilter mIntentFilter;
    private NetworkReceiver mNetworkReceiver;

    public NetworkLiveData(Context context) {
        mContext = context.getApplicationContext();
        mNetworkReceiver = new NetworkReceiver();
        mIntentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
    }

    public static NetworkLiveData getInstance(Context context) {
        if (mNetworkLiveData == null) {
            mNetworkLiveData = new NetworkLiveData(context);
        }
        return mNetworkLiveData;
    }

    @Override
    protected void onActive() {
        super.onActive();
        LogUtil.i(TAG, "onActive:");
        try {
            mContext.registerReceiver(mNetworkReceiver, mIntentFilter);
        } catch (Exception e) {
            LogUtil.e(TAG, "onActive: failed to register receiver: " + e.getMessage());
        }
    }

    @Override
    protected void onInactive() {
        super.onInactive();
        LogUtil.i(TAG, "onInactive: ");
        try {
            mContext.unregisterReceiver(mNetworkReceiver);
        } catch (IllegalArgumentException e) {
            // 接收器可能已经注销，忽略异常
            LogUtil.w(TAG, "onInactive: receiver already unregistered");
        }
    }

    private static class NetworkReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = manager.getActiveNetworkInfo();
            getInstance(context).setValue(activeNetwork);
        }
    }
}
