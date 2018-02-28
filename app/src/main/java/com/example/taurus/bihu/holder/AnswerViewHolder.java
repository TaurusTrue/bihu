package com.example.taurus.bihu.holder;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.taurus.bihu.R;
import com.example.taurus.bihu.config.Apiconfig;
import com.example.taurus.bihu.data.Answer;
import com.example.taurus.bihu.data.Question;
import com.example.taurus.bihu.data.User;
import com.example.taurus.bihu.utils.HttpUtil;
import com.example.taurus.bihu.utils.MyApplication;
import com.example.taurus.bihu.utils.Response;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Taurus on 2018/2/24.
 */

public class AnswerViewHolder extends RecyclerView.ViewHolder {
    private CircleImageView mAvatar;

    private TextView mAuthorName;
    private TextView mDate;
    private TextView mAnswerContent;
    private TextView mExcitingCount;
    private TextView mNaiveCount;

    private ImageButton mExcitingButton;
    private ImageButton mNaiveButton;
    private ImageButton mAcceptButton;

    private ArrayList<Answer> mAnswerList;
    private Question mQuestion;
    private User mUser;

    public AnswerViewHolder(View itemView, User user,Question question) {
        super(itemView);
        mAvatar = (CircleImageView) itemView.findViewById(R.id.avatar);// TODO: 2018/2/24 头像设置
        mAuthorName = (TextView) itemView.findViewById(R.id.authorName);
        mDate = (TextView) itemView.findViewById(R.id.date);
        mAnswerContent = (TextView) itemView.findViewById(R.id.answerContent);
        mExcitingCount = (TextView) itemView.findViewById(R.id.excitingCount);
        mNaiveCount = (TextView) itemView.findViewById(R.id.naiveCount);
        mNaiveButton = (ImageButton) itemView.findViewById(R.id.naiveButton);
        mExcitingButton = (ImageButton) itemView.findViewById(R.id.excitingButton);
        mAcceptButton = (ImageButton) itemView.findViewById(R.id.acceptButton);

        mUser = user;
        mQuestion = question;
    }

    public void initAnswerViewHolder(Answer answer){
        mAuthorName.setText(answer.getAuthorName());
        if(answer.getAuthorAvatarUrlString()=="null"){
            mAvatar.setImageResource(R.mipmap.default_avatar);
        }else{
            HttpUtil.loadAvatar(answer.getAuthorAvatarUrlString(),mAvatar);
        }
        mDate.setText(answer.getDate());
        mAnswerContent.setText(answer.getContent());
        mExcitingCount.setText("("+answer.getExcitingCount()+")");
        mNaiveCount.setText("("+answer.getNaiveCount()+")");
        if(answer.isExciting())mExcitingButton.setBackgroundResource(R.drawable.ic_thumb_up_pink_24dp);
        if(answer.isNaive())mNaiveButton.setBackgroundResource(R.drawable.ic_thumb_down_pink_24dp);
        if(answer.getBest()!=0)mAcceptButton.setBackgroundResource(R.drawable.ic_accept_pink_24dp);
        initAcceptButton(answer);
        initExcitingButton(answer);
        initNaiveButton(answer);
    }

    private void initNaiveButton(final Answer answer) {
        mNaiveButton.setOnClickListener(new View.OnClickListener() {
            String param = "id=" + answer.getId() + "&type=2&token=" + mUser.getToken();

            @Override
            public void onClick(View v) {
                if (answer.isNaive()) {
                    HttpUtil.sendHttpRequest(Apiconfig.CANCEL_NAIVE, param, new HttpUtil.HttpCallbackListener() {
                        @Override
                        public void onFinish(Response response) {
                            if (response.isSuccess()) {
                                mNaiveButton.setBackgroundResource(R.drawable.ic_thumb_down_gray_24dp);
                                answer.setNaive(false);
                                answer.setNaiveCount(answer.getNaiveCount() - 1);
                                mNaiveCount.setText("(" + answer.getNaiveCount() + ")");
                            }
                        }

                        @Override
                        public void onError(Exception e) {
                            Log.d("TAG", e.toString());
                        }
                    });
                } else {
                    HttpUtil.sendHttpRequest(Apiconfig.NAIVE, param, new HttpUtil.HttpCallbackListener() {
                        @Override
                        public void onFinish(Response response) {
                            if (response.isSuccess()) {
                                mNaiveButton.setBackgroundResource(R.drawable.ic_thumb_down_pink_24dp);
                                answer.setNaive(true);
                                answer.setNaiveCount(answer.getNaiveCount() + 1);
                                mNaiveCount.setText("(" + answer.getNaiveCount() + ")");
                            } else {
                            }
                        }

                        @Override
                        public void onError(Exception e) {
                            Log.d("TAG", e.toString());
                        }
                    });
                }
            }
        });

    }

    private void initExcitingButton(final Answer answer) {
        mExcitingButton.setOnClickListener(new View.OnClickListener() {
            String param = "id=" + answer.getId() + "&type=2&token=" + mUser.getToken();

            @Override
            public void onClick(View v) {
                if (answer.isExciting()) {
                    HttpUtil.sendHttpRequest(Apiconfig.CANCEL_EXCITING, param, new HttpUtil.HttpCallbackListener() {
                        @Override
                        public void onFinish(Response response) {
                            if (response.isSuccess()) {
                                mExcitingButton.setBackgroundResource(R.drawable.ic_thumb_up_gray_24dp);
                                answer.setExciting(false);
                                answer.setExcitingCount(answer.getExcitingCount() - 1);
                                mExcitingCount.setText("(" + answer.getExcitingCount() + ")");
                            }
                        }

                        @Override
                        public void onError(Exception e) {
                            Log.d("TAG", e.toString());
                        }
                    });
                } else {
                    HttpUtil.sendHttpRequest(Apiconfig.EXCITING, param, new HttpUtil.HttpCallbackListener() {
                        @Override
                        public void onFinish(Response response) {
                            if (response.isSuccess()) {
                                mExcitingButton.setBackgroundResource(R.drawable.ic_thumb_up_pink_24dp);
                                answer.setExciting(true);
                                answer.setExcitingCount(answer.getExcitingCount() + 1);
                                mExcitingCount.setText("(" + answer.getExcitingCount() + ")");
                            } else {
                            }
                        }

                        @Override
                        public void onError(Exception e) {
                            Log.d("TAG", e.toString());
                        }
                    });
                }
            }
        });
    }

    private void initAcceptButton(final Answer answer) {
        mAcceptButton.setOnClickListener(new View.OnClickListener() {
            String param = "qid=" + mQuestion.getId() + "&aid=" + answer.getId() + "&token=" + mUser.getToken();
            @Override
            public void onClick(View v) {
                if (mQuestion.getAuthorName().equals(mUser.getUsername())&&answer.getBest() == 0) {
                    mAcceptButton.setBackgroundResource(R.drawable.ic_accept_pink_24dp);
                    HttpUtil.sendHttpRequest(Apiconfig.ACCEPT, param, new HttpUtil.HttpCallbackListener() {
                        @Override
                        public void onFinish(Response response) {
                            if (response.isSuccess()) {

                                answer.setBest(1);
                            }
                        }

                        @Override
                        public void onError(Exception e) {
                            Log.d("TAG", e.toString());
                        }
                    });
                }else{
                    Toast.makeText(MyApplication.getContext(),"您已采纳该回答，或者您不是该问题的提出者",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
