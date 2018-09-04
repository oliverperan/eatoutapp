package com.rizosoft.eatoutapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.rizosoft.eatoutapp.interfaces.OnGettingResultsFromSearch;
import com.rizosoft.eatoutapp.pojos.Restaurant;
import com.rizosoft.eatoutapp.requests.RestaurantRequest;
import com.rizosoft.eatoutapp.services.CleanReservationsService;
import com.rizosoft.eatoutapp.singletons.SingletonGoogleApiClient;
import com.rizosoft.eatoutapp.utilities.NetworkUtilities;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, OnGettingResultsFromSearch {

    private GoogleApiClient mGoogleApiClient;
    private static String TAG = "Oliver";
    private Context context;
    private ConstraintLayout cl;
    private OnGettingResultsFromSearch grfs;
    private String sPlace;
    private ProgressBar bar;
    private FusedLocationProviderClient mFusedLocationClient;
    private String sLatLng;
    private static final int REQUEST_PERMISSION_LOCATION =1;
    private FloatingActionButton btNearBy;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;
        grfs = this;
        mGoogleApiClient = SingletonGoogleApiClient.getInstance(this).get_GoogleApiClient();

        cl = findViewById(R.id.cl);
        bar = findViewById(R.id.bar);
        btNearBy = findViewById(R.id.btNearby);
        btNearBy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkUtilities.isOnline(context)) {
                    getLatLangForNearbyRestaurants();
                } else {
                    NetworkUtilities.showMessage(findViewById(R.id.cl), getString(R.string.st_internet_needed));
                }
            }
        });


        CleanReservationsService.startActionCleanReservations(this);

        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.autocomplete_fragment);

/*
* The following code example shows setting an AutocompleteFilter on a PlaceAutocompleteFragment to
* set a filter returning only results with a precise address.
*/
        AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                .setTypeFilter(AutocompleteFilter.TYPE_FILTER_CITIES)
                .build();
        autocompleteFragment.setFilter(typeFilter);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                sPlace = place.getName().toString();
                double lLat = place.getLatLng().latitude;
                double lLong = place.getLatLng().longitude;

                String slLat = lLat+"";
                String slLong = lLong+"";
                String slLatlLong = slLat+","+slLong;
                getRestaurantsFromLatLng(slLatlLong);
            }

            @Override
            public void onError(Status status) {
                Log.i(TAG, "An error occurred: " + status);
            }
        });

    }



    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null)
            mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
        super.onStop();
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }



    @Override
    public void onGettingResultsFromSearch(List<Restaurant> restaurants) {
        bar.setVisibility(View.GONE);

        if ((restaurants!= null) && (restaurants.size() > 0)) {
            Intent intent = new Intent(getApplicationContext(), RestaurantResultSearchActivity.class);
            intent.putParcelableArrayListExtra("restaurants", (ArrayList) restaurants);
            intent.putExtra("NamePlace", sPlace);
            startActivity(intent);
        } else {
            NetworkUtilities.showMessage(cl, getString(R.string.no_results));
        }
    }


    public void getLatLangForNearbyRestaurants(){
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        sPlace = getResources().getString(R.string.stNearYou);
        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED) ||
                (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED))
        {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},REQUEST_PERMISSION_LOCATION);
        }
        else
        {
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                // Logic to handle location object
                                sLatLng = location.getLatitude()+","+location.getLongitude();
                                getRestaurantsFromLatLng(sLatLng);
                            }
                        }
                    });
        }
    }

    private void getRestaurantsFromLatLng(String sLatLng) {
        bar.setVisibility(View.VISIBLE);
        RestaurantRequest restRequest = new RestaurantRequest(this, this,"", null,sLatLng);
        String surl ="https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=:location&radius=500&hasNextPage=true&nextPage()=true&types=restaurant&key=:api_key";
        surl = surl.replace(":location", sLatLng);
        surl = surl.replace(":api_key", NetworkUtilities.getAPI_Key(this));

        Uri builtUri = Uri.parse(surl);

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        restRequest.execute(url);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Boolean sw=true;
        switch (requestCode) {
            case REQUEST_PERMISSION_LOCATION:
                for (int i = 0; i < permissions.length; i++) {
                    String permission = permissions[i];
                    int grantResult = grantResults[i];

                    sw = (grantResult == PackageManager.PERMISSION_GRANTED);
                }
                break;
        }

        if (!sw) {
            ConstraintLayout cl = findViewById(R.id.cl);
            NetworkUtilities.showMessage(cl,getResources().getString(R.string.permission_location_rejected));
        } else {
            getLatLangForNearbyRestaurants();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_credits:
                Intent i = new Intent(this, CreditsActivity.class);
                startActivity(i);
                return true;

            case R.id.action_reservations:
                Intent iAllReservations = new Intent(this, AllReservationsActivity.class);
                startActivity(iAllReservations);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
