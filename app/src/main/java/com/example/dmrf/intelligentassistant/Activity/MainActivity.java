package com.example.dmrf.intelligentassistant.Activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityCompat.OnRequestPermissionsResultCallback;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dmrf.intelligentassistant.ActivityManager.ActivityManager;
import com.example.dmrf.intelligentassistant.Adapter.ChatMessageAdapter;
import com.example.dmrf.intelligentassistant.Bean.ChatMessage;
import com.example.dmrf.intelligentassistant.R;
import com.example.dmrf.intelligentassistant.Bean.User;
import com.example.dmrf.intelligentassistant.Utils.BitmapAndStringUtils;
import com.example.dmrf.intelligentassistant.Utils.HttpUtils;
import com.example.dmrf.intelligentassistant.Utils.VoiceRecognitionUtils;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;


public class MainActivity extends Activity implements OnRequestPermissionsResultCallback {

    private static boolean flag;

    public static User user = new User();

    public static String Username;
    public static String Userid;

    //上次按下返回键的系统时间
    private long lastBackTime = 0;

    //当前按下返回键的系统时间
    private long currentBackTime = 0;

    private ListView list_msg;

    private ChatMessageAdapter mAdapter;

    public static List<ChatMessage> mDates;


    private EditText input_msg;


    private Button send_msg;

    private TextView main_title;

    private Intent intent;

    private ImageButton Setting;

    private int Num;

    public static String in_type = "";

    public static String user_type = "";

    public static String TAG = "information";

    private String NotDo = "";

    private Button btn_sad;

    private ImageButton change_input_type;

    private Intent resultIntent;

    private RecognizerDialog mDialog;


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Bmob.initialize(this, "0c16c3810a6216cc61493b68e3595eb0");
        SpeechUtility.createUtility(this, SpeechConstant.APPID + "=5993ef9f");

        ActivityManager.getInstance().addActivity(MainActivity.this);
        intent = getIntent();
        Username = user.getUsername();


        String update = intent.getStringExtra("update");
        NotDo = update;

        Log.i(TAG, "onCreate: " + user.getObjectId());
        if (in_type.equals("signup")) {
            initUser();
        }

        //初始化View
        initView();


        if (update != null) {
            if (update.equals("true")) {
                Log.i("information", "NAME" + user.getIn_name());
                mAdapter = new ChatMessageAdapter(MainActivity.this, mDates);
                list_msg.setAdapter(mAdapter);
                list_msg.setSelection(list_msg.getBottom());
            }
        }

        if (update!=null){
            if (update.equals("notdo")){
                mDates.clear();
                mDates.add(new ChatMessage("你好，可爱的小岗为您服务（害羞）", ChatMessage.Type.INCOMING, new Date()));
                mAdapter = new ChatMessageAdapter(MainActivity.this, mDates);
                list_msg.setAdapter(mAdapter);
                list_msg.setSelection(list_msg.getBottom());
            }else {
                //初始化数据
                initDatas();
            }
        }else {
            //初始化数据
            initDatas();
        }




