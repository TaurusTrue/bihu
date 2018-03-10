package com.example.taurus.bihu.utils;

import android.support.annotation.Nullable;
import android.util.Log;

import com.example.taurus.bihu.adapter.AnswerAdapter;
import com.example.taurus.bihu.adapter.QuestionAdapter;
import com.example.taurus.bihu.data.Answer;
import com.example.taurus.bihu.data.Question;
import com.example.taurus.bihu.data.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Taurus on 2018/2/21.
 * Json解析工具类
 */

public class JsonUtil {
    public static User getUser(String response) {
        User user = new User();
        try {
            JSONObject jsonObject = new JSONObject(response);
            user.setId(jsonObject.getInt("id"));
            user.setUsername(jsonObject.getString("username"));
            user.setImageUrl(jsonObject.getString("avatar"));
            user.setToken(jsonObject.getString("token"));
        } catch (Exception e) {
            Log.d("TAG", e.toString());
        }
        return user;
    }

    public static void getAnswerList(String data, List<Answer> answers,int type) {
        if(type== AnswerAdapter.INIT_DATA)answers.clear();
        try {
            JSONObject jsonObject = new JSONObject(data);
            JSONArray jsonArray = jsonObject.getJSONArray("answers");
            for (int i = 0; i < jsonArray.length(); i++) {
                Answer answer = new Answer();
                JSONObject object = jsonArray.getJSONObject(i);
                answer.setId(object.getInt("id"));
                answer.setContent(getString(object, "content"));
                answer.setImageUrlStrings(getString(object, "images"));
                answer.setDate(getString(object, "date"));
                answer.setBest(object.getInt("best"));
                answer.setExcitingCount(object.getInt("exciting"));
                answer.setNaiveCount(object.getInt("naive"));
                answer.setAuthorId(object.getInt("authorId"));
                answer.setAuthorName(getString(object, "authorName"));
                answer.setAuthorAvatarUrlString(getString(object, "authorAvatar"));
                answer.setNaive(object.getBoolean("is_naive"));
                answer.setExciting(object.getBoolean("is_exciting"));
                answers.add(answer);
            }
        } catch (JSONException e) {
            Log.d("TAG", e.toString());
        }
    }

    public static void getQuestionList(String data, List<Question> questions, int type) {
        if (type == QuestionAdapter.INIT_DATA) questions.clear();
        try {
            JSONObject jsonObject = new JSONObject(data);
            JSONArray jsonArray = jsonObject.getJSONArray("questions");
            for (int i = 0; i < jsonArray.length(); i++) {
                Question question = new Question();
                JSONObject object = jsonArray.getJSONObject(i);
                question.setId(object.getInt("id"));
                question.setTitle(getString(object, "title"));
                question.setContent(getString(object, "content"));
                question.setImagesUrlString(getString(object, "images"));
                question.setDate(getString(object, "date"));
                question.setExciting(object.getInt("exciting"));
                question.setNaive(object.getInt("naive"));
                question.setRecent(getString(object, "recent"));
                question.setAnswerCount(object.getInt("answerCount"));
                question.setAuthorName(getString(object, "authorName"));
                question.setAuthorId(object.getInt("authorId"));
                question.setAuthorAvatarUrlString(getString(object, "authorAvatar"));
                question.setIs_exciting(object.getBoolean("is_exciting"));
                question.setIs_naive(object.getBoolean("is_naive"));
                question.setIs_favorite(object.getBoolean("is_favorite"));
                questions.add(question);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void getFavoriteList(String data, List<Question> questions,int type) {
        if(type == QuestionAdapter.INIT_DATA)questions.clear();
        try {
            JSONObject jsonObject = new JSONObject(data);
            JSONArray jsonArray = jsonObject.getJSONArray("questions");
            for (int i = 0; i < jsonArray.length(); i++) {
                Question question = new Question();
                JSONObject object = jsonArray.getJSONObject(i);
                question.setId(object.getInt("id"));
                question.setTitle(getString(object, "title"));
                question.setContent(getString(object, "content"));
                question.setImagesUrlString(getString(object, "images"));
                question.setDate(getString(object, "date"));
                question.setExciting(object.getInt("exciting"));
                question.setNaive(object.getInt("naive"));
                question.setRecent(getString(object, "recent"));
                question.setAnswerCount(object.getInt("answerCount"));
                question.setAuthorName(getString(object, "authorName"));
                question.setAuthorId(object.getInt("authorId"));
                question.setAuthorAvatarUrlString(getString(object, "authorAvatar"));
                question.setIs_exciting(object.getBoolean("is_exciting"));
                question.setIs_naive(object.getBoolean("is_naive"));
                question.setIs_favorite(true);
                questions.add(question);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static int getStatus(String data) {
        try {
            return new JSONObject(data).getInt("status");
        } catch (JSONException e) {
            e.printStackTrace();
            Log.w("tag", e.toString());
        }
        return 0;
    }

    @Nullable
    public static String getString(String data, String name) {
        try {
            return (new JSONObject(data)).getString(name);
        } catch (JSONException e) {
        }
        return null;
    }

    @Nullable
    public static String getString(JSONObject object, String name) {
        try {
            return object.getString(name);
        } catch (JSONException e) {
            Log.w("tag", e.toString());
        }
        return null;
    }
}
