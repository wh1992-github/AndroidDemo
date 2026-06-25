package com.example.group.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import androidx.appcompat.app.AppCompatActivity;

import com.example.group.R;
import com.example.group.databinding.ActivityMainBinding;
import com.example.group.rxbus.RxBusActivity;

/**
 * Created by test on 2017/10/21.
 */
public class MainActivity extends AppCompatActivity implements OnClickListener {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnTabButton.setOnClickListener(this);
        binding.btnTabHost.setOnClickListener(this);
        binding.btnTabGroup.setOnClickListener(this);
        binding.btnTabFragment.setOnClickListener(this);
        binding.btnToolbar.setOnClickListener(this);
        binding.btnSeekbar.setOnClickListener(this);
        binding.btnClickEffect.setOnClickListener(this);
        binding.btnTouchEvent.setOnClickListener(this);
        binding.btnWindowDialog.setOnClickListener(this);
        binding.btnLocalBroadcast.setOnClickListener(this);
        binding.btnHandler.setOnClickListener(this);
        binding.btnToast.setOnClickListener(this);
        binding.btnToolbarCustom.setOnClickListener(this);
        binding.btnOverflowMenu.setOnClickListener(this);
        binding.btnSearchView.setOnClickListener(this);
        binding.btnTabLayout.setOnClickListener(this);
        binding.btnTabCustom.setOnClickListener(this);
        binding.btnBannerIndicator.setOnClickListener(this);
        binding.btnBannerPager.setOnClickListener(this);
        binding.btnBannerTop.setOnClickListener(this);
        binding.btnRecyclerLinear.setOnClickListener(this);
        binding.btnRecyclerGrid.setOnClickListener(this);
        binding.btnRecyclerCombine.setOnClickListener(this);
        binding.btnRecyclerStaggered.setOnClickListener(this);
        binding.btnRecyclerDynamic.setOnClickListener(this);
        binding.btnRecyclerDrag.setOnClickListener(this);
        binding.btnViewDrag1.setOnClickListener(this);
        binding.btnViewDrag2.setOnClickListener(this);
        binding.btnCoordinator.setOnClickListener(this);
        binding.btnAppbarRecycler.setOnClickListener(this);
        binding.btnAppbarNested.setOnClickListener(this);
        binding.btnCollapsePin.setOnClickListener(this);
        binding.btnCollapseParallax.setOnClickListener(this);
        binding.btnImageFade.setOnClickListener(this);
        binding.btnScrollFlag.setOnClickListener(this);
        binding.btnScrollAlipay.setOnClickListener(this);
        binding.btnSwipeRefresh.setOnClickListener(this);
        binding.btnSwipeRecycler.setOnClickListener(this);
        binding.btnDepartmentStore.setOnClickListener(this);
        binding.btnLiveData.setOnClickListener(this);
        binding.btnArouter.setOnClickListener(this);
        binding.btnGlide.setOnClickListener(this);
        binding.btnTimber.setOnClickListener(this);
        binding.btnOrientation.setOnClickListener(this);
        binding.btnBigPicture.setOnClickListener(this);
        binding.btnMmkv.setOnClickListener(this);
        binding.btnLevelList.setOnClickListener(this);
        binding.btnRxjava.setOnClickListener(this);
        binding.btnLog4j.setOnClickListener(this);
        binding.btnRxBus.setOnClickListener(this);
        binding.btnViewPager.setOnClickListener(this);
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
            Intent intent = new Intent(this, ProgressBarSeekBarActivity.class);
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
        } else if (v.getId() == R.id.btn_view_drag1) {
            Intent intent = new Intent(this, ViewDragActivity1.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_view_drag2) {
            Intent intent = new Intent(this, ViewDragActivity2.class);
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
        } else if (v.getId() == R.id.btn_level_list) {
            Intent intent = new Intent(this, LevelListActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_rxjava) {
            Intent intent = new Intent(this, RxJavaRetrofitOkHttpActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_log4j) {
            Intent intent = new Intent(this, Log4jActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_rx_bus) {
            Intent intent = new Intent(this, RxBusActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_view_pager) {
            Intent intent = new Intent(this, Log4jActivity.class);
            startActivity(intent);
        }
    }

}
