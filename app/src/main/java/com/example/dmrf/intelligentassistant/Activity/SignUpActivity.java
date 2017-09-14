package com.example.dmrf.intelligentassistant.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dmrf.intelligentassistant.ActivityManager.ActivityManager;
import com.example.dmrf.intelligentassistant.Bean.User;
import com.example.dmrf.intelligentassistant.R;
import com.example.dmrf.intelligentassistant.Utils.BitmapAndStringUtils;
import com.example.dmrf.intelligentassistant.Utils.GetPrictureFromNetByUrlUtils;
import com.example.dmrf.intelligentassistant.Utils.LoginByQqUtils;
import com.example.dmrf.intelligentassistant.Utils.NetWorkUtils;
import com.tencent.tauth.Tencent;

import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by DMRF on 2017/8/7.
 */

public class SignUpActivity extends Activity {
    EditText etUserName, etPassword;
    Button btnSignUp;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private TextView no_network_worning;
    private String icon = null;
    private String opid = null;
    private String nickname = null;
    private boolean flag;
    ProgressDialog dialog;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ActivityManager.getInstance().addActivity(SignUpActivity.this);
        Bmob.initialize(this, "0c16c3810a6216cc61493b68e3595eb0");
        initViews();
        initListener();


    }

    private void initViews() {
        dialog = new ProgressDialog(SignUpActivity.this);
        no_network_worning = findViewById(R.id.no_network_worning);
        etUserName = (EditText) findViewById(R.id.editUserName);
        etPassword = (EditText) findViewById(R.id.editPassword);

        etUserName.addTextChangedListener(new TextChange());
        etPassword.addTextChangedListener(new TextChange());

        btnSignUp = (Button) findViewById(R.id.signup);

        String flag = getIntent().getStringExtra("exit_login");


        pref = getSharedPreferences("UserInfo", MODE_PRIVATE);
        //启用编辑
        editor = pref.edit();

    }

    private void initListener() {

        //注册
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignUp();
            }
        });
    }

    private void SignUp() {
        if (NetWorkUtils.getAPNType(SignUpActivity.this) == 0) {
            no_network_worning.setVisibility(View.VISIBLE);
            return;
        }

        final String username = etUserName.getText().toString().trim();
        final String password = etPassword.getText().toString().trim();


        //使用BombSDK提供的注册功能
        MainActivity.user.setUsername(username);
        MainActivity.user.setPassword(password);


        MainActivity.user.signUp(new SaveListener<Object>() {
            @Override
            public void done(Object o, BmobException e) {
                if (e == null) {

                    editor.putString("userName", username);
                    editor.putString("Password", password);
                    editor.commit();


                    Toast.makeText(SignUpActivity.this, "注册成功!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    intent.setClass(SignUpActivity.this, MainActivity.class);
                    intent.putExtra("username", username);
                    MainActivity.in_type = "signup";
                    startActivity(intent);
                } else {
                    e.printStackTrace();
                    Log.i(MainActivity.TAG, "done: " + e.getMessage());
                }
            }
        });
    }


    //应用调用Andriod_SDK接口时，使能成功接收到回调
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        Tencent.onActivityResultData(requestCode, resultCode, data, LoginByQqUtils.listener);
    }

    // EditText监听器
    class TextChange implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            boolean Sign2 = etUserName.getText().length() > 0;
            boolean Sign3 = etPassword.getText().length() > 0;
            if (Sign2 & Sign3) {
                btnSignUp.setTextColor(0xFFFFFFFF);
                btnSignUp.setEnabled(true);
            }
            // 在layout文件中，对Button的text属性应预先设置默认值，否则刚打开程序的时候Button是无显示的
            else {
                btnSignUp.setTextColor(0xFFD0EFC6);
                btnSignUp.setEnabled(false);
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    }

}
