package com.macapella.foodies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import org.jetbrains.annotations.NotNull;

public class SplashActivity extends AppCompatActivity {
    private static final int NUM_PAGES = 3;
    private ViewPager viewPager;
    private ScreenSlidePagerAdapter pagerAdapter;
    ImageView splash;
    private static int SPLASH_TIME_OUT = 4500;
    SharedPreferences sharedPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        //Assinging resouces and setting splash screen time
        splash = findViewById(R.id.img_splash);
        splash.animate().translationY(-1600).setDuration(1000).setStartDelay(4000);
        viewPager = findViewById(R.id.pager);
        pagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        //shared preference for when the user opens the app again they won't see the walkthrough screens
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                sharedPrefs = getSharedPreferences("SharedPref",MODE_PRIVATE);
                boolean isFirstTime = sharedPrefs.getBoolean("firstTime", true);

                if(isFirstTime){
                    SharedPreferences.Editor editor = sharedPrefs.edit();
                    editor.putBoolean("firstTime", false);
                    editor.commit();
                }
                else{
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        },SPLASH_TIME_OUT);
        //The delay splash screen. 4000 millis is like 3 or 4 seconds, I think. Anyway the finish() end the splash screen so if the user clicks back they will exit the app.
        /*new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //Intent intent = new Intent(SplashActivity.this, GetstartedActivity.class);
                //startActivity(intent);
                finish();
            }
        }, 4000);*/
    }

    public class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }
        //Fragments navigation from page to page.
        @NonNull
        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    OnBoardingFragment1 tab1 = new OnBoardingFragment1();
                    return tab1;
                case 1:
                    OnBoardingFragment2 tab2 = new OnBoardingFragment2();
                    return tab2;
                case 2:
                    OnBoardingFragment3 tab3 = new OnBoardingFragment3();
                    return tab3;

            }

            return null;
        }
        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }

}