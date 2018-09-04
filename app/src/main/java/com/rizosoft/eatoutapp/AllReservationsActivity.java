package com.rizosoft.eatoutapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import com.rizosoft.eatoutapp.fragments.FragmentAllReservations;
import com.rizosoft.eatoutapp.fragments.FragmentMap;
import com.rizosoft.eatoutapp.interfaces.OnClickButtonMapReservation;

public class AllReservationsActivity extends AppCompatActivity implements OnClickButtonMapReservation {

    public final String TAG_FR_ALL_RESERVATIONS="ALL_RESERVATIONS";
    public final String TAG_FR_MAP="FR_MAP";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_reservations);
        FragmentAllReservations fragmentAllReservations = FragmentAllReservations.newInstance(this);
        FrameLayout fr = findViewById(R.id.frameAllReservations);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(fr.getId(),fragmentAllReservations, TAG_FR_ALL_RESERVATIONS);
        fragmentTransaction.commit();
    }

    @Override
    public void onClickButtonMapReservation(double lat, double longitude, String restaurantName) {
        if (getResources().getBoolean(R.bool.istablet)) {
            FragmentMap fragmentMap = FragmentMap.newInstance(lat, longitude, restaurantName);
            FrameLayout frMap = findViewById(R.id.frameMapReservation);
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(frMap.getId(), fragmentMap,TAG_FR_MAP);
            fragmentTransaction.commit();
        } else {
            Intent i = new Intent(this, LocateInMapActivity.class);
            i.putExtra(getResources().getString(R.string.latitude), lat);
            i.putExtra(getResources().getString(R.string.longitude), longitude);
            i.putExtra(getResources().getString(R.string.restaurantName), restaurantName);
            startActivity(i);
        }
    }
}
