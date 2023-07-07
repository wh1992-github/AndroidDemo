package com.example.hezi.viewpager2.vertical;

import android.annotation.SuppressLint;
import android.content.Context;
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

public class VerticalVpAdapter extends RecyclerView.Adapter<VerticalVpAdapter.VerticalVpViewHolder> {
    private List<Integer> backgrounds;
    private Context mContext;

    public VerticalVpAdapter(Context context) {
        mContext = context;
        if (backgrounds == null) {
            backgrounds = new ArrayList<>();
            backgrounds.add(android.R.color.holo_blue_bright);
            backgrounds.add(android.R.color.holo_red_dark);
            backgrounds.add(android.R.color.holo_green_dark);
            backgrounds.add(android.R.color.holo_orange_light);
            backgrounds.add(android.R.color.holo_purple);
        }
    }

    @NonNull
    @Override
    public VerticalVpViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VerticalVpViewHolder(LayoutInflater.from(mContext).inflate((R.layout.item_h_v), parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull VerticalVpViewHolder holder, int position) {
        holder.mTextView.setText("第  " + (position % backgrounds.size() + 1) + " 界面");
        holder.mLinearLayout.setBackgroundResource(backgrounds.get(position % backgrounds.size()));
    }

    @Override
    public int getItemCount() {
        if (backgrounds == null) {
            return 0;
        }
        return backgrounds.size() * 2;
    }

    static class VerticalVpViewHolder extends RecyclerView.ViewHolder {
        LinearLayout mLinearLayout;
        TextView mTextView;

        VerticalVpViewHolder(@NonNull View itemView) {
            super(itemView);
            mLinearLayout = itemView.findViewById(R.id.ll_h_v);
            mTextView = itemView.findViewById(R.id.tv_hv);
        }
    }
}
