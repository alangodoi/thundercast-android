package br.com.eaglehorn.thundercast.Adapter.ViewHolder;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import br.com.eaglehorn.thundercast.R;

public class TrendingLineHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    OnTrendClickListener onTrendClickListener;

    public ImageView ivTrending;
    public TextView tvTrendingTitle, tvTrendingAuthor;

    public TrendingLineHolder(@NonNull View itemView, final TrendingLineHolder.OnTrendClickListener onTrendClickListener) {
        super(itemView);

        ivTrending = itemView.findViewById(R.id.ivTrending);
        tvTrendingTitle = itemView.findViewById(R.id.tvTrendingTitle);
        tvTrendingAuthor = itemView.findViewById(R.id.tvTrendingAuthor);

        this.onTrendClickListener = onTrendClickListener;
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        onTrendClickListener.onTrendClick(getAdapterPosition());
    }

    public interface OnTrendClickListener {
        void onTrendClick(int position);
    }
}
