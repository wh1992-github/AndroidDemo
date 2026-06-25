package com.example.group.loop;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.viewpager.widget.PagerAdapter;

import com.example.group.R;
import com.example.group.databinding.RecommendPageItemBinding;
/**
 * 用于适配 Banner Pager 数据的适配器。
 */

public class BannerPagerAdapter extends PagerAdapter {

    private int mSize;
    private Activity mActivity;
    private int[] ResIds = new int[]{
            R.drawable.a,
            R.drawable.b,
            R.drawable.c,
            R.drawable.d,
            R.drawable.e,
    };

    private int[] TextIds = new int[]{
            R.string.a_name,
            R.string.b_name,
            R.string.c_name,
            R.string.d_name,
            R.string.e_name,
    };

    public BannerPagerAdapter(Activity activity) {
        mActivity = activity;
        mSize = 5;
    }

    @Override
    public int getCount() {
        return mSize;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup view, int position, Object object) {
        view.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        RecommendPageItemBinding binding = RecommendPageItemBinding.inflate(
                LayoutInflater.from(mActivity.getApplicationContext()), container, false);
        View view = binding.getRoot();
        ImageView imageView = binding.image;
        TextView textView = binding.imageDesc;
        textView.setText(TextIds[position]);
        imageView.setImageResource(ResIds[position]);
        container.addView(view);
        return view;
    }
}
