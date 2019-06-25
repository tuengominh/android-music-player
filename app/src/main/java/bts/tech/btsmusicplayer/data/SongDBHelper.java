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
import bts.tech.btsmusicplayer.util.SongUtil;

public class SongDBHelper extends SQLiteOpenHelper {

    /** DBHelper used to include songs' info into a SQLite database that can be accessed from the app */

    //fields for db & table info
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "song-database";
    public static final String TABLE_NAME = "songs";

    //fields for all columns of songs table
    public static final String KEY_ID_DB = "id";
    public static final String KEY_ID_RES = "resId";
    public static final String KEY_ICON_PATH = "resIcon";
    public static final String KEY_SONG_PATH = "resPath";
    public static final String KEY_TITLE = "title";
    public static final String KEY_COMMENT = "comment";
    public static final String KEY_COUNTRY = "country";
    public static final String KEY_DURATION = "duration";

    //re-usable SQL query statements
    public static final String CREATE_STATEMENT = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
            KEY_ID_DB + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            KEY_ID_RES + " INTEGER NOT NULL, " +
            KEY_ICON_PATH + " TEXT NOT NULL, " +
            KEY_SONG_PATH + " TEXT NOT NULL, " +
            KEY_TITLE + " TEXT NOT NULL, " +
            KEY_COMMENT + " TEXT NOT NULL, " +
            KEY_COUNTRY + " TEXT NOT NULL, " +
            KEY_DURATION + " TEXT NOT NULL);";

    //constructor
    public SongDBHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    //onCreate(): create table
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_STATEMENT);
    }

    //onUpgrade(): drop and re-create table
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    //add songs to database
    public void addSongData()  {
        int count = this.getCount();
        if(count == 0) {
            for (Song song : SongUtil.getSongList()) {
                add(song);
            }
        }
    }

    public void add(Song song) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        addSongDetails(song, values);
        db.insert(TABLE_NAME, null, values);
        db.close();
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

    //retrieve all songs in database
    public List<Song> getAll() {
        List<Song> songs = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT  * FROM " + TABLE_NAME, null);

        if (cursor.moveToFirst()) {
            do {
                songs.add(getSongFromCursor(cursor));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return songs;
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

    public int getCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT  * FROM " + TABLE_NAME, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }
}
