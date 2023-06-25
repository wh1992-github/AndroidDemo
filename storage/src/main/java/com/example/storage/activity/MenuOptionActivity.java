package com.example.storage.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.storage.R;

/**
 * Created by test on 2017/10/1.
 */
@SuppressLint("SetTextI18n")
public class MenuOptionActivity extends AppCompatActivity implements OnClickListener, PopupMenu.OnMenuItemClickListener {
    private static final String TAG = "MenuOptionActivity";
    private Button mMenuBtn;
    private EditText mEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_option);
        mMenuBtn = findViewById(R.id.btn_menu);
        mMenuBtn.setOnClickListener(this);
        findViewById(R.id.btn_option).setOnClickListener(this);

        mEditText = findViewById(R.id.editable_view);
        //注册上下文菜单
        registerForContextMenu(mEditText);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_option) {
            //注意:如果当前页面继承自AppCompatActivity,并且appcompat版本不低于22.1.0
            //那么调用openOptionsMenu方法将不会弹出菜单。这应该是Android的一个bug
            openOptionsMenu();
        } else if (v.getId() == R.id.btn_menu) {
            popupMenu();
        }
    }

    //选项菜单
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_option, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.i(TAG, "onOptionsItemSelected: " + item.getItemId());
        Toast.makeText(this, "点击了" + item.getTitle(), Toast.LENGTH_SHORT).show();
        return super.onOptionsItemSelected(item);
    }

    //上下文菜单
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.clear();
        getMenuInflater().inflate(R.menu.menu_context, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        Log.i(TAG, "onContextItemSelected: " + item.getItemId());
        Toast.makeText(this, "点击了" + item.getTitle(), Toast.LENGTH_SHORT).show();
        return super.onContextItemSelected(item);
    }

    //弹出菜单
    public void popupMenu() {
        PopupMenu popupMenu = new PopupMenu(this, mMenuBtn);
        Menu menu = popupMenu.getMenu();
        getMenuInflater().inflate(R.menu.menu_popup, menu);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        Log.i(TAG, "onMenuItemClick: " + item.getItemId());
        Toast.makeText(this, item.getTitle(), Toast.LENGTH_SHORT).show();
        return false;
    }
}
