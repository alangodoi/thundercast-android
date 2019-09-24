package br.com.eaglehorn.thundercast.Adapter;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.ResultReceiver;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import java.util.List;
import java.util.concurrent.Executors;

import br.com.eaglehorn.thundercast.Activity.MainActivity;
import br.com.eaglehorn.thundercast.Adapter.ViewHolder.EpisodeLineHolder;
import br.com.eaglehorn.thundercast.Helper.Helper;
import br.com.eaglehorn.thundercast.Model.Episode;
import br.com.eaglehorn.thundercast.Network.ApiClient;
import br.com.eaglehorn.thundercast.Network.ApiInterface;
import br.com.eaglehorn.thundercast.Preference.PrefManager;
import br.com.eaglehorn.thundercast.R;
import br.com.eaglehorn.thundercast.Receiver.DownloadReceiver;
import br.com.eaglehorn.thundercast.Service.DownloadService;
import br.com.eaglehorn.thundercast.Service.PlayerService;
import br.com.eaglehorn.thundercast.Service.TestService;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static br.com.eaglehorn.thundercast.Service.DownloadService.ACTION_START_DOWNLOAD;
import static br.com.eaglehorn.thundercast.Service.PlayerService.ACTION_PAUSE;
import static br.com.eaglehorn.thundercast.Service.PlayerService.ACTION_PLAY;
import static br.com.eaglehorn.thundercast.Service.PlayerService.ACTION_RESUME;
import static br.com.eaglehorn.thundercast.Service.PlayerService.ACTION_STOP;

public class EpisodeLineAdapter extends RecyclerView.Adapter<EpisodeLineHolder> {

    private static final String TAG = "EpisodeLineAdapter";

    private Context mContext;
    private List<Episode> episodesList;
    private EpisodeLineHolder.OnEpisodeClickListener onEpisodeClickListener;
    private Helper helper;
    private ApiInterface apiInterface;
    PrefManager prefManager;

    DownloadReceiver downloadReceiver;
    TestService mySvc;

    public EpisodeLineAdapter(Context mContext, List<Episode> episodesList, EpisodeLineHolder.OnEpisodeClickListener onEpisodeClickListener) {
        this.mContext = mContext;
        this.episodesList = episodesList;
        this.onEpisodeClickListener = onEpisodeClickListener;
    }

    @NonNull
    @Override
    public EpisodeLineHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rv_episodes, viewGroup, false);

        return new EpisodeLineHolder(view, onEpisodeClickListener);
    }

//    private static class MyVeryOwnHandler extends Handler {
//        public void handleMessage(Message msg) {
//            // do cool stuff
//        }
//    }

