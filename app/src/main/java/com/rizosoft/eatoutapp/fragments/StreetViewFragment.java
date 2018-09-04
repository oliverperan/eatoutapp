package com.rizosoft.eatoutapp.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import com.google.android.gms.maps.OnStreetViewPanoramaReadyCallback;
import com.google.android.gms.maps.StreetViewPanorama;
import com.google.android.gms.maps.SupportStreetViewPanoramaFragment;
import com.google.android.gms.maps.model.LatLng;
import com.rizosoft.eatoutapp.R;
import com.rizosoft.eatoutapp.interfaces.OnReturnToMapView;

/**
 * A simple {@link Fragment} subclass.
 */
public class StreetViewFragment extends Fragment  {


    public static final String TAG_ST_VIEW="TAG_ST_VIEW";



    private LatLng latLng;
    private String restaurnatName;
    private OnReturnToMapView onReturnToMapView;


    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }


    public void setRestaurnatName(String restaurnatName) {
        this.restaurnatName = restaurnatName;
    }

    public void setOnReturnToMapView(OnReturnToMapView onReturnToMapView) {
        this.onReturnToMapView = onReturnToMapView;
    }

    public StreetViewFragment() {
        // Required empty public constructor
    }




    public static StreetViewFragment newInstance(LatLng latLng, String restaurantName, OnReturnToMapView onReturnToMapView) {
        StreetViewFragment stvFragment = new StreetViewFragment();
        stvFragment.setLatLng(latLng);
        stvFragment.setRestaurnatName(restaurantName);
        stvFragment.setOnReturnToMapView(onReturnToMapView);
        return stvFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_street_view, container, false);
        FrameLayout fr = view.findViewById(R.id.frStreetView);

        SupportStreetViewPanoramaFragment mPanoramaFragment = new SupportStreetViewPanoramaFragment();

        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(fr.getId(), mPanoramaFragment, TAG_ST_VIEW).commit();

        mPanoramaFragment.getStreetViewPanoramaAsync(new OnStreetViewPanoramaReadyCallback() {
            @Override
            public void onStreetViewPanoramaReady(StreetViewPanorama streetViewPanorama) {
                streetViewPanorama.setPosition(latLng);
            }
        });


        Button btTurnToMapView = view.findViewById(R.id.btTurnToMapView);
        btTurnToMapView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                onReturnToMapView.onResturnToMapView(latLng, restaurnatName);
            }
        });

        return view;
    }


}
