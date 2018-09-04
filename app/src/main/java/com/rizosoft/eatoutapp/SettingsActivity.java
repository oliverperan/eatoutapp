package com.rizosoft.eatoutapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.rizosoft.eatoutapp.fragments.FragmentSettings;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.frSettings, new FragmentSettings())
                .commit();
    }
}
