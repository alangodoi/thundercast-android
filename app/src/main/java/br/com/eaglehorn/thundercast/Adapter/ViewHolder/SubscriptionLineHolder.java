package br.com.eaglehorn.thundercast.Adapter.ViewHolder;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import br.com.eaglehorn.thundercast.R;

public class SubscriptionLineHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    OnSubscriptionClickListener onSubscriptionClickListener;

    public ImageView ivPodcast;

    public SubscriptionLineHolder(@NonNull View itemView, final SubscriptionLineHolder.OnSubscriptionClickListener onSubscriptionClickListener) {
        super(itemView);

        ivPodcast = itemView.findViewById(R.id.ivPodcast);

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
