package io.alangodoi.thundercast.Adapter.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import io.alangodoi.thundercast.R;

public class EpisodeLineHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    EpisodeLineHolder.OnEpisodeClickListener onEpisodeClickListener;

    public TextView epTitle, epReleaseDate, epLength;

    public EpisodeLineHolder(@NonNull View itemView, final EpisodeLineHolder.OnEpisodeClickListener onEpisodeClickListener) {
        super(itemView);

        epTitle = itemView.findViewById(R.id.tvEpisodeTitle);
        epReleaseDate = itemView.findViewById(R.id.tvEpisodeReleaseDate);
        epLength = itemView.findViewById(R.id.tvEpisodeLength);

        this.onEpisodeClickListener = onEpisodeClickListener;
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        onEpisodeClickListener.onEpClick(getAdapterPosition());
    }

    public interface OnEpisodeClickListener {
        void onEpClick(int position);
    }
}
