package com.example.group;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.OrientationEventListener;
import android.view.View;
import android.widget.TextView;

@SuppressLint("LogNotTimber")
//屏幕方向自适应
public class OrientationActivity extends AppCompatActivity {
    private static final String TAG = "OrientationActivity";
    private int mOrientation = OrientationEventListener.ORIENTATION_UNKNOWN;
    private MyOrientationEventListener mOrientationListener;
    private TextView mTv1, mTv2, mTv3, mTv4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orientation);
        mTv1 = findViewById(R.id.tv1);
        mTv2 = findViewById(R.id.tv2);
        mTv3 = findViewById(R.id.tv3);
        mTv4 = findViewById(R.id.tv4);
        mTv2.setVisibility(View.INVISIBLE);
        mTv3.setVisibility(View.INVISIBLE);
        mTv4.setVisibility(View.INVISIBLE);
        mOrientationListener = new MyOrientationEventListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mOrientationListener.enable();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mOrientationListener.disable();
    }

    private class MyOrientationEventListener extends OrientationEventListener {
        public MyOrientationEventListener(Context context) {
            super(context);
        }

        @Override
        public void onOrientationChanged(int orientation) {
            if (orientation == mOrientation) {
                return;
            }
            orientationChange(orientation);
        }
    }

    private void orientationChange(int orientation) {
        if (orientation == OrientationEventListener.ORIENTATION_UNKNOWN) {
            return;
        }
        int oldOrientation = mOrientation;
        mOrientation = roundOrientation(orientation, mOrientation);
        if (oldOrientation != mOrientation) {
            Log.i(TAG, "orientationChange: oldOrientation = " + oldOrientation + ", mOrientation = " + mOrientation);
            rotateView(mOrientation);
        }
    }

    private void rotateView(int orientation) {
        mTv1.setVisibility(View.INVISIBLE);
        mTv2.setVisibility(View.INVISIBLE);
        mTv3.setVisibility(View.INVISIBLE);
        mTv4.setVisibility(View.INVISIBLE);
        if (orientation == 0) {
            mTv1.setVisibility(View.VISIBLE);
        } else if (orientation == 90) {
            mTv4.setVisibility(View.VISIBLE);
        } else if (orientation == 180) {
            mTv3.setVisibility(View.VISIBLE);
        } else if (orientation == 270) {
            mTv2.setVisibility(View.VISIBLE);
        }
    }

    public int roundOrientation(int orientation, int orientationHistory) {
        boolean changeOrientation;
        if (orientationHistory == OrientationEventListener.ORIENTATION_UNKNOWN) {
            changeOrientation = true;
        } else {
            int dist = Math.abs(orientation - orientationHistory);
            dist = Math.min(dist, 360 - dist);
            changeOrientation = (dist >= 45 + 5);
        }
        if (changeOrientation) {
            return ((orientation + 45) / 90 * 90) % 360;
        }
        return orientationHistory;
    }
}
