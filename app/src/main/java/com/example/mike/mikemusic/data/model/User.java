package com.example.mike.mikemusic.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by ThienNA on 08/08/2018.
 */

public class User {
    @SerializedName("username")
    @Expose
    private String mUserName;

    public User() {
    }

    public String getUserName() {
        return mUserName;
    }

    public void setUserName(String userName) {
        mUserName = userName;
    }
}
