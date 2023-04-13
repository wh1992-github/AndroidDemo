package com.example.customview.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.customview.R;
import com.example.customview.loadingview.LVChromeLogo;
import com.example.customview.loadingview.LVCircular;
import com.example.customview.loadingview.LVCircularCD;
import com.example.customview.loadingview.LVLineWithText;
import com.example.customview.loadingview.LVSunSetView;
import com.example.customview.loadingview.view.LVBattery;
import com.example.customview.loadingview.view.LVBlazeWood;
import com.example.customview.loadingview.view.LVBlock;
import com.example.customview.loadingview.view.LVCircularJump;
import com.example.customview.loadingview.view.LVCircularRing;
import com.example.customview.loadingview.view.LVCircularSmile;
import com.example.customview.loadingview.view.LVCircularZoom;
import com.example.customview.loadingview.view.LVEatBeans;
import com.example.customview.loadingview.view.LVFinePoiStar;
import com.example.customview.loadingview.view.LVFunnyBar;
import com.example.customview.loadingview.view.LVGears;
import com.example.customview.loadingview.view.LVGearsTwo;
import com.example.customview.loadingview.view.LVGhost;
import com.example.customview.loadingview.view.LVNews;
import com.example.customview.loadingview.view.LVPlayBall;
import com.example.customview.loadingview.view.LVRingProgress;
import com.example.customview.loadingview.view.LVWifi;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @author test
 */
public class LoadingViewActivity extends AppCompatActivity {

    private LVPlayBall mLVPlayBall;
    private LVCircularRing mLVCircularRing;
    private LVCircular mLVCircular;
    private LVCircularJump mLVCircularJump;
    private LVCircularZoom mLVCircularZoom;
    private LVLineWithText mLVLineWithText;
    private LVEatBeans mLVEatBeans;
    private LVCircularCD mLVCircularCD;
    private LVCircularSmile mLVCircularSmile;

    private LVGears mLVGears;
    private LVGearsTwo mLVGearsTwo;
    private LVFinePoiStar mLVFinePoiStar;
    private LVChromeLogo mLVChromeLogo;
    private LVBattery mLVBattery;
    private LVWifi mLVWifi;

    private LVNews mLVNews;
    private LVBlock mLVBlock;
    private LVGhost mLVGhost;
    private LVFunnyBar mLVFunnyBar;
    private LVRingProgress mLVRingProgress;
    private LVSunSetView lv_sunset;
    private LVBlazeWood mLVBlazeWood;

