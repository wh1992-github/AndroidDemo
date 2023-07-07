package com.example.hezi.viewpager2.horizontal;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.hezi.R;

public class HorizontalActivity extends AppCompatActivity {
    private static final String TAG = "HorizontalActivity";
    private static final int PAGE_COUNT = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_horizontal);
        final ViewPager2 viewPager2 = findViewById(R.id.vp_h);
        final HorizontalVpAdapter adapter = new HorizontalVpAdapter(this);
        viewPager2.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        viewPager2.setAdapter(adapter);
        viewPager2.setCurrentItem(2, false);

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                Log.i(TAG, "onPageScrolled: " + position);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                Log.i(TAG, "onBindViewHolder: onPageSelected: " + position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
                Log.i(TAG, "onPageScrollStateChanged: " + state + ", " + viewPager2.getCurrentItem());
                if (state == 0) {
                    if (viewPager2.getCurrentItem() == 0) {
                        viewPager2.setCurrentItem(PAGE_COUNT, false);
                        adapter.notifyDataSetChanged();
                    } else if (viewPager2.getCurrentItem() == PAGE_COUNT * 2 - 1) {
                        viewPager2.setCurrentItem(PAGE_COUNT - 1, false);
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }
}
