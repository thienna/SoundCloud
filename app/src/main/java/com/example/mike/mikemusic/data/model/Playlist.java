package com.example.mike.mikemusic.data.model;

import java.util.List;

/**
 * Created by ThienNA on 08/08/2018.
 */

public class Playlist {
    private int mId;
    private String mName;
    private List<Track> mTracks;

    public Playlist(int id, String name, List<Track> tracks) {
        mId = id;
        mName = name;
        mTracks = tracks;
    }

    public Playlist() {

    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public List<Track> getTracks() {
        return mTracks;
    }

    public void setTracks(List<Track> tracks) {
        mTracks = tracks;
    }
}
