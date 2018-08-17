package com.example.mike.mikemusic.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ThienNA on 17/08/2018.
 */

public class CollectionResultSearch {
    @SerializedName("collection")
    @Expose
    private List<Track> mTracks;
    @SerializedName("next_href")
    @Expose
    private String mNextHref;

    public List<Track> getTracks() {
        return mTracks;
    }

    public void setTracks(List<Track> tracks) {
        mTracks = tracks;
    }

    public String getNextHref() {
        return mNextHref;
    }

    public void setNextHref(String nextHref) {
        mNextHref = nextHref;
    }
}
