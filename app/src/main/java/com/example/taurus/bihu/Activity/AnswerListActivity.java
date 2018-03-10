package com.example.taurus.bihu.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.taurus.bihu.AnswerDialog;
import com.example.taurus.bihu.R;
import com.example.taurus.bihu.adapter.AnswerAdapter;
import com.example.taurus.bihu.adapter.QuestionAdapter;
import com.example.taurus.bihu.config.Apiconfig;
import com.example.taurus.bihu.data.Answer;
import com.example.taurus.bihu.data.Question;
import com.example.taurus.bihu.data.User;
import com.example.taurus.bihu.holder.QuestionViewHolder;
import com.example.taurus.bihu.utils.HttpUtil;
import com.example.taurus.bihu.utils.JsonUtil;
import com.example.taurus.bihu.utils.Response;

import java.util.ArrayList;
import java.util.List;

public class AnswerListActivity extends BaseActivity {

    private User user;
    private Question question;
    private RecyclerView recyclerView;
    private AnswerAdapter adapter;
    private List<Answer> answers;
    private QuestionViewHolder holder;
    private CardView cardView;
    private Toolbar toolbar;
    private AnswerDialog mAnswerDialog;
    private SwipeRefreshLayout swipeRefreshLayout;
    private FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer_list);
        Intent intent = getIntent();
        toolbar = (Toolbar)findViewById(R.id.toolbar_answer) ;
        user = (User) intent.getParcelableExtra("user_data");
        question = (Question) intent.getParcelableExtra("question_data");
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_answer);
        cardView = (CardView) findViewById(R.id.question_card);
        floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_answer);
        mAnswerDialog = new AnswerDialog(this);
        holder = new QuestionViewHolder(cardView, user,null);
        holder.initQuestionViewHolder(question, null, holder, 0);
        answers = new ArrayList<>();
        loadToolBar(toolbar);
        loadFABBuytton();
        loadAnswerRecyclerView();
        loadSwipe();
        initWindow(1);
    }

    private void initDialog() {
        mAnswerDialog.show();
        String answer =  mAnswerDialog.getmAnswerEdit().getText().toString();
        final String param = "qid="+question.getId()+"&content="+answer+"&token="+user.getToken();
        mAnswerDialog.setmButtonCancel(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAnswerDialog.dismiss();
            }
        });
        mAnswerDialog.setmButtonDone(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String answer =  mAnswerDialog.getmAnswerEdit().getText().toString();
                mAnswerDialog.getmAnswerEdit().getText().clear();
                final String param = "qid="+question.getId()+"&content="+answer+"&token="+user.getToken();
                HttpUtil.sendHttpRequest(Apiconfig.POST_ANSWER, param, new HttpUtil.HttpCallbackListener() {
                    @Override
                    public void onFinish(Response response) {
                        if(response.isSuccess()){
                            Toast.makeText(AnswerListActivity.this,"成功发表您的看法",Toast.LENGTH_SHORT).show();
                            mAnswerDialog.dismiss();
                        }else{
                            Toast.makeText(AnswerListActivity.this,"请检查您的网络",Toast.LENGTH_LONG).show();
                            mAnswerDialog.dismiss();
                        }
                    }
                    @Override
                    public void onError(Exception e) {
                        Log.d("TAG", "onError: "+e.toString());
                        mAnswerDialog.dismiss();
                    }
                });
            }
        });
    }

    private void loadFABBuytton() {
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initDialog();
            }
        });
    }

    private void loadAnswerRecyclerView() {
        initAnswerList(question, user);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new AnswerAdapter(answers, user, question);
        recyclerView.setAdapter(adapter);
    }

    private void initAnswerList(Question question, User user) {
        HttpUtil.sendHttpRequest(Apiconfig.ANSWER_LIST, "page=0&count=20&qid=" + question.getId() + "&token=" + user.getToken(), new HttpUtil.HttpCallbackListener() {
            @Override
            public void onFinish(Response response) {
                if (response.isSuccess()) {
                    JsonUtil.getAnswerList(response.getmData(), answers,AnswerAdapter.INIT_DATA);
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(AnswerListActivity.this, "请检查您的网络", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onError(Exception e) {
                Log.d("TAG", "onError: " + e.toString());
            }
        });
    }

    private void loadSwipe() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initAnswerList(question, user);
                swipeRefreshLayout.setRefreshing(false);
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
