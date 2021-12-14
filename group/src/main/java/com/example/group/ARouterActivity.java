package com.example.group;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.callback.NavCallback;
import com.alibaba.android.arouter.launcher.ARouter;
import com.example.group.arouter.activity.ResultActivity;
import com.example.group.arouter.data.ARouterConstants;
import com.example.group.arouter.data.Person;
import com.example.group.arouter.data.TestObj;
import com.example.group.arouter.data.TestService;

import java.lang.ref.WeakReference;

@SuppressLint("NonConstantResourceId")
public class ARouterActivity extends AppCompatActivity {
    private static final String TAG = "ARouterActivity";
    public static final int REQUEST_CODE = 2;
    public static final String NAME = "name";
    public static final String AGE = "age";
    public static final String PERSON = "person";
    public static final String TEST_OBJ = "testObj";

    private static WeakReference<Activity> weakReference;

    public static WeakReference<Activity> getWeakReference() {
        return weakReference;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arouter);
        weakReference = new WeakReference<>(this);
    }

    public void onButtonClick(View view) {
        switch (view.getId()) {
            case R.id.btn_1:
                ARouter.getInstance().build(ARouterConstants.COM_ACTIVITY_ONE).navigation();
                break;
            case R.id.btn_2:
                ARouterCallBack();
                break;
            case R.id.btn_3:
                Person person = new Person();
                person.age = 20;
                person.name = "test";
                person.weight = 60.5f;
                TestObj testObj = new TestObj();
                testObj.name = "test";
                ARouter.getInstance().build(ARouterConstants.COM_PARSE_ACTIVITY)
                        .withString(NAME, "test")
                        .withInt(AGE, 1)
                        .withParcelable(PERSON, person)
                        .withObject(TEST_OBJ, testObj)
                        .navigation();
                break;
            case R.id.btn_4:
                ARouter.getInstance().build(ARouterConstants.COM_ACTIVITY_RESULT).navigation(this, REQUEST_CODE);
                break;
            case R.id.btn_5:
                String result1 = ((TestService) ARouter.getInstance().build(ARouterConstants.SERVICE_HELLO).navigation()).sayYes("test");
                String result2 = ARouter.getInstance().navigation(TestService.class).sayNo("test");
                Log.i(TAG, "testIProvider: result1 = " + result1 + ", result2 = " + result2);
                break;
            case R.id.btn_7:
                ARouter.getInstance().build(ARouterConstants.COM_URL).navigation();
                break;
            case R.id.btn_8:
                ARouter.getInstance().build(ARouterConstants.COM_ACTIVITY_INTERCEPTOR).navigation();
                break;
        }
    }

    private void ARouterCallBack() {
        ARouter.getInstance()
                .build(ARouterConstants.COM_ACTIVITY_ONE)
                .navigation(this, new NavCallback() {
                    @Override
                    public void onFound(Postcard postcard) {
                        Log.i(TAG, "onArrival: 找到了 ");
                    }

                    @Override
                    public void onLost(Postcard postcard) {
                        Log.i(TAG, "onArrival: 找不到了 ");
                    }

                    @Override
                    public void onArrival(Postcard postcard) {
                        Log.i(TAG, "onArrival: 跳转完了 ");
                    }

                    @Override
                    public void onInterrupt(Postcard postcard) {
                        Log.i(TAG, "onArrival: 被拦截了 ");
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            Log.i(TAG, "onActivityResult: requestCode = " + requestCode + ", resultCode = " + resultCode + ", data = " + data);
            if (data != null) {
                Log.i(TAG, "onActivityResult: " + data.getStringExtra(ResultActivity.RESULT));
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (weakReference != null) {
            weakReference.clear();
        }
    }
}
