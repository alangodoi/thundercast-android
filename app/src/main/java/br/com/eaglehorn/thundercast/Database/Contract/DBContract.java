package br.com.eaglehorn.thundercast.Database.Contract;

import android.provider.BaseColumns;

public class DBContract {

    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private DBContract() {}

    /* Inner class that defines the table contents */
    public static class Podcasts implements BaseColumns {
        public static final String TABLE_NAME = "Podcasts";
        public static final String COLUMN_EID = "eid"; // Podcast External ID - From Server
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_ARTIST_NAME = "artistName";
        public static final String COLUMN_LINK = "link";
        public static final String COLUMN_FEED = "feed";
        public static final String COLUMN_ARTWORK = "artwork";
    }

    /* Inner class that defines the table contents */
    public static class Subscriptions implements BaseColumns {
        public static final String TABLE_NAME = "Subscriptions";
        public static final String COLUMN_PODCAST_ID = "podcastId";
    }
}
