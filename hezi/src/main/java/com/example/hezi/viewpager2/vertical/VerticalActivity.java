package com.example.hezi.viewpager2.vertical;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.hezi.R;

public class VerticalActivity extends AppCompatActivity {
    private static final String TAG = "VerticalActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vertical);
        final ViewPager2 viewPager2 = findViewById(R.id.vp_v);
        VerticalVpAdapter adapter = new VerticalVpAdapter(this);
        viewPager2.setOrientation(ViewPager2.ORIENTATION_VERTICAL);
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
                Log.i(TAG, "onPageSelected: " + position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
                Log.i(TAG, "onPageScrollStateChanged: " + state + ", " + viewPager2.getCurrentItem());
                if (state == 0) {
                    if (viewPager2.getCurrentItem() == 0) {
                        viewPager2.setCurrentItem(5, false);
                    } else if (viewPager2.getCurrentItem() == 9) {
                        viewPager2.setCurrentItem(4, false);
                    }
                }
            }
        });
    }
}
