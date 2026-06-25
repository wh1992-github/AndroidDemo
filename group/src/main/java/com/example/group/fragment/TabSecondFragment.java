package com.example.group.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.group.databinding.FragmentTabSecondBinding;

import java.util.Locale;
/**
 * 用于承载 Tab Second 内容的 Fragment。
 */

public class TabSecondFragment extends Fragment {
    private static final String TAG = "TabSecondFragment";
    protected View mView; //声明一个视图对象
    protected Context mContext; //声明一个上下文对象

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity(); //获取活动页面的上下文
        //根据布局文件fragment_tab_second.xml生成视图对象
        FragmentTabSecondBinding binding = FragmentTabSecondBinding.inflate(inflater, container, false);
        mView = binding.getRoot();
        //根据碎片标签栏传来的参数拼接文本字符串
        String desc = String.format(Locale.getDefault(), "我是%s页面,来自%s",
                "分类", getArguments().getString("tag"));
        TextView tv_second = binding.tvSecond;
        tv_second.setText(desc);

        return mView;
    }

}
