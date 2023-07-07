package com.example.hezi.viewpager2.withTab;

import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.hezi.R;
import com.google.android.material.tabs.TabLayout;

public class TabActivity extends AppCompatActivity {

    private ViewPager2 mViewPager2;
    private TabLayout mTabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);

        TabAdapter adapter = new TabAdapter(this);
        mViewPager2 = findViewById(R.id.vp_tb);
        mTabLayout = findViewById(R.id.tb_vp);
        mViewPager2.setAdapter(adapter);
        mTabLayout.setTabTextColors(Color.parseColor("#111111"), Color.parseColor("#0371DD"));

        adapter.addColor(android.R.color.darker_gray);
        adapter.addColor(android.R.color.holo_red_dark);
        adapter.addColor(android.R.color.holo_green_dark);
        adapter.addColor(android.R.color.holo_blue_dark);
        adapter.addColor(android.R.color.holo_purple);
        adapter.addColor(android.R.color.holo_orange_dark);

        mTabLayout.addTab(mTabLayout.newTab().setText("第一个界面"));
        mTabLayout.addTab(mTabLayout.newTab().setText("第二个界面"));
        mTabLayout.addTab(mTabLayout.newTab().setText("第三个界面"));
        mTabLayout.addTab(mTabLayout.newTab().setText("第四个界面"));
        mTabLayout.addTab(mTabLayout.newTab().setText("第五个界面"));
        mTabLayout.addTab(mTabLayout.newTab().setText("第六个界面"));

        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        mViewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                int currentPosition = mTabLayout.getSelectedTabPosition();
                TabLayout.Tab tab = mTabLayout.getTabAt(position);
                if (currentPosition != position && tab != null) {
                    tab.select();
                }
            }
        });
    }
}
