package com.example.group.rxbus;

import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;

import rx.Subscription;

/**
 * @author test
 */
public class RxBusBaseActivity extends AppCompatActivity {
    protected ArrayList<Subscription> mRxBusList = new ArrayList<>();

    /**
     * 取消该页面所有订阅
     */
    private void clearSubscription() {
        for (Subscription subscription : mRxBusList) {
            if (subscription != null && !subscription.isUnsubscribed()) {
                subscription.unsubscribe();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        clearSubscription();
    }
}
