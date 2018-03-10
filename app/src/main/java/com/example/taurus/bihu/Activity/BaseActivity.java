package com.example.taurus.bihu.Activity;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
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
import android.view.Window;
import android.view.WindowManager;

import com.example.taurus.bihu.utils.ActivityCollector;
import com.example.taurus.bihu.R;
import com.example.taurus.bihu.data.User;
import com.example.taurus.bihu.utils.MyApplication;
import com.readystatesoftware.systembartint.SystemBarTintManager;

/**
 * Created by Taurus on 2018/2/20.
 * Activity的基础类
 */

public class BaseActivity extends AppCompatActivity {


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        ActivityCollector.addActivity(this);
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void onDestroy() {
        ActivityCollector.removeActivity(this);
        super.onDestroy();


    }

    public void sendUserActionStart(Intent intent, User user) {
        intent.putExtra("user_data", user);
        startActivity(intent);
    }

    public void loadToolBar(Toolbar toolbar) {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    public void initWindow(int type) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (type == 1) {// 先将状态栏设为透明，然后设置颜色
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                SystemBarTintManager tintManager;
                tintManager = new SystemBarTintManager(this);
                int statusColor = Color.parseColor("#A1D6D5");
                tintManager.setStatusBarTintColor(statusColor);
                tintManager.setStatusBarTintEnabled(true);
            } else {//将状态栏设置为透明
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            }
        }
    }
}