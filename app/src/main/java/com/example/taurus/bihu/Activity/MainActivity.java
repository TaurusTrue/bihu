package com.example.taurus.bihu.Activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.taurus.bihu.AnswerDialog;
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

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends BaseActivity {
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private DrawerLayout mDrawerlayout;
    private TextView nav_username;
    private List<Question> mQuestionList;
    private RecyclerView questionRecyclerView;
    private QuestionAdapter questionAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private User user;
    private AnswerDialog resetPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mQuestionList = new ArrayList<>();
        pref = PreferenceManager.getDefaultSharedPreferences(this);//获得preference的实例
        editor = pref.edit();
        user = (User) getIntent().getParcelableExtra("user_data");
        mDrawerlayout = (DrawerLayout) findViewById(R.id.drawerlaout_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        questionRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_main);
        resetPassword = new AnswerDialog(this);
        loadToolBar(toolbar);
        loadNavigationView();
        loadSwipe();
        loadQuestionRecyclerView();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerlayout.openDrawer(GravityCompat.START);
                break;
            default:
        }
        return true;
    }

    private void initQuestionList(User user) {
        HttpUtil.sendHttpRequest(Apiconfig.QUESTION_LIST, "page=0&count=20&token=" + user.getToken(), new HttpUtil.HttpCallbackListener() {
            @Override
            public void onFinish(Response response) {
                if (response.isSuccess()) {
                    JsonUtil.getQuestionList(response.getmData(), mQuestionList,QuestionAdapter.INIT_DATA);
                    questionAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(MainActivity.this, "请检查您的网络", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onError(Exception e) {
                Log.d("TAG", "onError: " + e.toString());
            }
        });
    }

    private void loadQuestionRecyclerView() {
        initQuestionList(user);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        questionRecyclerView.setLayoutManager(layoutManager);
        questionAdapter = new QuestionAdapter(mQuestionList, user,QuestionAdapter.TYPE_QUESTION);
        questionRecyclerView.setAdapter(questionAdapter);
    }

    private void loadNavigationView() {
        navigationView.setCheckedItem(R.id.home);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        questionRecyclerView.scrollToPosition(0);
                        break;
                    case R.id.question:
                        Intent intent = new Intent(MainActivity.this, QuestionActivity.class);
                        sendUserActionStart(intent, user);
                        break;
                    case R.id.favorite:
                        Intent intent1 = new Intent(MainActivity.this, FavoriteListActivity.class);
                        sendUserActionStart(intent1, user);
                        break;
                    case R.id.avatar:
                        break;
                    case R.id.changePassword:
                        initDialog();
                        break;
                    case R.id.logout:
                        Intent intent2 = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent2);
                        finish();
                        break;
                    default:
                }
                mDrawerlayout.closeDrawers();
                return true;
            }
        });
        View view = navigationView.inflateHeaderView(R.layout.nav_header);
        TextView username = (TextView) view.findViewById(R.id.nav_username);
        username.setText(user.getUsername());
        final CircleImageView mAvatar = (CircleImageView) view.findViewById(R.id.avatar);
        HttpUtil.loadImageResource(user.getImageUrl(), new HttpUtil.loadBitmap() {
            @Override
            public void onFinish(Bitmap bitmap) {
                mAvatar.setImageBitmap(bitmap);
            }
            @Override
            public void onError(Exception e) {
            }
        });
    }

    private void loadSwipe() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initQuestionList(user);
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void loadToolBar(Toolbar toolbar) {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
    }

    private void initDialog(){
        resetPassword.show();
        resetPassword.getmTextView().setText("修改密码");
        resetPassword.getmAnswerEdit().setHint("请在此处输入您的新密码");
        resetPassword.setmButtonCancel(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPassword.dismiss();
            }
        });
        resetPassword.setmButtonDone(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String password = resetPassword.getmAnswerEdit().getText().toString();
                final String param ="password="+password+"&token="+user.getToken();
                HttpUtil.sendHttpRequest(Apiconfig.CHANGE_PASSWORD, param, new HttpUtil.HttpCallbackListener() {
                    @Override
                    public void onFinish(Response response) {
                        if(response.isSuccess()){
                            Toast.makeText(MainActivity.this,"您已经成功修改密码",Toast.LENGTH_SHORT).show();
                            resetPassword.getmAnswerEdit().getText().clear();
                            resetPassword.dismiss();
                            user.setToken(JsonUtil.getString(response.getmData(),"token"));
                            editor.putString("password",password);
                            editor.apply();
                        }
                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });
            }
        });

    }
}
