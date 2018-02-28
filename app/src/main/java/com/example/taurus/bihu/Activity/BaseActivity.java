package com.example.taurus.bihu.Activity;

import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.example.taurus.bihu.utils.ActivityCollector;
import com.example.taurus.bihu.R;
import com.example.taurus.bihu.data.User;
import com.example.taurus.bihu.utils.MyApplication;

/**
 * Created by Taurus on 2018/2/20.
 * Activity的基础类
 */

public class BaseActivity extends AppCompatActivity {


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        ActivityCollector.addActivity(this);
        Log.d("SIZE",""+ActivityCollector.activities.size());
        super.onCreate(savedInstanceState);

    }
    @Override
    protected void onDestroy() {
        ActivityCollector.removeActivity(this);
        Log.d("SIZE",""+ActivityCollector.activities.size());
        super.onDestroy();


    }

    public void sendUserActionStart(Intent intent, User user){
        intent.putExtra("user_data",user);
        startActivity(intent);
    }

    public void loadToolBar(Toolbar toolbar){
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @RequiresApi(api = 19)
    public  void handleImageOnKitKat(Intent data,String imagePath) {
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(MyApplication.getContext(), uri)) {
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
        //displayImage(imagePath);//根据图片路径显示图片
    }

    public  void handleImageBeforeKitKat(Intent data,String imagePath) {
        Uri uri = data.getData();
        imagePath = getImagePath(uri, null);
        //displayImage(imagePath);
    }

    public String getImagePath(Uri uri, String selection) {
        String path = null;
        //通过Uri和selection来获取图片的真实路径
        Cursor cursor = MyApplication.getContext().getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        Log.d("TAG", "getImagePath: " + path);
        return path;
    }

}
