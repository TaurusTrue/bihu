package com.example.taurus.bihu.Activity;

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

public class QuestionActivity extends BaseActivity {

    private Toolbar mToolbar;
    private EditText questionTitleEdit;
    private EditText questionContentEdit;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = (User) getIntent().getParcelableExtra("user_data");
        setContentView(R.layout.activity_question);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        questionTitleEdit = (EditText) findViewById(R.id.questionTitle_edit);
        questionContentEdit = (EditText) findViewById(R.id.questionContent_edit);
        loadToolBar(mToolbar);
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
            case R.id.up_load_image:
                // TODO: 2018/2/23 上传图片
                break;
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


}
