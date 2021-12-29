package com.example.group;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;

/**
 * Created by test on 2017/10/21.
 */
public class MainActivity extends AppCompatActivity implements OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_tab_button).setOnClickListener(this);
        findViewById(R.id.btn_tab_host).setOnClickListener(this);
        findViewById(R.id.btn_tab_group).setOnClickListener(this);
        findViewById(R.id.btn_tab_fragment).setOnClickListener(this);
        findViewById(R.id.btn_toolbar).setOnClickListener(this);
        findViewById(R.id.btn_seekbar).setOnClickListener(this);
        findViewById(R.id.btn_click_effect).setOnClickListener(this);
        findViewById(R.id.btn_touch_event).setOnClickListener(this);
        findViewById(R.id.btn_window_dialog).setOnClickListener(this);
        findViewById(R.id.btn_local_broadcast).setOnClickListener(this);
        findViewById(R.id.btn_handler).setOnClickListener(this);
        findViewById(R.id.btn_toast).setOnClickListener(this);
        findViewById(R.id.btn_toolbar_custom).setOnClickListener(this);
        findViewById(R.id.btn_overflow_menu).setOnClickListener(this);
        findViewById(R.id.btn_search_view).setOnClickListener(this);
        findViewById(R.id.btn_tab_layout).setOnClickListener(this);
        findViewById(R.id.btn_tab_custom).setOnClickListener(this);
        findViewById(R.id.btn_banner_indicator).setOnClickListener(this);
        findViewById(R.id.btn_banner_pager).setOnClickListener(this);
        findViewById(R.id.btn_banner_top).setOnClickListener(this);
        findViewById(R.id.btn_recycler_linear).setOnClickListener(this);
        findViewById(R.id.btn_recycler_grid).setOnClickListener(this);
        findViewById(R.id.btn_recycler_combine).setOnClickListener(this);
        findViewById(R.id.btn_recycler_staggered).setOnClickListener(this);
        findViewById(R.id.btn_recycler_dynamic).setOnClickListener(this);
        findViewById(R.id.btn_recycler_drag).setOnClickListener(this);
        findViewById(R.id.btn_coordinator).setOnClickListener(this);
        findViewById(R.id.btn_appbar_recycler).setOnClickListener(this);
        findViewById(R.id.btn_appbar_nested).setOnClickListener(this);
        findViewById(R.id.btn_collapse_pin).setOnClickListener(this);
        findViewById(R.id.btn_collapse_parallax).setOnClickListener(this);
        findViewById(R.id.btn_image_fade).setOnClickListener(this);
        findViewById(R.id.btn_scroll_flag).setOnClickListener(this);
        findViewById(R.id.btn_scroll_alipay).setOnClickListener(this);
        findViewById(R.id.btn_swipe_refresh).setOnClickListener(this);
        findViewById(R.id.btn_swipe_recycler).setOnClickListener(this);
        findViewById(R.id.btn_department_store).setOnClickListener(this);
        findViewById(R.id.btn_live_data).setOnClickListener(this);
        findViewById(R.id.btn_arouter).setOnClickListener(this);
        findViewById(R.id.btn_glide).setOnClickListener(this);
        findViewById(R.id.btn_timber).setOnClickListener(this);
        findViewById(R.id.btn_orientation).setOnClickListener(this);
        findViewById(R.id.btn_big_picture).setOnClickListener(this);
        findViewById(R.id.btn_mmkv).setOnClickListener(this);
        findViewById(R.id.btn_rxjava).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_tab_button) {
            Intent intent = new Intent(this, TabButtonActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_tab_host) {
            Intent intent = new Intent(this, TabHostActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_tab_group) {
            Intent intent = new Intent(this, TabGroupActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_tab_fragment) {
            Intent intent = new Intent(this, TabFragmentActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_toolbar) {
            Intent intent = new Intent(this, ToolbarAndPopWindowActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_seekbar) {
            Intent intent = new Intent(this, ProgressBarActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_click_effect) {
            Intent intent = new Intent(this, ClickEffectActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_touch_event) {
            Intent intent = new Intent(this, TouchEventActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_window_dialog) {
            Intent intent = new Intent(this, WindowDialogActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_local_broadcast) {
            Intent intent = new Intent(this, LocalBroadcastActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_handler) {
            Intent intent = new Intent(this, HandlerActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_toast) {
            Intent intent = new Intent(this, ToastActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_overflow_menu) {
            Intent intent = new Intent(this, OverflowMenuActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_search_view) {
            Intent intent = new Intent(this, SearchViewActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_toolbar_custom) {
            Intent intent = new Intent(this, ToolbarCustomActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_tab_layout) {
            Intent intent = new Intent(this, TabLayoutActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_tab_custom) {
            Intent intent = new Intent(this, TabCustomActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_banner_indicator) {
            Intent intent = new Intent(this, BannerIndicatorActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_banner_pager) {
            Intent intent = new Intent(this, BannerPagerActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_banner_top) {
            Intent intent = new Intent(this, BannerTopActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_recycler_linear) {
            Intent intent = new Intent(this, RecyclerLinearActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_recycler_grid) {
            Intent intent = new Intent(this, RecyclerGridActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_recycler_combine) {
            Intent intent = new Intent(this, RecyclerCombineActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_recycler_staggered) {
            Intent intent = new Intent(this, RecyclerStaggeredActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_recycler_dynamic) {
            Intent intent = new Intent(this, RecyclerDynamicActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_recycler_drag) {
            Intent intent = new Intent(this, RecyclerDragActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_coordinator) {
            Intent intent = new Intent(this, CoordinatorActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_appbar_recycler) {
            Intent intent = new Intent(this, AppbarRecyclerActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_appbar_nested) {
            Intent intent = new Intent(this, AppbarNestedActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_collapse_pin) {
            Intent intent = new Intent(this, CollapsePinActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_collapse_parallax) {
            Intent intent = new Intent(this, CollapseParallaxActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_image_fade) {
            Intent intent = new Intent(this, ImageFadeActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_scroll_flag) {
            Intent intent = new Intent(this, ScrollFlagActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_scroll_alipay) {
            Intent intent = new Intent(this, ScrollAlipayActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_swipe_refresh) {
            Intent intent = new Intent(this, SwipeRefreshActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_swipe_recycler) {
            Intent intent = new Intent(this, SwipeRecyclerActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_department_store) {
            Intent intent = new Intent(this, DepartmentStoreActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_live_data) {
            Intent intent = new Intent(this, LiveDataActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_arouter) {
            Intent intent = new Intent(this, ARouterActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_glide) {
            Intent intent = new Intent(this, GlideActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_timber) {
            Intent intent = new Intent(this, TimberActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_orientation) {
            Intent intent = new Intent(this, OrientationActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_big_picture) {
            Intent intent = new Intent(this, BigPictureActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_mmkv) {
            Intent intent = new Intent(this, MMKVActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_rxjava) {
            Intent intent = new Intent(this, RxJavaRetrofitOkHttpActivity.class);
            startActivity(intent);
        }
    }

}
