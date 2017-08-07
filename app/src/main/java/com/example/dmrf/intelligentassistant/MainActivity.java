package com.example.dmrf.intelligentassistant;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;


public class MainActivity extends Activity {


    private ListView list_msg;

    private ChatMessageAdapter mAdapter;

    private List<ChatMessage> mDates;


    private EditText input_msg;


    private Button send_msg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //连接服务器（初始化）
        Bmob.initialize(this, "0c16c3810a6216cc61493b68e3595eb0");

        //初始化View
        initView();
        //初始化数据
        initDatas();


        //初始化事件
        initListener();
       /* try {
            Test();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }*/
    }

    private void initListener() {
        send_msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String tomsg = input_msg.getText().toString();

                if (TextUtils.isEmpty(tomsg)) {
                    Toast.makeText(MainActivity.this, "发送消息不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                }

                ChatMessage toMessage = new ChatMessage();
                toMessage.setDate(new Date());
                toMessage.setType(ChatMessage.Type.OUTCOMING);
                toMessage.setMsg(tomsg);

                mDates.add(toMessage);
                //刷新数据
                mAdapter.notifyDataSetChanged();
                list_msg.setSelection(mDates.size() - 1);
                input_msg.setText("");
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            final ChatMessage fromMessage = HttpUtils.sendMessage(tomsg);

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    mDates.add(fromMessage);
                                    //刷新界面
                                    mAdapter.notifyDataSetChanged();
                                    list_msg.setSelection(mDates.size() - 1);

                                }
                            });

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();


            }
        });
    }

    private void initDatas() {

        mDates = new ArrayList<ChatMessage>();
        mDates.add(new ChatMessage("么么哒，可爱的岗岗为您服务（害羞）", ChatMessage.Type.INCOMING, new Date()));

        mAdapter = new ChatMessageAdapter(this, mDates);
        list_msg.setAdapter(mAdapter);
    }

    private void initView() {
        list_msg = findViewById(R.id.list_all_msgofus);
        input_msg = findViewById(R.id.input_msg);
        send_msg = findViewById(R.id.send_msg);
    }


    private void Test() throws IOException {
        Log.i("info", "result1:" + HttpUtils.doPOSt("讲个笑话"));


        /*Log.i("info", "result2:" + HttpUtils.doPOSt("你是谁"));
        Log.i("info", "result2:" + HttpUtils.doPOSt("我好喜欢你"));*/

    }


}
