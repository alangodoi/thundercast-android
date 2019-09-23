package br.com.eaglehorn.thundercast.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import br.com.eaglehorn.thundercast.Service.PlayerService;

import static br.com.eaglehorn.thundercast.Service.PlayerService.ACTION_PAUSE;
import static br.com.eaglehorn.thundercast.Service.PlayerService.ACTION_PLAY;
import static br.com.eaglehorn.thundercast.Service.PlayerService.ACTION_RESUME;
import static br.com.eaglehorn.thundercast.Service.PlayerService.ACTION_STOP;

public class NotificationReceiver extends BroadcastReceiver {

    private static final String TAG = "NotificationReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {

        Intent playerService = new Intent(context, PlayerService.class);
        switch (intent.getAction()) {
            case ACTION_PLAY:
                playerService.setAction(ACTION_PLAY);
                break;
            case ACTION_PAUSE:
                playerService.setAction(ACTION_PAUSE);
                break;
            case ACTION_STOP:
                playerService.setAction(ACTION_STOP);
                break;
            case ACTION_RESUME:
                playerService.setAction(ACTION_RESUME);
                break;
        }

        context.startService(playerService);
    }
}
