package com.rizosoft.eatoutapp.requests;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

import com.rizosoft.eatoutapp.interfaces.OnGettingResultsFromSearch;
import com.rizosoft.eatoutapp.pojos.Restaurant;
import com.rizosoft.eatoutapp.utilities.NetworkUtilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by oliver on 30/03/2018.
 */

public class RestaurantRequest extends AsyncTask<URL, Void, String> {



    private OnGettingResultsFromSearch onGettingResultsFromSearch;
    private Context context;
    private List<Restaurant> restaurantList;
    private String lat_lng;
    private String nextPageToken = "";
    private int numberPerRequest;
    @Override
    protected String doInBackground(URL...params) {
        URL getJSON = params[0];
        try {
            String results = NetworkUtilities.getResponseFromHttpUrl(getJSON);
            return results;
        } catch(IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public RestaurantRequest(Context context, OnGettingResultsFromSearch onGettingResultsFromSearch, String nextPageToken, List<Restaurant> restaurantList, String lat_lng) {
        this.context = context;
        this.onGettingResultsFromSearch = onGettingResultsFromSearch;
        this.nextPageToken = nextPageToken;
        this.restaurantList = restaurantList;
        this.lat_lng = lat_lng;
        this.numberPerRequest = NetworkUtilities.getNumberOfRestaurantsPerRequest(context);
    }


    @Override
    protected void onPostExecute(String results) {

        final String RESULTS = "results";
        final String GEOMETRY = "geometry";
        final String LOCATION = "location";
        final String LAT = "lat";
        final String LNG = "lng";
        final String NAME = "name";
        final String OPENING_HOURS = "opening_hours";
        final String OPEN_NOW = "open_now";
        final String PHOTOS = "photos";
        final String PHOTO_REFERENCE = "photo_reference";
        final String HEIGHT = "height";
        final String WIDTH = "width";
        final String PLACE_ID = "place_id";
        final String RATING = "rating";
        final String REFERENCE = "reference";
        final String VICINITY = "vicinity";
        final String NEXT_PAGE_TOKEN = "next_page_token";

        if (restaurantList == null) {
            restaurantList = new ArrayList<Restaurant>();
        }


        JSONObject jsonObject;

        try {
            jsonObject = new JSONObject(results);


            if (jsonObject.has(NEXT_PAGE_TOKEN)) {
                nextPageToken = jsonObject.getString(NEXT_PAGE_TOKEN);
            }


            JSONArray arResults=null;
            if (jsonObject.has(RESULTS)) {
                arResults = jsonObject.getJSONArray(RESULTS);
            } else {
                Log.i("eatoutApp",results);
            }



            for (int i = 0; i < arResults.length(); i++) {
                JSONObject jsonRest = arResults.getJSONObject(i);
                Restaurant rest = new Restaurant();
                rest.setDetailsLoaded(false);
                JSONObject jObject = jsonRest.getJSONObject(GEOMETRY);
                jObject = jObject.getJSONObject(LOCATION);
                rest.setLatitude(jObject.getString(LAT));
                rest.setLongitude(jObject.getString(LNG));
                rest.setName(jsonRest.getString(NAME));
                if (jsonRest.has(OPENING_HOURS)) {
                    jObject = jsonRest.getJSONObject(OPENING_HOURS);
                    rest.setOpenNow(jObject.getBoolean(OPEN_NOW));
                } else {
                    rest.setOpenNow(false);
                }

                if (jsonRest.has(PHOTOS)) {
                    JSONArray jPhotos = jsonRest.getJSONArray(PHOTOS);
                    jObject = jPhotos.getJSONObject(0);
                    rest.setPhotoReference(jObject.getString(PHOTO_REFERENCE));

                    if (jObject.has("html_attributions")) {
                        JSONArray jContributions = jObject.getJSONArray("html_attributions");
                        if (jContributions.length() > 0) {
                            rest.setPictureAttibutions(jContributions.getString(0));
                        }
                    }
                } else {
                    rest.setPhotoReference(null);
                }
                rest.setPlaceId(jsonRest.getString(PLACE_ID));

                if (jsonRest.has(RATING)) {
                    rest.setRating((float) jsonRest.getDouble(RATING));
                } else {
                    rest.setRating(0);
                }
                rest.setReference(jsonRest.getString(REFERENCE));
                rest.setVicinity(jsonRest.getString(VICINITY));

                restaurantList.add(rest);
            }

            if (((restaurantList.size() == 20) || (restaurantList.size()==40)) && (restaurantList.size() < numberPerRequest))  {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {


                        RestaurantRequest restaurantRequest = new RestaurantRequest(context, onGettingResultsFromSearch, nextPageToken,restaurantList, lat_lng);
                        String surl = "https://maps.googleapis.com/maps/api/place/search/json?location=:lat_lng&radius=500&types=restaurant&hasNextPage=true&nextPage()=true&sensor=false&key=:api_key&pagetoken=:page_token";
                        surl = surl.replace(":lat_lng", lat_lng);
                        surl = surl.replace(":api_key", NetworkUtilities.getAPI_Key(context));
                        surl = surl.replace(":page_token", nextPageToken);
                        Uri builtUri = Uri.parse(surl);

                        URL url = null;
                        try {
                            url = new URL(builtUri.toString());
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }

                        restaurantRequest.execute(url);


                    }
                }, 2500);

            } else  {
                onGettingResultsFromSearch.onGettingResultsFromSearch(restaurantList);
            }


        } catch (JSONException e) {
            e.printStackTrace();
            Log.i("eatoutApp",results);
            Log.e("eatoutApp",e.toString());
        }
    }
}
