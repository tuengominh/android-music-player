package bts.tech.btsmusicplayer.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

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
}
