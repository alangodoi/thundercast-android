package br.com.eaglehorn.thundercast.Receiver;

import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.util.Log;

import br.com.eaglehorn.thundercast.Service.DownloadService;

public class DownloadReceiver extends ResultReceiver {

    private static final String TAG = "DownloadReceiver";

    public DownloadReceiver(Handler handler) {
        super(handler);
    }

    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {
        super.onReceiveResult(resultCode, resultData);

        if (resultCode == DownloadService.UPDATE_PROGRESS) {

            int progress = resultData.getInt("progress"); //get the progress

//            Log.d(TAG, "DownloadReceiver - onReceiveResult: " + progress);

            if (progress == 100) {
//                Log.d(TAG, "onReceiveResult: 100%");

            }
        }
    }
}
