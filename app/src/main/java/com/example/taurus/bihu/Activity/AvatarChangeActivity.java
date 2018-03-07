package com.example.taurus.bihu.Activity;

import android.Manifest;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.taurus.bihu.R;
import com.example.taurus.bihu.config.Apiconfig;
import com.example.taurus.bihu.data.User;
import com.example.taurus.bihu.utils.ActivityCollector;
import com.example.taurus.bihu.utils.HttpUtil;
import com.example.taurus.bihu.utils.JsonUtil;
import com.example.taurus.bihu.utils.MyApplication;
import com.example.taurus.bihu.utils.Response;
import com.qiniu.android.common.Zone;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

import android.net.Uri;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import static com.example.taurus.bihu.config.Apiconfig.TOKEN_URL;


public class AvatarChangeActivity extends BaseActivity {

    private User user;
    private Button takePhotoButton;
    private Button openAlbumButton;
    private Button doneButton;
    private ImageView avatarPreview;
    private Toolbar toolbarAvatar;
    private String AccessKey = "e-XOp8QWkbvc7pw2zD_o7o6FZK4NmfSZkjPPKUE_";
    private String SecretKey = "B7udlu6gOXGCQmJX-givf_5iQXZ5cM45o5LDOoAa";
    private String bucket = "picture";
    private String fileName = "p4ptest7d.bkt.clouddn.com";
    private Uri imageUri;
    private String imagePath;
    public static final int CHOOSE_PHOTO = 110;
    public static final int TAKE_PHOTO = 120;
    private String uptoken;//服务器请求的token
    private String upKey;
    private UploadManager uploadManager;//七牛SDK管理者


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avatar_change);
        Intent intten = getIntent();
        user = (User) intten.getParcelableExtra("user_data");
        takePhotoButton = (Button) findViewById(R.id.take_photo);
        openAlbumButton = (Button) findViewById(R.id.open_album);
        doneButton = (Button) findViewById(R.id.done_button);
        avatarPreview = (ImageView) findViewById(R.id.avatar_preview);
        toolbarAvatar = (Toolbar) findViewById(R.id.toolbar_avatar);
        loadToolBar(toolbarAvatar);
        initData();
        loadOpenAlbumButton();
        loadDoneButton();
        loadtakePhotoButton();
    }

    private void getTokenFromService() {
        StringBuilder ask = new StringBuilder();
        ask.append("accessKey=" + AccessKey + "&secretKey=" + SecretKey + "&bucket=" + bucket);
        HttpUtil.sendHttpRequestH(Apiconfig.TOKEN_URL, ask.toString(), new HttpUtil.HttpCallbackListener() {
            @Override
            public void onFinish(Response response) {
                if (response.isSuccess())
                    uptoken = response.getmToken();
            }

            @Override
            public void onError(Exception e) {

            }
        });
    }//向七牛云服务器发出请求 获得用于上传的token

    private void initData() {
        getTokenFromService();//获得上传用的token
        upKey = "image" + String.valueOf(Math.random());
        Configuration config = new Configuration.Builder()
                .zone(Zone.zone2)//华南地区
                .build();
        uploadManager = new UploadManager(config);
    }// 用于初始化一些属性

    private void ChangeAvatar() {
        final String downUrl = "http://" + fileName + "/" + upKey;
        String param="token=" + user.getToken() + "&avatar=" + downUrl;
        HttpUtil.sendHttpRequest(Apiconfig.MODIFY_AVATAR, param, new HttpUtil.HttpCallbackListener() {
            @Override
            public void onFinish(Response response) {

                if (response.isSuccess()) {
                    Toast.makeText(AvatarChangeActivity.this, "更换成功", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MyApplication.getContext(),MainActivity.class);
                    user.setImageUrl(downUrl);
                    sendUserActionStart(intent,user);
                    finish();


                } else {
                    Toast.makeText(AvatarChangeActivity.this, "头像更换失败 错误：" + response.getmStatus(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Exception e) {

            }


        });
    }

    public void loadDoneButton() {
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChangeAvatar();
                if (TextUtils.isEmpty(uptoken)) {//注意可能token还没获取到 0.0 我觉得不存在
                    Toast.makeText(AvatarChangeActivity.this, "正在获取token,请等待", Toast.LENGTH_SHORT).show();
                } else {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            uploadManager.put(imagePath, upKey, uptoken, new UpCompletionHandler() {
                                @Override
                                public void complete(String key, ResponseInfo info, JSONObject response) {
                                    if (info.isOK()) {
                                        Toast.makeText(MyApplication.getContext(), "上传成功", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(MyApplication.getContext(), "上传失败", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }, null);
                        }
                    }).start();
                }
            }
        });
    }

    private void loadOpenAlbumButton() {
        openAlbumButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(AvatarChangeActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(AvatarChangeActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                } else {
                    openAlbum();
                }
            }
        });
    }

    private void loadtakePhotoButton() {
        takePhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File outputImage = new File(getExternalCacheDir(), "outputimg.jpg");
                if (outputImage.exists()) {
                    outputImage.delete();
                }
                try {
                    outputImage.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                /**
                 * 内容提供者要注册
                 */
                if (Build.VERSION.SDK_INT >= 24) {
                    //7.0直接使用本地真实uri是不安全的 FileProvider是一种特殊内容提供器
                    // 可以选择性地将封装过后的uri提供给外部 提高安全性
                    //第二个为任意唯一字符 第三个为刚刚的file
                    imageUri = FileProvider.getUriForFile(AvatarChangeActivity.this, "com.example.mac.bihu,fileprovider", outputImage);
                } else {
                    imageUri = Uri.fromFile(outputImage);//真实路径
                }
                //判断相机权限
                if (Build.VERSION.SDK_INT >= 23) {
                    if (ContextCompat.checkSelfPermission(AvatarChangeActivity.this, Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(AvatarChangeActivity.this, new String[]{Manifest.permission.CAMERA}, TAKE_PHOTO);
                    } else {
                        openCamera();
                    }
                } else {
                    openCamera();
                }
            }
//                //创建File对象，用于储存拍照后的图片
//                File outputImage = new File(getExternalCacheDir(), "output_image.jpg");
//                try {
//                    if (outputImage.exists()) {
//                        outputImage.delete();
//                    }
//                    outputImage.createNewFile();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                if(Build.VERSION.SDK_INT >= 24){
//                    //将File对象转换为封装过的Uri对象,记得注册内容提供器
//                    imageUri = FileProvider.getUriForFile(MainActivity.this,"666",outputImage);
//                }else{
//                    imageUri = Uri.fromFile(outputImage);//获取图片的真实本地路径，Android 7.0之前的版本才可以
//                }if(Build.VERSION.SDK_INT>=23){
//                    if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA)
//                            != PackageManager.PERMISSION_GRANTED){
//                        ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.CAMERA},TAKE_PHOTO);
//                    }else{
//                        openCamera();
//                    }
//                }else{
//                    openCamera();
//                }
//
//            }
//        });
        });
    }

    private void openCamera() {
        //启动相机程序
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, TAKE_PHOTO);
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openAlbum();
                } else {
                    Toast.makeText(AvatarChangeActivity.this, "你拒绝了权限请求", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_PHOTO:
                if (requestCode == RESULT_OK) {//判断拍照是否成功
                    try {
                        //将拍摄的照片显示出来
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        avatarPreview.setImageBitmap(bitmap);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case CHOOSE_PHOTO:
                if (resultCode == RESULT_OK) {
                    //判断手机系统版本
                    if (Build.VERSION.SDK_INT >= 19) {
                        //4.4及以上系统使用这个方法处理图片
                        handleImageOnKitKat(data);
                    } else {
                        //4.4及以下系统使用这个方法处理图片
                        handleImageBeforeKitKat(data);
                    }
                }
            default:
                break;
        }
    }

    private void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, CHOOSE_PHOTO);//打开相册
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:

                Intent intent = new Intent(AvatarChangeActivity.this,MainActivity.class);
                sendUserActionStart(intent,user);
                finish();
                break;
            default:
        }
        return true;
    }

    @RequiresApi(19)
    private void handleImageOnKitKat(Intent data) {

        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(this, uri)) {
            //如果是document类型的Uri，则通过document id处理
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1];//解析出数字格式的ID
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            //如果是content类型的Uri，则使用普通方法处理
            imagePath = getImagePath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            //如果是file类型的Uri直接获取图片路径即可
            imagePath = uri.getPath();
        }
        Log.d("TAG", "handleImageOnKitKat: " + imagePath);
        displayImage(imagePath);//根据图片路径显示图片
    }

    private void handleImageBeforeKitKat(Intent data) {
        Uri uri = data.getData();
        imagePath = getImagePath(uri, null);
        displayImage(imagePath);
    }

    private String getImagePath(Uri uri, String selection) {
        String path = null;
        //通过Uri和selection来获取图片的真实路径
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null){
            if(cursor.moveToFirst()){
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    private void displayImage(String imagePath) {
        if (imagePath != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            Log.d("TAG", "displayImage: " + imagePath);
            avatarPreview.setImageBitmap(bitmap);
        } else {
            Toast.makeText(AvatarChangeActivity.this, "failed to get iamge", Toast.LENGTH_SHORT).show();
        }
    }
}
