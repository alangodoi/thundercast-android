package br.com.eaglehorn.thundercast.Preference;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefManager {

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Context context;

    // shared pref mode
    private int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "thundercast";

    private static final String IS_PLAYING = "playerStatus";
    private static final String FILE_PLAYING = "playingFile";
    private static final String TITLE_PLAYING = "playingTitle";
    private static final String ACTIVITY_DETAILS = "activityDetails";
    private static final String TRACK_POSITION = "trackCurrentPosition";
    private static final String DOWNLOADING = "downloading";
    private static final String DOWNLOADING_FILE = "downloadingFile";

    public PrefManager(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
        editor.apply();
    }

    public String getPlayerStatus() {
        return pref.getString(IS_PLAYING,"stopped");
    }

    public void setPlayerStatus(String status) {
        editor.putString(IS_PLAYING, status);
        editor.apply();
    }

    public String getPlayingFile() {
        return pref.getString(FILE_PLAYING, "filename");
    }

    public void setPlayingFile(String filename) {
        editor.putString(FILE_PLAYING, filename);
        editor.apply();
    }

    public boolean getActivityDetailsRunning() {
        return pref.getBoolean(ACTIVITY_DETAILS, false);
    }

    public void setActivityDetailsRunning(boolean status) {
        editor.putBoolean(ACTIVITY_DETAILS, status);
        editor.apply();
    }

    public int getCurrentPosition() {
        return pref.getInt(TRACK_POSITION, 0);
    }

    public void setCurrentPosition(int currentPosition) {
        editor.putInt(TRACK_POSITION, currentPosition);
        editor.apply();
    }

    public void setPlayingTitle(String title) {
        editor.putString(TITLE_PLAYING, title);
        editor.apply();
    }

    public String getPlayingTitle() {
        return pref.getString(TITLE_PLAYING, "title");
    }

    public void setDownloading(boolean downloading) {
        editor.putBoolean(DOWNLOADING, downloading);
        editor.apply();
    }

    public boolean isDownloading() {
        return pref.getBoolean(DOWNLOADING, false);
    }

    public void setDownloadingFile(String file) {
        editor.putString(DOWNLOADING_FILE, file);
        editor.apply();
    }

    public String getDownloadingFile() {
        return pref.getString(DOWNLOADING_FILE, "file");
    }
}
