package br.com.eaglehorn.thundercast.Adapter.ViewHolder;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import br.com.eaglehorn.thundercast.R;
import butterknife.BindView;
import butterknife.ButterKnife;

public class EpisodeLineHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private EpisodeLineHolder.OnEpisodeClickListener onEpisodeClickListener;

    @BindView(R.id.tvEpisodeTitle) public TextView epTitle;
    @BindView(R.id.tvEpisodeReleaseDate) public TextView epReleaseDate;
    @BindView(R.id.tvEpisodeLength) public TextView epLength;
    @BindView(R.id.ivEpisodeFile) public ImageView ivEpisodeFile;

    public EpisodeLineHolder(@NonNull View itemView, final EpisodeLineHolder.OnEpisodeClickListener onEpisodeClickListener) {
        super(itemView);

        ButterKnife.bind(this, itemView);

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
