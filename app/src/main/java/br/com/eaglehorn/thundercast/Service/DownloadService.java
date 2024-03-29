package br.com.eaglehorn.thundercast.Service;

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
import android.os.ResultReceiver;
import android.util.Log;
import androidx.core.app.NotificationCompat;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import br.com.eaglehorn.thundercast.R;

public class DownloadService extends Service {

    private static final String TAG = "DownloadService";

    public static final String ACTION_START_DOWNLOAD = "br.com.eaglehorn.thundercast.action.STARTDOWNLOAD";
    public static final String ACTION_STOP_DOWNLOAD = "br.com.eaglehorn.thundercast.action.STOPDOWNLOAD";

    public static final int UPDATE_PROGRESS = 1337;

    ResultReceiver receiver;
    NotificationCompat.Builder notificationBuilder;
    Notification notification;

    private final String NOTIFICATION_CHANNEL_ID = "br.com.eaglehorn.thundercast";
    private final String channelName = "My Background Service";

//    BroadcastReceiver nofiticationReceiver;
    NotificationManager manager;
    NotificationChannel chan;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
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
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        String action = intent.getAction();
        Log.d(TAG, "onStartCommand: " + action);

        receiver = intent.getParcelableExtra("receiver");

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

        return Service.START_NOT_STICKY;
    }

    private void startDownload(String URL, String filename) {

        new Thread(() -> {
            try {
                // Your implementation
                URL url = null;
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
                    output.write(data, 0, count);
                }

                // close streams
                output.flush();
                output.close();
                input.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

    }

    private void updateNotification(int progress) {

        Log.d(TAG, "updateNotification: " + progress);
//        notificationBuilder.setProgress(100, progress,false);
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

//        Intent notificationIntent = new Intent(this, MainActivity.class);
//
//        Intent startDownloadIntent = new Intent(this, NotificationReceiver.class);
//        startDownloadIntent.setAction(ACTION_START_DOWNLOAD);
//
//        Intent stopDownloadIntent = new Intent(this, NotificationReceiver.class);
//        stopDownloadIntent.setAction(ACTION_STOP_DOWNLOAD);
//
//        PendingIntent pendingIntent = PendingIntent.getActivity(
//                this,
//                0,
//                notificationIntent,
//                0);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            chan.setLightColor(Color.BLUE);
            chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            assert manager != null;
            manager.createNotificationChannel(chan);

        }

        notification = notificationBuilder.setOngoing(true)
                .setSmallIcon(R.drawable.flash)
                .setContentTitle("Download")
//                .setContentIntent(pendingIntent)
                .setProgress(100, 0, false)
                .build();

        startForeground(1337, notification);

    }

}
