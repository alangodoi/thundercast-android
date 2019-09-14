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

public class MainActivity
        extends AppCompatActivity
        implements ExploreFragment.OnFragmentInteractionListener,
        PodcastsFragment.OnFragmentInteractionListener,
        ProfileFragment.OnFragmentInteractionListener {

    private static final String TAG = "MainActivity";
    Toolbar player;
    ImageView playPause, replay, forward, openPlayer;
    Fragment fragment;

    PrefManager prefManager;

    private String filename;

//    PlayerService playerService;

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



//        mediaPlayer = new MediaPlayer();

//        mediaPlayer.setDataSource("/sdcard/path_to_song");

//        mediaPlayer.prepareAsync();
//        mediaPlayer.start();
//        mediaPlayer.stop();
//        mediaPlayer.pause();
//        mediaPlayer.reset();
//        mediaPlayer.getDuration();
//        mediaPlayer.getCurrentPosition();
//        mediaPlayer.seekTo(30);

        playPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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



//                if (mediaPlayer.isPlaying()) {
//                    Glide.with(MainActivity.this).load(R.drawable.ic_pause)
//                            .skipMemoryCache(false)
//                            .diskCacheStrategy(DiskCacheStrategy.ALL)
//                            .into(playPause);
//                } else {
//                    Glide.with(MainActivity.this).load(R.drawable.ic_play)
//                            .skipMemoryCache(false)
//                            .diskCacheStrategy(DiskCacheStrategy.ALL)
//                            .into(playPause);
//                }



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
