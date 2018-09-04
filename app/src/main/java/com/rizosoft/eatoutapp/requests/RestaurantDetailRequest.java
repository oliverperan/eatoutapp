package com.rizosoft.eatoutapp.requests;

import android.os.AsyncTask;

import com.rizosoft.eatoutapp.interfaces.OnGettingRestaurantDetails;
import com.rizosoft.eatoutapp.pojos.Restaurant;
import com.rizosoft.eatoutapp.pojos.Review;
import com.rizosoft.eatoutapp.utilities.NetworkUtilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by oliver on 10/04/2018.
 */






public class RestaurantDetailRequest extends AsyncTask<URL, Void, String> {

    private OnGettingRestaurantDetails onGettingRestaurantDetails;
    private Restaurant restaurant;


    public RestaurantDetailRequest(Restaurant restaurant, OnGettingRestaurantDetails onGettingRestaurantDetails) {
        this.restaurant = restaurant;
        this.onGettingRestaurantDetails = onGettingRestaurantDetails;
    }


    @Override
    protected String doInBackground(URL... urls) {
        URL getJSON = urls[0];
        try {
            String results = NetworkUtilities.getResponseFromHttpUrl(getJSON);
            return results;
        } catch(IOException e) {
            e.printStackTrace();
        }

        return null;
    }


    @Override
    protected void onPostExecute(String json) {
        final String ADDRESS="vicinity";
        final String  PHONE_NUMBER = "formatted_phone_number";
        final String REVIEWS = "reviews";
        final String AUTHOR_NAME = "author_name";
        final String AUTHOR_URL = "author_url";
        final String LANGUAGE = "language";
        final String RATING = "rating";
        final String TEXT = "text";
        final String TIME = "time";
        final String RESULT = "result";
        final String RELATIVE_TIME_DESCRIPTION = "relative_time_description";
        final String VICINITY="vicinity";

        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(json);
            jsonObject = jsonObject.getJSONObject(RESULT);

            if (jsonObject.has(ADDRESS)) {
                restaurant.setStreetAddress(jsonObject.getString(ADDRESS));
            } else {
                restaurant.setStreetAddress("");
            }

            if (jsonObject.has(PHONE_NUMBER)) {
                restaurant.setPhoneNumber(jsonObject.getString(PHONE_NUMBER));
            }

            if (jsonObject.has(VICINITY)) {
                restaurant.setVicinity(jsonObject.getString(VICINITY));
            } else  {
                restaurant.setVicinity("");
            }

            restaurant.setReviews(new ArrayList<Review>());
            if (jsonObject.has(REVIEWS)) {
                JSONArray arReviews = jsonObject.getJSONArray(REVIEWS);

                for (int i=0; i < arReviews.length(); i++) {
                    Review review = new Review();
                    JSONObject jreview = arReviews.getJSONObject(i);
                    if (jreview.has(AUTHOR_NAME)) {
                        review.setAuthorName(jreview.getString(AUTHOR_NAME));
                    } else {
                        review.setAuthorName("");
                    }

                    if (jreview.has(AUTHOR_URL)) {
                        review.setAuthorUrl(jreview.getString(AUTHOR_URL));
                    } else {
                        review.setAuthorUrl("");
                    }

                    if (jreview.has(LANGUAGE)) {
                        review.setLanguage(jreview.getString(LANGUAGE));
                    } else {
                        review.setLanguage("");
                    }


                    if (jreview.has(RATING)) {
                        review.setRating((float) jreview.getDouble(RATING));
                    } else  {
                        review.setRating(0);
                    }

                    if (jreview.has(TEXT)) {
                        review.setText(jreview.getString(TEXT));
                    } else {
                        review.setText("");
                    }

                    if (jreview.has(TIME)) {
                        review.setTime(jreview.getString(TIME));
                    } else {
                        review.setTime("");
                    }

                    if (jreview.has(RELATIVE_TIME_DESCRIPTION)) {
                        review.setRelativeTimeDescription(jreview.getString(RELATIVE_TIME_DESCRIPTION));
                    } else {
                        review.setRelativeTimeDescription("");
                    }

                    restaurant.getReviews().add(review);
                }
            }

            restaurant.setDetailsLoaded(true);
            restaurant.sortByTimeDesc();
            onGettingRestaurantDetails.onGettingRestaurantDetails(restaurant);

        } catch (JSONException e) {
            e.printStackTrace();
        }




    }
}
