package com.example.taurus.bihu.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.example.taurus.bihu.utils.ActivityCollector;
import com.example.taurus.bihu.R;
import com.example.taurus.bihu.data.User;

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

}
