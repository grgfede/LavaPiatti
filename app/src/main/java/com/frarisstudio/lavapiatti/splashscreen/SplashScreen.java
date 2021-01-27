package com.frarisstudio.lavapiatti.splashscreen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

import com.frarisstudio.lavapiatti.MainActivity;
import com.frarisstudio.lavapiatti.R;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        getSupportActionBar().hide();
        ImageView ic_logo = findViewById(R.id.ic_logo);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                ActivityOptionsCompat option = ActivityOptionsCompat.makeSceneTransitionAnimation(SplashScreen.this, (View)ic_logo, ViewCompat.getTransitionName(ic_logo));
                startActivity(intent, option.toBundle());            }
        }, 1000);


    }
}