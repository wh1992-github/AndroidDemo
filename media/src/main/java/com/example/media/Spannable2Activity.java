package com.example.media;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by test on 2017/12/4.
 */
public class Spannable2Activity extends AppCompatActivity {
    //Spannable. SPAN_INCLUSIVE_EXCLUSIVE：前面包括，后面不包括，即在文本前插入新的文本会应用该样式，而在文本后插入新文本不会应用该样式
    //Spannable. SPAN_INCLUSIVE_INCLUSIVE：前面包括，后面包括
    //Spannable. SPAN_EXCLUSIVE_EXCLUSIVE：前面不包括，后面不包括
    //Spannable. SPAN_EXCLUSIVE_INCLUSIVE：前面不包括，后面包括

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spannable2);
        mode1();
        mode2();
        mode3();
        mode4();
        mode5();
        mode6();
        mode7();
        mode8();
        mode9();
        mode10();
    }

    /**
     * 使用SpannableString设置样式——字体颜色
     */
    private void mode1() {
        SpannableString spannableString = new SpannableString("暗影追踪已经开始暴走了");
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#009ad6"));
        spannableString.setSpan(colorSpan, 0, 8, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        ((TextView) findViewById(R.id.mode1)).setText(spannableString);
    }

    /**
     * 使用SpannableStringBuilder设置样式——字体颜色
     */
    private void mode2() {
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
        spannableStringBuilder.append("暗影追踪已经开始暴走了");
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#009ad6"));
        spannableStringBuilder.setSpan(colorSpan, 0, 8, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        ((TextView) findViewById(R.id.mode2)).setText(spannableStringBuilder);
    }

    /**
     * 使用SpannableStringBuilder设置样式——背景颜色
     */
    private void mode3() {
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
        spannableStringBuilder.append("暗影追踪已经开始暴走了");
        BackgroundColorSpan bgColorSpan = new BackgroundColorSpan(Color.parseColor("#009ad6"));
        spannableStringBuilder.setSpan(bgColorSpan, 0, 8, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        ((TextView) findViewById(R.id.mode3)).setText(spannableStringBuilder);
    }

    /**
     * 使用SpannableStringBuilder设置样式——字体大小
     */
    private void mode4() {
        SpannableStringBuilder spannableString = new SpannableStringBuilder();
        spannableString.append("暗影追踪已经开始暴走了");
        AbsoluteSizeSpan absoluteSizeSpan = new AbsoluteSizeSpan(32);
        spannableString.setSpan(absoluteSizeSpan, 0, 8, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        ((TextView) findViewById(R.id.mode4)).setText(spannableString);
    }

    /**
     * 使用SpannableStringBuilder设置样式——粗体\斜体
     */
    private void mode5() {
        SpannableStringBuilder spannableString = new SpannableStringBuilder();
        spannableString.append("暗影追踪已经开始暴走了");
        //setSpan可多次使用
        StyleSpan styleSpan = new StyleSpan(Typeface.BOLD);//粗体
        spannableString.setSpan(styleSpan, 0, 3, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        StyleSpan styleSpan2 = new StyleSpan(Typeface.ITALIC);//斜体
        spannableString.setSpan(styleSpan2, 3, 6, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        StyleSpan styleSpan3 = new StyleSpan(Typeface.BOLD_ITALIC);//粗斜体
        spannableString.setSpan(styleSpan3, 6, 9, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        ((TextView) findViewById(R.id.mode5)).setText(spannableString);
    }

    /**
     * 使用SpannableStringBuilder设置样式——删除线
     */
    private void mode6() {
        SpannableStringBuilder spannableString = new SpannableStringBuilder();
        spannableString.append("暗影追踪已经开始暴走了");
        StrikethroughSpan strikethroughSpan = new StrikethroughSpan();
        spannableString.setSpan(strikethroughSpan, 0, 8, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        ((TextView) findViewById(R.id.mode6)).setText(spannableString);
    }

    /**
     * 使用SpannableStringBuilder设置样式——下划线
     */
    private void mode7() {
        SpannableStringBuilder spannableString = new SpannableStringBuilder();
        spannableString.append("暗影追踪已经开始暴走了");
        UnderlineSpan underlineSpan = new UnderlineSpan();
        spannableString.setSpan(underlineSpan, 0, 8, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        ((TextView) findViewById(R.id.mode7)).setText(spannableString);
    }

    /**
     * 使用SpannableStringBuilder设置样式——图片
     */
    private void mode8() {
        SpannableStringBuilder spannableString = new SpannableStringBuilder();
        spannableString.append("暗影追踪已经开始暴走了");
        ImageSpan imageSpan = new ImageSpan(this, R.mipmap.ic_launcher);
        //也可以这样
        //Drawable drawable = getResources().getDrawable(R.mipmap.ic_launcher);
        //drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        //ImageSpan imageSpan1 = new ImageSpan(drawable);
        //将index为6、7的字符用图片替代
        spannableString.setSpan(imageSpan, 6, 8, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        ((TextView) findViewById(R.id.mode8)).setText(spannableString);
    }

    /**
     * 使用SpannableStringBuilder设置点击事件
     */
    private void mode9() {
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Spannable2Activity.this, "点击事件", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(Color.WHITE); //设置文字颜色
                ds.setUnderlineText(false); //设置下划线
            }
        };
        SpannableStringBuilder spannableString = new SpannableStringBuilder();
        spannableString.append("暗影追踪已经开始暴走了");
        spannableString.setSpan(clickableSpan, 4, 8, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        //文字颜色
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#0000ff"));
        spannableString.setSpan(colorSpan, 4, 8, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        TextView textView = (TextView) findViewById(R.id.mode9);
        textView.setHighlightColor(getResources().getColor(android.R.color.transparent));
        textView.setText(spannableString);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    /**
     * 使用SpannableStringBuilder事件组合使用
     */
    private void mode10() {
        SpannableStringBuilder spannableString = new SpannableStringBuilder();
        spannableString.append("暗影追踪已经开始暴走了");
        //图片
        ImageSpan imageSpan = new ImageSpan(this, R.mipmap.ic_launcher);
        spannableString.setSpan(imageSpan, 2, 4, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        //点击事件
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Spannable2Activity.this, "请不要点我", Toast.LENGTH_SHORT).show();
            }
        };
        spannableString.setSpan(clickableSpan, 2, 4, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        //文字颜色
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#FFFFFF"));
        spannableString.setSpan(colorSpan, 5, 8, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        //文字背景颜色
        BackgroundColorSpan bgColorSpan = new BackgroundColorSpan(Color.parseColor("#009ad6"));
        spannableString.setSpan(bgColorSpan, 5, 8, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        TextView textView = (TextView) findViewById(R.id.mode10);
        textView.setText(spannableString);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
    }

}
