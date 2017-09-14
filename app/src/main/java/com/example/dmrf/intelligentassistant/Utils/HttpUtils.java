package com.example.dmrf.intelligentassistant.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.util.Log;

import com.example.dmrf.intelligentassistant.Activity.MainActivity;
import com.example.dmrf.intelligentassistant.Bean.ChatMessage;
import com.example.dmrf.intelligentassistant.Bean.Result;
import com.example.dmrf.intelligentassistant.Bean.StandardList;
import com.example.dmrf.intelligentassistant.R;
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
    private static Context context;
    private static BitmapDrawable drawable;
    ;


    /**
     * 发送一个消息，得到回复的消息
     *
     * @param msg
     * @return
     * @throws UnsupportedEncodingException
     */
    public static ChatMessage sendMessage(String msg, Context context) throws IOException {
        ChatMessage chatMessage = new ChatMessage();
        String json = doPOSt(msg);
        Gson gson = new Gson();
        Result result = new Result();
        if (json == null) {
            chatMessage.setMsg("您的客户端出现了严重问题，请联系管理员！");
        } else {

            SpannableStringBuilder spannableString = new SpannableStringBuilder();


            try {
                result = gson.fromJson(json, Result.class);


                if (result.getText() != null) {
                    spannableString.append(result.getText() + "\n");

                }

                if (result.getList() != null) {
                    for (StandardList l : result.getList()) {
                        if (l.getArticle() != null) {
                            spannableString.append("\n" + "标题：" + l.getArticle() + "\n");
                        }
                        if (l.getName() != null) {
                            spannableString.append("\n" + "片名：" + l.getName() + "\n");

                        }

                        if (l.getInfo() != null) {
                            spannableString.append("\n" + "主题：" + l.getInfo() + "\n");
                        }

                        if (l.getIcon() != null) {
                            spannableString.append("\n" + "主图：" + l.getIcon() + "\n");

                            Handler handler = new Handler() {
                                @Override
                                public void handleMessage(Message msg) {
                                    super.handleMessage(msg);
                                    Bitmap bitmap;
                                    bitmap = (Bitmap) msg.obj;

                                    if (bitmap != null) {
                                        drawable = new BitmapDrawable(bitmap);
                                        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());

                                    }

                                }
                            };
                            ImageSpan imageSpan = new ImageSpan(drawable);
                            spannableString.setSpan(imageSpan, spannableString.length(), spannableString.length() + 15, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);

                        }
                        if (l.getSource() != null) {
                            spannableString.append("\n" + "信息来源：" + l.getSource() + "\n");
                        }
                        if (l.getDetailurl() != null) {
                            spannableString.append("\n" + "详情：" + l.getDetailurl() + "\n");
                        }
                    }
                }


                if (result.getUrl() != null) {
                    spannableString.append("\n详情：" + result.getUrl());
                }

                if (spannableString != null) {
                    chatMessage.setMsg(spannableString);
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
