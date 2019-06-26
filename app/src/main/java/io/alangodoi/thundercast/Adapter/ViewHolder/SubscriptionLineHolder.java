package io.alangodoi.thundercast.Adapter.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import io.alangodoi.thundercast.R;

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