    private int mValueLVLineWithText = 0;
    private int mValueLVNews = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_view);

        mLVCircular = findViewById(R.id.lv_circular);
        mLVCircular.setViewColor(Color.rgb(255, 99, 99));
        mLVCircular.setRoundColor(Color.rgb(255, 0, 0));

        mLVCircularCD = findViewById(R.id.lv_circularCD);
        mLVCircularCD.setViewColor(Color.rgb(0, 255, 0));


        mLVLineWithText = findViewById(R.id.lv_linetext);
        mLVLineWithText.setViewColor(Color.rgb(33, 66, 77));
        mLVLineWithText.setTextColor(Color.rgb(233, 166, 177));

        mLVCircularJump = findViewById(R.id.lv_circularJump);
        mLVCircularJump.setViewColor(Color.rgb(133, 66, 99));


        mLVCircularRing = findViewById(R.id.lv_circularring);
        mLVCircularRing.setViewColor(Color.argb(100, 255, 255, 255));
        mLVCircularRing.setBarColor(Color.YELLOW);

        mLVCircularSmile = findViewById(R.id.lv_circularSmile);
        mLVCircularSmile.setViewColor(Color.rgb(144, 238, 146));

        mLVCircularZoom = findViewById(R.id.lv_circularZoom);
        mLVCircularZoom.setViewColor(Color.rgb(255, 0, 122));

        mLVEatBeans = findViewById(R.id.lv_eatBeans);
        mLVEatBeans.setViewColor(Color.WHITE);
        mLVEatBeans.setEyeColor(Color.BLUE);

        mLVFinePoiStar = findViewById(R.id.lv_finePoiStar);
        mLVFinePoiStar.setViewColor(Color.WHITE);
        mLVFinePoiStar.setCircleColor(Color.YELLOW);
        mLVFinePoiStar.setDrawPath(true);

        mLVGears = findViewById(R.id.lv_gears);
        mLVGears.setViewColor(Color.rgb(55, 155, 233));

        mLVGearsTwo = findViewById(R.id.lv_gears_two);
        mLVGearsTwo.setViewColor(Color.rgb(155, 55, 233));

        mLVWifi = findViewById(R.id.lv_wifi);
        mLVWifi.setViewColor(Color.BLACK);

        mLVNews = findViewById(R.id.lv_news);
        mLVNews.setViewColor(Color.WHITE);

        mLVRingProgress = findViewById(R.id.lv_ringp);
        mLVRingProgress.setViewColor(Color.WHITE);
        mLVRingProgress.setTextColor(Color.BLACK);
        mLVRingProgress.setPorBarStartColor(Color.YELLOW);
        mLVRingProgress.setPorBarEndColor(Color.BLUE);

        mLVGhost = findViewById(R.id.lv_ghost);
        mLVGhost.setViewColor(Color.WHITE);
        mLVGhost.setHandColor(Color.BLACK);

        mLVPlayBall = findViewById(R.id.lv_playball);
        mLVPlayBall.setViewColor(Color.WHITE);
        mLVPlayBall.setBallColor(Color.RED);

        mLVChromeLogo = findViewById(R.id.lv_chromeLogo);

        mLVBattery = findViewById(R.id.lv_battery);
        mLVBattery.setBatteryOrientation(LVBattery.BatteryOrientation.VERTICAL);//LVBattery.BatteryOrientation.HORIZONTAL
        mLVBattery.setShowNum(false);

        mLVBattery.setViewColor(Color.WHITE);
        mLVBattery.setCellColor(Color.GREEN);

        mLVBlock = findViewById(R.id.lv_block);

        mLVBlock.setViewColor(Color.rgb(245, 209, 22));
        mLVBlock.setShadowColor(Color.BLACK);
        //mLVBlock.isShadow(false);

        mLVFunnyBar = findViewById(R.id.lv_funnybar);
        mLVFunnyBar.setViewColor(Color.rgb(234, 167, 107));

        mLVBlazeWood = findViewById(R.id.lv_wood);

        lv_sunset = findViewById(R.id.lv_sunset);
        lv_sunset.setSunendTime("2019-09-06 16:20:00");
        lv_sunset.setSunstartTime("2019-09-06 7:16:00");
    }

    public void startAnim(View v) {
        stopAll();
        if (v instanceof LVCircular) {
            ((LVCircular) v).startAnim();
        } else if (v instanceof LVCircularCD) {
            ((LVCircularCD) v).startAnim();
        } else if (v instanceof LVCircularSmile) {
            ((LVCircularSmile) v).startAnim();
        } else if (v instanceof LVCircularRing) {
            ((LVCircularRing) v).startAnim();
        } else if (v instanceof LVCircularZoom) {
            ((LVCircularZoom) v).startAnim();
        } else if (v instanceof LVCircularJump) {
            ((LVCircularJump) v).startAnim();
        } else if (v instanceof LVEatBeans) {
            ((LVEatBeans) v).startAnim(3500);
        } else if (v instanceof LVPlayBall) {
            ((LVPlayBall) v).startAnim();
        } else if (v instanceof LVLineWithText) {
            startLVLineWithTextAnim();
        } else if (v instanceof LVGears) {
            ((LVGears) v).startAnim();
        } else if (v instanceof LVGearsTwo) {
            ((LVGearsTwo) v).startAnim();
        } else if (v instanceof LVFinePoiStar) {
            ((LVFinePoiStar) v).setDrawPath(false);
            ((LVFinePoiStar) v).startAnim(3500);
        } else if (v instanceof LVChromeLogo) {
            ((LVChromeLogo) v).startAnim();
        } else if (v instanceof LVBattery) {
            ((LVBattery) v).startAnim(5000);
        } else if (v instanceof LVWifi) {
            ((LVWifi) v).startAnim(9000);
        } else if (v instanceof LVNews) {
            startLVNewsAnim();
        } else if (v instanceof LVBlock) {
            ((LVBlock) v).startAnim();
        } else if (v instanceof LVGhost) {
            ((LVGhost) v).startAnim();
        } else if (v instanceof LVFunnyBar) {
            ((LVFunnyBar) v).startAnim();
        } else if (v instanceof LVRingProgress) {
            ((LVRingProgress) v).startAnim(3000);
        } else if (v instanceof LVBlazeWood) {
            ((LVBlazeWood) v).startAnim(500);
        } else if (v instanceof LVSunSetView) {
            ((LVSunSetView) v).startSunset();
        }
    }

    public void startAnimAll(View v) {
        mLVCircular.startAnim();
        mLVCircularRing.startAnim();
        mLVPlayBall.startAnim();
        mLVCircularJump.startAnim();
        mLVCircularZoom.startAnim();
        startLVLineWithTextAnim();
        mLVEatBeans.startAnim(3500);
        mLVCircularCD.startAnim();
        mLVCircularSmile.startAnim(1000);
        mLVGears.startAnim();
        mLVGearsTwo.startAnim();
        mLVFinePoiStar.setDrawPath(true);
        mLVFinePoiStar.startAnim(3500);
        mLVChromeLogo.startAnim();
        mLVBattery.startAnim(5000);
        mLVWifi.startAnim(9000);
        startLVNewsAnim();
        mLVBlock.startAnim();
        mLVGhost.startAnim();
        mLVFunnyBar.startAnim();
        mLVRingProgress.startAnim(3000);
        mLVBlazeWood.startAnim(500);
        lv_sunset.startSunset();
    }

    public void stopAnimAll(View v) {
        stopAll();
    }

    private void stopAll() {
        mLVCircular.stopAnim();
        mLVPlayBall.stopAnim();
        mLVCircularJump.stopAnim();
        mLVCircularZoom.stopAnim();
        mLVCircularRing.stopAnim();
        mLVEatBeans.stopAnim();
        stopLVLineWithTextAnim();
        mLVCircularCD.stopAnim();
        mLVCircularSmile.stopAnim();
        mLVGears.stopAnim();
        mLVGearsTwo.stopAnim();
        mLVFinePoiStar.stopAnim();
        mLVChromeLogo.stopAnim();
        mLVBattery.stopAnim();
        mLVWifi.stopAnim();
        stopLVNewsAnim();
        mLVBlock.stopAnim();
        mLVGhost.stopAnim();
        mLVFunnyBar.stopAnim();
        mLVRingProgress.stopAnim();
        mLVBlazeWood.stopAnim();
    }

    public Timer mTimerLVLineWithText = new Timer();// 定时器
    public Timer mTimerLVNews = new Timer();// 定时器

    private void startLVLineWithTextAnim() {
        mValueLVLineWithText = 0;
        if (mTimerLVLineWithText != null) {
            mTimerLVLineWithText.cancel();// 退出之前的mTimer
        }
        mTimerLVLineWithText = new Timer();
        timerTaskLVLineWithText();
    }

    private void stopLVLineWithTextAnim() {
        if (mTimerLVLineWithText != null) {
            mTimerLVLineWithText.cancel();// 退出之前的mTimer
            mLVNews.setValue(mValueLVNews);
        }
    }

    private void startLVNewsAnim() {
        mValueLVNews = 0;
        if (mTimerLVNews != null) {
            mTimerLVNews.cancel();
        }
        mTimerLVNews = new Timer();
        timerTaskLVNews();
    }

    private void stopLVNewsAnim() {
        mLVNews.stopAnim();
        if (mTimerLVNews != null) {
            mTimerLVNews.cancel();
            mLVLineWithText.setValue(mValueLVLineWithText);
        }
    }

    public void timerTaskLVNews() {
        mTimerLVNews.schedule(new TimerTask() {
            @Override
            public void run() {
                if (mValueLVNews < 100) {
                    mValueLVNews++;
                    Message msg = mHandle.obtainMessage(1);
                    msg.arg1 = mValueLVNews;
                    mHandle.sendMessage(msg);
                } else {
                    mTimerLVNews.cancel();
                }
            }
        }, 0, 10);
    }

    public void timerTaskLVLineWithText() {
        mTimerLVLineWithText.schedule(new TimerTask() {
            @Override
            public void run() {
                if (mValueLVLineWithText < 100) {
                    mValueLVLineWithText++;
                    Message msg = mHandle.obtainMessage(2);
                    msg.arg1 = mValueLVLineWithText;
                    mHandle.sendMessage(msg);

                } else {
                    mTimerLVLineWithText.cancel();
                }
            }
        }, 0, 50);
    }

    private final Handler mHandle = new Handler(Looper.getMainLooper()) {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 2) {
                mLVLineWithText.setValue(msg.arg1);
            } else if (msg.what == 1) {
                mLVNews.setValue(msg.arg1);
            }
        }
    };
}

