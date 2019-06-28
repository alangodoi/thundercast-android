package io.alangodoi.thundercast.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import io.alangodoi.thundercast.Adapter.ViewHolder.TrendingLineHolder;
import io.alangodoi.thundercast.Model.Podcast;
import io.alangodoi.thundercast.R;

public class TrendingLineAdapter extends RecyclerView.Adapter<TrendingLineHolder> {

    private Context mContext;
    private List<Podcast> trendingList;
    private TrendingLineHolder.OnTrendClickListener onTrendClickListener;

    public TrendingLineAdapter(Context mContext, List<Podcast> trendingList, TrendingLineHolder.OnTrendClickListener onTrendClickListener) {
        this.mContext = mContext;
        this.trendingList = trendingList;
        this.onTrendClickListener = onTrendClickListener;
    }

    @NonNull
    @Override
    public TrendingLineHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rv_trending, viewGroup, false);
        return new TrendingLineHolder(view, onTrendClickListener);
    }

    @Override
    public void onBindViewHolder(final TrendingLineHolder holder, int position) {
        Podcast trend = trendingList.get(position);

        RequestOptions options = new RequestOptions();
        options
//                .placeholder(R.drawable.place_holder)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .skipMemoryCache(false)
//                .error(R.drawable.error_place_holder)
//                .transform(new CenterCrop())
                .transform(new RoundedCorners(8));

//        Glide.with(mContext).load(trend.getArtwork())
//                .placeholder(R.drawable.loading_animation)
//                .error(R.drawable.popcorn)
//                .transform(new RoundedCorners(5)
//                .skipMemoryCache(false)
//                .diskCacheStrategy(DiskCacheStrategy.NONE)
//                .into(holder.ivTrending);

        Glide.with(mContext).load(trend.getArtwork())
                .apply(options)
                .into(holder.ivTrending);

        holder.tvTrendingTitle.setText(trend.getTitle());
        holder.tvTrendingAuthor.setText(trend.getArtistName());
    }

    @Override
    public int getItemCount() {
        return trendingList != null ? trendingList.size() : 0;
    }

}
