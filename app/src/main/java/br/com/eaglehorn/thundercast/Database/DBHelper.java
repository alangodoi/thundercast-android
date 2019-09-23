package br.com.eaglehorn.thundercast.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import br.com.eaglehorn.thundercast.Database.Contract.DBContract;

public class DBHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 3;
    private static final String DATABASE_NAME = "Thundercast.db";

    private static final String SQL_CREATE_PODCASTS =
            "CREATE TABLE " + DBContract.Podcasts.TABLE_NAME + " (" +
                    DBContract.Podcasts._ID + " INTEGER PRIMARY KEY, " +
                    DBContract.Podcasts.COLUMN_EID + " INTEGER," +
                    DBContract.Podcasts.COLUMN_TITLE + " TEXT," +
                    DBContract.Podcasts.COLUMN_DESCRIPTION + " TEXT," +
                    DBContract.Podcasts.COLUMN_ARTIST_NAME + " TEXT," +
                    DBContract.Podcasts.COLUMN_LINK + " TEXT," +
                    DBContract.Podcasts.COLUMN_FEED + " TEXT," +
                    DBContract.Podcasts.COLUMN_ARTWORK + " TEXT)";

    private static final String SQL_CREATE_SUBSCRIPTIONS =
            "CREATE TABLE " + DBContract.Subscriptions.TABLE_NAME + " (" +
                    DBContract.Subscriptions._ID + " INTEGER PRIMARY KEY, " +
                    DBContract.Subscriptions.COLUMN_PODCAST_ID + " INTEGER," +
                    " FOREIGN KEY ("+DBContract.Subscriptions.COLUMN_PODCAST_ID+")" +
                    " REFERENCES "+DBContract.Podcasts.TABLE_NAME+"(" +
                    DBContract.Podcasts.COLUMN_EID +"));";

    private static final String SQL_DELETE_PODCASTS =
            "DROP TABLE IF EXISTS " + DBContract.Podcasts.TABLE_NAME;

    private static final String SQL_DELETE_SUBSCRIPTIONS =
            "DROP TABLE IF EXISTS " + DBContract.Subscriptions.TABLE_NAME;


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_PODCASTS);
        db.execSQL(SQL_CREATE_SUBSCRIPTIONS);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_SUBSCRIPTIONS);
        db.execSQL(SQL_DELETE_PODCASTS);

        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
