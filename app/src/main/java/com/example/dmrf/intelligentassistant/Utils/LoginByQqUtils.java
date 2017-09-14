package com.example.dmrf.intelligentassistant.Utils;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQToken;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DMRF on 2017/8/18.
 */

public class LoginByQqUtils {

    private static Context context;
    private static Handler qqHandler;
    private static Tencent mTencent;
    private static List<String> list = new ArrayList<String>();
    private static String SCOPE = "get_simple_userinfo,add_topic";


    public static IUiListener listener = new IUiListener() {
        /** 授权失败的回调*/
        @Override
        public void onError(UiError arg0) {
            // TODO Auto-generated method stub
            Toast.makeText(context, "授权失败", Toast.LENGTH_SHORT).show();
            Message msg = new Message();
            msg.arg1 = 2;


            qqHandler.sendMessage(msg);



        }


        /** 授权成功的回调*/
        @Override
        public void onComplete(Object arg0) {
            // TODO Auto-generated method stub
            Toast.makeText(context, "授权成功", Toast.LENGTH_SHORT).show();

            String result = arg0.toString();
            JSONObject data;
            try {
                data = new JSONObject(result);
                String pay_token = data.getString("pay_token");
                String expires_in = data.getString("expires_in");
                String openid = data.getString("openid");
                String access_token = data.getString("access_token");

                list.add(openid);


                mTencent.setOpenId(openid);
                mTencent.setAccessToken(access_token, expires_in);

                final QQToken qqToken = mTencent.getQQToken();
                UserInfo info = new UserInfo(context, qqToken);

                info.getUserInfo(new IUiListener() {
                    @Override
                    public void onComplete(Object o) {
                        String r = o.toString();
                        JSONObject js;
                        try {
                            js = new JSONObject(r);
                            String nick = js.getString("nickname");
                            list.add(nick);

                            String icon = js.getString("figureurl_1");
                            list.add(icon);

                            Message message = qqHandler.obtainMessage();
                            message.obj = list;
                            boolean isok= qqHandler.sendMessage(message);
                            if (!isok){

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(UiError uiError) {

                    }

                    @Override
                    public void onCancel() {

                    }
                });

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }


        }

        /** 取消授权的回调*/
        @Override
        public void onCancel() {
            // TODO Auto-generated method stub
            Toast.makeText(context, "取消授权", Toast.LENGTH_SHORT).show();
        }
    };


    public static void LogInByQQ(Context context1, Handler qqHandler1) {
        context = context1;
        qqHandler = qqHandler1;
        mTencent = Tencent.createInstance("1106273959", context.getApplicationContext());
        if (!mTencent.isSessionValid()) {
            mTencent.login((Activity) context, SCOPE, listener);
        } else {
            mTencent.logout(context);
        }
    }


}
