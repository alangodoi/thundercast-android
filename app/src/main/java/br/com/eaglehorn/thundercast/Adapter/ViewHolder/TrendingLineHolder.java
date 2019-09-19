package br.com.eaglehorn.thundercast.Adapter.ViewHolder;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import br.com.eaglehorn.thundercast.R;
import butterknife.BindView;
import butterknife.ButterKnife;

public class TrendingLineHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private OnTrendClickListener onTrendClickListener;

    @BindView(R.id.ivTrending) public ImageView ivTrending;
    @BindView(R.id.tvTrendingTitle) public TextView tvTrendingTitle;
    @BindView(R.id.tvTrendingAuthor) public TextView tvTrendingAuthor;

    public TrendingLineHolder(@NonNull View itemView, final TrendingLineHolder.OnTrendClickListener onTrendClickListener) {
        super(itemView);

        ButterKnife.bind(this, itemView);

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
