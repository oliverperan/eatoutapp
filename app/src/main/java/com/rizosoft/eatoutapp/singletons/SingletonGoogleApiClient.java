package com.rizosoft.eatoutapp.singletons;

/**
 * Created by oliver on 04/04/2018.
 */

import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Places;

public class SingletonGoogleApiClient {
    private static final String TAG = "GoogleApiClient_Singleton";
    private static SingletonGoogleApiClient instance = null;

    private static GoogleApiClient mGoogleApiClient = null;

    protected SingletonGoogleApiClient() {

    }

    public static SingletonGoogleApiClient getInstance(AppCompatActivity activity) {
        if(instance == null) {
            instance = new SingletonGoogleApiClient();
            if (mGoogleApiClient == null)
                mGoogleApiClient = new GoogleApiClient
                        .Builder(activity)
                        .addApi(Places.GEO_DATA_API)
                        .addApi(Places.PLACE_DETECTION_API)
                        .enableAutoManage(activity, (GoogleApiClient.OnConnectionFailedListener) activity)
                        .build();

        }
        return instance;
    }

    public GoogleApiClient get_GoogleApiClient(){
        return mGoogleApiClient;
    }






}