package com.example.taurus.bihu.Activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.taurus.bihu.R;
import com.example.taurus.bihu.config.Apiconfig;
import com.example.taurus.bihu.data.User;
import com.example.taurus.bihu.utils.HttpUtil;
import com.example.taurus.bihu.utils.Response;
import com.qiniu.android.common.Zone;
import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.UploadManager;

public class QuestionActivity extends BaseActivity {

    private Toolbar mToolbar;
    private EditText questionTitleEdit;
    private EditText questionContentEdit;
    private User user;
    private String AccessKey = "e-XOp8QWkbvc7pw2zD_o7o6FZK4NmfSZkjPPKUE_";
    private String SecretKey = "B7udlu6gOXGCQmJX-givf_5iQXZ5cM45o5LDOoAa";
    private String bucket = "picture";
    private Uri imageUri;
    private String imagePath;
    private String uptoken;//服务器请求的token
    private String upKey;
    private UploadManager uploadManager;//七牛SDK管理者

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = (User) getIntent().getParcelableExtra("user_data");
        setContentView(R.layout.activity_question);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        questionTitleEdit = (EditText) findViewById(R.id.questionTitle_edit);
        questionContentEdit = (EditText) findViewById(R.id.questionContent_edit);
        loadToolBar(mToolbar);
        initWindow(1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();break;
//            case R.id.up_load_image:
//                // TODO: 2018/2/23 上传图片
//                break;
            case R.id.done:
                HttpUtil.sendHttpRequest(Apiconfig.POST_QUESTION, "title="+questionTitleEdit.getText()+"&content="+ questionContentEdit.getText()+"&token="+user.getToken(),
                        new HttpUtil.HttpCallbackListener() {
                            @Override
                            public void onFinish(Response response) {
                                if(response.isSuccess()){
                                    Toast.makeText(QuestionActivity.this,"问题上传成功！",Toast.LENGTH_LONG).show();
                                    Log.d("TAG",response.getmStatus()+"");
                                    finish();
                                }
                            }
                            @Override
                            public void onError(Exception e) {

                            }
                        });
                break;
            default:
        }return true;
    }

//    private void getTokenFromService() {
//        StringBuilder ask = new StringBuilder();
//        ask.append("accessKey=" + AccessKey + "&secretKey=" + SecretKey + "&bucket=" + bucket);
//        HttpUtil.sendHttpRequestH(Apiconfig.TOKEN_URL, ask.toString(), new HttpUtil.HttpCallbackListener() {
//            @Override
//            public void onFinish(Response response) {
//                if (response.isSuccess())
//                    uptoken = response.getmToken();
//            }
//
//            @Override
//            public void onError(Exception e) {
//
//            }
//        });
//    }//向七牛云服务器发出请求 获得用于上传的token

//    private void initData() {
//        getTokenFromService();//获得上传用的token
//        upKey = "image" + String.valueOf(Math.random());
//        Configuration config = new Configuration.Builder()
//                .zone(Zone.zone2)//华南地区
//                .build();
//        uploadManager = new UploadManager(config);
//    }// 用于初始化一些属性



}
