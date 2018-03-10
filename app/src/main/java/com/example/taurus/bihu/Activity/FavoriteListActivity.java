package com.example.taurus.bihu.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.taurus.bihu.R;
import com.example.taurus.bihu.adapter.QuestionAdapter;
import com.example.taurus.bihu.config.Apiconfig;
import com.example.taurus.bihu.data.Question;
import com.example.taurus.bihu.data.User;
import com.example.taurus.bihu.utils.HttpUtil;
import com.example.taurus.bihu.utils.JsonUtil;
import com.example.taurus.bihu.utils.Response;

import java.util.ArrayList;
import java.util.List;

public class FavoriteListActivity extends BaseActivity {
private User user;
private List<Question> mQuestionList;
private RecyclerView mFavoriteRecyclerView;
private QuestionAdapter questionAdapter;
private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_list);
        mQuestionList = new ArrayList<>();
        user = (User) getIntent().getParcelableExtra("user_data");
        toolbar = (Toolbar)findViewById(R.id.toolbar_favorite);
        mFavoriteRecyclerView = (RecyclerView)findViewById(R.id.recycler_view_favorite) ;
        loadToolBar(toolbar);
        loadQuestionRecyclerView();
        initWindow(1);
    }

    private void loadQuestionRecyclerView(){
        initQuestionList(user);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        mFavoriteRecyclerView.setLayoutManager(layoutManager);
        questionAdapter = new QuestionAdapter(mQuestionList,user,QuestionAdapter.TYPE_FAVORITE);
        mFavoriteRecyclerView.setAdapter(questionAdapter);
    }

    private void initQuestionList(User user) {
        HttpUtil.sendHttpRequest(Apiconfig.FAVORITE_LIST, "page=0&count=20&token=" + user.getToken(), new HttpUtil.HttpCallbackListener() {
            @Override
            public void onFinish(Response response) {
                if (response.isSuccess()) {
                    JsonUtil.getFavoriteList(response.getmData(),mQuestionList,QuestionAdapter.INIT_DATA);
                    questionAdapter.notifyDataSetChanged();
                } else {
                }
            }
            @Override
            public void onError(Exception e) {
                Log.d("TAG", "onError: "+e.toString());
            }
        });
    }

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
