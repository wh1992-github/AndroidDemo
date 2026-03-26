package com.example.customview.activity;

import android.animation.ValueAnimator;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextSwitcher;
import android.widget.TextView;

import com.example.customview.R;
import com.example.customview.databinding.ActivityTextSwitcherBinding;
import com.example.customview.utils.ShaderSpan;

import java.util.ArrayList;
import java.util.List;

public class TextSwitcherActivity extends AppCompatActivity {
    private static final String TAG = "TextSwitcherActivity";

    private ActivityTextSwitcherBinding binding;
    private TextSwitcher mTextSwitcher;
    private int mCount = 0;
    private static final List<String> mList = new ArrayList<>();

    static {
        mList.add("你好丰田呢");
        mList.add("你有啥事呢你说吧");
        mList.add("你好雷克萨斯你在干嘛");
        mList.add("有啥事就说吧");
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        binding = ActivityTextSwitcherBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mTextSwitcher = binding.switcher;

        //设置显示Text文本到View创建工厂
        mTextSwitcher.setFactory(() -> {
            TextView textView = new TextView(TextSwitcherActivity.this);
            textView.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
            textView.setTextSize(20);
            textView.setLayoutParams(new TextSwitcher.LayoutParams(TextSwitcher.LayoutParams.WRAP_CONTENT, TextSwitcher.LayoutParams.WRAP_CONTENT));
            TextSwitcher.LayoutParams layoutParams = (TextSwitcher.LayoutParams) textView.getLayoutParams();
            layoutParams.width = TextSwitcher.LayoutParams.MATCH_PARENT;
            layoutParams.height = 100;
            layoutParams.gravity = Gravity.CENTER;
            textView.setLayoutParams(layoutParams);
            return textView;
        });

        Animation in = AnimationUtils.loadAnimation(this, R.anim.text_in);
        Animation out = AnimationUtils.loadAnimation(this, R.anim.text_out);
        mTextSwitcher.setInAnimation(in);//设置文本出现动画
        mTextSwitcher.setOutAnimation(out);//设置文本消失动画
        mTextSwitcher.setCurrentText(mList.get(0));//设置初始值，初始值不显示动画

        binding.button1.setOnClickListener(v -> {
            mCount++;
            if (mCount >= mList.size()) {
                mCount = 0;
            }
            //更新文本显示值，会出现动画
            mTextSwitcher.setCurrentText(mList.get(mCount));
            //设置渐变色
            int[] colors = new int[]{Color.RED, Color.BLUE, Color.RED, Color.BLUE, Color.RED};
            float[] positions = new float[]{0.1f, 0.3f, 0.5f, 0.7f, 0.9f};
            TextView textView = (TextView) mTextSwitcher.getCurrentView();
            Paint.FontMetrics fm = textView.getPaint().getFontMetrics();
            float width = textView.getPaint().measureText(mList.get(mCount));
            LinearGradient linearGradient = new LinearGradient((textView.getMeasuredWidth() - width) / 2, 0, (textView.getMeasuredWidth() - width) / 2 + width, 0, colors, positions, Shader.TileMode.CLAMP);
            SpannableString spannableString = new SpannableString(mList.get(mCount));
            spannableString.setSpan(new ShaderSpan(linearGradient), 0, textView.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            textView.setText(spannableString);
        });

        binding.button2.setOnClickListener(v -> {
            mCount++;
            if (mCount >= mList.size()) {
                mCount = 0;
            }
            //更新文本显示值，会出现动画
            mTextSwitcher.setText(mList.get(mCount));
            //设置渐变色
            int[] colors = new int[]{Color.RED, Color.BLUE, Color.RED, Color.BLUE, Color.RED};
            float[] positions = new float[]{0.1f, 0.3f, 0.5f, 0.7f, 0.9f};
            TextView textView = (TextView) mTextSwitcher.getCurrentView();
            Paint.FontMetrics fm = textView.getPaint().getFontMetrics();
            float width = textView.getPaint().measureText(mList.get(mCount));
            LinearGradient linearGradient = new LinearGradient((textView.getMeasuredWidth() - width) / 2, 0, (textView.getMeasuredWidth() - width) / 2 + width, 0, colors, positions, Shader.TileMode.CLAMP);
            SpannableString spannableString = new SpannableString(mList.get(mCount));
            spannableString.setSpan(new ShaderSpan(linearGradient), 0, textView.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            textView.setText(spannableString);
        });

        binding.button3.setOnClickListener(v -> {
            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) mTextSwitcher.getLayoutParams();
            int originalPosition = layoutParams.topMargin <= 200 ? 400 : layoutParams.topMargin;
            Log.i(TAG, "onCreate: " + originalPosition);
            ValueAnimator valueAnimator = ValueAnimator.ofInt(0, 400).setDuration(1000);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(@NonNull ValueAnimator animation) {
                    int value = (int) animation.getAnimatedValue();
                    layoutParams.topMargin = originalPosition - value;
                    Log.i(TAG, "onCreate: " + layoutParams.topMargin);
                    mTextSwitcher.setLayoutParams(layoutParams);
                }
            });
            valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
            valueAnimator.start();
        });

        binding.button4.setOnClickListener(v -> {
            binding.textSwitcherView.addText("思考中");
            binding.textSwitcherView.addText("搜索中 111");
            binding.textSwitcherView.addText("搜索中 222");
            binding.textSwitcherView.addText("搜索中 333");
            binding.textSwitcherView.addText("整理中 111");
            binding.textSwitcherView.addText("整理中 222");
            binding.textSwitcherView.addText("整理中 333");
            binding.textSwitcherView.addText("整理完成");
        });
    }
}
