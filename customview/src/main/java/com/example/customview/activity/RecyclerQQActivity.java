package com.example.customview.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.example.customview.R;
import com.example.customview.adapter.RecyclerQQAdapter;

import java.util.ArrayList;
import java.util.List;

public class RecyclerQQActivity extends AppCompatActivity implements RecyclerQQAdapter.onSlidingViewClickListener {

    private RecyclerView mRecyclerView;//在xml中RecyclerView布局
    private RecyclerQQAdapter mAdapter;//item_recycler布局的适配器

    //设置数据
    private List<Bitmap> dataImage;//头像（谁的头像）
    private List<String> dataTitle;//标题（谁的消息）
    private List<String> dataContent;//内容（消息内容）

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_qq);
        mRecyclerView = findViewById(R.id.recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        initData();
        mAdapter = new RecyclerQQAdapter(this, dataImage, dataTitle, dataContent);
        mRecyclerView.setAdapter(mAdapter);
        //设置滑动监听器（侧滑）
        mAdapter.setOnSlidListener(this);
    }

    //通过 position 区分点击了哪个 item
    @Override
    public void onItemClick(View view, int position) {
        //在这里可以做出一些反应（跳转界面、弹出弹框之类）
        Toast.makeText(RecyclerQQActivity.this, "点击了：" + position, Toast.LENGTH_SHORT).show();
    }

    //点击删除按钮时，根据传入的 position 调用 RecyclerAdapter 中的 removeData() 方法
    @Override
    public void onDeleteBtnClick(View view, int position) {
        mAdapter.removeData(position);
    }

    public void initData() {
        dataImage = new ArrayList<>();    //头像（谁的头像）
        dataTitle = new ArrayList<>();     //标题（谁的消息）
        dataContent = new ArrayList<>();  //内容（消息内容）
        dataImage.add(BitmapFactory.decodeResource(getResources(), R.mipmap.a1));
        dataImage.add(BitmapFactory.decodeResource(getResources(), R.mipmap.a2));
        dataImage.add(BitmapFactory.decodeResource(getResources(), R.mipmap.a3));
        dataImage.add(BitmapFactory.decodeResource(getResources(), R.mipmap.a4));
        dataImage.add(BitmapFactory.decodeResource(getResources(), R.mipmap.a5));
        dataImage.add(BitmapFactory.decodeResource(getResources(), R.mipmap.a6));
        dataImage.add(BitmapFactory.decodeResource(getResources(), R.mipmap.a7));
        dataImage.add(BitmapFactory.decodeResource(getResources(), R.mipmap.a8));
        dataImage.add(BitmapFactory.decodeResource(getResources(), R.mipmap.a9));
        dataImage.add(BitmapFactory.decodeResource(getResources(), R.mipmap.a10));
        dataImage.add(BitmapFactory.decodeResource(getResources(), R.mipmap.a11));
        dataImage.add(BitmapFactory.decodeResource(getResources(), R.mipmap.a12));
        dataTitle.add("Android开发交流群");
        dataTitle.add("R语言初级入门学习");
        dataTitle.add("刘亦菲");
        dataTitle.add("策划书交流群");
        dataTitle.add("15生态宜居学院学生群");
        dataTitle.add("湘环资助 （助学贷款）");
        dataTitle.add("湘环编程研讨会");
        dataTitle.add("丰风");
        dataTitle.add("阿娇");
        dataTitle.add("图书馆流通服务交流群");
        dataTitle.add("one3胡了");
        dataTitle.add("读者协会策划部");
        dataContent.add("广州_Even：[图片]");
        dataContent.add("轻舟飘飘：auto基本不准");
        dataContent.add("不会的");
        dataContent.add("残留的余温。：分享[熊猫直播]");
        dataContent.add("刘老师：[文件]2018年6月全国大学……");
        dataContent.add("17级园林");
        dataContent.add("黄晓明：baby一般般吧");
        dataContent.add("[文件]编程之美");
        dataContent.add("i5的处理器，比较稳定，蛮好的");
        dataContent.add("寥寥：好的，谢谢老师");
        dataContent.add("易天：阿龙还在面试呢");
        dataContent.add("策划部陈若依：请大家把备注改好");
    }
}
