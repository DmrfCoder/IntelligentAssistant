package com.example.dmrf.intelligentassistant.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dmrf.intelligentassistant.ActivityManager.ActivityManager;
import com.example.dmrf.intelligentassistant.R;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by DMRF on 2017/8/9.
 */

public class SetNameActivity extends Activity {
    private String type;
    private TextView setting_name_oldname_txt;
    private TextView setting_name_oldname;
    private TextView setting_name_newname_txt;
    private EditText setting_name_newname;
    private Button setting_name_cancel;
    private Button setting_name_sure;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_setting_name_second);
        ActivityManager.getInstance().addActivity(SetNameActivity.this);


        initViews();
        initListener();
        Intent intent = getIntent();
        type = intent.getStringExtra("type");
        if (type.equals("in")) {
            setting_name_oldname_txt.setText("智能助手的原昵称：");
            setting_name_oldname.setText(MainActivity.user.getIn_name());
            setting_name_newname_txt.setText("智能助手的新昵称：");
        } else {
            setting_name_oldname_txt.setText("您的原昵称：");
            setting_name_oldname.setText(MainActivity.user.getOut_name());
            setting_name_newname_txt.setText("您的新昵称：");
        }

    }

    private void initListener() {
        setting_name_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SetNameActivity.this.finish();
            }
        });

        setting_name_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (setting_name_newname.getText().toString().equals("")) {
                    Toast.makeText(SetNameActivity.this, "昵称不能为空！", Toast.LENGTH_SHORT).show();
                } else {
                    String name = setting_name_newname.getText().toString();
                    if (type.equals("in")) {
                        MainActivity.user.setValue("in_name", name);
                        MainActivity.user.setIn_name(name);
                    } else {
                        MainActivity.user.setValue("out_name", name);
                        MainActivity.user.setOut_name(name);
                    }

                    MainActivity.user.update(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {

                            Toast.makeText(SetNameActivity.this, "修改成功！", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(SetNameActivity.this, MainActivity.class);
                            intent.putExtra("update","true");
                            startActivity(intent);
                            SetNameActivity.this.finish();
                        }
                    });
                }

            }
        });


    }

    private void initViews() {

        setting_name_oldname_txt = findViewById(R.id.setting_name_oldname_txt);
        setting_name_oldname = findViewById(R.id.setting_name_oldname);
        setting_name_newname_txt = findViewById(R.id.setting_name_newname_txt);
        setting_name_newname = findViewById(R.id.setting_name_newname);
        setting_name_cancel = findViewById(R.id.setting_name_cancel);
        setting_name_sure = findViewById(R.id.setting_name_sure);

    }
}
