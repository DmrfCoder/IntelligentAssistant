package com.example.dmrf.intelligentassistant.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.dmrf.intelligentassistant.ActivityManager.ActivityManager;
import com.example.dmrf.intelligentassistant.Bean.ChatMessage;
import com.example.dmrf.intelligentassistant.R;

import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by DMRF on 2017/8/8.
 */

public class SettingActivity extends Activity {
    private Button setting_icon;
    private Button setting_person_name;
    private Button setting_background;
    private Button setting_feedback;
    private Button exit_login;
    private Button setting_function_introduce;
    private Button setting_clear_record;
    private String imageName;
    private static final int PHOTO_REQUEST_TAKEPHOTO = 1;// 拍照
    private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
    private static final int PHOTO_REQUEST_CUT = 3;// 结果

    int mCheck = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.latout_settings);
        Bmob.initialize(this, "0c16c3810a6216cc61493b68e3595eb0");
        ActivityManager.getInstance().addActivity(SettingActivity.this);
        initViews();
        initListener();
    }

    private void initListener() {


        setting_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {





                LayoutInflater mLI = (LayoutInflater) SettingActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final RelativeLayout mRL = (RelativeLayout) mLI.inflate(R.layout.select_which_icon, null);
                final RadioGroup mRadioGroup = mRL.findViewById(R.id.select_which_icon_radiogroup);
                final RadioButton in = mRL.findViewById(R.id.set_in_icon);
                RadioButton out = mRL.findViewById(R.id.set_out_icon);
                in.setChecked(true);

                mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                        if (i == in.getId()) {
                            mCheck = 1;
                        } else {
                            mCheck = 2;
                        }
                    }
                });

                AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
                builder.setTitle("您要为谁设置头像?");
                builder.setView(mRL);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (mCheck == 1) {
                            Intent intent = new Intent();
                            intent.setClass(SettingActivity.this, SetIconActivity.class);
                            intent.putExtra("type", "in");
                            SettingActivity.this.startActivity(intent);
                        } else {
                            Intent intent = new Intent();
                            intent.setClass(SettingActivity.this, SetIconActivity.class);
                            intent.putExtra("type", "out");
                            SettingActivity.this.startActivity(intent);
                        }
                    }
                });
                builder.setNegativeButton("取消", null);
                builder.show();

            }
        });


        setting_person_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater mLI = (LayoutInflater) SettingActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final RelativeLayout mRL = (RelativeLayout) mLI.inflate(R.layout.select_which_name, null);
                final RadioGroup mRadioGroup = mRL.findViewById(R.id.select_which_name_radiogroup);
                final RadioButton in = mRL.findViewById(R.id.set_in_name);
                RadioButton out = mRL.findViewById(R.id.set_out_name);
                in.setChecked(true);

                mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                        if (i == in.getId()) {
                            mCheck = 1;
                        } else {
                            mCheck = 2;
                        }
                    }
                });

                AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
                builder.setTitle("您要为谁设置昵称?");
                builder.setView(mRL);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (mCheck == 1) {
                            Intent intent = new Intent();
                            intent.setClass(SettingActivity.this, SetNameActivity.class);
                            intent.putExtra("type", "in");
                            SettingActivity.this.startActivity(intent);
                        } else {
                            Intent intent = new Intent();
                            intent.setClass(SettingActivity.this, SetNameActivity.class);
                            intent.putExtra("type", "out");
                            SettingActivity.this.startActivity(intent);
                        }
                    }
                });
                builder.setNegativeButton("取消", null);
                builder.show();
            }
        });


        setting_background.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingActivity.this, SetMainBackgroundActivity.class);
                startActivity(intent);
            }
        });


        setting_feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingActivity.this, FeedBackActivity.class);
                startActivity(intent);
            }
        });


        exit_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = (LayoutInflater) SettingActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                LinearLayout linearLayout = (LinearLayout) inflater.inflate(R.layout.activity_log_in, null);
                if (MainActivity.mDates != null) {
                    MainActivity.mDates.clear();
                }
                Intent intent = new Intent(SettingActivity.this, LoginActivity.class);
                intent.putExtra("exit_login", "true");
                startActivity(intent);
            }
        });

        setting_function_introduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(SettingActivity.this, FunctionIntroduceActivity.class);
                startActivity(intent);
            }
        });


        setting_clear_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
                builder.setTitle("删除聊天记录");
                builder.setMessage("确认删除与智能助手的聊天记录");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        BmobQuery<ChatMessage> query = new BmobQuery<ChatMessage>();
                        query.addWhereEqualTo("name", MainActivity.user.getUsername());
                        query.setLimit(10000000);
                        query.findObjects(new FindListener<ChatMessage>() {
                            @Override
                            public void done(List<ChatMessage> list, BmobException e) {
                                if (e == null) {
                                    Log.i("information", "log----->2");
                                    // finalMDates = new ArrayList<ChatMessage>();
                                    for (ChatMessage c : list) {
                                        final ChatMessage t = new ChatMessage();
                                        t.setObjectId(c.getObjectId());
                                        t.delete(new UpdateListener() {
                                            @Override
                                            public void done(BmobException e) {
                                                if (e == null) {
                                                    Log.i(MainActivity.TAG, "deletesuccess -->" + t.getObjectId());
                                                }
                                            }
                                        });
                                    }


                                } else {
                                    Log.i("information", "e-->" + e.getMessage());
                                }
                            }
                        });


                        MainActivity.mDates.clear();
                        Toast.makeText(SettingActivity.this, "聊天记录删除成功!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SettingActivity.this, MainActivity.class);
                        intent.putExtra("update", "notdo");
                        startActivity(intent);
                    }
                });

                builder.setNegativeButton("取消", null);
                builder.show();


            }
        });
    }

    private void initViews() {
        setting_icon = findViewById(R.id.setting_icon);
        setting_person_name = findViewById(R.id.setting_person_name);
        setting_background = findViewById(R.id.setting_background);
        setting_feedback = findViewById(R.id.setting_feedback);
        exit_login = findViewById(R.id.exit_login);
        setting_function_introduce = findViewById(R.id.setting_function_introduce);
        setting_clear_record = findViewById(R.id.setting_clear_record);
    }

}
