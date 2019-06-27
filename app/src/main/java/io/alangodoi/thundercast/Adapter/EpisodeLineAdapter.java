package io.alangodoi.thundercast.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import io.alangodoi.thundercast.Adapter.ViewHolder.EpisodeLineHolder;
import io.alangodoi.thundercast.Helper.Helper;
import io.alangodoi.thundercast.Model.Episode;
import io.alangodoi.thundercast.R;

public class EpisodeLineAdapter extends RecyclerView.Adapter<EpisodeLineHolder> {

    private Context mContext;
    private List<Episode> episodesList;
    private EpisodeLineHolder.OnEpisodeClickListener onEpisodeClickListener;
    Helper helper;

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
    public void onBindViewHolder(final EpisodeLineHolder holder, int position) {
        Episode episodes = episodesList.get(position);
        helper = new Helper();

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
//        holder.epReleaseDate.setText(episodes.getReleaseDate());
    }

    @Override
    public int getItemCount() {
        return episodesList != null ? episodesList.size() : 0;
    }
}
