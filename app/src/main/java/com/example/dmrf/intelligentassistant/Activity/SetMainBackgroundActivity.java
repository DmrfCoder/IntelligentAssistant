package com.example.dmrf.intelligentassistant.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;


public class SetMainBackgroundActivity extends AppCompatActivity implements View.OnClickListener {
    //用于展示选择的图片
    private ImageView mImageView;

    private static final int CAMERA_CODE = 1;
    private static final int GALLERY_CODE = 2;
    private static final int CROP_CODE = 3;


    private String imageName;
    private static final int PHOTO_REQUEST_TAKEPHOTO = 1;// 拍照
    private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
    private static final int PHOTO_REQUEST_CUT = 3;// 结果

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_setting_main_back);
        ActivityManager.getInstance().addActivity(SetMainBackgroundActivity.this);
        intent = getIntent();

        initView();

        //  Toast.makeText(SetIconActivity.this, "type:" + type, Toast.LENGTH_SHORT).show();

    }

    private void initView() {
        mImageView = (ImageView) findViewById(R.id.show_image_back);
        Bitmap bitmap = BitmapAndStringUtils.convertStringToIcon(MainActivity.user.getBackground());
        Log.i("info", "initView: " + bitmap.toString());
        mImageView.setImageBitmap(bitmap);


        Button chooseCamera = (Button) findViewById(R.id.choose_camera_back);
        chooseCamera.setOnClickListener(this);
        Button chooseGallery = (Button) findViewById(R.id.choose_gallery_back);
        chooseGallery.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.choose_camera_back:
                //拍照选择
                imageName = getNowTime() + ".png";
                //拍照选择
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // 指定调用相机拍照后照片的储存路径
                intent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(new File("/sdcard/fanxin/", imageName)));
                startActivityForResult(intent, PHOTO_REQUEST_TAKEPHOTO);
                break;
            case R.id.choose_gallery_back:
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

    /**
     * 拍照选择图片
     */
    private void chooseFromCamera() {
        //构建隐式Intent
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //调用系统相机
        startActivityForResult(intent, CAMERA_CODE);
    }

    /**
     * 从相册选择图片
     */
    private void chooseFromGallery() {
        //构建一个内容选择的Intent
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        //设置选择类型为图片类型
        intent.setType("image/*");
        //打开图片选择
        startActivityForResult(intent, GALLERY_CODE);
    }


    /**
     * 将content类型的Uri转化为文件类型的Uri
     *
     * @param uri
     * @return
     */
    private Uri convertUri(Uri uri) {
        InputStream is;
        try {
            //Uri ----> InputStream
            is = getContentResolver().openInputStream(uri);
            //InputStream ----> Bitmap
            Bitmap bm = BitmapFactory.decodeStream(is);
            //关闭流
            is.close();
            return saveBitmap(bm, "temp");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 将Bitmap写入SD卡中的一个文件中,并返回写入文件的Uri
     *
     * @param bm
     * @param dirPath
     * @return
     */
    private Uri saveBitmap(Bitmap bm, String dirPath) {
        //新建文件夹用于存放裁剪后的图片
        File tmpDir = new File(Environment.getExternalStorageDirectory() + "/" + dirPath);
        if (!tmpDir.exists()) {
            tmpDir.mkdir();
        }

        //新建文件存储裁剪后的图片
        File img = new File(tmpDir.getAbsolutePath() + "/avator.png");
        try {
            //打开文件输出流
            FileOutputStream fos = new FileOutputStream(img);
            //将bitmap压缩后写入输出流(参数依次为图片格式、图片质量和输出流)
            bm.compress(Bitmap.CompressFormat.PNG, 85, fos);
            //刷新输出流
            fos.flush();
            //关闭输出流
            fos.close();
            //返回File类型的Uri
            return Uri.fromFile(img);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * 通过Uri传递图像信息以供裁剪
     *
     * @param uri
     */
    private void startImageZoom(Uri uri) {
        //构建隐式Intent来启动裁剪程序
        Intent intent = new Intent("com.android.camera.action.CROP");
        //设置数据uri和类型为图片类型
        intent.setDataAndType(uri, "image/*");
        //显示View为可裁剪的
        intent.putExtra("crop", true);
        //裁剪的宽高的比例为1:1
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        //输出图片的宽高均为150
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        //裁剪之后的数据是通过Intent返回
        intent.putExtra("return-data", true);
        startActivityForResult(intent, CROP_CODE);
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
                    MainActivity.user.setValue("background", pic);
                    MainActivity.user.setBackground(pic);
                    MainActivity.user.update(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            Toast.makeText(SetMainBackgroundActivity.this, "修改成功！", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(SetMainBackgroundActivity.this, MainActivity.class);
                            intent.putExtra("update", "true");
                            startActivity(intent);
                            SetMainBackgroundActivity.this.finish();
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


