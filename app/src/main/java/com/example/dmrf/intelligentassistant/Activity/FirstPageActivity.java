package com.example.dmrf.intelligentassistant.Activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.example.dmrf.intelligentassistant.ActivityManager.ActivityManager;
import com.example.dmrf.intelligentassistant.Bean.User;
import com.example.dmrf.intelligentassistant.R;
import com.example.dmrf.intelligentassistant.Utils.NetWorkUtils;

import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

public class FirstPageActivity extends Activity {
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private boolean flag;
    private String name;
    private String key;
    private String auto;
    //上次按下返回键的系统时间
    private long lastBackTime = 0;

    //当前按下返回键的系统时间
    private long currentBackTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_first_page);
        ActivityManager.getInstance().addActivity(FirstPageActivity.this);
        Bmob.initialize(this, "0c16c3810a6216cc61493b68e3595eb0");
        pref = getSharedPreferences("UserInfo", MODE_PRIVATE);
        //启用编辑
        editor = pref.edit();
        name = pref.getString("userName", "");
        key = pref.getString("Password", "");
        auto = pref.getString("auto", "");
        BeginLogIn();
    }


    private void BeginLogIn() {

        Intent intent = new Intent();

        flag = false;

        if (NetWorkUtils.getAPNType(FirstPageActivity.this) == 0) {
            intent.setClass(FirstPageActivity.this, LoginAndSignUpActivity.class);
            startActivity(intent);
            return;
        }


        final String username = name;
        final String password = key;

        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            intent.setClass(FirstPageActivity.this, LoginAndSignUpActivity.class);
            startActivity(intent);
        }


        MainActivity.user.setPassword(password);
        MainActivity.user.setUsername(password);


        MainActivity.user.login(new SaveListener<Object>() {
            @Override
            public void done(Object o, BmobException e) {


                if (e == null) {
                    BmobQuery<User> query = new BmobQuery<User>();
                    query.addWhereEqualTo("username", password);
                    query.findObjects(new FindListener<User>() {
                        @Override
                        public void done(List<User> list, BmobException e) {
                            if (e == null) {
                                MainActivity.user = list.get(0);
                                Intent intent = new Intent(FirstPageActivity.this, MainActivity.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(FirstPageActivity.this, "登录失败" + e.getMessage(), Toast.LENGTH_LONG).show();
                                e.printStackTrace();
                            }
                        }
                    });

                } else {
                    if (flag) {
                        Toast.makeText(FirstPageActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        Intent intent1 = new Intent();
                        intent1.setClass(FirstPageActivity.this, LoginAndSignUpActivity.class);
                        startActivity(intent1);
                        FirstPageActivity.this.finish();
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
                    BmobQuery<User> query = new BmobQuery<User>();
                    query.addWhereEqualTo("username", username);
                    query.findObjects(new FindListener<User>() {
                        @Override
                        public void done(List<User> list, BmobException e) {
                            if (e == null) {
                                MainActivity.user = list.get(0);
                                Intent intent = new Intent(FirstPageActivity.this, MainActivity.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(FirstPageActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }
                        }
                    });

                } else {
                    if (flag) {
                        Toast.makeText(FirstPageActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        Intent intent1 = new Intent();
                        intent1.setClass(FirstPageActivity.this, LoginAndSignUpActivity.class);
                        startActivity(intent1);
                        FirstPageActivity.this.finish();
                    } else {
                        flag = true;
                    }
                }
            }
        });

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {


        //捕获返回键按下的事件
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //获取当前系统时间的毫秒数
            currentBackTime = System.currentTimeMillis();
            //比较上次按下返回键和当前按下返回键的时间差，如果大于2秒，则提示再按一次退出
            if (currentBackTime - lastBackTime > 2 * 1000) {
                lastBackTime = currentBackTime;
            } else { //如果两次按下的时间差小于2秒，则退出程序
                ActivityManager.getInstance().exit();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
