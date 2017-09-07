package com.example.dmrf.intelligentassistant.Utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by DMRF on 2017/8/18.
 */

public class GetPrictureFromNetByUrlUtils {


    public static void LoadingPicture(String url, final Handler mHandler) {


        OkHttpClient okHttpClient = new OkHttpClient();

        Request.Builder builder = new Request.Builder();
        Request request = builder.url(url).build();

        Call call = okHttpClient.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("infomation", "onFailure: " + e.getMessage());
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                byte[] Picture = response.body().bytes();
                Bitmap bitmap;
                //使用BitmapFactory工厂，把字节数组转化为bitmap
                bitmap = BitmapFactory.decodeByteArray(Picture, 0, Picture.length);

                Message message = mHandler.obtainMessage();
                message.obj = bitmap;

                mHandler.sendMessage(message);


            }
        });


    }
}
