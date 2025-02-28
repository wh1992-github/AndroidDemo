package com.example.custom.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;

import com.example.custom.R;

/**
 * Created by test on 2017/10/14.
 */
public class MainActivity extends AppCompatActivity implements OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_custom_property).setOnClickListener(this);
        findViewById(R.id.btn_measure_text).setOnClickListener(this);
        findViewById(R.id.btn_measure_layout).setOnClickListener(this);
        findViewById(R.id.btn_on_measure1).setOnClickListener(this);
        findViewById(R.id.btn_on_measure2).setOnClickListener(this);
        findViewById(R.id.btn_on_measure3).setOnClickListener(this);
        findViewById(R.id.btn_on_layout).setOnClickListener(this);
        findViewById(R.id.btn_show_draw).setOnClickListener(this);
        findViewById(R.id.btn_runnable).setOnClickListener(this);
        findViewById(R.id.btn_pull_refresh).setOnClickListener(this);
        findViewById(R.id.btn_circle_animation).setOnClickListener(this);
        findViewById(R.id.btn_circle_clock).setOnClickListener(this);
        findViewById(R.id.btn_ripple_anim).setOnClickListener(this);
        findViewById(R.id.btn_round_imageview).setOnClickListener(this);
        findViewById(R.id.btn_nice_imageview).setOnClickListener(this);
        findViewById(R.id.btn_custom_textview).setOnClickListener(this);
        findViewById(R.id.btn_bezier_quad).setOnClickListener(this);
        findViewById(R.id.btn_bezier_cubic).setOnClickListener(this);
        findViewById(R.id.btn_bezier_custom).setOnClickListener(this);
        findViewById(R.id.btn_bezier_ripple).setOnClickListener(this);
        findViewById(R.id.btn_window).setOnClickListener(this);
        findViewById(R.id.btn_dialog_date).setOnClickListener(this);
        findViewById(R.id.btn_dialog_multi).setOnClickListener(this);
        findViewById(R.id.btn_notify_simple).setOnClickListener(this);
        findViewById(R.id.btn_notify_counter).setOnClickListener(this);
        findViewById(R.id.btn_notify_progress).setOnClickListener(this);
        findViewById(R.id.btn_notify_custom).setOnClickListener(this);
        findViewById(R.id.btn_service_normal).setOnClickListener(this);
        findViewById(R.id.btn_bind_immediate).setOnClickListener(this);
        findViewById(R.id.btn_bind_delay).setOnClickListener(this);
        findViewById(R.id.btn_notify_service).setOnClickListener(this);
        findViewById(R.id.btn_app_info).setOnClickListener(this);
        findViewById(R.id.btn_traffic_info).setOnClickListener(this);
        findViewById(R.id.btn_mobile_assistant).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_custom_property) {
            Intent intent = new Intent(this, CustomPropertyActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_measure_text) {
            Intent intent = new Intent(this, MeasureTextActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_measure_layout) {
            Intent intent = new Intent(this, MeasureLayoutActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_on_measure1) {
            Intent intent = new Intent(this, OnMeasureActivity1.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_on_measure2) {
            Intent intent = new Intent(this, OnMeasureActivity2.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_on_measure3) {
            Intent intent = new Intent(this, OnMeasureActivity3.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_on_layout) {
            Intent intent = new Intent(this, OnLayoutActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_show_draw) {
            Intent intent = new Intent(this, ShowDrawActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_runnable) {
            Intent intent = new Intent(this, RunnableActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_pull_refresh) {
            Intent intent = new Intent(this, PullRefreshActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_circle_animation) {
            Intent intent = new Intent(this, CircleAnimationActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_circle_clock) {
            Intent intent = new Intent(this, CircleClockActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_ripple_anim) {
            Intent intent = new Intent(this, RippleAnimActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_round_imageview) {
            Intent intent = new Intent(this, RoundImageViewActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_nice_imageview) {
            Intent intent = new Intent(this, NiceImageViewActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_custom_textview) {
            Intent intent = new Intent(this, CustomTextViewActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_bezier_quad) {
            Intent intent = new Intent(this, BezierQuadActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_bezier_cubic) {
            Intent intent = new Intent(this, BezierCubicActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_bezier_custom) {
            Intent intent = new Intent(this, BezierCustomActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_bezier_ripple) {
            Intent intent = new Intent(this, BezierRippleActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_window) {
            Intent intent = new Intent(this, WindowActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_dialog_date) {
            Intent intent = new Intent(this, DialogDateActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_dialog_multi) {
            Intent intent = new Intent(this, DialogMultiActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_notify_simple) {
            Intent intent = new Intent(this, NotifySimpleActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_notify_counter) {
            Intent intent = new Intent(this, NotifyCounterActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_notify_progress) {
            Intent intent = new Intent(this, NotifyProgressActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_notify_custom) {
            Intent intent = new Intent(this, NotifyCustomActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_service_normal) {
            Intent intent = new Intent(this, ServiceNormalActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_bind_immediate) {
            Intent intent = new Intent(this, BindImmediateActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_bind_delay) {
            Intent intent = new Intent(this, BindDelayActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_notify_service) {
            Intent intent = new Intent(this, NotifyServiceActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_app_info) {
            Intent intent = new Intent(this, AppInfoActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_traffic_info) {
            Intent intent = new Intent(this, TrafficInfoActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_mobile_assistant) {
            Intent intent = new Intent(this, MobileAssistantActivity.class);
            startActivity(intent);
        }
    }

}
