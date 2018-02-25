package com.example.taurus.bihu.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.taurus.bihu.Activity.AnswerListActivity;
import com.example.taurus.bihu.R;
import com.example.taurus.bihu.config.Apiconfig;
import com.example.taurus.bihu.data.Answer;
import com.example.taurus.bihu.data.Question;
import com.example.taurus.bihu.data.User;
import com.example.taurus.bihu.holder.AnswerViewHolder;
import com.example.taurus.bihu.utils.HttpUtil;
import com.example.taurus.bihu.utils.JsonUtil;
import com.example.taurus.bihu.utils.MyApplication;
import com.example.taurus.bihu.utils.Response;

import java.util.List;

/**
 * Created by Taurus on 2018/2/24.
 */

public class AnswerAdapter extends RecyclerView.Adapter<AnswerViewHolder> {

    private List<Answer> mAnswerList;
    private User mUser;
    private Question mQuestion;
    public static final int INIT_DATA = 0;
    public static final int LOAD_MORE = 1;

    public AnswerAdapter(List<Answer> answers, User user, Question question) {
        mAnswerList = answers;
        mUser = user;
        mQuestion = question;
    }

    @Override
    public AnswerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_answer, parent, false);
        return new AnswerViewHolder(view, mUser, mQuestion);
    }

    @Override
    public void onBindViewHolder(AnswerViewHolder holder, int position) {
        Answer answer = mAnswerList.get(position);
        holder.initAnswerViewHolder(answer);
        if (position == getItemCount() - 1) {
            loadData();
        }
    }

    @Override
    public int getItemCount() {
        return mAnswerList.size();
    }

    private void loadData() {
        final int temp = mAnswerList.size();
        final int page;
        if (temp % 20 == 0) {
            page = temp / 20;
        } else {
            page = temp / 20 + 1;
        }
        HttpUtil.sendHttpRequest(Apiconfig.ANSWER_LIST, "page=" + page + "&count=20&qid=" + mQuestion.getId() + "&token=" + mUser.getToken(), new HttpUtil.HttpCallbackListener() {
            @Override
            public void onFinish(Response response) {
                if (response.isSuccess()) {
                    if ((temp % 20) != 0) {
                        Toast.makeText(MyApplication.getContext(), "没有更多了", Toast.LENGTH_SHORT).show();
                    } else {
                        JsonUtil.getAnswerList(response.getmData(), mAnswerList, AnswerAdapter.LOAD_MORE);
                        notifyDataSetChanged();
                    }
                } else {
                    Toast.makeText(MyApplication.getContext(), "没有更多了", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Exception e) {
                Log.d("TAG", "onError: " + e.toString());
            }
        });
    }
}
