package com.rizosoft.eatoutapp;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.rizosoft.eatoutapp.fragments.FragmentObservations;

public class SeeObservationsActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_observations);
        String observations = getIntent().getStringExtra(getString(R.string.st_Observations));
        FragmentObservations fragmentObservations = FragmentObservations.newInstance(observations, this);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.frObservations, fragmentObservations)
                .commit();
    }
}
