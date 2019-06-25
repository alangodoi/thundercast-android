package io.alangodoi.thundercast.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import io.alangodoi.thundercast.Adapter.TrendingLineAdapter;
import io.alangodoi.thundercast.Adapter.ViewHolder.TrendingLineHolder;
import io.alangodoi.thundercast.Model.Podcast;
import io.alangodoi.thundercast.Network.ApiClient;
import io.alangodoi.thundercast.Network.ApiInterface;
import io.alangodoi.thundercast.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ExploreFragment extends Fragment implements TrendingLineHolder.OnTrendClickListener {

    private static final String TAG = "ExploreFragment";

    private OnFragmentInteractionListener mListener;
    private ApiInterface apiInterface;
    private ConstraintLayout clSearchView;
    private SearchView svExplore;
    private ImageView ivFeatured;
    private TextView tvFeaturedTitle, tvFeaturedAuthor;
    private RecyclerView recyclerView;

    private TrendingLineAdapter trendingAdapter;
    private List<Podcast> trendinglist;

    public ExploreFragment() {
        // Required empty public constructor
    }

    public static ExploreFragment newInstance(String param1, String param2) {
        ExploreFragment fragment = new ExploreFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_explore, container,
                false);

        clSearchView = rootView.findViewById(R.id.clSearchView);
        svExplore = rootView.findViewById(R.id.svExplore);
        ivFeatured = rootView.findViewById(R.id.ivFeatured);
        tvFeaturedTitle = rootView.findViewById(R.id.tvFeaturedTitle);
        tvFeaturedAuthor = rootView.findViewById(R.id.tvFeaturedAuthor);

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

        return rootView;
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
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void getFeatured() {
        Log.d(TAG, "getFeatured: GETTING FEATUREDS");
        Call<List<Podcast>> call = apiInterface.podcasts();

        call.enqueue(new Callback<List<Podcast>>() {
            @Override
            public void onResponse(Call<List<Podcast>> call, Response<List<Podcast>> response) {
//                Log.d(TAG, "onResponse: " + response.body().get(0).getArtwork());

                Random rand = new Random();
                int value = rand.nextInt(8);

                Glide.with(getActivity()).load(response.body().get(value).getArtwork())
//                .placeholder(R.drawable.loading_animation)
//                .error(R.drawable.popcorn)
                .skipMemoryCache(false)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(ivFeatured);

                tvFeaturedTitle.setText(response.body().get(value).getTitle());
                tvFeaturedAuthor.setText(response.body().get(value).getArtistName());
            }

            @Override
            public void onFailure(Call<List<Podcast>> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.toString());
            }
        });
    }

    private void getTrending() {
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
            }

            @Override
            public void onFailure(Call<List<Podcast>> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.toString());
            }
        });
    }
}
