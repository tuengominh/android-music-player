package bts.tech.btsmusicplayer.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import bts.tech.btsmusicplayer.model.Song;

public class SongDatabase extends SQLiteOpenHelper {

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "song-database";
    public static final String TABLE_NAME = "songs";

    public static final String KEY_ID_DB = "id";
    public static final String KEY_ID_RES = "resId";
    public static final String KEY_ICON_PATH = "resIcon";
    public static final String KEY_SONG_PATH = "resPath";
    public static final String KEY_TITLE = "title";
    public static final String KEY_COMMENT = "comment";
    public static final String KEY_COUNTRY = "country";
    public static final String KEY_DURATION = "duration";

    public static final String CREATE_STATEMENT = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
            KEY_ID_DB + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            KEY_ID_RES + " INTEGER NOT NULL, " +
            KEY_ICON_PATH + " TEXT NOT NULL, " +
            KEY_SONG_PATH + " TEXT NOT NULL, " +
            KEY_TITLE + " TEXT NOT NULL, " +
            KEY_COMMENT + " TEXT NOT NULL, " +
            KEY_COUNTRY + " TEXT NOT NULL, " +
            KEY_DURATION + " TEXT NOT NULL);";

    public static final String SELECT_STATEMENT = String.format("SELECT %1$s, %2$s, %3$s, %4$s, %5$s, %6$s, %7$s FROM %8$s ",
            KEY_ID_RES, KEY_SONG_PATH, KEY_ICON_PATH, KEY_TITLE, KEY_COMMENT, KEY_COUNTRY, KEY_DURATION,
            TABLE_NAME);

    public SongDatabase(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_STATEMENT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    //add songs to database
    public void addSong(Song song) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        addSongDetails(song, values);

        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    //update songs in database
    public int updateSong(Song song) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        addSongDetails(song, values);

        int result = db.update(TABLE_NAME, values, KEY_ID_RES + " = ?",
                new String[]{String.valueOf(song.getResId())});
        db.close();
        return result;
    }

    //get all songs in database
    public List<Song> getAll() {
        List<Song> songs = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(SELECT_STATEMENT, null);

        if (cursor.moveToFirst()) {
            while (cursor.moveToNext()) {
                songs.add(getSongFromCursor(cursor));
            }
        }

        cursor.close();
        db.close();
        return songs;
    }

    //get song by resId
    public Song getSongByResId(int resId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME,
                new String[] { KEY_ID_RES, KEY_SONG_PATH, KEY_ICON_PATH, KEY_TITLE, KEY_COMMENT, KEY_COUNTRY, KEY_DURATION},
                KEY_ID_RES + "=?",
                new String[] { String.valueOf(resId) }, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();
        Song song = getSongFromCursor(cursor);

        cursor.close();
        db.close();
        return song;
    }

    private void addSongDetails(Song song, ContentValues values) {
        values.put(KEY_ID_RES, song.getResId());
        values.put(KEY_SONG_PATH, song.getResPath());
        values.put(KEY_ICON_PATH, song.getFlagResPath());
        values.put(KEY_TITLE, song.getTitle());
        values.put(KEY_COMMENT, song.getComment());
        values.put(KEY_COUNTRY, song.getCountry());
        values.put(KEY_DURATION, song.getDuration());
    }

    private Song getSongFromCursor(Cursor cursor) {
        return new Song(
                cursor.getInt(1), //resId
                cursor.getString(3), //resPath
                cursor.getString(2), //flagPath
                cursor.getString(4), //title
                cursor.getString(6), //country
                cursor.getString(7), //duration
                cursor.getString(5)); //comment
    }
}
