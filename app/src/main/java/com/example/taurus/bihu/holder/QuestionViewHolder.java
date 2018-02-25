package com.example.taurus.bihu.holder;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.taurus.bihu.Activity.AnswerListActivity;
import com.example.taurus.bihu.R;
import com.example.taurus.bihu.config.Apiconfig;
import com.example.taurus.bihu.data.Question;
import com.example.taurus.bihu.data.User;
import com.example.taurus.bihu.utils.HttpUtil;
import com.example.taurus.bihu.utils.MyApplication;
import com.example.taurus.bihu.utils.Response;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by Taurus on 2018/2/21.
 */

public class QuestionViewHolder extends RecyclerView.ViewHolder {
    private CircleImageView mAvatar;

    private TextView mAuthorName;
    private TextView mDate;
    private TextView mRecentDate;
    private TextView mQuestionTitle;
    private TextView mQuestionDetail;
    private TextView mExcitingCount;
    private TextView mNaiveCount;
    private TextView mAnswerCount;

    private ImageButton mExcitingButton;
    private ImageButton mNaiveButton;
    private ImageButton mAnswerButton;
    private ImageButton mFavoriteButton;

    private RelativeLayout relativeLayout;

    private User mUser;
    private List<Question> questionList;

    public QuestionViewHolder(View itemView, User user, List<Question> questions) {
        super(itemView);
        mUser = user;
        questionList = questions;
        mAvatar = (CircleImageView) itemView.findViewById(R.id.avatar);
        mAuthorName = (TextView) itemView.findViewById(R.id.authorName);
        mDate = (TextView) itemView.findViewById(R.id.date);
        mRecentDate = (TextView) itemView.findViewById(R.id.recentDate);
        mQuestionTitle = (TextView) itemView.findViewById(R.id.questionTitle);
        mQuestionDetail = (TextView) itemView.findViewById(R.id.questionDetail);
        mExcitingCount = (TextView) itemView.findViewById(R.id.excitingCount);
        mNaiveCount = (TextView) itemView.findViewById(R.id.naiveCount);
        mAnswerCount = (TextView) itemView.findViewById(R.id.answerCount);
        mNaiveButton = (ImageButton) itemView.findViewById(R.id.naiveButton);
        mExcitingButton = (ImageButton) itemView.findViewById(R.id.excitingButton);
        mAnswerButton = (ImageButton) itemView.findViewById(R.id.answerButton);
        mFavoriteButton = (ImageButton) itemView.findViewById(R.id.favoriteButton);
        relativeLayout = (RelativeLayout) itemView.findViewById(R.id.up_layout);
    }

    public void initQuestionViewHolder(final Question question, List<Question> questions, QuestionViewHolder holder, int type) {
        mAuthorName.setText(question.getAuthorName());
        mDate.setText(question.getDate());
        if (question.getRecent().equals("null")) {
            mRecentDate.setText("");
        } else {
            mRecentDate.setText(question.getRecent());
        }
        mQuestionTitle.setText(question.getTitle());
        mQuestionDetail.setText(question.getContent());
        mNaiveCount.setText("(" + question.getNaive() + ")");
        mExcitingCount.setText("(" + question.getExciting() + ")");
        mAnswerCount.setText("(" + question.getAnswerCount() + ")");
        mNaiveButton.setBackgroundResource(question.is_naive() ? R.drawable.ic_thumb_down_pink_24dp : R.drawable.ic_thumb_down_gray_24dp);
        mExcitingButton.setBackgroundResource(question.is_exciting() ? R.drawable.ic_thumb_up_pink_24dp : R.drawable.ic_thumb_up_gray_24dp);
        mFavoriteButton.setBackgroundResource(question.is_favorite() ? R.drawable.ic_favorite_pink_24dp : R.drawable.ic_favorite_border_gray_24dp);
        initNaiveButton(question);
        initExcitingButton(question);
        initFavoriteButton(question);
        if (type == 1) openQuestion(relativeLayout, questions, holder);
    }


    private void initNaiveButton(final Question question) {
        mNaiveButton.setOnClickListener(new View.OnClickListener() {
            String param = "id=" + question.getId() + "&type=1&token=" + mUser.getToken();

            @Override
            public void onClick(View v) {
                if (question.is_naive()) {
                    HttpUtil.sendHttpRequest(Apiconfig.CANCEL_NAIVE, param, new HttpUtil.HttpCallbackListener() {
                        @Override
                        public void onFinish(Response response) {
                            if (response.isSuccess()) {
                                mNaiveButton.setBackgroundResource(R.drawable.ic_thumb_down_gray_24dp);
                                question.setIs_naive(false);
                                question.setNaive(question.getNaive() - 1);
                                mNaiveCount.setText("(" + question.getNaive() + ")");
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
                                question.setIs_naive(true);
                                question.setNaive(question.getNaive() + 1);

                                mNaiveCount.setText("(" + question.getNaive() + ")");
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

    private void initExcitingButton(final Question question) {
        mExcitingButton.setOnClickListener(new View.OnClickListener() {
            String param = "id=" + question.getId() + "&type=1&token=" + mUser.getToken();

            @Override
            public void onClick(View v) {
                if (question.is_exciting()) {
                    HttpUtil.sendHttpRequest(Apiconfig.CANCEL_EXCITING, param, new HttpUtil.HttpCallbackListener() {
                        @Override
                        public void onFinish(Response response) {
                            if (response.isSuccess()) {
                                mExcitingButton.setBackgroundResource(R.drawable.ic_thumb_up_gray_24dp);
                                question.setIs_exciting(false);
                                question.setExciting(question.getExciting() - 1);
                                mExcitingCount.setText("(" + question.getExciting() + ")");
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
                                question.setIs_exciting(true);
                                question.setExciting(question.getExciting() + 1);
                                mExcitingCount.setText("(" + question.getExciting() + ")");
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

    private void initFavoriteButton(final Question question) {
        mFavoriteButton.setOnClickListener(new View.OnClickListener() {
            String param = "qid=" + question.getId() + "&token=" + mUser.getToken();

            @Override
            public void onClick(View v) {
                if (question.is_favorite()) {
                    HttpUtil.sendHttpRequest(Apiconfig.CANCEL_FAVORITE, param, new HttpUtil.HttpCallbackListener() {
                        @Override
                        public void onFinish(Response response) {
                            if (response.isSuccess()) {
                                mFavoriteButton.setBackgroundResource(R.drawable.ic_favorite_border_gray_24dp);
                                question.setIs_favorite(false);
                            }
                        }

                        @Override
                        public void onError(Exception e) {
                            Log.d("TAG", e.toString());
                        }
                    });
                } else {
                    HttpUtil.sendHttpRequest(Apiconfig.FAVORITE, param, new HttpUtil.HttpCallbackListener() {
                        @Override
                        public void onFinish(Response response) {
                            if (response.isSuccess()) {
                                mFavoriteButton.setBackgroundResource(R.drawable.ic_favorite_pink_24dp);
                                question.setIs_favorite(true);
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

    public void openQuestion(RelativeLayout relativeLayout, final List<Question> questions, final QuestionViewHolder holder) {
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Question question = questions.get(position);
                Intent intent = new Intent(MyApplication.getContext(), AnswerListActivity.class);
                intent.putExtra("user_data", mUser);
                intent.putExtra("question_data", question);
                MyApplication.getContext().startActivity(intent);
            }
        });
    }


}
