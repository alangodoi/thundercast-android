package br.com.eaglehorn.thundercast.Helper;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import br.com.eaglehorn.thundercast.Database.DB;
import br.com.eaglehorn.thundercast.Model.Podcast;

public class OPMLHelper {

    private static final String TAG = "OPMLHelper";

    private Context context;
    private DB db;
    private List<Podcast> subscriptionlist;
    StorageHelper storageHelper;
    String OPML;

    public OPMLHelper(Context context) {
        this.context = context;
    }

    public OPMLHelper generate() {
        db = new DB(context);
        StringBuilder sb = new StringBuilder();

        sb.append("<?xml version='1.0' encoding='UTF-8' standalone='yes' ?>");
        sb.append("<opml version=\"1.0\">");
        sb.append("<head>");
        sb.append("<title>Podcast Feeds</title>");
        sb.append("</head>");
        sb.append("<body>");
        sb.append("<outline text=\"feeds\">");

        subscriptionlist = new ArrayList<>();
        subscriptionlist = db.getPodcasts();

        for (int i=0; i<subscriptionlist.size(); i++) {
            sb.append("<outline type=\"rss\" ");
            sb.append("text=\""+ subscriptionlist.get(i).getTitle() +"\" ");
            sb.append("xmlUrl=\""+ subscriptionlist.get(i).getFeed() +"\"");
            sb.append("/>");
        }

        sb.append("</outline>");
        sb.append("</body>");
        sb.append("</opml>");

        OPML = sb.toString();

        Log.d(TAG, "generate: " + OPML);
        return this;
    }

    public OPMLHelper export() {

        storageHelper = new StorageHelper(context);
        storageHelper
                .fileString(OPML)
                .filename("subscriptions")
                .extension(".opml")
                .save();

        return this;
    }
}