        //初始化事件
        initListener();
    }

    private void initUser() {

        //out的icon
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.person_me);

        //in的icon
        Bitmap bitmap2 = BitmapFactory.decodeResource(getResources(), R.drawable.log_in_icon);

        String out_icon = BitmapAndStringUtils.convertIconToString(bitmap);
        String in_icon = BitmapAndStringUtils.convertIconToString(bitmap2);

        MainActivity.user.setIn_icon(in_icon);

        if (MainActivity.user.getLogin_type() != null) {
            if (!MainActivity.user.getLogin_type().equals("qq")) {
                MainActivity.user.setOut_icon(out_icon);
                MainActivity.user.setValue("out_icon", out_icon);
                MainActivity.user.setOut_name("我");
                MainActivity.user.setValue("out_name", "我");
            }
        } else {
            MainActivity.user.setOut_icon(out_icon);
            MainActivity.user.setValue("out_icon", out_icon);
            MainActivity.user.setOut_name("我");
            MainActivity.user.setValue("out_name", "我");
        }


        MainActivity.user.setIn_name("小岗");


        MainActivity.user.setValue("in_icon", in_icon);
        MainActivity.user.setValue("in_name", "小岗");
        MainActivity.user.setValue("num", 0);

        Bitmap bitmapback = BitmapFactory.decodeResource(getResources(), R.drawable.main_background);
        String mainback = BitmapAndStringUtils.convertIconToString(bitmapback);

        MainActivity.user.setValue("background", mainback);
        MainActivity.user.setBackground(mainback);

        Bitmap bitmapinair = BitmapFactory.decodeResource(getResources(), R.drawable.chatfrom_bg_normal);
        String inair = BitmapAndStringUtils.convertIconToString(bitmapinair);
        MainActivity.user.setValue("in_air", inair);
        MainActivity.user.setIn_air(inair);

        Bitmap bitmapoutair = BitmapFactory.decodeResource(getResources(), R.drawable.chatto_bg_normal);
        String outair = BitmapAndStringUtils.convertIconToString(bitmapoutair);
        MainActivity.user.setValue("out_air", outair);
        MainActivity.user.setOut_air(outair);

        MainActivity.user.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    Log.i("information", "更新成功");
                } else {
                    Log.i("information", "更新失败" + e.getMessage());
                }
            }
        });

    }


    private void initListener() {

        input_msg.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || (event != null && KeyEvent.KEYCODE_ENTER == event.getKeyCode() && KeyEvent.ACTION_DOWN == event.getAction())) {
                    final String tomsg = input_msg.getText().toString();

                    if (TextUtils.isEmpty(tomsg)) {
                        Toast.makeText(MainActivity.this, "发送消息不能为空！", Toast.LENGTH_SHORT).show();
                        return false;
                    }


                    final ChatMessage toMessage = new ChatMessage();
                    long d = System.currentTimeMillis() - 1000;

                    Date de = new Date(d);
                    toMessage.setDate(de);
                    toMessage.setType(ChatMessage.Type.OUTCOMING);
                    toMessage.setMsg(tomsg);
                    toMessage.setisNew(true);
                    toMessage.setNum(mDates.size());
                    toMessage.setName(user.getUsername());

                    mDates.add(toMessage);
                    //刷新数据
                    mAdapter.notifyDataSetChanged();
                    list_msg.setSelection(mDates.size() - 1);
                    input_msg.setText("");
                    new SendMessage(tomsg, toMessage).start();

                }
                return false;
            }
        });


        input_msg.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    final String tomsg = input_msg.getText().toString();

                    if (TextUtils.isEmpty(tomsg)) {
                        Toast.makeText(MainActivity.this, "发送消息不能为空！", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                    final ChatMessage toMessage = new ChatMessage();
                    long d = System.currentTimeMillis() - 1000;

                    Date de = new Date(d);
                    toMessage.setDate(de);
                    toMessage.setType(ChatMessage.Type.OUTCOMING);
                    toMessage.setMsg(tomsg);
                    toMessage.setisNew(true);
                    toMessage.setNum(mDates.size());
                    toMessage.setName(user.getUsername());

                    mDates.add(toMessage);
                    //刷新数据
                    mAdapter.notifyDataSetChanged();
                    list_msg.setSelection(mDates.size() - 1);
                    input_msg.setText("");
                    new SendMessage(tomsg, toMessage).start();
                }
                return false;
            }
        });


        send_msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String tomsg = input_msg.getText().toString();

                if (TextUtils.isEmpty(tomsg)) {
                    Toast.makeText(MainActivity.this, "发送消息不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                }
                final ChatMessage toMessage = new ChatMessage();
                long d = System.currentTimeMillis() - 1000;

                Date de = new Date(d);
                toMessage.setDate(de);

                toMessage.setType(ChatMessage.Type.OUTCOMING);
                toMessage.setMsg(tomsg);
                toMessage.setisNew(true);
                toMessage.setNum(mDates.size());
                toMessage.setName(user.getUsername());

                mDates.add(toMessage);
                //刷新数据
                mAdapter.notifyDataSetChanged();
                list_msg.setSelection(mDates.size() - 1);
                input_msg.setText("");
                new SendMessage(tomsg, toMessage).start();
            }
        });

        Setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, SettingActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });

        change_input_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flag) {
                    change_input_type.setBackgroundResource(R.mipmap.voice);
                    input_msg.setVisibility(View.VISIBLE);
                    btn_sad.setVisibility(View.GONE);
                    flag = false;
                } else {
                    change_input_type.setBackgroundResource(R.mipmap.keyboard);
                    input_msg.setVisibility(View.GONE);
                    btn_sad.setVisibility(View.VISIBLE);
                    flag = true;
                }
            }
        });

        btn_sad.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                initSpeech();
                return false;
            }
        });


    }


    private void initDatas() {

        BmobQuery<ChatMessage> query = new BmobQuery<ChatMessage>();
        query.addWhereEqualTo("name", user.getUsername());
      //  query.setLimit(10000000);
        query.order("num");
        query.findObjects(new FindListener<ChatMessage>() {
            @Override
            public void done(List<ChatMessage> list, BmobException e) {
                if (e == null) {

                    for (ChatMessage c : list) {
                        mDates.add(c);
                    }

                    if (NotDo != null) {
                        if (!NotDo.equals("notdo")) {
                            mAdapter = new ChatMessageAdapter(MainActivity.this, mDates);
                            list_msg.setAdapter(mAdapter);
                            list_msg.setSelection(list_msg.getBottom());
                            return;
                        }
                    } else {
                        mAdapter = new ChatMessageAdapter(MainActivity.this, mDates);
                        list_msg.setAdapter(mAdapter);
                        list_msg.setSelection(list_msg.getBottom());
                        return;
                    }

                } else {
                    Log.i("information", "e-->" + e.getMessage());
                }
            }
        });

        if (mDates == null) {
            mDates = new ArrayList<ChatMessage>();
            mDates.add(new ChatMessage("你好，可爱的小岗为您服务（害羞）", ChatMessage.Type.INCOMING, new Date()));
        }
        if (mDates.size() == 0) {
            mDates.add(new ChatMessage("你好，可爱的小岗为您服务（害羞）", ChatMessage.Type.INCOMING, new Date()));
        }
        mAdapter = new ChatMessageAdapter(this, mDates);
        list_msg.setAdapter(mAdapter);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void initView() {
        list_msg = findViewById(R.id.list_all_msgofus);
        input_msg = findViewById(R.id.input_msg);
        send_msg = findViewById(R.id.send_msg);
        Setting = findViewById(R.id.image_btn_setting);
        main_title = findViewById(R.id.main_title);
        btn_sad = findViewById(R.id.btn_sad);
        change_input_type = findViewById(R.id.change_input_type);

        change_input_type.setBackgroundResource(R.mipmap.voice);
        input_msg.setVisibility(View.VISIBLE);
        btn_sad.setVisibility(View.GONE);

        flag = false;


        String title = user.getIn_name();
        if (title != null) {
            main_title.setText(title);
        }

        LayoutInflater layoutInflater = (LayoutInflater) MainActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        RelativeLayout relativeLayout = (RelativeLayout) layoutInflater.inflate(R.layout.activity_main, null);
        String mainback = user.getBackground();
        if (mainback != null) {
            Bitmap back = BitmapAndStringUtils.convertStringToIcon(mainback);
            BitmapDrawable bd = new BitmapDrawable(getResources(), back);
            list_msg.setBackground(bd);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {


        //捕获返回键按下的事件
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //获取当前系统时间的毫秒数
            currentBackTime = System.currentTimeMillis();
            //比较上次按下返回键和当前按下返回键的时间差，如果大于2秒，则提示再按一次退出
            if (currentBackTime - lastBackTime > 2 * 1000) {
                Toast.makeText(this, "再按一次返回键退出", Toast.LENGTH_SHORT).show();
                lastBackTime = currentBackTime;
            } else { //如果两次按下的时间差小于2秒，则退出程序
                ActivityManager.getInstance().exit();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    private class SendMessage extends Thread {

        private final String tomsg;
        private final ChatMessage toMessage;

        public SendMessage(String tomsg, ChatMessage toMessage) {
            this.tomsg = tomsg;
            this.toMessage = toMessage;
        }

        @Override
        public void run() {
            try {
                final ChatMessage fromMessage = HttpUtils.sendMessage(tomsg,MainActivity.this);
                fromMessage.setName(user.getUsername());
                fromMessage.setNum(mDates.size());
                fromMessage.save(new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {

                    }
                });

                toMessage.save(new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {

                    }
                });

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
    }


    /**
     * 初始化语音识别
     */
    public void initSpeech() {

        mDialog = new RecognizerDialog(MainActivity.this, null);
        //2.设置accent、language等参数
        mDialog.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
        mDialog.setParameter(SpeechConstant.ACCENT, "mandarin");
        //3.设置回调接口
        mDialog.setListener(new RecognizerDialogListener() {
            @Override
            public void onResult(RecognizerResult recognizerResult, boolean isLast) {
                if (!isLast) {
                    //解析语音
                    String tomsg = VoiceRecognitionUtils.parseVoice(recognizerResult.getResultString());
                    if (TextUtils.isEmpty(tomsg)) {
                        Toast.makeText(MainActivity.this, "发送消息不能为空！", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    final ChatMessage toMessage = new ChatMessage();
                    long d = System.currentTimeMillis() - 1000;

                    Date de = new Date(d);
                    toMessage.setDate(de);

                    toMessage.setType(ChatMessage.Type.OUTCOMING);
                    toMessage.setMsg(tomsg);
                    toMessage.setisNew(true);
                    toMessage.setNum(mDates.size());
                    toMessage.setName(user.getUsername());

                    mDates.add(toMessage);
                    //刷新数据
                    mAdapter.notifyDataSetChanged();
                    list_msg.setSelection(mDates.size() - 1);
                    input_msg.setText("");
                    new SendMessage(tomsg, toMessage).start();
                }
            }

            @Override
            public void onError(SpeechError speechError) {

            }
        });
        //4.显示dialog，接收语音输入
        mDialog.show();
    }


    /**
     * 请求授权
     */
    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_SETTINGS) != PackageManager.PERMISSION_DENIED) { //表示未授权时
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.CAMERA)) {
                    //用于开发者提示用户权限的用途
                    Toast.makeText(MainActivity.this, "此权限用于语音识别", Toast.LENGTH_SHORT).show();
                } else {
                    //申请权限
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.CAMERA},
                            1);
                }
                return;
            } else {
                //调用语音识别的方法
                initSpeech();
            }
        } else {
            initSpeech();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) { //同意权限申请
                    initSpeech();
                } else { //拒绝权限申请
                    Toast.makeText(MainActivity.this, "拒绝授予权限后您将无法使用语音识别功能！", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

}
