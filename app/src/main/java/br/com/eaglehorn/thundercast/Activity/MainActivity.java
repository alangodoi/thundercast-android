package br.com.eaglehorn.thundercast.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import br.com.eaglehorn.thundercast.Fragment.ExploreFragment;
import br.com.eaglehorn.thundercast.Fragment.PlayerFragment;
import br.com.eaglehorn.thundercast.Fragment.PodcastsFragment;
import br.com.eaglehorn.thundercast.Fragment.ProfileFragment;
import br.com.eaglehorn.thundercast.Preference.PrefManager;
import br.com.eaglehorn.thundercast.R;
import br.com.eaglehorn.thundercast.Service.PlayerService;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity
        extends AppCompatActivity
        implements ExploreFragment.OnFragmentInteractionListener,
        PodcastsFragment.OnFragmentInteractionListener,
        ProfileFragment.OnFragmentInteractionListener {

    private static final String TAG = "MainActivity";

    @BindView(R.id.player) Toolbar player;
    @BindView(R.id.ivPlayPodcast) ImageView playPause;
    @BindView(R.id.ivReplayPodcast) ImageView replay;
    @BindView(R.id.ivForwardPodcast) ImageView forward;
    @BindView(R.id.ivOpenPlayer) ImageView openPlayer;
    @BindView(R.id.nav_view) BottomNavigationView navView;

    Fragment fragment;

    PrefManager prefManager;

    private String filename;

//    PlayerService playerService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        loadFragment(new ExploreFragment());
    }

    @OnClick(R.id.ivPlayPodcast)
    void playPause() {
        Log.d(TAG, "Player: Play");

        if (prefManager.getPlayerStatus().equals("playing")) {
            Intent intent = new Intent(MainActivity.this, PlayerService.class);
            intent.putExtra("action", PlayerService.ACTION_PAUSE);
            startService(intent);

            Glide.with(MainActivity.this).load(R.drawable.ic_play)
                    .into(playPause);

        } else if (prefManager.getPlayerStatus().equals("paused")) {
            Intent intent = new Intent(MainActivity.this, PlayerService.class);
            intent.putExtra("action", PlayerService.ACTION_RESUME);
            intent.putExtra("filename", filename);
            startService(intent);

            Glide.with(MainActivity.this).load(R.drawable.ic_pause)
                    .into(playPause);
        }
    }

    @OnClick(R.id.ivReplayPodcast)
    void replay() {
        Log.d(TAG, "Player: Replay");
    }

    @OnClick(R.id.ivForwardPodcast)
    void forward() {
        Log.d(TAG, "Player: Forward");
    }

    @OnClick(R.id.ivOpenPlayer)
    void openPlayer() {
        Log.d(TAG, "Player: Open Player");
        fragment = new PlayerFragment();
        loadFragment(fragment);
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

    @Override
    public void onBackPressed() {
        finish();
//        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        prefManager = new PrefManager(this);
        if (!prefManager.getPlayerStatus().equals("stopped")) {
            player.setVisibility(View.VISIBLE);

            if (prefManager.getPlayerStatus().equals("playing")){
                Glide.with(MainActivity.this).load(R.drawable.ic_pause)
                        .into(playPause);
            } else if (prefManager.getPlayerStatus().equals("paused")) {
                Glide.with(MainActivity.this).load(R.drawable.ic_play)
                        .into(playPause);

                filename = prefManager.getPlayingFile();
            }

        } else {
            player.setVisibility(View.GONE);
        }
    }

}
