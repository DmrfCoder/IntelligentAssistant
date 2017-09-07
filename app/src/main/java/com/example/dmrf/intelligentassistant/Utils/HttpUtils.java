package com.example.dmrf.intelligentassistant.Utils;

import android.util.Log;

import com.example.dmrf.intelligentassistant.Activity.MainActivity;
import com.example.dmrf.intelligentassistant.Bean.ChatMessage;
import com.example.dmrf.intelligentassistant.Bean.Result;
import com.example.dmrf.intelligentassistant.Bean.StandardList;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by DMRF on 2017/8/6.
 */

public class HttpUtils {
    private static final String mURL = "http://www.tuling123.com/openapi/api";
    private static final String APIKEY = "97c9c70eef104882906b0cbebfccbd94";
    static String result;

    /**
     * 发送一个消息，得到回复的消息
     *
     * @param msg
     * @return
     * @throws UnsupportedEncodingException
     */
    public static ChatMessage sendMessage(String msg) throws IOException {
        ChatMessage chatMessage = new ChatMessage();
        String json = doPOSt(msg);
        Gson gson = new Gson();
        Result result = new Result();
        if (json == null) {
            chatMessage.setMsg("您的客户端出现了严重问题，请联系管理员！");
        } else {
            String str = "";

            try {
                result = gson.fromJson(json, Result.class);


                if (result.getText() != null) {
                    str += result.getText()+"\n";
                }

                if (result.getList() != null) {
                    for (StandardList l : result.getList()) {
                        if (l.getArticle() != null) {
                            str += "\n" + "标题：" + l.getArticle() + "\n";
                        }
                        if (l.getName() != null) {
                            str += "\n" + "片名：" + l.getName() + "\n";
                        }

                        if (l.getInfo() != null) {
                            str += "\n" + "主题：" + l.getInfo() + "\n";
                        }

                        if (l.getIcon() != null) {
                            str += "\n" + "主图：" + l.getIcon() + "\n";
                        }
                        if (l.getSource() != null) {
                            str += "\n" + "信息来源：" + l.getSource() + "\n";
                        }
                        if (l.getDetailurl() != null) {
                            str += "\n" + "详情：" + l.getDetailurl() + "\n";
                        }
                    }
                }


                if (result.getUrl() != null) {
                    str += "\n详情：" + result.getUrl();
                }

                if (str != null) {
                    chatMessage.setMsg(str);
                }



            } catch (JsonSyntaxException e) {
                chatMessage.setMsg("服务器繁忙，请稍后再试！");
            }
        }
        long d = System.currentTimeMillis() + 1000;
        Date de = new Date(d);
        chatMessage.setDate(de);

        chatMessage.setisNew(true);
        chatMessage.setType(ChatMessage.Type.INCOMING);

        return chatMessage;
    }

    public static String doPOSt(String msg) throws IOException {

        OkHttpClient okHttpClient = new OkHttpClient();
        FormBody.Builder builder = new FormBody.Builder();
        Log.i("userid", "doPOSt: " + MainActivity.user.getObjectId());
        FormBody formBody = builder.add("key", APIKEY).add("info", msg).add("userid", MainActivity.user.getObjectId()).build();
        Request.Builder builder1 = new Request.Builder();
        final Request request = builder1.url(mURL).post(formBody).build();
        Call call = okHttpClient.newCall(request);
        Response response = call.execute();
        if (response.isSuccessful()) {

            result = response.body().string();
        } else {
            result = null;
        }

        return result;
    }

}
