package com.rizosoft.eatoutapp.interfaces;

import com.rizosoft.eatoutapp.pojos.Restaurant;

import java.util.List;

/**
 * Created by oliver on 30/03/2018.
 */

public interface OnGettingResultsFromSearch {
    void onGettingResultsFromSearch(List<Restaurant> restaurants);
}
