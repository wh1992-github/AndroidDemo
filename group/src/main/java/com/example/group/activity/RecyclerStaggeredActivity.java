package com.example.group.activity;

import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.group.adapter.RecyclerStaggeredAdapter;
import com.example.group.bean.GoodsInfo;
import com.example.group.databinding.ActivityRecyclerStaggeredBinding;
import com.example.group.widget.SpacesItemDecoration;

/**
 * Created by test on 2017/10/21.
 */
public class RecyclerStaggeredActivity extends AppCompatActivity {
    private ActivityRecyclerStaggeredBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRecyclerStaggeredBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initRecyclerStaggered(); //初始化瀑布流布局的循环视图
    }

    //初始化瀑布流布局的循环视图
    private void initRecyclerStaggered() {
        //从布局文件中获取名叫rv_staggered的循环视图
        RecyclerView rv_staggered = binding.rvStaggered;
        //创建一个垂直方向的瀑布流布局管理器
        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(
                3, LinearLayout.VERTICAL);
        //设置循环视图的布局管理器
        rv_staggered.setLayoutManager(manager);
        //构建一个服装列表的瀑布流适配器
        RecyclerStaggeredAdapter adapter = new RecyclerStaggeredAdapter(this, GoodsInfo.getDefaultStag());
        //设置瀑布流列表的点击监听器
        adapter.setOnItemClickListener(adapter);
        //设置瀑布流列表的长按监听器
        adapter.setOnItemLongClickListener(adapter);
        //给rv_staggered设置服装瀑布流适配器
        rv_staggered.setAdapter(adapter);
        //设置rv_staggered的默认动画效果
        rv_staggered.setItemAnimator(new DefaultItemAnimator());
        //给rv_staggered添加列表项之间的空白装饰
        rv_staggered.addItemDecoration(new SpacesItemDecoration(3));
    }

}
