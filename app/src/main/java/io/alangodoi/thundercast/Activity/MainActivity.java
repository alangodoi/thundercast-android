package io.alangodoi.thundercast.Activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import io.alangodoi.thundercast.Fragment.ExploreFragment;
import io.alangodoi.thundercast.Fragment.PlayerFragment;
import io.alangodoi.thundercast.Fragment.PodcastsFragment;
import io.alangodoi.thundercast.Fragment.ProfileFragment;
import io.alangodoi.thundercast.R;

public class MainActivity
        extends AppCompatActivity
        implements ExploreFragment.OnFragmentInteractionListener,
        PodcastsFragment.OnFragmentInteractionListener,
        ProfileFragment.OnFragmentInteractionListener {

    private static final String TAG = "MainActivity";
    Toolbar player;
    ImageView playPause, replay, forward, openPlayer;
    Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        player = findViewById(R.id.player);
//        player.setVisibility(View.GONE);

        playPause = player.findViewById(R.id.ivPlayPodcast);
        replay = player.findViewById(R.id.ivReplayPodcast);
        forward = player.findViewById(R.id.ivForwardPodcast);
        openPlayer = player.findViewById(R.id.ivOpenPlayer);

        playPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Player: Play");
//                if playing
                Glide.with(MainActivity.this).load(R.drawable.ic_pause)
                        .skipMemoryCache(false)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(playPause);
            }
        });

        replay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Player: Replay");
            }
        });

        forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Player: Forward");
            }
        });

        openPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Player: Open Player");
                fragment = new PlayerFragment();
                loadFragment(fragment);
            }
        });

        loadFragment(new ExploreFragment());
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
                case R.id.navigation_explore:
                    fragment = new ExploreFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_podcasts:
                    fragment = new PodcastsFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_player:
                    fragment = new PlayerFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_profile:
                    fragment = new ProfileFragment();
                    loadFragment(fragment);
                    return true;
            }
            return false;
        }
    };

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
