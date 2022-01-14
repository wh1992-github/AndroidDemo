package com.example.middle;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.middle.util.ViewUtil;

import java.util.Locale;

/**
 * Created by test on 2017/9/24.
 */

public class LoginMainActivity extends AppCompatActivity implements OnClickListener {
    private RadioGroup rg_login; //声明一个单选组对象
    private RadioButton rb_password; //声明一个单选按钮对象
    private RadioButton rb_verifycode; //声明一个单选按钮对象
    private EditText et_phone; //声明一个编辑框对象
    private TextView tv_password; //声明一个文本视图对象
    private EditText et_password; //声明一个编辑框对象
    private Button btn_forget; //声明一个按钮控件对象
    private CheckBox ck_remember; //声明一个复选框对象

    private final int mRequestCode = 0; //跳转页面时的请求代码
    private int mType = 0; //用户类型
    private boolean bRemember = false; //是否记住密码
    private String mPassword = "111111"; //默认密码
    private String mVerifyCode; //验证码

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        rg_login = findViewById(R.id.rg_login);
        rb_password = findViewById(R.id.rb_password);
        rb_verifycode = findViewById(R.id.rb_verifycode);
        et_phone = findViewById(R.id.et_phone);
        tv_password = findViewById(R.id.tv_password);
        et_password = findViewById(R.id.et_password);
        btn_forget = findViewById(R.id.btn_forget);
        ck_remember = findViewById(R.id.ck_remember);
        //给rg_login设置单选监听器
        rg_login.setOnCheckedChangeListener(new RadioListener());
        //给ck_remember设置勾选监听器
        ck_remember.setOnCheckedChangeListener(new CheckListener());
        //给et_phone添加文本变更监听器
        et_phone.addTextChangedListener(new HideTextWatcher(et_phone));
        //给et_password添加文本变更监听器
        et_password.addTextChangedListener(new HideTextWatcher(et_password));
        btn_forget.setOnClickListener(this);
        findViewById(R.id.btn_login).setOnClickListener(this);
        initTypeSpinner();
    }

    //初始化用户类型的下拉框
    private void initTypeSpinner() {
        //声明一个下拉列表的数组适配器
        ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(this,
                R.layout.item_select, typeArray);
        //设置数组适配器的布局样式
        typeAdapter.setDropDownViewResource(R.layout.item_dropdown);
        //从布局文件中获取名叫sp_type的下拉框
        Spinner sp_type = findViewById(R.id.sp_type);
        //设置下拉框的标题
        sp_type.setPrompt("请选择用户类型");
        //设置下拉框的数组适配器
        sp_type.setAdapter(typeAdapter);
        //设置下拉框默认显示第几项
        sp_type.setSelection(mType);
        //给下拉框设置选择监听器,一旦用户选中某一项,就触发监听器的onItemSelected方法
        sp_type.setOnItemSelectedListener(new TypeSelectedListener());
    }

    private final String[] typeArray = {"个人用户", "公司用户"};

    //定义用户类型的选择监听器
    class TypeSelectedListener implements OnItemSelectedListener {
        //选择事件的处理方法,其中arg2代表选择项的序号
        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
            mType = arg2;
        }

        //未选择时的处理方法,通常无需关注
        public void onNothingSelected(AdapterView<?> arg0) {
        }
    }

    //定义登录方式的单选监听器
    private class RadioListener implements RadioGroup.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            if (checkedId == R.id.rb_password) { //选择了密码登录
                tv_password.setText("登录密码：");
                et_password.setHint("请输入密码");
                btn_forget.setText("忘记密码");
                ck_remember.setVisibility(View.VISIBLE);
            } else if (checkedId == R.id.rb_verifycode) { //选择了验证码登录
                tv_password.setText("　验证码：");
                et_password.setHint("请输入验证码");
                btn_forget.setText("获取验证码");
                ck_remember.setVisibility(View.INVISIBLE);
            }
        }
    }

    //定义是否记住密码的勾选监听器
    private class CheckListener implements CompoundButton.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (buttonView.getId() == R.id.ck_remember) {
                bRemember = isChecked;
            }
        }
    }

    //定义编辑框的文本变化监听器
    private class HideTextWatcher implements TextWatcher {
        private final EditText mView;
        private final int mMaxLength;
        private CharSequence mStr;

        HideTextWatcher(EditText v) {
            super();
            mView = v;
            mMaxLength = ViewUtil.getMaxLength(v);
        }

        //在编辑框的输入文本变化前触发
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        //在编辑框的输入文本变化时触发
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            mStr = s;
        }

        //在编辑框的输入文本变化后触发
        public void afterTextChanged(Editable s) {
            if (mStr == null || mStr.length() == 0)
                return;
            //手机号码输入达到11位,或者密码/验证码输入达到6位,都关闭输入法软键盘
            if ((mStr.length() == 11 && mMaxLength == 11) ||
                    (mStr.length() == 6 && mMaxLength == 6)) {
                ViewUtil.hideOneInputMethod(LoginMainActivity.this, mView);
            }
        }
    }

    @Override
    public void onClick(View v) {
        String phone = et_phone.getText().toString();
        if (v.getId() == R.id.btn_forget) { //点击了“忘记密码”按钮
            if (phone.length() < 11) { //手机号码不足11位
                Toast.makeText(this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
                return;
            }
            if (rb_password.isChecked()) { //选择了密码方式校验,此时要跳到找回密码页面
                Intent intent = new Intent(this, LoginForgetActivity.class);
                //携带手机号码跳转到找回密码页面
                intent.putExtra("phone", phone);
                startActivityForResult(intent, mRequestCode);
            } else if (rb_verifycode.isChecked()) { //选择了验证码方式校验,此时要生成六位随机数字验证码
                //生成六位随机数字的验证码
                mVerifyCode = String.format(Locale.getDefault(), "%06d", (int) (Math.random() * 1000000 % 1000000));
                //弹出提醒对话框,提示用户六位验证码数字
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("请记住验证码");
                builder.setMessage("手机号" + phone + ",本次验证码是" + mVerifyCode + ",请输入验证码");
                builder.setPositiveButton("好的", null);
                AlertDialog alert = builder.create();
                alert.show();
            }
        } else if (v.getId() == R.id.btn_login) { //点击了“登录”按钮
            if (phone.length() < 11) { //手机号码不足11位
                Toast.makeText(this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
                return;
            }
            if (rb_password.isChecked()) { //密码方式校验
                if (!et_password.getText().toString().equals(mPassword)) {
                    Toast.makeText(this, "请输入正确的密码", Toast.LENGTH_SHORT).show();
                } else { //密码校验通过
                    loginSuccess(); //提示用户登录成功
                }
            } else if (rb_verifycode.isChecked()) { //验证码方式校验
                if (!et_password.getText().toString().equals(mVerifyCode)) {
                    Toast.makeText(this, "请输入正确的验证码", Toast.LENGTH_SHORT).show();
                } else { //验证码校验通过
                    loginSuccess(); //提示用户登录成功
                }
            }
        }
    }

    //从后一个页面携带参数返回当前页面时触发
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == mRequestCode && data != null) {
            //用户密码已改为新密码,故更新密码变量
            mPassword = data.getStringExtra("new_password");
        }
    }

    //从修改密码页面返回登录页面,要清空密码的输入框
    @Override
    protected void onRestart() {
        et_password.setText("");
        super.onRestart();
    }

    //校验通过,登录成功
    private void loginSuccess() {
        String desc = String.format(Locale.getDefault(), "您的手机号码是%s,类型是%s。恭喜你通过登录验证,点击“确定”按钮返回上个页面",
                et_phone.getText().toString(), typeArray[mType]);
        //弹出提醒对话框,提示用户登录成功
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("登录成功");
        builder.setMessage(desc);
        builder.setPositiveButton("确定返回", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.setNegativeButton("我再看看", null);
        AlertDialog alert = builder.create();
        alert.show();
    }

}
