package com.example.group.rxbus;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.group.R;

import rx.Subscription;
import rx.functions.Action1;

/**
 * @author wh
 */
@SuppressLint("SetTextI18n")
public class RxBusActivity extends RxBusBaseActivity {
    private TextView tvContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rxbus);

        tvContent = findViewById(R.id.tv_content);
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
