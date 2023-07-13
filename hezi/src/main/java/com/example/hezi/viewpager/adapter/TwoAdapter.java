package com.example.hezi.viewpager.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.viewpager.widget.PagerAdapter;

import com.example.hezi.databinding.VpItemBinding;

import java.util.List;

public class TwoAdapter extends PagerAdapter {
    private List<Integer> list;
    private LayoutInflater inflater;

    public TwoAdapter(Context context, List<Integer> list) {
        this.list = list;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        VpItemBinding binding = VpItemBinding.inflate(inflater, container, false);
        binding.iv.setImageResource(list.get(position));
        binding.iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
        container.addView(binding.getRoot());
        return binding.getRoot();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
