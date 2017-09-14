package com.example.dmrf.intelligentassistant.Activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dmrf.intelligentassistant.ActivityManager.ActivityManager;
import com.example.dmrf.intelligentassistant.R;
import com.example.dmrf.intelligentassistant.Utils.BitmapAndStringUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;


public class SetIconActivity extends AppCompatActivity implements View.OnClickListener {
    //用于展示选择的图片
    private ImageView mImageView;
    private String imageName;
    private static final int CAMERA_CODE = 1;
    private static final int GALLERY_CODE = 2;
    private static final int CROP_CODE = 3;
    private static final int PHOTO_REQUEST_TAKEPHOTO = 1;// 拍照
    private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
    private static final int PHOTO_REQUEST_CUT = 3;// 结果

    private static String type;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_setting_icon);
        Bmob.initialize(this, "0c16c3810a6216cc61493b68e3595eb0");
        ActivityManager.getInstance().addActivity(SetIconActivity.this);
        intent = getIntent();
        type = intent.getStringExtra("type");
        initView();

        //  Toast.makeText(SetIconActivity.this, "type:" + type, Toast.LENGTH_SHORT).show();

    }

    private void initView() {
        mImageView = (ImageView) findViewById(R.id.show_image);
        Bitmap bitmap;
        if (type.equals("in")) {

            bitmap = BitmapAndStringUtils.convertStringToIcon(MainActivity.user.getIn_icon());
        } else {
            bitmap = BitmapAndStringUtils.convertStringToIcon(MainActivity.user.getOut_icon());
        }
        mImageView.setImageBitmap(bitmap);


        Button chooseCamera = (Button) findViewById(R.id.choose_camera);
        chooseCamera.setOnClickListener(this);
        Button chooseGallery = (Button) findViewById(R.id.choose_gallery);
        chooseGallery.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.choose_camera:
                //拍照选择
                imageName = getNowTime() + ".png";
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // 指定调用相机拍照后照片的储存路径
                intent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(new File("/sdcard/fanxin/", imageName)));
                startActivityForResult(intent, PHOTO_REQUEST_TAKEPHOTO);
                break;
            case R.id.choose_gallery:
                //从相册选取
                getNowTime();
                imageName = getNowTime() + ".png";
                Intent intent2 = new Intent(Intent.ACTION_PICK, null);
                intent2.setDataAndType(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent2, PHOTO_REQUEST_GALLERY);

                break;
            default:
                break;
        }
    }


    @SuppressLint("SimpleDateFormat")
    private String getNowTime() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddHHmmssSS");
        return dateFormat.format(date);
    }


    @SuppressLint("SdCardPath")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PHOTO_REQUEST_TAKEPHOTO:

                    startPhotoZoom(
                            Uri.fromFile(new File("/sdcard/fanxin/", imageName)),
                            480);
                    break;

                case PHOTO_REQUEST_GALLERY:
                    if (data != null)
                        startPhotoZoom(data.getData(), 480);
                    break;

                case PHOTO_REQUEST_CUT:
//                BitmapFactory.Options options = new BitmapFactory.Options();
//
//                /**
//                 * 最关键在此，把options.inJustDecodeBounds = true;
//                 * 这里再decodeFile()，返回的bitmap为空
//                 * ，但此时调用options.outHeight时，已经包含了图片的高了
//                 */
//                options.inJustDecodeBounds = true;
                    Bitmap bitmap = BitmapFactory.decodeFile("/sdcard/fanxin/"
                            + imageName);
                    mImageView.setImageBitmap(bitmap);

                    String pic = BitmapAndStringUtils.convertIconToString(bitmap);
                    if (type.equals("in")) {
                        MainActivity.user.setValue("in_icon",pic);
                        MainActivity.user.setIn_icon(pic);
                    } else {
                        MainActivity.user.setValue("out_icon",pic);
                        MainActivity.user.setOut_icon(pic);
                    }
                    MainActivity.user.update(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            Toast.makeText(SetIconActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(SetIconActivity.this, MainActivity.class);
                            intent.putExtra("update","true");
                            startActivity(intent);
                            SetIconActivity.this.finish();
                        }
                    });


                    break;

            }
            super.onActivityResult(requestCode, resultCode, data);

        }
    }


    @SuppressLint("SdCardPath")
    private void startPhotoZoom(Uri uri1, int size) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri1, "image/*");
        // crop为true是设置在开启的intent中设置显示的view可以剪裁
        intent.putExtra("crop", "true");

        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);

        // outputX,outputY 是剪裁图片的宽高
        intent.putExtra("outputX", size);
        intent.putExtra("outputY", size);
        intent.putExtra("return-data", false);

        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                Uri.fromFile(new File("/sdcard/fanxin/", imageName)));
        intent.putExtra("outputFormat", Bitmap.CompressFormat.PNG.toString());
        intent.putExtra("noFaceDetection", true); // no face detection
        startActivityForResult(intent, PHOTO_REQUEST_CUT);
    }

}


