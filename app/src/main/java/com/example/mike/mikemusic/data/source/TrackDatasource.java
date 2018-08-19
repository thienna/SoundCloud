package com.example.mike.mikemusic.data.source;

import com.example.mike.mikemusic.data.model.Playlist;
import com.example.mike.mikemusic.data.model.Track;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by ThienNA on 01/08/2018.
 */

public interface TrackDatasource {

    interface RemoteDataSource {
        Observable<List<Track>> getTracksByGenre(String genrePath, int offset);

        Observable<List<Track>> searchTrack(String query, int offset);
    }

    interface LocalDataSource {
        Observable<List<Track>> getTracksLocal();

        boolean deleteTrack(Track track);

        void addTracksToPlaylist(int playlistId,
                                 OnHandleDatabaseListener listener, Track... tracks);

        void addTracksToNewPlaylist(String newPlaylistName,
                                    OnHandleDatabaseListener listener, Track... tracks);

        List<Playlist> getPlaylist();

        List<Playlist> getDetailPlaylist();
    }

    interface OnHandleDatabaseListener {
        void onHandleSuccess(String message);

        void onHandleFailure(String message);
    }
}
