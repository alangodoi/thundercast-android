package br.com.eaglehorn.thundercast.Adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import br.com.eaglehorn.thundercast.Adapter.ViewHolder.SubscriptionLineHolder;
import br.com.eaglehorn.thundercast.Model.Podcast;
import br.com.eaglehorn.thundercast.R;

public class SubscriptionLineAdapter extends RecyclerView.Adapter<SubscriptionLineHolder> {

    private Context mContext;
    private List<Podcast> subscriptionList;
    private SubscriptionLineHolder.OnSubscriptionClickListener onSubscriptionClickListener;

    public SubscriptionLineAdapter(Context mContext, List<Podcast> subscriptionList, SubscriptionLineHolder.OnSubscriptionClickListener onSubscriptionClickListener) {
        this.mContext = mContext;
        this.subscriptionList = subscriptionList;
        this.onSubscriptionClickListener = onSubscriptionClickListener;
    }

    @NonNull
    @Override
    public SubscriptionLineHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rv_podcasts, viewGroup, false);
        return new SubscriptionLineHolder(view, onSubscriptionClickListener);
    }

    @Override
    public void onBindViewHolder(final SubscriptionLineHolder holder, int position) {
        Podcast subscriptions = subscriptionList.get(position);

        Glide.with(mContext).load(subscriptions.getArtwork())
//                .placeholder(R.drawable.loading_animation)
//                .error(R.drawable.popcorn)
                .skipMemoryCache(false)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(holder.ivPodcast);

    }

    @Override
    public int getItemCount() {
        return subscriptionList != null ? subscriptionList.size() : 0;
    }
}
