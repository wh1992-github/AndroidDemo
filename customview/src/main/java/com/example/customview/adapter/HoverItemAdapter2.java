package com.example.customview.adapter;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.customview.R;

import java.util.List;

@SuppressLint("RecyclerView")
public class HoverItemAdapter2 extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "MainAdapter";
    private final int type_one = 1;
    private final int type_two = 2;
    private final int type_three = 3;
    private final List<String> mDatas;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public HoverItemAdapter2(List<String> mDatas) {
        this.mDatas = mDatas;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == type_one) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hover_item_one, parent, false);
            return new OneViewHolder(view);
        } else if (viewType == type_two) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hover_item_two, parent, false);
            return new TwoViewHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hover_item_three, parent, false);
            return new ThreeViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        switch (holder.getItemViewType()) {
            case type_one:
                OneViewHolder oneViewHolder = (OneViewHolder) holder;
                oneViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.i(TAG, "onItemClick: 点击了 " + position);
                    }
                });
                break;
            case type_two:
                TwoViewHolder twoViewHolder = (TwoViewHolder) holder;
                twoViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mOnItemClickListener.onItemClick(view, position);
                    }
                });
                break;
            case type_three:
                ThreeViewHolder threeViewHolder = (ThreeViewHolder) holder;
                threeViewHolder.tv_text.setText(mDatas.get(position));
                threeViewHolder.tv_text.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.i(TAG, "onItemClick: 点击了 " + position);
                    }
                });
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position < 3) {
            return type_one;
        } else if (position == 3) {
            return type_two;
        } else {
            return type_three;
        }
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    static class OneViewHolder extends RecyclerView.ViewHolder {
        public OneViewHolder(View itemView) {
            super(itemView);
        }
    }

    static class TwoViewHolder extends RecyclerView.ViewHolder {
        public TwoViewHolder(View itemView) {
            super(itemView);
        }
    }

    static class ThreeViewHolder extends RecyclerView.ViewHolder {
        TextView tv_text;

        public ThreeViewHolder(View itemView) {
            super(itemView);
            tv_text = itemView.findViewById(R.id.tv_text);
        }
    }
}
