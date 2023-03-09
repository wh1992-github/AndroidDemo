package com.example.group.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.group.bean.GoodsInfo;
import com.example.group.databinding.ItemGridBinding;
import com.example.group.util.BaseViewHolder;
import com.example.group.widget.RecyclerExtras.OnItemClickListener;
import com.example.group.widget.RecyclerExtras.OnItemLongClickListener;

import java.util.ArrayList;
import java.util.Locale;
/**
 * @author wh
 */
@SuppressLint("RecyclerView")
public class RecyclerGridAdapter extends RecyclerView.Adapter<BaseViewHolder<ItemGridBinding>> implements OnItemClickListener, OnItemLongClickListener {
    private static final String TAG = "RecyclerGridAdapter";
    private Context mContext; //声明一个上下文对象
    private ArrayList<GoodsInfo> mGoodsArray;

    public RecyclerGridAdapter(Context context, ArrayList<GoodsInfo> goodsArray) {
        mContext = context;
        mGoodsArray = goodsArray;
    }

    //获取列表项的个数
    public int getItemCount() {
        return mGoodsArray.size();
    }

    //创建列表项的视图持有者
    @Override
    public BaseViewHolder<ItemGridBinding> onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        //根据布局文件item_grid.xml生成视图对象
        return new BaseViewHolder<>(ItemGridBinding.inflate(LayoutInflater.from(mContext), viewGroup, false));
    }

    //绑定列表项的视图持有者
    public void onBindViewHolder(@NonNull BaseViewHolder<ItemGridBinding> holder, int position) {
        ItemGridBinding viewBinding = holder.getViewBinding();
        viewBinding.ivPic.setImageResource(mGoodsArray.get(position).pic_id);
        viewBinding.tvTitle.setText(mGoodsArray.get(position).title);
        //列表项的点击事件需要自己实现
        viewBinding.llItem.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(v, position);
                }
            }
        });
        //列表项的长按事件需要自己实现
        viewBinding.llItem.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mOnItemLongClickListener != null) {
                    mOnItemLongClickListener.onItemLongClick(v, position);
                }
                return true;
            }
        });
    }

    //获取列表项的类型
    public int getItemViewType(int position) {
        return 0;
    }

    //获取列表项的编号
    public long getItemId(int position) {
        return position;
    }

    //声明列表项的点击监听器对象
    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    //声明列表项的长按监听器对象
    private OnItemLongClickListener mOnItemLongClickListener;

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.mOnItemLongClickListener = listener;
    }

    //处理列表项的点击事件
    public void onItemClick(View view, int position) {
        String desc = String.format(Locale.getDefault(), "您点击了第%d项,栏目名称是%s", position + 1, mGoodsArray.get(position).title);
        Toast.makeText(mContext, desc, Toast.LENGTH_SHORT).show();
    }

    //处理列表项的长按事件
    public void onItemLongClick(View view, int position) {
        String desc = String.format(Locale.getDefault(), "您长按了第%d项,栏目名称是%s", position + 1, mGoodsArray.get(position).title);
        Toast.makeText(mContext, desc, Toast.LENGTH_SHORT).show();
    }

}
