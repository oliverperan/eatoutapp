package com.rizosoft.eatoutapp.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.rizosoft.eatoutapp.R;
import com.rizosoft.eatoutapp.interfaces.OnReturnToMapView;

/**
 * Created by oliver on 25/04/2018.
 */

public class FragmentMap extends Fragment implements OnMapReadyCallback, OnReturnToMapView {
    @Override
    public void onResturnToMapView(LatLng latLng, String restaurantName) {
        btStreetView = false;
        setMapa(latLng, btStreetView);
    }

    private static final String TAG_ST_VIEW = "TAG_ST_VIEW";
    private static final String TAG_MP_VIEW = "TAG_MP_VIEW";

    SupportMapFragment mapFragment;
    private double latitude;
    private Boolean bStreetView = false;
    private double longitud;
    private String restaurantName;
    private FrameLayout fr;
    private FragmentMap fragmentMap;
    private Boolean btStreetView;
    private String tagMapView;
    private StreetViewFragment streetViewFragment;
    private TextView tvStreetView;

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public static FragmentMap newInstance(double latitude, double longitud, String restaurantName) {
        FragmentMap fragmentFirst = new FragmentMap();
        fragmentFirst.setLatitude(latitude);
        fragmentFirst.setLongitud(longitud);
        fragmentFirst.setRestaurantName(restaurantName);
        fragmentFirst.fragmentMap = fragmentFirst;
        fragmentFirst.btStreetView = false;
        return fragmentFirst;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_map_layout, container, false);
        fr = view.findViewById(R.id.frMapView);
        tvStreetView = view.findViewById(R.id.tvStreetView);
        return  view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng latLng = new LatLng(latitude, longitud);
        MarkerOptions mo = new MarkerOptions();
        mo.position(latLng).title(restaurantName);
        googleMap.addMarker(mo);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.animateCamera( CameraUpdateFactory.zoomTo( 18.0f ));
        googleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(final LatLng latLng) {
                btStreetView = true;
                setLatitude(latLng.latitude);
                setLongitud(latLng.longitude);
                setMapa(latLng, true);
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (btStreetView != null) {
            outState.putBoolean(getString(R.string.map_in_st_view_mode), btStreetView);
        }
        outState.putDouble(getString(R.string.latitude), latitude);
        outState.putDouble(getString(R.string.longitude), longitud);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            btStreetView = savedInstanceState.getBoolean(getString(R.string.map_in_st_view_mode));
            latitude = savedInstanceState.getDouble(getString(R.string.latitude));
            longitud = savedInstanceState.getDouble(getString(R.string.longitude));
            LatLng latLng = new LatLng(latitude, longitud);
            setMapa(latLng, btStreetView);
        } else {
            LatLng latLng = new LatLng(latitude, longitud);
            btStreetView = false;
            setMapa(latLng, btStreetView);
        }
    }

    public void setMapa(LatLng latLng, Boolean bStreetView) {
        if (!bStreetView) {

            tvStreetView.setVisibility(View.VISIBLE);
            mapFragment = new SupportMapFragment();

            mapFragment.getMapAsync(this);
            getChildFragmentManager().beginTransaction()
                    .replace(fr.getId(), mapFragment).commit();

        } else {
            tvStreetView.setVisibility(View.GONE);
            streetViewFragment = StreetViewFragment.newInstance(latLng, restaurantName, this);

            getChildFragmentManager().beginTransaction()
                    .replace(fr.getId(), streetViewFragment).commit();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }


    /*  @Override
    public void onPause() {
        super.onPause();
        if (streetViewFragment != null) {
            streetViewFragment.onPause();
        }

        if (mapFragment != null) {
            mapFragment.onPause();
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        if (streetViewFragment != null) {
            streetViewFragment.onPause();
        }

        if (mapFragment != null) {
            mapFragment.onPause();
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (streetViewFragment != null) {
            streetViewFragment.onDestroy();
        }

        if(mapFragment != null) {
            mapFragment.onDestroy();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if(streetViewFragment!=null) {
            streetViewFragment.onStart();
        }

        if(mapFragment!= null) {
            mapFragment.onStart();
        }
    }*/
}
