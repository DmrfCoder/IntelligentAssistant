package com.example.dmrf.intelligentassistant;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.io.SequenceInputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
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
            try {
                result = gson.fromJson(json, Result.class);
                if (result.getUrl() != null) {
                    chatMessage.setMsg(result.getText() + "\n" + result.getUrl());
                } else {
                    chatMessage.setMsg(result.getText());
                }

            } catch (JsonSyntaxException e) {
                chatMessage.setMsg("服务器繁忙，请稍后再试！");
            }
        }


        chatMessage.setDate(new Date());
        chatMessage.setType(ChatMessage.Type.INCOMING);

        return chatMessage;
    }

    public static String doPOSt(String msg) throws IOException {

        OkHttpClient okHttpClient = new OkHttpClient();
        FormBody.Builder builder = new FormBody.Builder();
        FormBody formBody = builder.add("key", APIKEY).add("info", msg).build();
        Request.Builder builder1 = new Request.Builder();
        final Request request = builder1.url(mURL).post(formBody).build();
        Call call = okHttpClient.newCall(request);
        Response response = call.execute();
        if (response.isSuccessful()) {
            result = response.body().string();
        } else {
            result = null;
        }

      /*  call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                result = "onFailure!" + e.getMessage();
                Log.i("info", "result:" + result);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //  Log.i("info", response.body().string());
                result = response.body().string();
                Log.i("info", "result:" + result);
            }
        });*/
        return result;
    }

}
