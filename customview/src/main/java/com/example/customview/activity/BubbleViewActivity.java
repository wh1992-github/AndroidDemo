package com.example.customview.activity;

import android.graphics.PointF;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.customview.R;
import com.example.customview.bean.CircleBean;
import com.example.customview.utils.DisplayUtils;
import com.example.customview.widget.BubbleView;

import java.util.ArrayList;
import java.util.List;

public class BubbleViewActivity extends AppCompatActivity {
    private static final String TAG = "BubbleViewActivity";
    private ImageView hxbIv;
    private TextView hxbTv;
    private Button button;
    private BubbleView bezierView;

    private List<CircleBean> circleBeanList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bubble_view);
        hxbIv = findViewById(R.id.hxb_iv);
        hxbTv = findViewById(R.id.center_tv);
        bezierView = findViewById(R.id.circle_view);
        button = findViewById(R.id.start_btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bezierView.setCenterImg(hxbTv);
                bezierView.openAnimation();
            }
        });

        initPoint();

        bezierView.setCircleBeen(circleBeanList);
//       bezierView.createInAnimation();
    }

    private void initPoint() {
        int height = DisplayUtils.getDisplayHeight(this);
        int width = DisplayUtils.getDisplayWidth(this);

        int centerX = width / 2;
        int centerY = height / 2;
        Log.i(TAG, "initPoint: " + centerX + "----" + centerY);
        CircleBean circleBean = new CircleBean(
                new PointF((float) (-width / 5.1), (float) (height / 1.5)),
                new PointF(centerX - 30, height * 2 / 3),
                new PointF((float) (width / 2.4), (float) (height / 3.4)),
                new PointF(width / 6, centerY - 120),
                new PointF((float) (width / 7.2), -height / 128),
                (float) (width / 14.4), 60);
        CircleBean circleBean2 = new CircleBean(
                new PointF(-width / 4, (float) (height / 1.3)),
                new PointF(centerX - 20, height * 3 / 5),
                new PointF((float) (width / 2.1), (float) (height / 2.5)),
                new PointF(width / 3, centerY - 10),
                new PointF(width / 4, (float) (-height / 5.3)),
                width / 4, 60);
        CircleBean circleBean3 = new CircleBean(
                new PointF(-width / 12, (float) (height / 1.1)),
                new PointF(centerX - 100, height * 2 / 3),
                new PointF((float) (width / 3.4), height / 2),
                new PointF(0, centerY + 100),
                new PointF(0, 0),
                width / 24, 60);

        CircleBean circleBean4 = new CircleBean(
                new PointF(-width / 9, (float) (height / 0.9)),
                new PointF(centerX, height * 3 / 4),
                new PointF((float) (width / 2.1), (float) (height / 2.3)),
                new PointF(width / 2, centerY),
                new PointF((float) (width / 1.5), (float) (-height / 5.6)),
                width / 4, 60);

        CircleBean circleBean5 = new CircleBean(
                new PointF((float) (width / 1.4), (float) (height / 0.9)),
                new PointF(centerX, height * 3 / 4),
                new PointF(width / 2, (float) (height / 2.37)),
                new PointF(width * 10 / 13, centerY - 20),
                new PointF(width / 2, (float) (-height / 7.1)),
                width / 4, 60);
        CircleBean circleBean6 = new CircleBean(
                new PointF((float) (width / 0.8), height),
                new PointF(centerX + 20, height * 2 / 3),
                new PointF((float) (width / 1.9), (float) (height / 2.3)),
                new PointF(width * 11 / 14, centerY + 10),

                new PointF((float) (width / 1.1), (float) (-height / 6.4)),
                (float) (width / 4), 60);
        CircleBean circleBean7 = new CircleBean(
                new PointF((float) (width / 0.9), (float) (height / 1.2)),
                new PointF(centerX + 20, height * 4 / 7),
                new PointF((float) (width / 1.6), (float) (height / 1.9)),
                new PointF(width, centerY + 10),

                new PointF(width, 0),
                (float) (width / 9.6), 60);

        circleBeanList.add(circleBean);
        circleBeanList.add(circleBean2);
        circleBeanList.add(circleBean3);
        circleBeanList.add(circleBean4);
        circleBeanList.add(circleBean5);
        circleBeanList.add(circleBean6);
        circleBeanList.add(circleBean7);
    }

    /**
     * 获取状态栏高度
     *
     * @return
     */
    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume: ");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "onPause: ");
    }
}
