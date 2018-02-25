package com.example.taurus.bihu.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.taurus.bihu.R;
import com.example.taurus.bihu.config.Apiconfig;
import com.example.taurus.bihu.data.Question;
import com.example.taurus.bihu.data.User;
import com.example.taurus.bihu.holder.QuestionViewHolder;
import com.example.taurus.bihu.utils.HttpUtil;
import com.example.taurus.bihu.utils.JsonUtil;
import com.example.taurus.bihu.utils.MyApplication;
import com.example.taurus.bihu.utils.Response;

import java.util.List;

/**
 * Created by Taurus on 2018/2/21.
 *
 */

public class QuestionAdapter extends RecyclerView.Adapter<QuestionViewHolder> {
    private List<Question> mQuestionList;
    private User mUser;
    private int mType;
    public static final int TYPE_FAVORITE = 0;
    public static final int TYPE_QUESTION = 1;
    public static final int LOAD_MORE = 2;
    public static final int INIT_DATA = 3;

    public QuestionAdapter(List<Question> questions, User user, int type) {
        mQuestionList = questions;
        mUser = user;
        mType = type;
    }

    @Override
    public QuestionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_question, parent, false);
        return new QuestionViewHolder(view, mUser,mQuestionList);
    }

    @Override
    public void onBindViewHolder(QuestionViewHolder holder, int position) {
        Question question = mQuestionList.get(position);
        holder.initQuestionViewHolder(question, mQuestionList, holder, 1);
        if (position == getItemCount() - 1) {
            loadData();
        }
    }

    @Override
    public int getItemCount() {
        return mQuestionList.size();
    }

    private void loadData() {
        final int temp = mQuestionList.size();
        final int page;
        if (temp % 20 == 0) {
            page = temp / 20;
        } else {
            page = temp / 20 + 1;
        }
        if (mType == TYPE_QUESTION)
            HttpUtil.sendHttpRequest(Apiconfig.QUESTION_LIST, "page=" + page + "&count=20&token=" + mUser.getToken(), new HttpUtil.HttpCallbackListener() {
                @Override
                public void onFinish(Response response) {
                    if (response.isSuccess()) {
                        if (temp % 20 != 0) {
                            Toast.makeText(MyApplication.getContext(), "没有更多了", Toast.LENGTH_SHORT).show();
                        } else {
                            JsonUtil.getQuestionList(response.getmData(), mQuestionList, LOAD_MORE);
                            notifyDataSetChanged();
                        }
                    } else {
                        Toast.makeText(MyApplication.getContext(), "没有更多了", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onError(Exception e) {

                }
            });
        if (mType == TYPE_FAVORITE)
            HttpUtil.sendHttpRequest(Apiconfig.FAVORITE_LIST, "page=" + page + "&count=20&token=" + mUser.getToken(), new HttpUtil.HttpCallbackListener() {
                @Override
                public void onFinish(Response response) {
                    if (response.isSuccess()) {
                        if (temp % 20 != 0) {
                            Toast.makeText(MyApplication.getContext(), "没有更多了", Toast.LENGTH_SHORT).show();
                        } else {
                            JsonUtil.getFavoriteList(response.getmData(), mQuestionList, QuestionAdapter.LOAD_MORE);
                            notifyDataSetChanged();
                        }
                    } else {
                        Toast.makeText(MyApplication.getContext(), "没有更多了", Toast.LENGTH_SHORT).show();

                    }
                }

                @Override
                public void onError(Exception e) {

                }
            });
    }
}


