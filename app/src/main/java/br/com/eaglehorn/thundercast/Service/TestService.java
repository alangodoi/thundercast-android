package br.com.eaglehorn.thundercast.Service;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.os.Message;
import android.os.ResultReceiver;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import br.com.eaglehorn.thundercast.Preference.PrefManager;
import br.com.eaglehorn.thundercast.R;

public class TestService extends IntentService {

    private static final String TAG = "TestService";

    public static final String ACTION_START_DOWNLOAD = "br.com.eaglehorn.thundercast.action.STARTDOWNLOAD";
    public static final String ACTION_STOP_DOWNLOAD = "br.com.eaglehorn.thundercast.action.STOPDOWNLOAD";

    public static final int UPDATE_PROGRESS = 1337;

    ResultReceiver receiver, extra_receiver, dowrec;
    NotificationCompat.Builder notificationBuilder;
    Notification notification;

    private final String NOTIFICATION_CHANNEL_ID = "br.com.eaglehorn.thundercast";
    private final String channelName = "My Background Service";

    NotificationManager manager;
    NotificationChannel chan;

    PrefManager prefManager;

    public TestService() {
//        super(name);
        super("myService");
        Log.d(TAG, "TestService: ");
    }

    @Override
    public void setIntentRedelivery(boolean enabled) {
        super.setIntentRedelivery(enabled);
        Log.d(TAG, "setIntentRedelivery: ");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: ");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            chan = new NotificationChannel(
                    NOTIFICATION_CHANNEL_ID,
                    channelName,
                    NotificationManager.IMPORTANCE_DEFAULT);
        }


        manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;

        notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);

        prefManager = new PrefManager(getApplicationContext());
    }

    @Override
    public void onStart(@Nullable Intent intent, int startId) {
        super.onStart(intent, startId);
        Log.d(TAG, "onStart: ");
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand: ");
        return super.onStartCommand(intent, flags, startId);

//        return Service.START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
//        prefManager.setDownloading(false);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind: ");
        return super.onBind(intent);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.d(TAG, "onHandleIntent: ");

        String action = intent.getAction();
        receiver = intent.getParcelableExtra("receiver");
//        extra_receiver = intent.getParcelableExtra("extra_receiver");
//        dowrec = intent.getParcelableExtra("receiver2");

        if (action.equals(ACTION_START_DOWNLOAD)) {
            String url = intent.getStringExtra("url");
            String filename = intent.getStringExtra("filename");

            Log.d(TAG, "onStartCommand: " + url);

            startDownload(url, filename);

        } else if (action.equals(ACTION_STOP_DOWNLOAD)) {
//            stop();
//            stopDownload();
        }

        createNotification();
    }

    private void startDownload(String URL, String filename) {

        new Thread(() -> {
            try {
                prefManager.setDownloading(true);
                prefManager.setDownloadingFile(URL);
                // Your implementation
                java.net.URL url = null;
                url = new URL(URL);

                URLConnection connection = url.openConnection();
                connection.connect();

                // this will be useful so that you can show a typical 0-100% progress bar
                int fileLength = connection.getContentLength();

                InputStream input = new BufferedInputStream(connection.getInputStream());

                File file = new File(
                        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PODCASTS),
                        filename
                );

                OutputStream output = new FileOutputStream(file.getAbsolutePath());

                byte data[] = new byte[1024];
                long total = 0;
                int count;
                while ((count = input.read(data)) != -1) {
                    total += count;

                    // publishing the progress....
                    Bundle resultData = new Bundle();
                    int progress = (int) (total * 100 / fileLength);
                    updateNotification(progress);
                    resultData.putInt("progress" , progress);
                    receiver.send(UPDATE_PROGRESS, resultData);
//                    extra_receiver.send(UPDATE_PROGRESS, resultData);
//                    if (dowrec != null)
//                    dowrec.send(UPDATE_PROGRESS, resultData);
                    output.write(data, 0, count);
                }

                // close streams
                output.flush();
                output.close();
                input.close();

            } catch (Exception e) {
                prefManager.setDownloading(false);
                e.printStackTrace();
            }
        }).start();

    }

    private void updateNotification(int progress) {

//        Log.d(TAG, "updateNotification: " + progress);

        notification =
                notificationBuilder
                        .setSmallIcon(R.drawable.flash)
                        .setProgress(100, progress,false)
                        .setContentTitle("Download")
                        .build();

        if (progress == 100) {
            notification =
                    notificationBuilder
                            .setSmallIcon(R.drawable.flash)
                            .setProgress(100, progress,false)
                            .setContentTitle("Download")
                            .setContentText("Download concluded")
                            .build();


            stopForeground(true);
        }

        manager.notify(1337, notification);

    }

    private void createNotification() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            chan.setLightColor(Color.BLUE);
            chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            assert manager != null;
            manager.createNotificationChannel(chan);

        }

        notification = notificationBuilder.setOngoing(true)
                .setSmallIcon(R.drawable.flash)
                .setContentTitle("Download")
                .setProgress(100, 0, false)
                .build();

        startForeground(1337, notification);

    }


}
