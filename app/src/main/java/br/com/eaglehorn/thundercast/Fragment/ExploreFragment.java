package br.com.eaglehorn.thundercast.Fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.SearchView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import br.com.eaglehorn.thundercast.Activity.PodcastDetails;
import br.com.eaglehorn.thundercast.Adapter.TrendingLineAdapter;
import br.com.eaglehorn.thundercast.Adapter.ViewHolder.TrendingLineHolder;
import br.com.eaglehorn.thundercast.Model.Podcast;
import br.com.eaglehorn.thundercast.Network.ApiClient;
import br.com.eaglehorn.thundercast.Network.ApiInterface;
import br.com.eaglehorn.thundercast.Preference.PrefManager;
import br.com.eaglehorn.thundercast.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ExploreFragment extends Fragment implements TrendingLineHolder.OnTrendClickListener {

    private static final String TAG = "ExploreFragment";

    private OnFragmentInteractionListener mListener;
    private ApiInterface apiInterface;
    private ConstraintLayout clSearchView, clFeatured, clBottom;
    private SearchView svExplore;
    private ImageView ivFeatured;
    private TextView tvFeaturedTitle, tvFeaturedAuthor;
    private RecyclerView recyclerView;
    private ScrollView svExp;
    private ProgressBar progressBar;

    private int featuredPodcastId = 0;
    private String featuredPodcastTitle, featuredPodcastArtistName, featuredPodcastDescription,
            featuredPodcastArtwork, featuredPodcastLink, featuredPodcastCopyright;

    private TrendingLineAdapter trendingAdapter;
    private List<Podcast> trendinglist;

    PrefManager prefManager;

    public ExploreFragment() {
        // Required empty public constructor
    }

    public static ExploreFragment newInstance(String param1, String param2) {
        ExploreFragment fragment = new ExploreFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_explore, container,
                false);

        clSearchView = rootView.findViewById(R.id.clSearchView);
        clFeatured = rootView.findViewById(R.id.clFeatured);
        svExplore = rootView.findViewById(R.id.svExplore);
        ivFeatured = rootView.findViewById(R.id.ivFeatured);
        tvFeaturedTitle = rootView.findViewById(R.id.tvFeaturedTitle);
        tvFeaturedAuthor = rootView.findViewById(R.id.tvFeaturedAuthor);
        svExp = rootView.findViewById(R.id.svExp);
        clBottom = rootView.findViewById(R.id.clBottom);
        progressBar = rootView.findViewById(R.id.progressBar);

        clSearchView.requestFocus();

        recyclerView = rootView.findViewById(R.id.rvTrending);
        trendinglist = new ArrayList<>();
        trendingAdapter = new TrendingLineAdapter(getActivity(), trendinglist, this);

//        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
//        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(trendingAdapter);

        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        getFeatured();
        getTrending();

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            Window window = getActivity().getWindow();
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.setStatusBarColor(ContextCompat
//                    .getColor(getActivity(), R.color.colorPrimaryDark));
//            window.setNavigationBarColor(ContextCompat.getColor(getActivity(), R.color.colorWhite));
//        }


        clFeatured.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                podcastDetails(
                        featuredPodcastId,
                        featuredPodcastTitle,
                        featuredPodcastArtistName,
                        featuredPodcastDescription,
                        featuredPodcastArtwork,
                        featuredPodcastLink,
                        featuredPodcastCopyright
                );
            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        prefManager = new PrefManager(getActivity());
        if (!prefManager.getPlayerStatus().equals("stopped")) {
            int paddingDp = 130;
            float density = getActivity().getResources().getDisplayMetrics().density;
            int paddingPixel = (int)(paddingDp * density);
            svExp.setPadding(0,0,0,paddingPixel);
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onTrendClick(int position) {
        Log.d(TAG, "onTrendClick: " + position);
        podcastDetails(
                trendinglist.get(position).getId(),
                trendinglist.get(position).getTitle(),
                trendinglist.get(position).getArtistName(),
                trendinglist.get(position).getDescription(),
                trendinglist.get(position).getArtwork(),
                trendinglist.get(position).getLink(),
                trendinglist.get(position).getCopyright()
        );
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void getFeatured() {
        progressBar.setVisibility(View.VISIBLE);
        Log.d(TAG, "getFeatured: GETTING FEATUREDS");
        Call<List<Podcast>> call = apiInterface.podcasts();

        call.enqueue(new Callback<List<Podcast>>() {
            @Override
            public void onResponse(Call<List<Podcast>> call, Response<List<Podcast>> response) {
                if (response.code() == 200) {
                    Log.d(TAG, "onResponse: " + response.body().get(0).getArtwork());

                    Random rand = new Random();
                    int value = rand.nextInt(12);

                    RequestOptions options = new RequestOptions();
                    options
//                .placeholder(R.drawable.place_holder)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .skipMemoryCache(false)
//                .error(R.drawable.error_place_holder)
//                .transform(new CenterCrop())
                            .transform(new RoundedCorners(8));

                    Glide.with(getActivity()).load(response.body().get(value).getArtwork())
                            .apply(options)
                            .into(ivFeatured);

                    featuredPodcastId = response.body().get(value).getId();
                    featuredPodcastTitle = response.body().get(value).getTitle();
                    featuredPodcastArtistName = response.body().get(value).getArtistName();
                    featuredPodcastDescription = response.body().get(value).getDescription();
                    featuredPodcastArtwork = response.body().get(value).getArtwork();
                    featuredPodcastLink = response.body().get(value).getLink();
                    featuredPodcastCopyright = response.body().get(value).getCopyright();

                    tvFeaturedTitle.setText(response.body().get(value).getTitle());
                    tvFeaturedAuthor.setText(response.body().get(value).getArtistName());
                } else {
                    Log.d(TAG, "onResponse - Failed: " + response.code());
                }

                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<List<Podcast>> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.toString());
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void getTrending() {
        progressBar.setVisibility(View.VISIBLE);
        Log.d(TAG, "getTrending: GETTING TRENDING");
        Call<List<Podcast>> call = apiInterface.podcasts();

        call.enqueue(new Callback<List<Podcast>>() {
            @Override
            public void onResponse(Call<List<Podcast>> call, Response<List<Podcast>> response) {
                Log.d(TAG, "onResponse: " + response.body().get(0).getArtistName());
                for (int i=0; i<5; i++) {
                    trendinglist.add(new Podcast(
                            response.body().get(i).getId(),
                            response.body().get(i).getArtistName(),
                            response.body().get(i).getTitle(),
                            response.body().get(i).getDescription(),
                            response.body().get(i).getLink(),
                            response.body().get(i).getFeed(),
                            response.body().get(i).getArtwork(),
                            response.body().get(i).getCopyright(),
                            response.body().get(i).getCreatedAt(),
                            response.body().get(i).getUpdatedAt()
                    ));
                }

                trendingAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
                clBottom.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(Call<List<Podcast>> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.toString());
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void podcastDetails(int id, String title, String artistName, String description, String artwork, String link, String copyright) {
        Intent intent = new Intent(getActivity(), PodcastDetails.class);
        intent.putExtra("id", id);
        intent.putExtra("title", title);
        intent.putExtra("artistName", artistName);
        intent.putExtra("description", description);
        intent.putExtra("artwork", artwork);
        intent.putExtra("link", link);
        intent.putExtra("copyright", copyright);
        startActivity(intent);
    }
}