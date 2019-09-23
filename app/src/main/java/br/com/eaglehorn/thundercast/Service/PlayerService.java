package br.com.eaglehorn.thundercast.Service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import java.io.File;
import java.io.IOException;

import br.com.eaglehorn.thundercast.Activity.MainActivity;
import br.com.eaglehorn.thundercast.BroadcastReceiver.NotificationReceiver;
import br.com.eaglehorn.thundercast.Preference.PrefManager;
import br.com.eaglehorn.thundercast.R;

public class PlayerService extends Service implements MediaPlayer.OnPreparedListener {

    private static final String TAG = "PlayerService";

//    public static final String ACTION_ISPLAYING = "io.alangodoi.thundercast.action.ISPLAYING";
    public static final String ACTION_PLAY = "br.com.eaglehorn.thundercast.action.PLAY";
    public static final String ACTION_PAUSE = "br.com.eaglehorn.thundercast.action.PAUSE";
    public static final String ACTION_RESUME = "br.com.eaglehorn.thundercast.action.RESUME";
    public static final String ACTION_STOP = "br.com.eaglehorn.thundercast.action.STOP";

    MediaPlayer mediaPlayer;
    PrefManager prefManager;
    BroadcastReceiver nofiticationReceiver;

    @Override
    public IBinder onBind(Intent intent) {
//        throw new UnsupportedOperationException("Not yet implemented");
        return null;
    }

    private void initMediaPlayer() {
        // ...initialize the MediaPlayer here...
//        mediaPlayer.setOnErrorListener(this);
        mediaPlayer.setOnPreparedListener(this);
    }

    @Override
    public void onCreate() {
        Toast.makeText(this, "Service Created", Toast.LENGTH_LONG).show();

        nofiticationReceiver = new NotificationReceiver();

        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();

        Log.d(TAG, "onStartCommand: ");

        String action = intent.getAction();
        Log.d(TAG, "onStartCommand: " + action);

        prefManager = new PrefManager(this);

        if (action.equals(ACTION_PLAY)) {
            String title = intent.getStringExtra("title");
            play(intent.getStringExtra("filename"), "new");
            prefManager.setPlayingTitle(title);
        } else if (action.equals(ACTION_PAUSE)) {
            pause();
        } else if (action.equals(ACTION_RESUME)) {
            resume(prefManager.getPlayingFile());
        } else if (action.equals(ACTION_STOP)) {
            stop();
        }


        createNotification();

        return Service.START_NOT_STICKY;
    }

    private void createNotification() {

        Intent notificationIntent = new Intent(this, MainActivity.class);

        Intent resumeIntent = new Intent(this, NotificationReceiver.class);
        resumeIntent.setAction(ACTION_RESUME);

        Intent pauseIntent = new Intent(this, NotificationReceiver.class);
        pauseIntent.setAction(ACTION_PAUSE);

        Intent stopIntent = new Intent(this, NotificationReceiver.class);
        stopIntent.setAction(ACTION_STOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(
                this,
                0,
                notificationIntent,
                0);


        PendingIntent resumePendingIntent =
                PendingIntent.getBroadcast(this, 0, resumeIntent, 0);

        PendingIntent pausePendingIntent =
                PendingIntent.getBroadcast(this, 0, pauseIntent, 0);

        PendingIntent stopPendingIntent =
                PendingIntent.getBroadcast(this, 0, stopIntent, 0);

        // Action to play podcast
        NotificationCompat.Action resumeAction =
                new NotificationCompat.Action.Builder(
                        R.drawable.ic_play,
                        "Resume",
                        resumePendingIntent)
                        .build();

        // Action to pause podcast
        NotificationCompat.Action pauseAction =
                new NotificationCompat.Action.Builder(
                        R.drawable.ic_pause,
                        "Pause",
                        pausePendingIntent)
                        .build();

        // Action to stop podcast
        NotificationCompat.Action stopAction =
                new NotificationCompat.Action.Builder(
                        R.drawable.ic_stop,
                        "Stop",
                        stopPendingIntent)
                        .build();

//        Notification notification = new Notification.Builder(this, CHANNEL_ID)
//                .setContentTitle("TITLE")
//                .setContentText("TEXT")
//                .setSmallIcon(R.drawable.ic_launcher_foreground)
//                .setContentIntent(pendingIntent);

        Notification notification = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.flash)
                .setContentTitle(prefManager.getPlayingTitle())
//                .setContentText("Doing some work...")
                .setContentIntent(pendingIntent)
                .addAction(resumeAction)
                .addAction(pauseAction)
                .addAction(stopAction)
//                .addAction(R.drawable.ic_play, "Resume",
//                        resumePendingIntent)
//                .addAction(R.drawable.ic_pause, "Pause",
//                        pausePendingIntent)
//                .addAction(R.drawable.ic_stop, "Stop",
//                        stopPendingIntent)
                .build();

        startForeground(1, notification);
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
        super.onDestroy();
        stopForeground(true);
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        Toast.makeText(this, "Player Prepared", Toast.LENGTH_LONG).show();
        mp.start();
//        updatePlayerStatus("playing");
    }

    public void updatePlayerStatus(String status) {
        prefManager = new PrefManager(this);
        prefManager.setPlayerStatus(status);
    }

    private void updateTimer(int currentPosition) {
        prefManager = new PrefManager(this);
        prefManager.setCurrentPosition(currentPosition);
    }

    private void play(String filename, String flag) {
        File file = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PODCASTS),
                filename
        );

        mediaPlayer = new MediaPlayer();
        initMediaPlayer();
        try {
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setDataSource(file.getAbsolutePath());
            mediaPlayer.prepareAsync();

            prefManager = new PrefManager(this);
            prefManager.setPlayingFile(filename);
            updatePlayerStatus("playing");
            if (flag.equals("new")) {
                prefManager.setCurrentPosition(0);
            } else if (flag.equals("resume")) {
                goToPosition();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void pause() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
            updatePlayerStatus("paused");
            updateTimer(mediaPlayer.getCurrentPosition());
        }
    }

    public void resume(String filename) {
        Log.d(TAG, "resume: ");
        if (mediaPlayer != null) {
            Log.d(TAG, "resume: mediaPlayer not NULL");
            goToPosition();
            mediaPlayer.start();
            updatePlayerStatus("playing");
        } else {
            Log.d(TAG, "resume: mediaPlayer NULL");
            play(filename, "resume");
        }

    }

    public void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
            updatePlayerStatus("stopped");
            prefManager.setPlayingFile("filename");
        }

        stopSelf();
    }

    public void goToPosition() {
        prefManager = new PrefManager(this);
        mediaPlayer.seekTo(prefManager.getCurrentPosition());
    }
}
