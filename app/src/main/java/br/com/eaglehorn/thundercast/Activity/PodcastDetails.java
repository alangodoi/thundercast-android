package br.com.eaglehorn.thundercast.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import br.com.eaglehorn.thundercast.Adapter.EpisodeLineAdapter;
import br.com.eaglehorn.thundercast.Adapter.ViewHolder.EpisodeLineHolder;
import br.com.eaglehorn.thundercast.Database.DB;
import br.com.eaglehorn.thundercast.Model.Episode;
import br.com.eaglehorn.thundercast.Model.Podcast;
import br.com.eaglehorn.thundercast.Model.Subscription;
import br.com.eaglehorn.thundercast.Network.ApiClient;
import br.com.eaglehorn.thundercast.Network.ApiInterface;
import br.com.eaglehorn.thundercast.Preference.PrefManager;
import br.com.eaglehorn.thundercast.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PodcastDetails extends AppCompatActivity implements EpisodeLineHolder.OnEpisodeClickListener {

    private static final String TAG = "PodcastDetails";

    private static final int PERMISSION_REQUEST_STORAGE = 100;

    private ApiInterface apiInterface;
    private EpisodeLineAdapter episodeAdapter;
    private List<Episode> episodelist;
    PrefManager prefManager;

    @BindView(R.id.ivPodcastDetailsLogo) ImageView ivPodcastDetailsLogo;
    @BindView(R.id.tvPodcastDetailsTitle) TextView tvPodcastDetailsTitle;
    @BindView(R.id.tvPodcastDetailsAuthor) TextView tvPodcastDetailsAuthor;
    @BindView(R.id.tvPodcastDetailsLink) TextView tvPodcastDetailsLink;
    @BindView(R.id.tvPodcastDetailsCategory) TextView tvPodcastDetailsCategory;
    @BindView(R.id.tvPodcastDetailsDescription) TextView tvPodcastDetailsDescription;
    @BindView(R.id.rvEpisodes) RecyclerView recyclerView;
    @BindView(R.id.progressBar) ProgressBar progressBar;
    @BindView(R.id.btnSubscribe) Button btnSubscribe;

    private int id;

    DB db;
    String title, description, artistName, link, artwork;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_podcastdetails);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        id = intent.getIntExtra("id", 0);
        artwork = intent.getStringExtra("artwork");
        title = intent.getStringExtra("title");
        artistName = intent.getStringExtra("artistName");
        link = intent.getStringExtra("link");
        description = intent.getStringExtra("description");

        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        prefManager = new PrefManager(this);
        db = new DB(this);

        episodelist = new ArrayList<>();

        episodeAdapter = new EpisodeLineAdapter(this, episodelist, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                ((LinearLayoutManager) mLayoutManager).getOrientation());

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setAdapter(episodeAdapter);

        RequestOptions options = new RequestOptions();
        options.diskCacheStrategy(DiskCacheStrategy.ALL)
                .skipMemoryCache(false)
                .transform(new RoundedCorners(8));

        Glide.with(this).load(artwork)
                .apply(options)
                .into(ivPodcastDetailsLogo);

        tvPodcastDetailsTitle.setText(title);
        tvPodcastDetailsAuthor.setText(artistName);
        tvPodcastDetailsLink.setText(link.replaceFirst("^(http[s]?://www\\.|http[s]?://|www\\.)",""));
        tvPodcastDetailsDescription.setText(description);

        habilitarInteracao();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    PERMISSION_REQUEST_STORAGE);

        }

        getEpisodes(id);
    }

    @Override
    protected void onResume() {
        super.onResume();
        prefManager.setActivityDetailsRunning(true);

        if (db.isSubscribed(new Subscription(id))) {
            btnSubscribe.setText(getString(R.string.hint_unsubscribe));
        }
    }

    private void getEpisodes(int podcast) {
        progressBar.setVisibility(View.VISIBLE);
        desabilitarInteracao();
        Call<List<Episode>> call = apiInterface.episodes(podcast);

        call.enqueue(new Callback<List<Episode>>() {
            @Override
            public void onResponse(Call<List<Episode>> call, Response<List<Episode>> response) {
                Log.d(TAG, "Episode List Size: " + response.body().size());
                for (int i=0; i<response.body().size(); i++) {
                    episodelist.add(new Episode(
                            response.body().get(i).getId(),
                            response.body().get(i).getTitle(),
                            response.body().get(i).getDescription(),
                            response.body().get(i).getDuration(),
                            response.body().get(i).getReleaseDate(),
                            response.body().get(i).getLink(),
                            response.body().get(i).getAudioFile(),
                            response.body().get(i).getLength(),
                            response.body().get(i).getAudioFileType(),
                            response.body().get(i).getPodcastId(),
                            response.body().get(i).getCreatedAt(),
                            response.body().get(i).getUpdatedAt()
                    ));
                }

                episodeAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                habilitarInteracao();
            }

            @Override
            public void onFailure(Call<List<Episode>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                habilitarInteracao();
            }
        });
    }

    @OnClick(R.id.btnSubscribe)
    void subscribe() {

        if (!db.isSubscribed(new Subscription(id))) {
            Log.d(TAG, "subscribe: " + id);

            long insertedPodcastId = db.insertPodcast(new Podcast(
                    id,
                    artistName,
                    title,
                    description,
                    link,
                    artwork
            ));

            if (insertedPodcastId > 0) {
                long subscriptionId = db.insertSubscription(new Subscription(
                        id
                ));

                if (subscriptionId > 0) {
                    btnSubscribe.setText(getString(R.string.hint_unsubscribe));
                }
            } else {

                //TODO Error message

            }
        } else {

            if (db.removeSubscription(new Subscription(id))) {
                btnSubscribe.setText(getString(R.string.hint_subscribe));

                db.removePodcast(new Podcast(
                        id
                ));

                //TODO Success message
            }

        }

    }

    @Override
    public void onEpClick(int position) {
        Log.d(TAG, "onEpClick: " + position);

        episodelist.get(position).getId();
    }

    @Override
    protected void onStop() {
        super.onStop();
        prefManager.setActivityDetailsRunning(false);
    }

    private void desabilitarInteracao() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    private void habilitarInteracao() {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }
}
