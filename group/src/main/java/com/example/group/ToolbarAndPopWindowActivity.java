package com.example.group;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

@SuppressLint("RtlHardcoded")
public class ToolbarAndPopWindowActivity extends AppCompatActivity implements View.OnClickListener {
    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private LinearLayout mLlTab;
    private Toast mToast;
    private PopupWindow mPopupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_toolbar_popwindow);
        mToast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
        mToast.setGravity(Gravity.CENTER, 0, 0);

        mToolbar = findViewById(R.id.toolbar);
        //App Logo
        mToolbar.setLogo(R.mipmap.ic_launcher);
        //主标题,默认为app_label的名字,此处为空
        mToolbar.setTitle("Title");
        mToolbar.setTitleTextColor(Color.YELLOW);
        //副标题,此处为空
        mToolbar.setSubtitle("Subtitle");
        mToolbar.setSubtitleTextColor(Color.RED);
        //设置工具栏的背景
        mToolbar.setBackgroundResource(R.color.blue_light);
        //侧边栏的导航按钮
        mToolbar.setNavigationIcon(R.drawable.ic_back);
        //取代原本的actionbar
        setSupportActionBar(mToolbar);
        //DrawerToggle和NavigationIcon冲突，只能显示一个
        //设置NavigationIcon的点击事件,需要放在setSupportActionBar之后设置才会生效,
        //因为setSupportActionBar里面也会setNavigationOnClickListener
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mToast.setText("click NavigationIcon");
                mToast.show();
            }
        });

        //设置侧或布局
        mDrawerLayout = findViewById(R.id.drawerlayout);
        mLlTab = findViewById(R.id.ll_tabs);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.drawer_open, R.string.drawer_close);
        mDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mLlTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.closeDrawer(Gravity.LEFT);
            }
        });

        //设置toolBar上的MenuItem点击事件
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_edit:
                        mToast.setText("click edit");
                        break;
                    case R.id.action_share:
                        mToast.setText("click share");
                        break;
                    case R.id.action_overflow:
                        //弹出自定义overflow
                        popupWindow();
                        break;
                }
                mToast.show();
                return true;
            }
        });
        //ToolBar里面还可以包含子控件
        mToolbar.findViewById(R.id.tv_title).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mToast.setText("点击首页");
                mToast.show();
            }
        });
        mToolbar.findViewById(R.id.btn_diy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mToast.setText("点击自定义按钮");
                mToast.show();
            }
        });
    }

    //如果有Menu,创建完后,系统会自动添加到ToolBar上
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_toolbar_popwindow, menu);
        return true;
    }

    /**
     * 弹出自定义的popWindow
     */
    public void popupWindow() {
        //获取状态栏高度
        Rect frame = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        //状态栏高度+toolbar的高度
        int yOffset = frame.top + mToolbar.getHeight();
        if (null == mPopupWindow) {
            //初始化PopupWindow的布局
            View popView = getLayoutInflater().inflate(R.layout.popwindow, null);
            //popView即popupWindow的布局，ture设置focusAble.
            mPopupWindow = new PopupWindow(popView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
            //必须设置BackgroundDrawable后setOutsideTouchable(true)才会有效
            mPopupWindow.setBackgroundDrawable(new ColorDrawable());
            //点击外部关闭。
            mPopupWindow.setOutsideTouchable(true);
            //设置一个动画。
            mPopupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
            //设置Gravity，让它显示在右上角。
            mPopupWindow.showAtLocation(mToolbar, Gravity.RIGHT | Gravity.TOP, 40, yOffset);
            //显示在某个view下面
//          mPopupWindow.showAsDropDown(mToolbar);
            //设置item的点击监听
            popView.findViewById(R.id.ll_item1).setOnClickListener(this);
            popView.findViewById(R.id.ll_item2).setOnClickListener(this);
            popView.findViewById(R.id.ll_item3).setOnClickListener(this);
        } else {
            mPopupWindow.showAtLocation(mToolbar, Gravity.RIGHT | Gravity.TOP, 40, yOffset);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_item1:
                mToast.setText("哈哈");
                break;
            case R.id.ll_item2:
                mToast.setText("呵呵");
                break;
            case R.id.ll_item3:
                mToast.setText("嘻嘻");
                break;
        }
        //点击PopWindow的item后,关闭此PopWindow
        if (null != mPopupWindow && mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
        }
        mToast.show();
    }
}