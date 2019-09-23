package br.com.eaglehorn.thundercast.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import br.com.eaglehorn.thundercast.Database.Contract.DBContract;
import br.com.eaglehorn.thundercast.Model.Podcast;
import br.com.eaglehorn.thundercast.Model.Subscription;

public class DB {

    private SQLiteDatabase db;

    public DB(Context context) {
        DBHelper dbHelper = new DBHelper(context);

        // Gets the data repository in write mode
        db = dbHelper.getWritableDatabase();
    }

    public void clearTables() {
        db.execSQL("delete from Podcasts");
        db.execSQL("delete from Subscriptions");
    }

    public long insertPodcast(Podcast podcast) {
        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(DBContract.Podcasts.COLUMN_EID, podcast.getId());
        values.put(DBContract.Podcasts.COLUMN_TITLE, podcast.getTitle());
        values.put(DBContract.Podcasts.COLUMN_DESCRIPTION, podcast.getDescription());
        values.put(DBContract.Podcasts.COLUMN_ARTIST_NAME, podcast.getArtistName());
        values.put(DBContract.Podcasts.COLUMN_LINK, podcast.getLink());
        values.put(DBContract.Podcasts.COLUMN_FEED, podcast.getFeed());
        values.put(DBContract.Podcasts.COLUMN_ARTWORK, podcast.getArtwork());


        // Insert the new row, returning the primary key value of the new row
        return db.insert(DBContract.Podcasts.TABLE_NAME, null, values);
    }

    public long insertSubscription(Subscription subscription) {
        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();

        values.put(DBContract.Subscriptions.COLUMN_PODCAST_ID, subscription.getPodcastId());

        // Insert the new row, returning the primary key value of the new row
        return db.insert(DBContract.Subscriptions.TABLE_NAME, null, values);
    }

    public boolean removeSubscription(Subscription subscription) {
        return db.delete(
                DBContract.Subscriptions.TABLE_NAME,
                "podcastId = ?",
                new String[]{String.valueOf(subscription.getPodcastId())}) > 0;
    }

    public boolean isSubscribed(Subscription subscription) {

        String[] columns = new String[]{"_id", "podcastId"};
        String selection = "podcastId = ?";
        String[] selectionArgs = { String.valueOf(subscription.getPodcastId()) };

        Cursor cursor = db.query(
                "Subscriptions",
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        return (cursor.getCount() > 0);
    }

    public boolean removePodcast(Podcast podcast) {
        return db.delete(
                DBContract.Podcasts.TABLE_NAME,
                "eid = ?",
                new String[]{String.valueOf(podcast.getId())}) > 0;
    }

    public List<Podcast> getPodcasts() {
        List<Podcast> list = new ArrayList<>();

        String[] columns = new String[]
                {"_id", "eid", "title", "artistName", "description", "link", "feed", "artwork"};

        Cursor cursor = db.query(
                "Podcasts",
                columns,
                null,
                null,
                null,
                null,
                null
        );

        // Verify if there are results
        if(cursor.getCount() > 0) {
            cursor.moveToFirst();

            do {
                Podcast podcast = new Podcast();
                podcast.setId(cursor.getInt(1));
                podcast.setTitle(cursor.getString(2));
                podcast.setArtistName(cursor.getString(3));
                podcast.setDescription(cursor.getString(4));
                podcast.setLink(cursor.getString(5));
                podcast.setFeed(cursor.getString(6));
                podcast.setArtwork(cursor.getString(7));

                list.add(podcast);
            } while (cursor.moveToNext());

        }

        cursor.close();
        return(list);
    }

//    public List<Lecture> getLectures() {
//        List<Lecture> list = new ArrayList<>();
//        String[] columns = new String[]{"_id", "speakerId", "name", "description", "period", "time", "room", "speakerId"};
//
//        Cursor cursor = db.query(
//                "Lectures",
//                columns,
//                null,
//                null,
//                null,
//                null,
//                "time ASC"
//        );
//
//        // Verify if there are results
//        if(cursor.getCount() > 0) {
//            cursor.moveToFirst();
//
//            do {
//                Lecture lecture = new Lecture();
//                lecture.setSpeakerId(cursor.getInt(1));
//                lecture.setName(cursor.getString(2));
//                lecture.setDescription(cursor.getString(3));
//                lecture.setPeriod(cursor.getString(4));
//                lecture.setTime(cursor.getInt(5));
//                lecture.setRoom(cursor.getString(6));
//                lecture.setSpeakerId(cursor.getInt(7));
//
//                list.add(lecture);
//            } while (cursor.moveToNext());
//
//        }
//
//        cursor.close();
//        return(list);
//    }

}
