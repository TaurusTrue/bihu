package com.example.taurus.bihu.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.taurus.bihu.R;
import com.example.taurus.bihu.config.Apiconfig;
import com.example.taurus.bihu.data.User;
import com.example.taurus.bihu.utils.HttpUtil;
import com.example.taurus.bihu.utils.JsonUtil;
import com.example.taurus.bihu.utils.Response;

public class LoginActivity extends BaseActivity {

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private EditText usernameEdit;
    private EditText passwordEdit;
    private CheckBox rememberPassword;
    private Button login;
    private Button register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        pref = PreferenceManager.getDefaultSharedPreferences(this);//获得preference的实例
        usernameEdit = (EditText) findViewById(R.id.username_edit);
        passwordEdit = (EditText) findViewById(R.id.password_edit);
        rememberPassword = (CheckBox) findViewById(R.id.remember_password);
        login = (Button) findViewById(R.id.login_button);
        register = (Button) findViewById(R.id.login_register_button);
        initWindow(0);
        boolean isRememberPassword = pref.getBoolean("is_remember_password", false);
        if (isRememberPassword) {
            //将账号和密码输入到文本框中
            String account = pref.getString("username", "");
            String password = pref.getString("password", "");
            usernameEdit.setText(account);
            passwordEdit.setText(password);
            rememberPassword.setChecked(true);
        }else{
            //输入账号
            String account = pref.getString("username","");
            if(!"null".equals(account))usernameEdit.setText(account);
            Log.d("TAG", "onCreate: "+account);
        }
        initLoginButton();
        initRegisterButton();
    }

    private void initLoginButton(){
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username = usernameEdit.getText().toString();
                final String password = passwordEdit.getText().toString();
                HttpUtil.sendHttpRequest(Apiconfig.LOGIN, "username=" + username + "&password=" + password, new HttpUtil.HttpCallbackListener() {
                    @Override
                    public void onFinish(Response response) {
                        if (response.isSuccess()) {
                            editor = pref.edit();
                            if (rememberPassword.isChecked()) {
                                //检查复选框是否被选中
                                editor.putBoolean("is_remember_password", true);
                                editor.putString("username", username);
                                editor.putString("password", password);
                            } else {
                                editor.clear();
                                Log.d("TAG", "onFinish: "+username);
                                editor.putString("username",username);
                            }
                            editor.apply();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            User user = JsonUtil.getUser(response.getmData());
                            sendUserActionStart(intent, user);
                            finish();
                        } else {
                            AlertDialog.Builder dialog = new AlertDialog.Builder(LoginActivity.this);
                            dialog.setTitle("错误");
                            dialog.setMessage("您输入的账号或密码错误,请重新输入");
                            dialog.setPositiveButton("好的", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    passwordEdit.getText().clear();
                                }
                            });
                            dialog.show();
                        }

                    }

                    @Override
                    public void onError(Exception e) {
                        passwordEdit.getText().clear();
                        Log.d("Login", e.toString());
                    }
                });
            }
        });
    }

    private void initRegisterButton(){
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

}
