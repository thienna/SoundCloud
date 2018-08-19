package com.example.mike.mikemusic.data.source.local.config;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import com.example.mike.mikemusic.data.model.Playlist;
import com.example.mike.mikemusic.data.model.Track;
import com.example.mike.mikemusic.data.model.User;
import com.example.mike.mikemusic.utils.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ThienNA on 19/08/2018.
 */

public class PlaylistTrackDbHelper extends SQLiteOpenHelper {

    private static final String SQL_CREATE_PLAYLIST_ENTRIES = "CREATE TABLE "
            + PlaylistEntry.TABLE_NAME_PLAYLIST
            + " ( "
            + PlaylistEntry.COLUMN_NAME_PLAYLIST_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + PlaylistEntry.COLUMN_NAME_PLAYLIST + " TEXT NOT NULL"
            + " );";

    private static final String SQL_CREATE_TRACK_ENTRIES = "CREATE TABLE "
            + PlaylistEntry.TABLE_NAME_TRACK + " ( "
            + Track.TrackEntity.ARTWORK_URL + " TEXT, "
            + Track.TrackEntity.DESCRIPTION + " TEXT, "
            + Track.TrackEntity.DOWNLOADABLE + " INTEGER, "
            + Track.TrackEntity.DOWNLOAD_URL + " TEXT, "
            + Track.TrackEntity.DURATION + " INTEGER, "
            + Track.TrackEntity.ID + " INTEGER NOT NULL, "
            + Track.TrackEntity.PLAYBACK_COUNT + " INTEGER, "
            + Track.TrackEntity.TITLE + " TEXT, "
            + Track.TrackEntity.URI + " TEXT, "
            + Track.TrackEntity.USERNAME + " TEXT, "
            + Track.TrackEntity.LIKES_COUNT + " INTEGER, "
            + " PRIMARY KEY( "
            + Track.TrackEntity.ID
            + " )"
            + ");";

    private static final String SQL_CREATE_PLAYLIST_HAS_TRACK = "CREATE TABLE "
            + PlaylistEntry.TABLE_NAME_PLAYLIST_HAS_TRACK
            + " ( "
            + PlaylistEntry.COLUMN_NAME_PLAYLIST_ID + " INTEGER NOT NULL,"
            + PlaylistEntry.COLUMN_NAME_TRACK_ID + " INTEGER NOT NULL, "
            + "PRIMARY KEY( "
            + PlaylistEntry.COLUMN_NAME_TRACK_ID + " , "
            + PlaylistEntry.COLUMN_NAME_PLAYLIST_ID
            + " ) "
            + ");";

    private static PlaylistTrackDbHelper sPlaylistTrackDbHelper;

    public static PlaylistTrackDbHelper getInstance(@NonNull Context context) {
        if (sPlaylistTrackDbHelper == null) {
            sPlaylistTrackDbHelper = new PlaylistTrackDbHelper(context);
        }
        return sPlaylistTrackDbHelper;
    }

