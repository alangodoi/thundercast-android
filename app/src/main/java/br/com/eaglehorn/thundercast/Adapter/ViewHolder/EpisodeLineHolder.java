package br.com.eaglehorn.thundercast.Adapter.ViewHolder;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import br.com.eaglehorn.thundercast.R;

public class EpisodeLineHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    EpisodeLineHolder.OnEpisodeClickListener onEpisodeClickListener;

    public TextView epTitle, epReleaseDate, epLength;
    public ImageView ivEpisodeFile;

    public EpisodeLineHolder(@NonNull View itemView, final EpisodeLineHolder.OnEpisodeClickListener onEpisodeClickListener) {
        super(itemView);

        epTitle = itemView.findViewById(R.id.tvEpisodeTitle);
        epReleaseDate = itemView.findViewById(R.id.tvEpisodeReleaseDate);
        epLength = itemView.findViewById(R.id.tvEpisodeLength);
        ivEpisodeFile = itemView.findViewById(R.id.ivEpisodeFile);

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
