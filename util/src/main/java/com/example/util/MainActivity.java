package com.example.util;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.facebook.drawee.view.SimpleDraweeView;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity---";
    private ConstraintLayout mRootView;
    private ImageView mImageView;
    private SimpleDraweeView mSimpleDraweeView;
    private Button mButton;
    private CountDownTimerUtil mCountDownTimerUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ScreenUtils.printScreenParams(this);
        mRootView = findViewById(R.id.constraint_layout);
        mImageView = findViewById(R.id.imageview);
        mSimpleDraweeView = findViewById(R.id.simpledraweeview);
        mButton = findViewById(R.id.button);
        mButton.setOnClickListener(this::onClick);

        GlideUtil.loadImage(this, mImageView);
        FrescoUtil.loadImage(mSimpleDraweeView);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onClick(View view) {
        DefaultType defaultType = new DefaultType();
        defaultType.setSexName(DefaultType.Person.female);
        defaultType.setSexType(DefaultType.Sex.MAN);

        Class<?> childClass = DefaultType.Child.class;
        DefaultType.People people = childClass.getAnnotation(DefaultType.People.class);
        String name = people.getName();
        int age = people.getAge();
        Log.i(TAG, "onClick: value = " + name + ", age = " + age);

        mCountDownTimerUtil = new CountDownTimerUtil(10 * 1000, 1000);
        mCountDownTimerUtil.start();

        ReflectUtil.reflect();
        AtomicWork.atomicWork();
        JavaTest.switchLight();
        JavaTest.fbnq();
        JavaTest.test();
        SerializableUtil.saveObject();
        SerializableUtil.getObject();

        Log.i(TAG, "onClick: " + tryCatch());

        //PhoneWindow
        Log.i(TAG, "onClick: " + getWindow().getClass());
        //DecorView
        Log.i(TAG, "onClick: " + getWindow().getDecorView().getClass());
        //ContentFrameLayout
        Log.i(TAG, "onClick: " + mRootView.getParent().getClass());
        //ActionBarOverlayLayout
        Log.i(TAG, "onClick: " + mRootView.getParent().getParent().getClass());
        //FrameLayout
        Log.i(TAG, "onClick: " + mRootView.getParent().getParent().getParent().getClass());
        //LinearLayout
        Log.i(TAG, "onClick: " + mRootView.getParent().getParent().getParent().getParent().getClass());
        //DecorView
        Log.i(TAG, "onClick: " + mRootView.getParent().getParent().getParent().getParent().getParent().getClass());
        //ViewRootImpl
        Log.i(TAG, "onClick: " + mRootView.getParent().getParent().getParent().getParent().getParent().getParent().getClass());

    }

    public int tryCatch() {
        int i = 0;
        Log.i(TAG, "tryCatch: a = " + i);
        try {
            i = 2;
            Log.i(TAG, "tryCatch: b = " + i);
            return i;
        } finally {
            i = 3;
            Log.i(TAG, "tryCatch: c = " + i);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCountDownTimerUtil != null) {
            mCountDownTimerUtil.cancel();
            mCountDownTimerUtil = null;
        }
    }
}
