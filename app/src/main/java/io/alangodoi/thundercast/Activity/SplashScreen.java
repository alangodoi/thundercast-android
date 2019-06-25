package io.alangodoi.thundercast.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;

import io.alangodoi.thundercast.R;

public class SplashScreen extends AppCompatActivity {

    ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splashscreen);
        views();

        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    progressBar.setVisibility(View.VISIBLE);
                    sleep(4500);
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }

    private void views() {
        progressBar = findViewById(R.id.pbSplash);
    }
}
