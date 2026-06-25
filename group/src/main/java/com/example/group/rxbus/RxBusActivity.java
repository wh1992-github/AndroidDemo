package com.example.group.rxbus;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.group.databinding.ActivityRxbusBinding;

import rx.Subscription;
import rx.functions.Action1;

/**
 * @author test
 */
@SuppressLint("SetTextI18n")
public class RxBusActivity extends RxBusBaseActivity {
    private ActivityRxbusBinding binding;
    private TextView tvContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRxbusBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        tvContent = binding.tvContent;
        receiverEvent();
    }

    private void receiverEvent() {
        Subscription subscription = RxBus.getDefault().toObservable(EventBean.class).subscribe(new Action1<EventBean>() {
            @Override
            public void call(EventBean eventBean) {
                tvContent.setText(eventBean.getUserId() + " --- " + eventBean.getNickName());
            }
        });
        mRxBusList.add(subscription);
    }

    public void sendEvent(View view) {
        RxBus.getDefault().post(new EventBean(1, "nickName"));
    }
}
