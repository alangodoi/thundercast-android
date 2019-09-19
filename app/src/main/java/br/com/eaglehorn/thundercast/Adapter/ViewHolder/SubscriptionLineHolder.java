package br.com.eaglehorn.thundercast.Adapter.ViewHolder;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import br.com.eaglehorn.thundercast.R;
import butterknife.BindView;
import butterknife.ButterKnife;

public class SubscriptionLineHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private OnSubscriptionClickListener onSubscriptionClickListener;

    @BindView(R.id.ivPodcast) public ImageView ivPodcast;

    public SubscriptionLineHolder(@NonNull View itemView, final SubscriptionLineHolder.OnSubscriptionClickListener onSubscriptionClickListener) {
        super(itemView);

        ButterKnife.bind(this, itemView);

        this.onSubscriptionClickListener = onSubscriptionClickListener;
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        onSubscriptionClickListener.onSubClick(getAdapterPosition());
    }

    public interface OnSubscriptionClickListener {
        void onSubClick(int position);
    }
}
