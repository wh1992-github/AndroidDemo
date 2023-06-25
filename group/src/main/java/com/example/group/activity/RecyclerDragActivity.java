package com.example.group.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;

import com.example.group.R;
import com.example.group.adapter.DragAdapter;

import java.util.ArrayList;
import java.util.List;

public class RecyclerDragActivity extends AppCompatActivity {
    private static final String TAG = "RecyclerDragActivity";
    private RecyclerView mRecyclerView;
    private DragAdapter mDragAdapter;
    private List<String> mDragList = new ArrayList<>();
    private ItemTouchHelper mItemTouchHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_drag);
        initData();
        initView();
    }

    private List<String> initData() {
        for (int i = 0; i < 10; i++) {
            mDragList.add("可拖拽#" + i);
        }
        return mDragList;
    }

    private void initView() {
        mRecyclerView = findViewById(R.id.rv_drag_layout);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        mDragAdapter = new DragAdapter(mDragList);
        mRecyclerView.setAdapter(mDragAdapter);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                Log.i(TAG, "onScrollStateChanged: newState = " + newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                Log.i(TAG, "onScrolled: dx = " + dx + ", dy = " + dy);
            }
        });

        mItemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                /**
                 * ItemTouchHelper支持事件方向判断,但是必须重写当前getMovementFlags来指定支持的方向
                 * 这里我同时设置了dragFlag为上下左右四个方向,swipeFlag的左右方向
                 * 最后通过makeMovementFlags（dragFlag,swipe）创建方向的Flag,
                 * 因为我们目前只需要实现拖拽,所以我并未创建swipe的flag
                 */
                Log.i(TAG, "getMovementFlags: ");
                int dragFlag = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
                int swipe = ItemTouchHelper.START | ItemTouchHelper.END;
                return makeMovementFlags(dragFlag, swipe);
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder1, @NonNull RecyclerView.ViewHolder viewHolder2) {
                Log.i(TAG, "onMove: position1 = " + viewHolder1.getAdapterPosition() + ", position2 = " + viewHolder2.getAdapterPosition());
                mDragAdapter.onItemMove(viewHolder1.getAdapterPosition(), viewHolder2.getAdapterPosition());
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int swipe) {
                Log.i(TAG, "onSwiped: position = " + viewHolder.getAdapterPosition() + ", swipe = " + swipe);
                mDragAdapter.onItemDismiss(viewHolder.getAdapterPosition());
            }
        });
        mItemTouchHelper.attachToRecyclerView(mRecyclerView);
    }
}
