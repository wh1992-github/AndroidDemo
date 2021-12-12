package com.example.customview.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.customview.R;
import com.example.customview.adapter.HoverItemAdapter3;
import com.example.customview.bean.HoverItemModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class HoverItemActivity3 extends AppCompatActivity {

    public static final int MODEL_COUNT = 40;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hover_item_3);

        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        final TextView tvStickyHeaderView = (TextView) findViewById(R.id.header_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(new HoverItemAdapter3(this, getData()));
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NotNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                View stickyInfoView = recyclerView.findChildViewUnder(tvStickyHeaderView.getMeasuredWidth() >> 1, 1);
                if (stickyInfoView != null) {
                    tvStickyHeaderView.setText(stickyInfoView.getContentDescription().toString());
                }

                View transInfoView = recyclerView.findChildViewUnder(tvStickyHeaderView.getMeasuredWidth() >> 1, tvStickyHeaderView.getMeasuredHeight() + 1);
                if (transInfoView != null) {
                    int transViewStatus = (int) transInfoView.getTag();
                    int dealtY = transInfoView.getTop() - tvStickyHeaderView.getMeasuredHeight();

                    if (transViewStatus == HoverItemAdapter3.HAS_STICKY_VIEW && transInfoView.getTop() > 0) {
                        tvStickyHeaderView.setTranslationY(dealtY);
                    } else {
                        tvStickyHeaderView.setTranslationY(0);
                    }
                }
            }
        });
    }

    public List<HoverItemModel> getData() {
        List<HoverItemModel> hoverItemModels = new ArrayList<>();
        for (int index = 0; index < MODEL_COUNT; index++) {
            if (index < 6) {
                hoverItemModels.add(new HoverItemModel("吸顶文本1", "name:" + index));
            } else if (index < 12) {
                hoverItemModels.add(new HoverItemModel("吸顶文本2", "name:" + index));
            } else if (index < 18) {
                hoverItemModels.add(new HoverItemModel("吸顶文本3", "name:" + index));
            } else {
                hoverItemModels.add(new HoverItemModel("吸顶文本4", "name:" + index));
            }
        }
        return hoverItemModels;
    }
}
