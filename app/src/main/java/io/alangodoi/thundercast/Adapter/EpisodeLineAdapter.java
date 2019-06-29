package io.alangodoi.thundercast.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import java.util.List;

import io.alangodoi.thundercast.Adapter.ViewHolder.EpisodeLineHolder;
import io.alangodoi.thundercast.Helper.Helper;
import io.alangodoi.thundercast.Model.Episode;
import io.alangodoi.thundercast.Network.ApiClient;
import io.alangodoi.thundercast.Network.ApiInterface;
import io.alangodoi.thundercast.Preference.PrefManager;
import io.alangodoi.thundercast.R;
import io.alangodoi.thundercast.Service.PlayerService;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.support.constraint.Constraints.TAG;

public class EpisodeLineAdapter extends RecyclerView.Adapter<EpisodeLineHolder> {

    private Context mContext;
    private List<Episode> episodesList;
    private EpisodeLineHolder.OnEpisodeClickListener onEpisodeClickListener;
    private Helper helper;
    private ApiInterface apiInterface;
    PrefManager prefManager;

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

    @Override
    public void onBindViewHolder(final EpisodeLineHolder holder, final int position) {
        final Episode episodes = episodesList.get(position);
        helper = new Helper();
        prefManager = new PrefManager(mContext);

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

                                intent.putExtra("action", PlayerService.ACTION_PAUSE);
                                mContext.startService(intent);
                                Glide.with(mContext).load(R.drawable.ic_play)
                                        .into(holder.ivEpisodeFile);
                            } else {
                                // Stop and release
                                intent.putExtra("action", PlayerService.ACTION_STOP);
                                intent.putExtra("filename", fileName);
                                mContext.startService(intent);

                                // Play other file
                                intent.putExtra("action", PlayerService.ACTION_PLAY);
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

                                intent.putExtra("action", PlayerService.ACTION_STOP);
                                intent.putExtra("filename", fileName);
                                mContext.startService(intent);

                                intent.putExtra("action", PlayerService.ACTION_RESUME);
                                intent.putExtra("filename", fileName);
                                mContext.startService(intent);

                                Glide.with(mContext).load(R.drawable.ic_pause)
                                        .into(holder.ivEpisodeFile);
                            } else {
                                // Stop and release
                                intent.putExtra("action", PlayerService.ACTION_STOP);
                                intent.putExtra("filename", fileName);
                                mContext.startService(intent);

                                // Play other file
                                intent.putExtra("action", PlayerService.ACTION_PLAY);
                                intent.putExtra("filename", fileName);
                                mContext.startService(intent);

                                Glide.with(mContext).load(R.drawable.ic_pause)
                                        .into(holder.ivEpisodeFile);
                            }

                            break;
                        case "stopped":
                            intent.putExtra("action", PlayerService.ACTION_PLAY);
                            intent.putExtra("filename", fileName);
                            mContext.startService(intent);
                            Glide.with(mContext).load(R.drawable.ic_pause)
                                    .into(holder.ivEpisodeFile);
                            break;
                    }

                } else {
                    Log.d(TAG, "Download or Play: Download");
                    apiInterface = ApiClient.getClient().create(ApiInterface.class);

                    Call<ResponseBody> call = apiInterface.download(episodes.getAudioFile());

                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (response.isSuccessful()){
                                Log.d(TAG, "Downloaded: " + response.body());

                                if (helper.saveFileToDisk(mContext, fileName, response.body())) {
                                    Log.d(TAG, "onResponse: Saved");
                                    if (prefManager.getActivityDetailsRunning()) {
                                        Glide.with(mContext).load(R.drawable.ic_play)
                                                .into(holder.ivEpisodeFile);
                                    }
                                } else {
                                    Log.d(TAG, "onResponse: Not Saved");
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Log.d(TAG, "onFailure: " + t.toString());
                        }
                    });
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return episodesList != null ? episodesList.size() : 0;
    }
}
