package com.example.taurus.bihu.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Taurus on 2018/2/21.
 * <p>
 * <p>
 * "id": 2,
 * "title": "孤独的等待",
 * "content": "怎么还没有人来玩啊",
 * "images": "http://ok4qp4ux0.bkt.clouddn.com/1485064307258",
 * "date": "2017-12-26 16:53:04",
 * "exciting": 0,
 * "naive": 0,
 * "recent": null,    最近回复时间，没有就是null
 * "answerCount": 0,
 * "authorId": 2,
 * "authorName": "Jay",
 * "authorAvatar": "http://ok4qp4ux0.bkt.clouddn.com/img-222c4cafc0af1718a6a3b45224cf5229.jpg",
 * "is_exciting": false,
 * "is_naive": false,
 * "is_favorite": false
 */

public class Question implements Parcelable {
    private int id;
    private String title;
    private String content;
    private String imagesUrlString;
    private String date;
    private int exciting;
    private int naive;
    private String recent;
    private int answerCount;
    private int authorId;
    private String authorName;
    private String authorAvatarUrlString;
    private boolean is_exciting;
    private boolean is_naive;
    private boolean is_favorite;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImagesUrlString() {
        return imagesUrlString;
    }

    public void setImagesUrlString(String imagesUrlString) {
        this.imagesUrlString = imagesUrlString;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getExciting() {
        return exciting;
    }

    public void setExciting(int exciting) {
        this.exciting = exciting;
    }

    public int getNaive() {
        return naive;
    }

    public void setNaive(int naive) {
        this.naive = naive;
    }

    public String getRecent() {
        return recent;
    }

    public void setRecent(String recent) {
        this.recent = recent;
    }

    public int getAnswerCount() {
        return answerCount;
    }

    public void setAnswerCount(int answerCount) {
        this.answerCount = answerCount;
    }

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getAuthorAvatarUrlString() {
        return authorAvatarUrlString;
    }

    public void setAuthorAvatarUrlString(String authorAvatarUrlString) {
        this.authorAvatarUrlString = authorAvatarUrlString;
    }

    public boolean is_exciting() {
        return is_exciting;
    }

    public void setIs_exciting(boolean is_exciting) {
        this.is_exciting = is_exciting;
    }

    public boolean is_naive() {
        return is_naive;
    }

    public void setIs_naive(boolean is_naive) {
        this.is_naive = is_naive;
    }

    public boolean is_favorite() {
        return is_favorite;
    }

    public void setIs_favorite(boolean is_favorite) {
        this.is_favorite = is_favorite;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(content);
        dest.writeString(date);
        dest.writeString(recent);
        dest.writeInt(answerCount);
        dest.writeInt(authorId);
        dest.writeInt(exciting);
        dest.writeInt(naive);
        dest.writeString(authorName);
        dest.writeString(authorAvatarUrlString);
        dest.writeString(imagesUrlString);
        dest.writeByte((byte) (is_naive ? 1 : 0));
        dest.writeByte((byte) (is_exciting ? 1 : 0));
        dest.writeByte((byte) (is_favorite ? 1 : 0));
    }

    public static final Parcelable.Creator<Question> CREATOR = new Parcelable.Creator<Question>() {

        @Override
        public Question createFromParcel(Parcel source) {
            Question question = new Question();
            question.id = source.readInt();
            question.title = source.readString();
            question.content = source.readString();
            question.date = source.readString();
            question.recent = source.readString();
            question.answerCount = source.readInt();
            question.authorId = source.readInt();
            question.exciting = source.readInt();
            question.naive = source.readInt();
            question.authorName = source.readString();
            question.authorAvatarUrlString = source.readString();
            question.imagesUrlString = source.readString();
            question.is_naive = source.readByte() == 1;
            question.is_exciting = source.readByte() == 1;
            question.is_favorite = source.readByte() == 1;
            return question;
        }

        @Override
        public Question[] newArray(int size) {
            return new Question[size];
        }
    };
}
