package com.example.mike.mikemusic.data.source.local;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.example.mike.mikemusic.R;
import com.example.mike.mikemusic.data.model.Playlist;
import com.example.mike.mikemusic.data.model.Track;
import com.example.mike.mikemusic.data.source.TrackDatasource;
import com.example.mike.mikemusic.data.source.local.config.PlaylistTrackDbHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

/**
 * Created by ThienNA on 02/08/2018.
 */

public class TrackLocalDataSource implements TrackDatasource.LocalDataSource {

    private static final String QUERY_DIRECTORY_NAME = "%MikeMusic%";
    private static final String VOLUME_NAME_EXTERNAL = "external";
    private static TrackLocalDataSource sInstance;
    private Context mContext;
    private PlaylistTrackDbHelper mPlaylistTrackDbHelper;

    private TrackLocalDataSource(Context context) {
        mContext = context;
        mPlaylistTrackDbHelper = PlaylistTrackDbHelper.getInstance(context);
    }

    public static synchronized TrackLocalDataSource getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new TrackLocalDataSource(context);
        }
        return sInstance;
    }

    public static void destroyInstance() {
        sInstance = null;
    }

    @Override
    public Observable<List<Track>> getTracksLocal() {
        List<Track> tracks = new ArrayList<>();
        ContentResolver musicResolver = mContext.getContentResolver();
        Uri musicUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor musicCursor = musicResolver.query(musicUri, null,
                MediaStore.Audio.Media.DATA + " LIKE ?",
                new String[]{QUERY_DIRECTORY_NAME}, null);

        if (musicCursor == null) {
            return Observable.error(new NullPointerException());
        }

        if (!musicCursor.moveToFirst()) {
            return Observable.just(tracks);
        }

        int titleColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
        int filePathColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media.DATA);
        int durationColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media.DURATION);
        int idColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media._ID);

        do {
            Track track = new Track();
            track.setTitle(musicCursor.getString(titleColumn));
            track.setUri(musicCursor.getString(filePathColumn));
            track.setDuration(musicCursor.getLong(durationColumn));
            track.setId(musicCursor.getInt(idColumn));
            tracks.add(track);
        } while (musicCursor.moveToNext());

        return Observable.just(tracks);
    }

    @Override
    public boolean deleteTrack(Track track) {
        File file = new File(track.getUri());
        String where = MediaStore.MediaColumns.DATA + "=?";
        String[] selectionArgs = new String[]{file.getAbsolutePath()};
        ContentResolver contentResolver = mContext.getContentResolver();
        Uri fileUri = MediaStore.Files.getContentUri(VOLUME_NAME_EXTERNAL);
        contentResolver.delete(fileUri, where, selectionArgs);
        return file.delete();
    }

    @Override
    public void addTracksToPlaylist(int playlistId, TrackDatasource.OnHandleDatabaseListener listener, Track... tracks) {
        if (mPlaylistTrackDbHelper == null || tracks == null || tracks.length == 0) {
            if (listener == null) return;
            listener.onHandleFailure(mContext.getString(R.string.msg_add_track_to_playlist_fail));
            return;
        }

        for (int i = 0; i < tracks.length; i++) {
            mPlaylistTrackDbHelper.insertTrack(tracks[i]);
            mPlaylistTrackDbHelper.insertToTablePlaylistHasTrack(tracks[i].getId(), playlistId);
        }

        if (listener == null) return;
        listener.onHandleSuccess(mContext.getString(R.string.msg_added_to_playlist));
    }

    @Override
    public void addTracksToNewPlaylist(String newPlaylistName, TrackDatasource.OnHandleDatabaseListener listener, Track... tracks) {
        if (mPlaylistTrackDbHelper == null || tracks == null
                || tracks.length == 0 || newPlaylistName == null) {
            if (listener == null) return;
            listener.onHandleFailure(mContext.getString(R.string.msg_add_track_to_playlist_fail));
            return;
        }

        mPlaylistTrackDbHelper.insertPlaylist(newPlaylistName);
        List<Playlist> playlists = mPlaylistTrackDbHelper.getAllPlaylist();
        int newPlaylistId = playlists.get(playlists.size() - 1).getId();
        addTracksToPlaylist(newPlaylistId, listener, tracks);
    }

    @Override
    public List<Playlist> getPlaylist() {
        if (mPlaylistTrackDbHelper == null) return null;
        return mPlaylistTrackDbHelper.getAllPlaylist();
    }

    @Override
    public List<Playlist> getDetailPlaylist() {
        if (mPlaylistTrackDbHelper == null) return null;

        List<Playlist> playlists = getPlaylist();
        if (playlists == null) return null;

        for (Playlist playlist : playlists) {
            List<Track> tracks = mPlaylistTrackDbHelper.getTracksWithPlaylistId(playlist.getId());
            playlist.setTracks(tracks);
        }
        return playlists;
    }
}
