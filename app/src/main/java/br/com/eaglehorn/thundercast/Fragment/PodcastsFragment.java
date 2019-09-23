package br.com.eaglehorn.thundercast.Fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import br.com.eaglehorn.thundercast.Activity.PodcastDetails;
import br.com.eaglehorn.thundercast.Adapter.SubscriptionLineAdapter;
import br.com.eaglehorn.thundercast.Adapter.ViewHolder.SubscriptionLineHolder;
import br.com.eaglehorn.thundercast.Database.DB;
import br.com.eaglehorn.thundercast.Model.Podcast;
import br.com.eaglehorn.thundercast.Network.ApiClient;
import br.com.eaglehorn.thundercast.Network.ApiInterface;
import br.com.eaglehorn.thundercast.Preference.PrefManager;
import br.com.eaglehorn.thundercast.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PodcastsFragment extends Fragment implements SubscriptionLineHolder.OnSubscriptionClickListener {

    private static final String TAG = "PodcastsFragment";

    private OnFragmentInteractionListener mListener;
    private ApiInterface apiInterface;
    private SubscriptionLineAdapter subscriptionAdapter;
    private List<Podcast> subscriptionlist;

    private PrefManager prefManager;

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.rvSubscriptions) RecyclerView recyclerView;

    private DB db;

    public PodcastsFragment() {
        // Required empty public constructor
    }

    public static PodcastsFragment newInstance(String param1, String param2) {
        PodcastsFragment fragment = new PodcastsFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_podcasts, container,
                false);
        ButterKnife.bind(this, rootView);

        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.hint_subscriptions);

//        getSubscriptions();

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            Window window = getActivity().getWindow();
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.setStatusBarColor(getResources().getColor(R.color.colorWhite));
//            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
//        }

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
            recyclerView.setPadding(0,0,0,paddingPixel);
        }

        db = new DB(getActivity());
        subscriptionlist = new ArrayList<>();

        subscriptionlist = db.getPodcasts();
        subscriptionAdapter = new SubscriptionLineAdapter(getActivity(), subscriptionlist, this);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 4);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(subscriptionAdapter);

        apiInterface = ApiClient.getClient().create(ApiInterface.class);
    }

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
    public void onSubClick(int position) {
        Log.d(TAG, "onSubClick: " + position);
        Intent intent = new Intent(getActivity(), PodcastDetails.class);
        intent.putExtra("id", subscriptionlist.get(position).getId());
        intent.putExtra("title", subscriptionlist.get(position).getTitle());
        intent.putExtra("artistName", subscriptionlist.get(position).getArtistName());
        intent.putExtra("description", subscriptionlist.get(position).getDescription());
        intent.putExtra("artwork", subscriptionlist.get(position).getArtwork());
        intent.putExtra("link", subscriptionlist.get(position).getLink());
        intent.putExtra("feed", subscriptionlist.get(position).getFeed());
        intent.putExtra("copyright", subscriptionlist.get(position).getCopyright());
        startActivity(intent);
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void getSubscriptions() {
        Call<List<Podcast>> call = apiInterface.podcasts();

        call.enqueue(new Callback<List<Podcast>>() {
            @Override
            public void onResponse(Call<List<Podcast>> call, Response<List<Podcast>> response) {
                for (int i=0; i<response.body().size(); i++) {
                    subscriptionlist.add(new Podcast(
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

                subscriptionAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Podcast>> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.toString());
            }
        });
    }
}
