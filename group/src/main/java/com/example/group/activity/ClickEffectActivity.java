package com.example.group.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.group.R;
import com.example.group.databinding.ActivityClickEffectBinding;
/**
 * 用于展示 Click Effect 功能的 Activity。
 */

public class ClickEffectActivity extends AppCompatActivity {
    private ActivityClickEffectBinding binding;
    private static final String TAG = "ClickEffectActivity";
    private TextView mTextView1, mTextView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityClickEffectBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mTextView1 = binding.textview1;
        mTextView2 = binding.textview2;
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
