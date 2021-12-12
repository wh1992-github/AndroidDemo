package com.example.customview.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.customview.R;
import com.example.customview.adapter.HoverItemAdapter2;

import java.util.ArrayList;
import java.util.List;

public class HoverItemActivity2 extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private HoverItemAdapter2 mAdapter;
    private RecyclerView mRecyclerView;
    private List<String> mDatas;
    private View mIncludeView;
    private String mTypeName;
    //目标项是否在最后一个可见项之后
    private boolean mShouldScroll;
    //记录目标项位置
    private int mToPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hover_item_2);
        mIncludeView = findViewById(R.id.include);
        mRecyclerView = findViewById(R.id.recyclerView);
        //添加数据
        addData();
        mAdapter = new HoverItemAdapter2(this, mDatas);
        mRecyclerView.setAdapter(mAdapter);
        //线性layout
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        //滑动监听
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //定位初始点坐标
                View stickyInfoView = recyclerView.findChildViewUnder(recyclerView.getMeasuredWidth() >> 1, 1);
                if (stickyInfoView != null) {
                    TextView typeName = stickyInfoView.findViewById(R.id.typeName);
                    if (typeName != null) {
                        mTypeName = typeName.getText().toString();
                    }
                    Log.i(TAG, "onScrolled: mTypeName = " + mTypeName);
                    if (mTypeName.equals("第一项")) {
                        mIncludeView.setVisibility(View.GONE);
                    } else if (mTypeName.equals("第二项")) {
                        mIncludeView.setVisibility(View.VISIBLE);
                    } else if (mTypeName.equals("第三项")) {
                        mIncludeView.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (mShouldScroll && RecyclerView.SCROLL_STATE_IDLE == newState) {
                    mShouldScroll = false;
                    smoothMoveToPosition(recyclerView, mToPosition);
                }
            }
        });

        mAdapter.setOnItemClickListener(new HoverItemAdapter2.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (position != -1) {
                    smoothMoveToPosition(mRecyclerView, 20);
                }
            }
        });
    }

    //滑动到指定位置,可以保证滑动后的item位于最上面
    private void smoothMoveToPosition(RecyclerView mRecyclerView, int position) {
        //第一个可见位置
        int firstItem = mRecyclerView.getChildLayoutPosition(mRecyclerView.getChildAt(0));
        //最后一个可见位置
        int lastItem = mRecyclerView.getChildLayoutPosition(mRecyclerView.getChildAt(mRecyclerView.getChildCount() - 1));
        Log.i(TAG, "smoothMoveToPosition: first = " + firstItem + ", position = " + position + ", last = " + lastItem);
        if (position < firstItem) {
            //第一种可能:跳转位置在第一个可见位置之前
            mRecyclerView.smoothScrollToPosition(position);
        } else if (position <= lastItem) {
            //第二种可能:跳转位置在第一个可见位置之后
            int movePosition = position - firstItem;
            int top = mRecyclerView.getChildAt(movePosition).getTop();
            mRecyclerView.smoothScrollBy(0, top);
        } else {
            //第三种可能:跳转位置在最后可见项之后
            mRecyclerView.smoothScrollToPosition(position);
            mToPosition = position;
            mShouldScroll = true;
        }
    }

    private void addData() {
        mDatas = new ArrayList<>();
        mDatas.add("0");
        mDatas.add("1");
        mDatas.add("2");
        mDatas.add("3");
        for (int i = 1; i <= 20; i++) {
            mDatas.add("这是第" + i + "条数据");
        }
    }
}