    private PlaylistTrackDbHelper(Context context) {
        super(context, Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_PLAYLIST_ENTRIES);
        db.execSQL(SQL_CREATE_TRACK_ENTRIES);
        db.execSQL(SQL_CREATE_PLAYLIST_HAS_TRACK);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void insertTrack(Track track) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Track.TrackEntity.ARTWORK_URL, track.getArtworkUrl());
        if (track.isDownloadable()) {
            values.put(Track.TrackEntity.DOWNLOADABLE, 1);
        } else {
            values.put(Track.TrackEntity.DOWNLOADABLE, 0);
        }
        values.put(Track.TrackEntity.DOWNLOAD_URL, track.getDownloadUrl());
        values.put(Track.TrackEntity.DURATION, track.getDuration());
        values.put(Track.TrackEntity.ID, track.getId());
        values.put(Track.TrackEntity.PLAYBACK_COUNT, track.getPlaybackCount());
        values.put(Track.TrackEntity.TITLE, track.getTitle());
        values.put(Track.TrackEntity.URI, track.getUri());
        values.put(Track.TrackEntity.USERNAME, track.getUser().getUserName());
        values.put(Track.TrackEntity.LIKES_COUNT, track.getLikesCount());
        db.insert(PlaylistEntry.TABLE_NAME_TRACK, null, values);
        db.close();
    }

    public void insertPlaylist(String namePlaylist) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(PlaylistEntry.COLUMN_NAME_PLAYLIST, namePlaylist);
        db.insert(PlaylistEntry.TABLE_NAME_PLAYLIST, null, values);
        db.close();
    }

    public void insertToTablePlaylistHasTrack(int trackId, int playlistId) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(PlaylistEntry.COLUMN_NAME_PLAYLIST_ID, playlistId);
        contentValues.put(PlaylistEntry.COLUMN_NAME_TRACK_ID, trackId);
        db.insert(PlaylistEntry.TABLE_NAME_PLAYLIST_HAS_TRACK, null, contentValues);
        db.close();
    }

    public List<Playlist> getAllPlaylist() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(PlaylistEntry.TABLE_NAME_PLAYLIST, null,
                null, null, null, null, null);

        List<Playlist> playlists = new ArrayList<>();
        while (cursor.moveToNext()) {
            Playlist playlist = new Playlist();
            playlist.setName(cursor.getString(cursor.getColumnIndexOrThrow(PlaylistEntry.COLUMN_NAME_PLAYLIST)));
            playlist.setId(cursor.getInt(cursor.getColumnIndexOrThrow(PlaylistEntry.COLUMN_NAME_PLAYLIST_ID)));
            playlists.add(playlist);
        }
        cursor.close();
        db.close();
        return playlists;
    }

    public List<Track> getTracksWithPlaylistId(int playlistId) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " +
            PlaylistEntry.TABLE_NAME_PLAYLIST_HAS_TRACK + " , " + PlaylistEntry.TABLE_NAME_TRACK
                + " WHERE " + PlaylistEntry.TABLE_NAME_PLAYLIST_HAS_TRACK + "." + PlaylistEntry
                .COLUMN_NAME_PLAYLIST_ID + " = " + playlistId + " AND " + PlaylistEntry
                .TABLE_NAME_TRACK + "." + Track.TrackEntity.ID + " = " + PlaylistEntry
                .TABLE_NAME_PLAYLIST_HAS_TRACK + "." + PlaylistEntry.COLUMN_NAME_TRACK_ID, null);

        if (cursor == null) return null;

        List<Track> tracks = new ArrayList<>();
        cursor.moveToFirst();
        do {
            Track track = parseTrackFromCusor(cursor);
            if (track != null) {
                tracks.add(track);
            }
        } while (cursor.moveToNext());

        cursor.close();
        db.close();

        return tracks;
    }

    private Track parseTrackFromCusor(Cursor cursor) {
        Track track = new Track();

        int downloadable = cursor.getInt(
                cursor.getColumnIndexOrThrow(Track.TrackEntity.DOWNLOADABLE));

        if (downloadable == 1) {
            track.setDownloadable(true);
        } else {
            track.setDownloadable(false);
        }

        track.setId(cursor.getInt(cursor.getColumnIndexOrThrow(Track.TrackEntity.ID)));
        track.setDuration(cursor.getInt(cursor.getColumnIndexOrThrow(Track.TrackEntity.DURATION)));
        track.setUri(cursor.getString(cursor.getColumnIndexOrThrow(Track.TrackEntity.URI)));
        track.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(Track.TrackEntity.TITLE)));
        track.setArtworkUrl(cursor.getString(cursor.getColumnIndexOrThrow(Track.TrackEntity.ARTWORK_URL)));
        track.setLikesCount(cursor.getInt(cursor.getColumnIndexOrThrow(Track.TrackEntity.LIKES_COUNT)));
        track.setPlaybackCount(cursor.getInt(cursor.getColumnIndexOrThrow(Track.TrackEntity.PLAYBACK_COUNT)));
        track.setDownloadUrl(cursor.getString(cursor.getColumnIndexOrThrow(Track.TrackEntity.DOWNLOAD_URL)));
        User user = new User();
        user.setUserName(cursor.getString(cursor.getColumnIndexOrThrow(Track.TrackEntity.USERNAME)));
        track.setUser(user);

        return track;
    }

    public static final class PlaylistEntry {
        public static final String TABLE_NAME_PLAYLIST = "Playlists";
        public static final String TABLE_NAME_TRACK = "Tracks";
        public static final String TABLE_NAME_PLAYLIST_HAS_TRACK = "PlaylistTrack";
        public static final String COLUMN_NAME_PLAYLIST = "playlist_name";
        public static final String COLUMN_NAME_PLAYLIST_ID = "playlist_id";
        public static final String COLUMN_NAME_TRACK_ID = "track_id";
    }
}
