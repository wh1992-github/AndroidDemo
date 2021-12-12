package com.example.customview.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.customview.R;
import com.example.customview.adapter.MainAdapter;
import com.example.customview.bean.TypeBean;
import com.example.customview.tagview.TagActivity;
import com.example.customview.widget.SuperDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements BaseQuickAdapter.OnItemClickListener {

    private List<TypeBean> mList = new ArrayList<>();
    private MainAdapter mAdapter;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = findViewById(R.id.recycler_view);

        mAdapter = new MainAdapter(getData());
        mAdapter.setOnItemClickListener(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new SuperDividerItemDecoration.Builder(this).build());
        mRecyclerView.setAdapter(mAdapter);
    }

    private List<TypeBean> getData() {
        mList.add(new TypeBean("气泡漂浮动画", 0));
        mList.add(new TypeBean("波浪动画--贝塞尔曲线实现", 1));
        mList.add(new TypeBean("波浪动画--正余弦函数实现", 2));
        mList.add(new TypeBean("水波（雷达）扩散效果", 3));
        mList.add(new TypeBean("RecyclerView实现另类的Tag标签", 4));
        mList.add(new TypeBean("按钮自定义动画", 5));
        mList.add(new TypeBean("自定义支付密码输入框", 6));
        mList.add(new TypeBean("自定义进度条", 7));
        mList.add(new TypeBean("使用的带动画的view", 8));
        mList.add(new TypeBean("粘性小球", 9));
        mList.add(new TypeBean("banner", 10));
        mList.add(new TypeBean("吸顶效果--一行代码实现", 11));
        mList.add(new TypeBean("吸顶效果--动态实现", 12));
        mList.add(new TypeBean("吸顶效果--滑动切换", 13));
        mList.add(new TypeBean("揭露动画", 14));
        mList.add(new TypeBean("支付宝首页效果", 15));
        mList.add(new TypeBean("RecyclerView的item动画", 16));
        mList.add(new TypeBean("路径path动画", 17));
        mList.add(new TypeBean("仿新浪投票控件", 18));
        mList.add(new TypeBean("直播侧滑清屏效果", 19));
        mList.add(new TypeBean("指纹验证", 20));
        mList.add(new TypeBean("仿写QQ界面", 21));
        return mList;
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        switch (mList.get(position).getType()) {
            case 0:
                startActivity(new Intent(MainActivity.this, BubbleViewActivity.class));
                break;
            case 1:
                startActivity(new Intent(MainActivity.this, WaveByBezierActivity.class));
                break;
            case 2:
                startActivity(new Intent(MainActivity.this, WaveBySinCosActivity.class));
                break;
            case 3:
                startActivity(new Intent(MainActivity.this, RadarActivity.class));
                break;
            case 4:
                startActivity(new Intent(MainActivity.this, TagActivity.class));
                break;
            case 5:
                startActivity(new Intent(MainActivity.this, AnimationBtnActivity.class));
                break;
            case 6:
                startActivity(new Intent(MainActivity.this, PayPsdViewActivity.class));
                break;
            case 7:
                startActivity(new Intent(MainActivity.this, ProgressBarActivity.class));
                break;
            case 8:
                startActivity(new Intent(MainActivity.this, AnimationViewActivity.class));
                break;
            case 9:
                startActivity(new Intent(MainActivity.this, DragBallActivity.class));
                break;
            case 10:
                startActivity(new Intent(MainActivity.this, BannerActivity.class));
                break;
            case 11:
                startActivity(new Intent(MainActivity.this, HoverItemActivity.class));
                break;
            case 12:
                startActivity(new Intent(MainActivity.this, HoverItemActivity2.class));
                break;
            case 13:
                startActivity(new Intent(MainActivity.this, HoverItemActivity3.class));
                break;
            case 14:
                startActivity(new Intent(MainActivity.this, RevealAnimationActivity.class));
                break;
            case 15:
                startActivity(new Intent(MainActivity.this, AliPayHomeActivity.class));
                break;
            case 16:
                startActivity(new Intent(MainActivity.this, RecyclerViewItemAnimActivity.class));
                break;
            case 17:
                startActivity(new Intent(MainActivity.this, PathActivity.class));
                break;
            case 18:
                startActivity(new Intent(MainActivity.this, SinaVoteActivity.class));
                break;
            case 19:
                startActivity(new Intent(MainActivity.this, ClearScreenActivity.class));
                break;
            case 20:
                startActivity(new Intent(MainActivity.this, FingerprintActivity.class));
                break;
            case 21:
                startActivity(new Intent(MainActivity.this, RecyclerQQActivity.class));
                break;
        }
    }
}

