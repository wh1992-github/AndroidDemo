package com.example.group.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.group.R;

import java.util.Collections;
import java.util.List;

public class DragAdapter extends RecyclerView.Adapter<DragAdapter.DragViewHolder> {
    private static final String TAG = "DragAdapter";
    private List<String> mList;

    public DragAdapter(List<String> mList) {
        this.mList = mList;
    }

    @Override
    public DragViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.drag_item, parent, false);
        return new DragViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DragViewHolder holder, int position) {
        holder.textView.setText(mList.get(position));
        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick: position = " + position + ", " + holder.textView.getText().toString());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void onItemMove(int fromPosition, int toPosition) {
        //拖拽后,切换位置,数据排序
        Collections.swap(mList, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
    }

    public void onItemDismiss(int position) {
        //移除之前的数据
        mList.remove(position);
        notifyItemRemoved(position);
    }

    public static class DragViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public DragViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.txt_item);
        }
    }
}
