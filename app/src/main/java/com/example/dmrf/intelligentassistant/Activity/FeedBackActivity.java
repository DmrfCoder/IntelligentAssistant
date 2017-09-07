package com.example.dmrf.intelligentassistant.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.dmrf.intelligentassistant.ActivityManager.ActivityManager;
import com.example.dmrf.intelligentassistant.Bean.FeedBack;
import com.example.dmrf.intelligentassistant.R;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by DMRF on 2017/8/9.
 */

public class FeedBackActivity extends Activity {
    private FeedBack feedBack;
    private EditText setting_feedback_contact;
    private EditText setting_feedback_content;
    private Button setting_feedbacl_cancel;
    private Button setting_feedback_sure;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_setting_feedback);
        Bmob.initialize(this, "0c16c3810a6216cc61493b68e3595eb0");
        feedBack = new FeedBack();
        ActivityManager.getInstance().addActivity(FeedBackActivity.this);
        initViews();
        initListener();
    }

    private void initListener() {
        setting_feedbacl_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FeedBackActivity.this.finish();
            }
        });
        setting_feedback_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String contact = "";
                contact = setting_feedback_contact.getText().toString().trim();
                String feedback = setting_feedback_content.getText().toString().trim();
                if (feedback.equals("") || feedback == null) {
                    Toast.makeText(FeedBackActivity.this, "反馈信息不能为空！", Toast.LENGTH_SHORT).show();
                } else {
                    feedBack.setContact(contact);
                    feedBack.setFeedback(feedback);
                    feedBack.save(new SaveListener<String>() {
                        @Override
                        public void done(String s, BmobException e) {
                            if (e == null) {
                                Toast.makeText(FeedBackActivity.this, "反馈成功！", Toast.LENGTH_SHORT).show();
                                Intent in = new Intent(FeedBackActivity.this, MainActivity.class);
                                startActivity(in);
                            } else {
                                Toast.makeText(FeedBackActivity.this, "反馈失败！", Toast.LENGTH_SHORT).show();
                                Intent in = new Intent(FeedBackActivity.this, MainActivity.class);
                                startActivity(in);
                            }
                        }
                    });
                }
            }
        });

    }

    private void initViews() {
        setting_feedback_contact = findViewById(R.id.setting_feedback_contact);
        setting_feedback_content = findViewById(R.id.setting_feedback_content);
        setting_feedbacl_cancel = findViewById(R.id.setting_feedbacl_cancel);
        setting_feedback_sure = findViewById(R.id.setting_feedback_sure);
    }
}
