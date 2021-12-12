package com.example.customview.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.customview.R;
import com.example.customview.bean.HoverItemModel;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class HoverItemAdapter3 extends RecyclerView.Adapter<HoverItemAdapter3.MyViewHolder> {
    public static final int HAS_STICKY_VIEW = 1;
    public static final int NONE_STICKY_VIEW = 2;

    private final Context mContext;
    private final List<HoverItemModel> mList;

    public HoverItemAdapter3(Context mContext, List<HoverItemModel> list) {
        this.mContext = mContext;
        this.mList = list;
    }

    @NotNull
    @Override
    public MyViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_list_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder viewHolder, int position) {
        HoverItemModel hoverItemModel = mList.get(position);
        viewHolder.tvName.setText(hoverItemModel.name);

        if (position == 0 || !hoverItemModel.sticky.equals(mList.get(position - 1).sticky)) {
            viewHolder.tvHeader.setVisibility(View.VISIBLE);
            viewHolder.tvHeader.setText(hoverItemModel.sticky);
            viewHolder.itemView.setTag(HAS_STICKY_VIEW);
        } else {
            viewHolder.tvHeader.setVisibility(View.GONE);
            viewHolder.itemView.setTag(NONE_STICKY_VIEW);
        }
        viewHolder.itemView.setContentDescription(hoverItemModel.sticky);
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvHeader;
        public TextView tvName;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvHeader = (TextView) itemView.findViewById(R.id.header_view);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
        }
    }
}
