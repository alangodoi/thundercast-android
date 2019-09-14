package br.com.eaglehorn.thundercast.Service;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Environment;
import android.os.IBinder;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import br.com.eaglehorn.thundercast.Preference.PrefManager;

public class PlayerService extends Service implements MediaPlayer.OnPreparedListener {

    MediaPlayer mediaPlayer;

//    public static final String ACTION_ISPLAYING = "io.alangodoi.thundercast.action.ISPLAYING";
    public static final String ACTION_PLAY = "io.alangodoi.thundercast.action.PLAY";
    public static final String ACTION_PAUSE = "io.alangodoi.thundercast.action.PAUSE";
    public static final String ACTION_RESUME = "io.alangodoi.thundercast.action.RESUME";
    public static final String ACTION_STOP = "io.alangodoi.thundercast.action.STOP";

    PrefManager prefManager;

//    public PlayerService() {
//    }

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
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();

        String action = intent.getStringExtra("action");

//        if (action.equals(ACTION_PLAY)) play("teste");
        if (action.equals(ACTION_PLAY)) play(intent.getStringExtra("filename"), "new");
        else if (action.equals(ACTION_PAUSE)) pause();
        else if (action.equals(ACTION_RESUME)) resume(intent.getStringExtra("filename"));
        else if (action.equals(ACTION_STOP)) stop();

//        if (action.equals(ACTION_TOGGLE_PLAYBACK)) processTogglePlaybackRequest();
//        else if (action.equals(ACTION_PLAY)) processPlayRequest();
//        else if (action.equals(ACTION_PAUSE)) processPauseRequest();
//        else if (action.equals(ACTION_SKIP)) processSkipRequest();
//        else if (action.equals(ACTION_STOP)) processStopRequest();
//        else if (action.equals(ACTION_REWIND)) processRewindRequest();
//        else if (action.equals(ACTION_URL)) processAddRequest(intent);

//        return super.onStartCommand(intent, flags, startId);
        return Service.START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
        super.onDestroy();
//        mediaPlayer.stop();
        updatePlayerStatus("stopped");
        mediaPlayer.release();
        mediaPlayer = null;

    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        Toast.makeText(this, "Player Prepared", Toast.LENGTH_LONG).show();
        mp.start();
        updatePlayerStatus("playing");
    }

//    private static void notification() {
//
//        Notification notification = new Notification.Builder(context)
//                .setContentText(message)
//                .setSmallIcon(icon)
//                .setWhen(when)
//                .build();
//
//    }

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
        if (mediaPlayer != null) {
            goToPosition();
            mediaPlayer.start();
            updatePlayerStatus("playing");
        } else {
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
    }

    public void goToPosition() {
        prefManager = new PrefManager(this);
        mediaPlayer.seekTo(prefManager.getCurrentPosition());
    }

}
