package com.example.mike.mikemusic.data.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Parcel;
import android.os.Parcelable;

import com.example.mike.mikemusic.BR;

/**
 * Created by ThienNA on 03/08/2018.
 */

public class Genre extends BaseObservable implements Parcelable{
    private String mTitle;
    private String mPath;
    private int mResourceImage;
    private int mResourceImageSmall;

    public Genre(String path) {
        mPath = path;
    }

    protected Genre(Parcel in) {
        mTitle = in.readString();
        mPath = in.readString();
        mResourceImage = in.readInt();
        mResourceImageSmall = in.readInt();
    }

    public static final Creator<Genre> CREATOR = new Creator<Genre>() {
        @Override
        public Genre createFromParcel(Parcel in) {
            return new Genre(in);
        }

        @Override
        public Genre[] newArray(int size) {
            return new Genre[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mTitle);
        parcel.writeString(mPath);
        parcel.writeInt(mResourceImage);
        parcel.writeInt(mResourceImageSmall);
    }

    @Bindable
    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
        notifyPropertyChanged(BR.title);
    }

    @Bindable
    public String getPath() {
        return mPath;
    }

    public void setPath(String path) {
        mPath = path;
        notifyPropertyChanged(BR.path);
    }

    @Bindable
    public int getResourceImage() {
        return mResourceImage;
    }

    public void setResourceImage(int resourceImage) {
        mResourceImage = resourceImage;
        notifyPropertyChanged(BR.resourceImage);
    }

    @Bindable
    public int getResourceImageSmall() {
        return mResourceImageSmall;
    }

    public void setResourceImageSmall(int resourceImageSmall) {
        mResourceImageSmall = resourceImageSmall;
        notifyPropertyChanged(BR.resourceImageSmall);
    }
}
