package com.example.taurus.bihu.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Taurus on 2018/2/21.
 * 储存用户信息
 *  "id": 2,
 *"username": "Jay",
 *"avatar": null,
 *"token": "ac2f704deb121877d0895cf5bb96716981610c5f"
 */

public class User implements Parcelable{
    private int id;
    private String username;
    private String imageUrl;
    private String token;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(username);
        dest.writeString(imageUrl);
        dest.writeString(token);
        dest.writeInt(id);
    }
    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>(){

        @Override
        public User createFromParcel(Parcel source) {
            User user = new User();
            user.username = source.readString();
            user.imageUrl = source.readString();
            user.token = source.readString();
            user.id = source.readInt();
            return user;
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
