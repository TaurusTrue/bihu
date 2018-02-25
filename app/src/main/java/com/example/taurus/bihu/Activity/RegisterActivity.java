package com.example.taurus.bihu.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.example.taurus.bihu.utils.ActivityCollector;
import com.example.taurus.bihu.R;
import com.example.taurus.bihu.config.Apiconfig;
import com.example.taurus.bihu.data.User;
import com.example.taurus.bihu.utils.HttpUtil;
import com.example.taurus.bihu.utils.JsonUtil;
import com.example.taurus.bihu.utils.MyApplication;
import com.example.taurus.bihu.utils.Response;

public class RegisterActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_register);
        loadToolBar(toolbar);
        Button register = (Button) findViewById(R.id.register_button);
        final EditText username = (EditText) findViewById(R.id.username_edit);
        final EditText password = (EditText) findViewById(R.id.password_edit);
        final EditText passwordTwice = (EditText) findViewById(R.id.password_edit_twice);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!password.getText().toString().equals(passwordTwice.getText().toString())) {
                    Toast.makeText(RegisterActivity.this, "两次输入的密码不一致", Toast.LENGTH_SHORT).show();
                } else {
                    HttpUtil.sendHttpRequest(Apiconfig.REGISTER, "username=" + username.getText() + "&password=" + password.getText(), new HttpUtil.HttpCallbackListener() {
                        @Override
                        public void onFinish(Response response) {
                            if (response.isSuccess()) {
                                Toast.makeText(MyApplication.getContext(), "注册成功！正在为您登陆", Toast.LENGTH_LONG).show();
                                HttpUtil.sendHttpRequest(Apiconfig.LOGIN, "username=" + username.getText() + "&password=" + password.getText(), new HttpUtil.HttpCallbackListener() {
                                    @Override
                                    public void onFinish(Response response) {
                                        User user = JsonUtil.getUser(response.getmData());
                                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                        sendUserActionStart(intent,user);
                                        ActivityCollector.finishAll();
                                    }

                                    @Override
                                    public void onError(Exception e) {
                                        Log.d("TAG",e.toString());
                                    }
                                });

                            }else{
                                Toast.makeText(RegisterActivity.this,response.getmInfo(),Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onError(Exception e) {
                            Toast.makeText(MyApplication.getContext(), "请检查您的网络", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
        }
        return true;
    }
}