//    private DownloadReceiver downloadReceiver2 = new DownloadReceiver(new Handler()) {
//        @Override
//        protected void onReceiveResult(int resultCode, Bundle resultData) {
//            super.onReceiveResult(resultCode, resultData);
//            Log.d(TAG, "TWO + onReceiveResult: " + resultData.getInt("progress"));
//        }
//    };

    @Override
    public void onBindViewHolder(final EpisodeLineHolder holder, final int position) {
        final Episode episodes = episodesList.get(position);
        helper = new Helper();
        prefManager = new PrefManager(mContext);

//        Intent intent = new Intent(mContext, DownloadService.class);
//        intent.putExtra("receiver2", downloadReceiver2);

//        downloadReceiver2 = new DownloadReceiver(null);

//        downloadReceiver = new DownloadReceiver(null) {
//
//            @Override
//            protected void onReceiveResult(int resultCode, Bundle resultData) {
//                super.onReceiveResult(resultCode, resultData);
//                Log.d(TAG, "onReceiveResult: " + resultData.getInt("progress"));
//            }
//        };

//            Intent dowint = new Intent(mContext, TestService.class);
//            dowint.putExtra("downrec", new ResultReceiver(null) {
//
//                @Override
//                protected void onReceiveResult(int resultCode, Bundle resultData) {
//                    super.onReceiveResult(resultCode, resultData);
//                    if (resultCode == DownloadService.UPDATE_PROGRESS) {
//
//                        int progress = resultData.getInt("progress"); //get the progress
//
//                        new Handler(Looper.getMainLooper()).post(() -> {
//                            holder.downloadStatus.setText("Downloading...");
//                            holder.progressBar.setProgress(progress);
//                        });
//
//                        Log.d(TAG, "onReceiveResult - ALAN: " + progress);
//
//                        if (progress == 100) {
//                            Log.d(TAG, "onReceiveResult - ALAN: 100%");
//
//                            new Handler(Looper.getMainLooper()).post(() -> {
//
//                                holder.downloadStatus.setText("Downloaded");
//                                holder.progressBar.setProgress(progress);
//
//                                Glide.with(mContext)
//                                        .load(R.drawable.ic_play)
//                                        .into(holder.ivEpisodeFile);
//
//                                holder.clDownload.setVisibility(View.INVISIBLE);
//                            });
//
//                        }
//                    }
//
//                }
//            });


        String[] wordCounter = episodes.getTitle().split(" ");
        if (wordCounter.length > 13) {
            StringBuilder title = new StringBuilder();
            for (int i=0; i<13; i++) {

                title.append(wordCounter[i]);
                title.append(" ");
            }
            title.append("...");
            holder.epTitle.setText(title);
        } else {
            holder.epTitle.setText(episodes.getTitle());
        }

        holder.epReleaseDate.setText(helper.releaseDate(episodes.getReleaseDate()));
        holder.epLength.setText(episodes.getDuration());

        final String fileName = episodes.getPodcastId() +
                episodes.getId() +
                episodes.getReleaseDate() +
                ".mp3";

        if (helper.locateFileInDisk(mContext, fileName)) {
            String playerStatus = prefManager.getPlayerStatus();

            if (playerStatus.equals("playing")) {
                if (prefManager.getPlayingFile().equals(episodes.getPodcastId() +
                        episodes.getId() +
                        episodes.getReleaseDate() +
                        ".mp3")) {

                    Glide.with(mContext).load(R.drawable.ic_pause)
                            .into(holder.ivEpisodeFile);
                }

                Glide.with(mContext).load(R.drawable.ic_play)
                            .into(holder.ivEpisodeFile);

            } else if ((playerStatus.equals("paused")) || (playerStatus.equals("stopped"))) {
                if (prefManager.getPlayingFile().equals(episodes.getPodcastId() +
                        episodes.getId() +
                        episodes.getReleaseDate() +
                        ".mp3")) {
                    Glide.with(mContext).load(R.drawable.ic_pause)
                            .into(holder.ivEpisodeFile);
                }
                Glide.with(mContext).load(R.drawable.ic_play)
                        .into(holder.ivEpisodeFile);
            } else {
                Glide.with(mContext).load(R.drawable.ic_play)
                        .into(holder.ivEpisodeFile);
            }
        }

        if (prefManager.isDownloading() &&
                prefManager.getDownloadingFile().equals(episodes.getAudioFile())) {
            Log.d(TAG, "onBindViewHolder: Show Progress");
            holder.clDownload.setVisibility(View.VISIBLE);
        }

        holder.ivEpisodeFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Download or Play: " + episodes.getAudioFile());

                String playerStatus = prefManager.getPlayerStatus();

                if (helper.locateFileInDisk(mContext, fileName)) {
                    Log.d(TAG, "Download or Play: Play");
                    Log.d(TAG, "Player Status: " + playerStatus);

                    Intent intent = new Intent(mContext, PlayerService.class);

                    // checar se o que está em execução é o mesmo

                    switch (playerStatus) {
                        case "playing":
                            if (prefManager.getPlayingFile().equals(episodes.getPodcastId() +
                                    episodes.getId() +
                                    episodes.getReleaseDate() +
                                    ".mp3")) {

//                                intent.putExtra("action", ACTION_PAUSE);
                                intent.setAction(ACTION_PAUSE);
                                mContext.startService(intent);
                                Glide.with(mContext).load(R.drawable.ic_play)
                                        .into(holder.ivEpisodeFile);
                            } else {
                                // Stop and release
//                                intent.putExtra("action", PlayerService.ACTION_STOP);
                                intent.setAction(ACTION_STOP);
                                intent.putExtra("filename", fileName);
                                mContext.startService(intent);

                                // Play other file
//                                intent.putExtra("action", PlayerService.ACTION_PLAY);
                                intent.setAction(ACTION_PLAY);
                                intent.putExtra("title", episodes.getTitle());
                                intent.putExtra("filename", fileName);
                                mContext.startService(intent);

                                Glide.with(mContext).load(R.drawable.ic_pause)
                                        .into(holder.ivEpisodeFile);
                            }

                            break;
                        case "paused":
                            if (prefManager.getPlayingFile().equals(episodes.getPodcastId() +
                                    episodes.getId() +
                                    episodes.getReleaseDate() +
                                    ".mp3")) {

//                                intent.putExtra("action", ACTION_STOP);
                                intent.setAction(ACTION_STOP);
                                intent.putExtra("filename", fileName);
                                mContext.startService(intent);

//                                intent.putExtra("action", PlayerService.ACTION_RESUME);
                                intent.setAction(ACTION_RESUME);
                                intent.putExtra("filename", fileName);
                                mContext.startService(intent);

                                Glide.with(mContext).load(R.drawable.ic_pause)
                                        .into(holder.ivEpisodeFile);
                            } else {
                                // Stop and release
//                                intent.putExtra("action", ACTION_STOP);
                                intent.setAction(ACTION_STOP);
                                intent.putExtra("filename", fileName);
                                mContext.startService(intent);

                                // Play other file
//                                intent.putExtra("action", ACTION_PLAY);
                                intent.setAction(ACTION_PLAY);
                                intent.putExtra("title", episodes.getTitle());
                                intent.putExtra("filename", fileName);
                                mContext.startService(intent);

                                Glide.with(mContext).load(R.drawable.ic_pause)
                                        .into(holder.ivEpisodeFile);
                            }

                            break;
                        case "stopped":
//                            intent.putExtra("action", ACTION_PLAY);
                            intent.setAction(ACTION_PLAY);
                            intent.putExtra("title", episodes.getTitle());
                            intent.putExtra("filename", fileName);
                            mContext.startService(intent);
                            Glide.with(mContext).load(R.drawable.ic_pause)
                                    .into(holder.ivEpisodeFile);
                            break;
                    }

                } else {
                    Log.d(TAG, "Download or Play: Download");
                    apiInterface = ApiClient.getClient().create(ApiInterface.class);

//                    Intent downloadIntent = new Intent(mContext, DownloadService.class);

                    Log.d(TAG, "onClick: ");

                    holder.clDownload.setVisibility(View.VISIBLE);
                    holder.downloadStatus.setText("Queued for download");

                    Intent downloadIntent = new Intent(mContext, TestService.class);
                    downloadIntent.setAction(ACTION_START_DOWNLOAD);
                    downloadIntent.putExtra("receiver", new DownloadReceiver(new Handler()));
                    downloadIntent.putExtra("url", episodes.getAudioFile());
                    downloadIntent.putExtra("filename", fileName);
                    downloadIntent.putExtra("extra_receiver", new ResultReceiver(null) {
                        @Override
                        protected void onReceiveResult(int resultCode, Bundle resultData) {
                            super.onReceiveResult(resultCode, resultData);
                            if (resultCode == DownloadService.UPDATE_PROGRESS) {

                                int progress = resultData.getInt("progress"); //get the progress

                                new Handler(Looper.getMainLooper()).post(() -> {
                                            holder.downloadStatus.setText("Downloading...");
                                            holder.progressBar.setProgress(progress);
                                });

//                                Log.d(TAG, "onReceiveResult - Adapter: " + progress);

                                if (progress == 100) {
//                                    Log.d(TAG, "onReceiveResult - Adapter: 100%");

                                    new Handler(Looper.getMainLooper()).post(() -> {

                                        holder.downloadStatus.setText("Downloaded");
                                        holder.progressBar.setProgress(progress);

                                        Glide.with(mContext)
                                            .load(R.drawable.ic_play)
                                            .into(holder.ivEpisodeFile);

                                        holder.clDownload.setVisibility(View.GONE);
                                        mContext.stopService(downloadIntent);
                                    });

                                }
                            }

                        }
                    });
                    mContext.startService(downloadIntent);

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return episodesList != null ? episodesList.size() : 0;
    }
}
