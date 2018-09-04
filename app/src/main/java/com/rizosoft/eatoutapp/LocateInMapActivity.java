package com.rizosoft.eatoutapp;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import com.rizosoft.eatoutapp.fragments.FragmentMap;

public class LocateInMapActivity extends AppCompatActivity {

    private double latitude;
    private double longitude;
    private String restaurantName;
    private static final String TAG_FR_MAP = "MAP";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locate_in_map);

        if (savedInstanceState != null) {
           /* latitude = savedInstanceState.getDouble(getString(R.string.latitude));
            longitude = savedInstanceState.getDouble(getString(R.string.longitude));
            restaurantName = savedInstanceState.getString(getString(R.string.restaurantName));

            FragmentMap fragmentMap = FragmentMap.newInstance(latitude, longitude, restaurantName);

            FrameLayout fr = findViewById(R.id.frameLocateInMap);
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(fr.getId(), fragmentMap,TAG_FR_MAP);
            fragmentTransaction.commit();*/
        } else {
            latitude = getIntent().getDoubleExtra(getString(R.string.latitude), 0);
            longitude = getIntent().getDoubleExtra(getString(R.string.longitude), 0);

            restaurantName = getIntent().getStringExtra(getString(R.string.restaurantName));
            FrameLayout fr = findViewById(R.id.frameLocateInMap);

            FragmentMap fragmentMap = FragmentMap.newInstance(latitude, longitude, restaurantName);
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(fr.getId(), fragmentMap,TAG_FR_MAP);
            fragmentTransaction.commit();
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putDouble(getString(R.string.latitude), latitude);
        outState.putDouble(getString(R.string.longitude), longitude);
        outState.putString(getString(R.string.restaurantName), restaurantName);
    }

}
