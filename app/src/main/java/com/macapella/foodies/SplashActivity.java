package com.macapella.foodies;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        //The delay splash screen. 4000 millis is like 3 or 4 seconds, I think. Anyway the finish() end the splash screen so if the user clicks back they will exit the app.
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, GetstartedActivity.class);
                startActivity(intent);
                finish();
            }
        }, 4000);
    }
}