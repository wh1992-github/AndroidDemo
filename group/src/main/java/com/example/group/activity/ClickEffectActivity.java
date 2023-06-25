package com.example.group.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.example.group.R;

public class ClickEffectActivity extends AppCompatActivity {
    private static final String TAG = "ClickEffectActivity";
    private TextView mTextView1, mTextView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_click_effect);
        mTextView1 = findViewById(R.id.textview1);
        mTextView2 = findViewById(R.id.textview2);
        setClickEffect();
    }

    private void setClickEffect() {
        Log.i(TAG, "setClickEffect: ");
        mTextView1.setClickable(true);
        mTextView1.setBackgroundResource(R.drawable.click_effect_bg);
        mTextView1.setTextColor(getColorStateList(R.color.click_effect_color));

        mTextView2.setClickable(true);
        mTextView2.setBackgroundResource(R.drawable.click_effect_bg);
        mTextView2.setTextColor(getColorStateList(R.color.click_effect_color));
    }
}
