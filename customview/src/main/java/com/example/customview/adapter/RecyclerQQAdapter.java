package com.example.customview.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.customview.R;
import com.example.customview.widget.RecyclerQQItemView;

import java.util.List;

public class RecyclerQQAdapter extends RecyclerView.Adapter<RecyclerQQAdapter.MyViewHolder> implements RecyclerQQItemView.onSlidingButtonListener {
    private static final String TAG = "RecyclerViewAdapter";
    private Context mContext;
    private List<Bitmap> mDataImage;    //头像（谁的头像）
    private List<String> mDataTitle;     //标题（谁的消息）
    private List<String> mDataContent;  //内容（消息内容）

    private onSlidingViewClickListener onSvcl;

    private RecyclerQQItemView recyclers;

    public RecyclerQQAdapter(Context context, List<Bitmap> dataImage, List<String> dataTitle, List<String> dataContent) {
        this.mContext = context;
        this.mDataImage = dataImage;
        this.mDataTitle = dataTitle;
        this.mDataContent = dataContent;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_recycler_qq, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.image.setImageBitmap(mDataImage.get(position));
        holder.title.setText(mDataTitle.get(position));
        holder.content.setText(mDataContent.get(position));
        holder.layout_left.getLayoutParams().width = getScreenWidth(mContext);

        holder.layout_left.setOnClickListener(view -> {
            //判断是否有删除菜单打开
            if (menuIsOpen()) {
                closeMenu();//关闭菜单
            } else {
                //获得布局下标（点的哪一个）
                int subscript = holder.getLayoutPosition();
                onSvcl.onItemClick(view, subscript);
            }
        });
        holder.other.setOnClickListener(view -> {
            //
            Toast.makeText(mContext, "其他：" + position, Toast.LENGTH_SHORT).show();
        });
        holder.delete.setOnClickListener(view -> {
            Toast.makeText(mContext, "删除了：" + position, Toast.LENGTH_SHORT).show();
            int subscript = holder.getLayoutPosition();
            onSvcl.onDeleteBtnClick(view, subscript);
        });
    }

    @Override
    public int getItemCount() {
        return mDataImage.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public TextView title;
        public TextView content;
        public TextView other;
        public TextView delete;
        public RelativeLayout layout_left;

        public MyViewHolder(View view) {
            super(view);
            image = view.findViewById(R.id.image);
            title = view.findViewById(R.id.title);
            content = view.findViewById(R.id.content);
            other = view.findViewById(R.id.other);
            delete = view.findViewById(R.id.delete);
            layout_left = view.findViewById(R.id.layout_left);

            ((RecyclerQQItemView) view).setSlidingButtonListener(RecyclerQQAdapter.this);
        }
    }

    //删除数据
    public void removeData(int position) {
        mDataImage.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public void onMenuIsOpen(View view) {
        recyclers = (RecyclerQQItemView) view;
    }

    @Override
    public void onDownOrMove(RecyclerQQItemView recycler) {
        if (menuIsOpen()) {
            if (recyclers != recycler) {
                closeMenu();
            }
        }
    }

    //关闭菜单
    public void closeMenu() {
        recyclers.closeMenu();
        recyclers = null;
    }

    //判断是否有菜单打开
    public Boolean menuIsOpen() {
        return (recyclers != null);
    }

    //设置在滑动侦听器上
    public void setOnSlidListener(onSlidingViewClickListener listener) {
        onSvcl = listener;
    }

    //在滑动视图上单击侦听器
    public interface onSlidingViewClickListener {
        void onItemClick(View view, int position);

        void onDeleteBtnClick(View view, int position);
    }

    //获取屏幕宽度
    public int getScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }
}
