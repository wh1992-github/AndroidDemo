package com.example.group.loop;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.group.R;


public class LoopAdapter extends PagerAdapter {

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

    public LoopAdapter(Activity activity) {
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
        View view = LayoutInflater.from(mActivity.getApplicationContext()).inflate(R.layout.recommend_page_item, container, false);
        ImageView imageView = view.findViewById(R.id.image);
        TextView textView = view.findViewById(R.id.image_desc);
        textView.setText(TextIds[position]);
        imageView.setImageResource(ResIds[position]);
        container.addView(view);
        return view;
    }
}
