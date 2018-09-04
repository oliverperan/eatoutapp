package com.rizosoft.eatoutapp.interfaces;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by oliver on 28/04/2018.
 */

public interface OnReturnToMapView {
    public void onResturnToMapView(LatLng latLng, String restaurantName);
}
