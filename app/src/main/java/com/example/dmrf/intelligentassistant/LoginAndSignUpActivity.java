package com.example.dmrf.intelligentassistant;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by DMRF on 2017/8/7.
 */

public class LoginAndSignUpActivity extends Activity {
    EditText etUserName, etPassword;
    Button btnSignUp, btnLogIn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in_and_sign_up);
        Bmob.initialize(this, "0c16c3810a6216cc61493b68e3595eb0");
        etUserName = (EditText) findViewById(R.id.editUserName);
        etPassword = (EditText) findViewById(R.id.editPassword);
        btnSignUp = (Button) findViewById(R.id.regist);
        btnLogIn = findViewById(R.id.login);

        //登录
        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String username = etUserName.getText().toString();
                String password = etPassword.getText().toString();

                if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
                    Toast.makeText(LoginAndSignUpActivity.this, "用户名和密码不能为空！", Toast.LENGTH_SHORT).show();
                }

                //使用BombSDK提供的注册功能
                BmobUser user = new BmobUser();
                user.setUsername(username);
                user.setPassword(password);
                user.login(new SaveListener<Object>() {
                    @Override
                    public void done(Object o, BmobException e) {
                        if (e == null) {
                            Toast.makeText(LoginAndSignUpActivity.this, "登录成功！", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent();
                            intent.setClass(LoginAndSignUpActivity.this, MainActivity.class);
                            intent.putExtra("username", username);
                            startActivity(intent);
                        } else {
                            Toast.makeText(LoginAndSignUpActivity.this,e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });


        //注册
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String username = etUserName.getText().toString();
                String password = etPassword.getText().toString();

                if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
                    Toast.makeText(LoginAndSignUpActivity.this, "用户名和密码不能为空！", Toast.LENGTH_SHORT).show();
                }

                //使用BombSDK提供的注册功能
                BmobUser user = new BmobUser();
                user.setUsername(username);
                user.setPassword(password);
                user.signUp(new SaveListener<Object>() {
                    @Override
                    public void done(Object o, BmobException e) {
                        if (e == null) {
                            Toast.makeText(LoginAndSignUpActivity.this, "注册成功!", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent();
                            intent.setClass(LoginAndSignUpActivity.this, MainActivity.class);
                            intent.putExtra("username", username);
                            startActivity(intent);

                        } else {
                            Toast.makeText(LoginAndSignUpActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();

                        }
                    }
                });
            }
        });
    }
}
