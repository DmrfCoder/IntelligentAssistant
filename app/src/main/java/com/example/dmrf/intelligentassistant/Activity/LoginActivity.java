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

public class LoginActivity extends Activity {
    EditText etUserName, etPassword;
    Button btnSignUp, btnLogIn;
    private CheckBox chk;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private TextView no_network_worning;
    private CheckBox auto_log_in;
    private ImageButton login_by_QQ;
    private String SCOPE = "get_simple_userinfo,add_topic";
    private String icon = null;
    private String opid = null;
    private String nickname = null;
    private boolean flag;
    ProgressDialog dialog;

    private Handler qqHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            List<String> list = (List<String>) msg.obj;
            opid = list.get(0);
            nickname = list.get(1);
            icon = list.get(2);
            if (icon != null) {
                GetPrictureFromNetByUrlUtils.LoadingPicture(icon, mHander);
            } else {
                Toast.makeText(LoginActivity.this, "用户头像信息获取失败！", Toast.LENGTH_SHORT).show();
            }

        }
    };

    private Handler mHander = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bitmap bitmap = (Bitmap) msg.obj;
            if (bitmap != null) {
                icon = BitmapAndStringUtils.convertIconToString(bitmap);
                LogInOrSignUpByUserNameAndPassword();
            } else {
                Toast.makeText(LoginActivity.this, "从网络获取图片失败！", Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        ActivityManager.getInstance().addActivity(LoginActivity.this);
        Bmob.initialize(this, "0c16c3810a6216cc61493b68e3595eb0");
        initViews();
        initListener();

        if (opid != null && nickname != null) {

            etPassword.setText(opid.substring(0, 12));
            etUserName.setText(nickname);
        }

    }

    private void initViews() {
        dialog = new ProgressDialog(LoginActivity.this);
        no_network_worning = findViewById(R.id.no_network_worning);
        etUserName = (EditText) findViewById(R.id.editUserName);
        etPassword = (EditText) findViewById(R.id.editPassword);

        etUserName.addTextChangedListener(new TextChange());
        etPassword.addTextChangedListener(new TextChange());

        btnSignUp = (Button) findViewById(R.id.regist);
        btnLogIn = findViewById(R.id.login);
        chk = (CheckBox) findViewById(R.id.checkSaveName);
        auto_log_in = findViewById(R.id.auto_log_in);
        login_by_QQ = findViewById(R.id.login_by_QQ);

        String flag = getIntent().getStringExtra("exit_login");
        if (flag != null) {
            if (flag.equals("true")) {
                auto_log_in.setChecked(false);
            } else {
                auto_log_in.setChecked(true);
            }
        } else {
            auto_log_in.setChecked(true);
        }

        pref = getSharedPreferences("UserInfo", MODE_PRIVATE);
        //启用编辑
        editor = pref.edit();


        String name = pref.getString("userName", "");
        String key = pref.getString("Password", "");
        String auto = pref.getString("auto", "");

        if (name == null || name.equals("")) {
            chk.setChecked(false);
            auto_log_in.setChecked(false);
        } else {
            chk.setChecked(true);
            etUserName.setText(name);
            if (key.length() > 20) {
                etPassword.setText(key.substring(0, 20));
            } else {
                etPassword.setText(key);
            }

            if (auto_log_in.isChecked()) {
                LogIn();
            }

        }
    }

    private void initListener() {

        //注册
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent tempIntent = new Intent();
                tempIntent.setClass(LoginActivity.this, SignUpActivity.class);
                LoginActivity.this.startActivity(tempIntent);
            }
        });


        //登录
        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LogIn();
            }
        });


        login_by_QQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginByQqUtils.LogInByQQ(LoginActivity.this, qqHandler);
            }
        });


    }

    private void SignUp() {
        if (NetWorkUtils.getAPNType(LoginActivity.this) == 0) {
            no_network_worning.setVisibility(View.VISIBLE);
            return;
        }

        final String username = etUserName.getText().toString().trim();
        final String password = etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            Toast.makeText(LoginActivity.this, "用户名和密码不能为空！", Toast.LENGTH_SHORT).show();
        }


        //使用BombSDK提供的注册功能
        MainActivity.user.setUsername(username);
        MainActivity.user.setPassword(password);


        MainActivity.user.signUp(new SaveListener<Object>() {
            @Override
            public void done(Object o, BmobException e) {
                if (e == null) {
                    if (chk.isChecked()) {
                        editor.putString("userName", username);
                        editor.putString("Password", password);
                        editor.commit();
                    } else {
                        editor.remove("userName");
                        editor.remove("Password");
                        editor.commit();
                    }


                    Toast.makeText(LoginActivity.this, "注册成功!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    intent.setClass(LoginActivity.this, MainActivity.class);
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


    private void LogIn() {

        dialog.setMessage("正在登录...");
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.show();

        flag = false;

        if (NetWorkUtils.getAPNType(LoginActivity.this) == 0) {
            no_network_worning.setVisibility(View.VISIBLE);
            return;
        }


        final String username = etUserName.getText().toString().trim();
        final String password = etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            dialog.dismiss();
            Toast.makeText(LoginActivity.this, "用户名和密码不能为空！", Toast.LENGTH_SHORT).show();
        }


        MainActivity.user.setPassword(password);
        MainActivity.user.setUsername(password);


        MainActivity.user.login(new SaveListener<Object>() {
            @Override
            public void done(Object o, BmobException e) {
                if (e == null) {
                    if (chk.isChecked()) {
                        editor.putString("userName", username);
                        editor.putString("Password", password);

                        editor.commit();

                    } else {
                        editor.remove("userName");
                        editor.remove("Password");
                        editor.commit();
                    }


                    BmobQuery<User> query = new BmobQuery<User>();
                    query.addWhereEqualTo("username", username);
                    query.findObjects(new FindListener<User>() {
                        @Override
                        public void done(List<User> list, BmobException e) {
                            if (e == null) {
                                MainActivity.user = list.get(0);
                                Toast.makeText(LoginActivity.this, "登录成功！", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                            } else {
                                dialog.dismiss();
                                Toast.makeText(LoginActivity.this, "登录失败！", Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }
                        }
                    });

                } else {

                    if (flag) {
                        dialog.dismiss();
                        Toast.makeText(LoginActivity.this, "登录失败！", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                        Log.i(MainActivity.TAG, "done: " + e.getMessage());
                    } else {
                        flag = true;
                    }

                }
            }
        });


        MainActivity.user.setUsername(username);
        MainActivity.user.setPassword(password);


        MainActivity.user.login(new SaveListener<Object>() {
            @Override
            public void done(Object o, BmobException e) {
                if (e == null) {
                    if (chk.isChecked()) {
                        editor.putString("userName", username);
                        editor.putString("Password", password);

                        editor.commit();

                    } else {
                        editor.remove("userName");
                        editor.remove("Password");
                        editor.commit();
                    }


                    BmobQuery<User> query = new BmobQuery<User>();
                    query.addWhereEqualTo("username", username);
                    query.findObjects(new FindListener<User>() {
                        @Override
                        public void done(List<User> list, BmobException e) {
                            if (e == null) {
                                MainActivity.user = list.get(0);
                                Toast.makeText(LoginActivity.this, "登录成功！", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                            } else {
                                dialog.dismiss();
                                Toast.makeText(LoginActivity.this, "登录失败！", Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }
                        }
                    });

                } else {
                    if (flag) {
                        dialog.dismiss();
                        Toast.makeText(LoginActivity.this, "登录失败！", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                        Log.i(MainActivity.TAG, "done: " + e.getMessage());
                    } else {
                        flag = true;
                    }
                }
            }
        });


    }


    private void LogInOrSignUpByUserNameAndPassword() {
        flag = false;


        MainActivity.user.setUsername(opid);
        MainActivity.user.setPassword(opid);

        //登录
        MainActivity.user.login(new SaveListener<Object>() {
            @Override
            public void done(Object o, BmobException e) {
                if (e == null) {
                    if (chk.isChecked()) {
                        editor.putString("userName", nickname);
                        editor.putString("Password", opid);
                        editor.commit();

                    } else {
                        editor.remove("userName");
                        editor.remove("Password");
                        editor.commit();
                    }


                    BmobQuery<User> query = new BmobQuery<User>();
                    query.addWhereEqualTo("username", opid);
                    query.findObjects(new FindListener<User>() {
                        @Override
                        public void done(List<User> list, BmobException e) {
                            if (e == null) {
                                MainActivity.user = list.get(0);
                                Toast.makeText(LoginActivity.this, "登录成功！", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                            } else {
                                e.printStackTrace();
                            }
                        }
                    });
                } else {
                    if (flag) {
                        e.printStackTrace();
                        Log.i(MainActivity.TAG, "done: " + e.getMessage());
                    } else {
                        flag = true;
                    }
                }
            }
        });

        //注册
        MainActivity.user.signUp(new SaveListener<Object>() {
            @Override
            public void done(Object o, BmobException e) {
                if (e == null) {
                    if (chk.isChecked()) {
                        editor.putString("userName", nickname);
                        editor.putString("Password", opid);
                        editor.commit();
                    } else {
                        editor.remove("userName");
                        editor.remove("Password");
                        editor.commit();
                    }


                    MainActivity.user.setLogin_type("qq");
                    MainActivity.user.setValue("out_name", nickname);
                    MainActivity.user.setValue("login_type", "qq");
                    MainActivity.user.setOut_name(nickname);
                    if (icon != null) {
                        MainActivity.user.setOut_icon(icon);
                        MainActivity.user.setValue("out_icon", icon);
                    }


                    Toast.makeText(LoginActivity.this, "登录成功!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    intent.setClass(LoginActivity.this, MainActivity.class);
                    MainActivity.in_type = "signup";

                    startActivity(intent);
                } else {
                    if (flag) {
                        e.printStackTrace();
                        Log.i(MainActivity.TAG, "done: " + e.getMessage());
                    } else {
                        flag = true;
                    }
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
                btnLogIn.setTextColor(0xFFFFFFFF);
                btnLogIn.setEnabled(true);
            }
            // 在layout文件中，对Button的text属性应预先设置默认值，否则刚打开程序的时候Button是无显示的
            else {
                btnLogIn.setTextColor(0xFFD0EFC6);
                btnLogIn.setEnabled(false);
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    }

}
