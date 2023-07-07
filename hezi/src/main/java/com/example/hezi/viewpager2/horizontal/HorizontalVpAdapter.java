package com.example.hezi.viewpager2.horizontal;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hezi.R;

import java.util.ArrayList;
import java.util.List;

public class HorizontalVpAdapter extends RecyclerView.Adapter<HorizontalVpAdapter.HorizontalVpViewHolder> {
    private static final String TAG = "HorizontalActivity";
    private final Context mContext;
    private final List<Integer> backgrounds = new ArrayList<>();
    private final TextView mTextView1, mTextView2, mTextView3, mTextView4, mTextView5;

    public HorizontalVpAdapter(Context context) {
        mContext = context;
        backgrounds.clear();
        backgrounds.add(android.R.color.holo_blue_bright);
        backgrounds.add(android.R.color.holo_red_dark);
        backgrounds.add(android.R.color.holo_green_dark);
        backgrounds.add(android.R.color.holo_orange_light);
        backgrounds.add(android.R.color.holo_purple);

        mTextView1 = new TextView(mContext);
        mTextView1.setTextSize(20);
        mTextView1.setTextColor(Color.parseColor("#ffffff"));
        mTextView1.setText("第 1 界面");
        mTextView1.setBackgroundColor(Color.parseColor("#000000"));

        mTextView2 = new TextView(mContext);
        mTextView2.setTextSize(20);
        mTextView2.setTextColor(Color.parseColor("#ffffff"));
        mTextView2.setText("第 2 界面");
        mTextView2.setBackgroundColor(Color.parseColor("#000000"));

        mTextView3 = new TextView(mContext);
        mTextView3.setTextSize(20);
        mTextView3.setTextColor(Color.parseColor("#ffffff"));
        mTextView3.setText("第 3 界面");
        mTextView3.setBackgroundColor(Color.parseColor("#000000"));

        mTextView4 = new TextView(mContext);
        mTextView4.setTextSize(20);
        mTextView4.setTextColor(Color.parseColor("#ffffff"));
        mTextView4.setText("第 4 界面");
        mTextView4.setBackgroundColor(Color.parseColor("#000000"));

        mTextView5 = new TextView(mContext);
        mTextView5.setTextSize(20);
        mTextView5.setTextColor(Color.parseColor("#ffffff"));
        mTextView5.setText("第 5 界面");
        mTextView5.setBackgroundColor(Color.parseColor("#000000"));
    }

    @NonNull
    @Override
    public HorizontalVpViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HorizontalVpViewHolder(LayoutInflater.from(mContext).inflate((R.layout.item_h_v), parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull HorizontalVpViewHolder holder, int position) {
        Log.i(TAG, "onBindViewHolder: position = " + position + ", " + (mTextView1.getParent() == null));
        if (position % backgrounds.size() == 0) {
            removeFromParent(mTextView1);
            holder.mLinearLayout.removeAllViews();
            holder.mLinearLayout.addView(mTextView1);
        } else if (position % backgrounds.size() == 1) {
            removeFromParent(mTextView2);
            holder.mLinearLayout.removeAllViews();
            holder.mLinearLayout.addView(mTextView2);
        } else if (position % backgrounds.size() == 2) {
            removeFromParent(mTextView3);
            holder.mLinearLayout.removeAllViews();
            holder.mLinearLayout.addView(mTextView3);
        } else if (position % backgrounds.size() == 3) {
            removeFromParent(mTextView4);
            holder.mLinearLayout.removeAllViews();
            holder.mLinearLayout.addView(mTextView4);
        } else if (position % backgrounds.size() == 4) {
            removeFromParent(mTextView5);
            holder.mLinearLayout.removeAllViews();
            holder.mLinearLayout.addView(mTextView5);
        }

        holder.mTextView.setText("第  " + (position % backgrounds.size() + 1) + " 界面");
        holder.mLinearLayout.setBackgroundResource(backgrounds.get(position % backgrounds.size()));
    }

    @Override
    public int getItemCount() {
        return backgrounds.size() * 2;
    }

    public void removeFromParent(TextView textView) {
        ViewGroup viewGroup1 = (ViewGroup) textView.getParent();
        if (viewGroup1 != null) {
            viewGroup1.removeAllViews();
        }
    }

    static class HorizontalVpViewHolder extends RecyclerView.ViewHolder {
        LinearLayout mLinearLayout;
        TextView mTextView;

        HorizontalVpViewHolder(@NonNull View itemView) {
            super(itemView);
            mLinearLayout = itemView.findViewById(R.id.ll_h_v);
            mTextView = itemView.findViewById(R.id.tv_hv);
        }
    }
}
