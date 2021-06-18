package com.macapella.foodies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class GetstartedActivity extends AppCompatActivity {

    private ViewPager screenPager;
    IntroViewPagerAdapter introViewPagerAdapter ;
    TabLayout tabIndicator;
    Button btnNext;
    int position = 0;
    Button btnGetStarted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //When activity is booting app checks if user has savedPrefs
        if(restorePrefData()){
            Intent mainActivity = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(mainActivity);
            finish();
        }

        setContentView(R.layout.activity_getstarted);

        btnNext = findViewById(R.id.btn_next);
        tabIndicator = findViewById(R.id.tab_indicator);
        btnGetStarted = findViewById(R.id.btn_getstarted);

        //populate the list screen
        List<ScreenItem> mlist = new ArrayList<>();
        mlist.add(new ScreenItem("Fresh Food", "The fallow restaurant provides gourmet dishes. High-grade ingredients sourced from all over the world are cooked with sophisticated cooking techniques by a dedicated chef, creating a unique dish that can't be found anywhere else on this earth.", R.drawable.img1));
        mlist.add(new ScreenItem("Fast Delivery", "You may order a takeaway from the restaurant menu or the takeout menu. A short time later you will receive a text message that your order is on the way, followed shortly by an SMS that it has arrived.", R.drawable.img2));
        mlist.add(new ScreenItem("Easy Payment", "Select your order and pay. Easy as that.", R.drawable.img3));

        // setup viewpager the slider
        screenPager = findViewById(R.id.screen_viewpager);
        introViewPagerAdapter = new IntroViewPagerAdapter(this,mlist);
        screenPager.setAdapter(introViewPagerAdapter);

        //setup tablayout with
        tabIndicator.setupWithViewPager(screenPager);

        //Next Button click listener to switch to the next pictures in list.
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position = screenPager.getCurrentItem();
                if(position < mlist.size()){
                    position++;
                    screenPager.setCurrentItem(position);
                }
                if(position == mlist.size()){
                    //When the user reaches end of list hide indicators and show get started button
                    loadLastScreen();
                }
            }
        });

        tabIndicator.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getPosition() == mlist.size()-1){
                    loadLastScreen();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        //Get started cllick listner
        btnGetStarted.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                //Button moves to main screen or login idk right now.
                Intent mainActivity = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(mainActivity);

                //Saved data when user returns to app getstarted screens are not displayed
                savePrefsData();
                finish();
            }
        });
    }

    private boolean restorePrefData() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("myPrefs",MODE_PRIVATE);
        Boolean isIntroActivityOpenedBefore = pref.getBoolean("hasIntroOpened", false);
        return isIntroActivityOpenedBefore;
    }

    private void savePrefsData() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("myPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("hasIntroOpened", true);
        editor.commit();
    }

    //Show get started buton
    private void loadLastScreen() {
        btnNext.setVisibility(View.INVISIBLE);
        btnGetStarted.setVisibility(View.VISIBLE);
        tabIndicator.setVisibility(View.INVISIBLE);
    }
}