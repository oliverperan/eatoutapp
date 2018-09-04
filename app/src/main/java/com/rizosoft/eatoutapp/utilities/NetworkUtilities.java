package com.rizosoft.eatoutapp.utilities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.rizosoft.eatoutapp.R;
import com.rizosoft.eatoutapp.interfaces.OnGettingRestaurantDetails;
import com.rizosoft.eatoutapp.interfaces.OnSetDateAndTime;
import com.rizosoft.eatoutapp.pojos.Restaurant;
import com.rizosoft.eatoutapp.requests.RestaurantDetailRequest;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by oliver on 28/11/2017.
 */

public class NetworkUtilities {


    private static String TAG = "Oliver";
    public static final String URL_PHOTO = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=:PHOTO_REFERENCE&key=:API_KEY";

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();

        String result;
        StringBuffer sb = new StringBuffer();
        InputStream is = null;

        try {
            is = new BufferedInputStream(urlConnection.getInputStream());
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String inputLine;
            while ((inputLine = br.readLine()) != null) {
                sb.append(inputLine);
            }
            result = sb.toString();
        } catch (Exception e) {
            result = null;
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return result;

    }

    public static boolean isOnline(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            return netInfo != null && netInfo.isConnectedOrConnecting();
        } else {
            return false;
        }
    }

    public static String getAPI_Key(Context context) {

        try {
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = ai.metaData;
            String myApiKey = bundle.getString("com.google.android.geo.API_KEY");
            return  myApiKey;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "Failed to load meta-data, NameNotFound: " + e.getMessage());
            return null;
        } catch (NullPointerException e) {
            Log.e(TAG, "Failed to load meta-data, NullPointer: " + e.getMessage());
            return  null;
        }


    }


    public static void getDetails(final Restaurant restaurant, AppCompatActivity activity, final OnGettingRestaurantDetails onGettingRestaurantDetails) {
            String surl = "https://maps.googleapis.com/maps/api/place/details/json?placeid=:place_id&key=:api_key";
            surl = surl.replace(":place_id", restaurant.getPlaceId());
            surl = surl.replace(":api_key", getAPI_Key(activity));

            Uri builtUri = Uri.parse(surl);
            URL url = null;
            try {
                url = new URL(builtUri.toString());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            RestaurantDetailRequest restaurantDetailRequest = new RestaurantDetailRequest(restaurant, onGettingRestaurantDetails);

            restaurantDetailRequest.execute(url);
    }


    public static float convertDpToPixel(float dp, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }


    public static float convertPixelsToDp(float px, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return dp;
    }


    public static String getCurrentDate() {
        Date currentDate = new Date();
        SimpleDateFormat curFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String newDateStr = curFormater.format(currentDate);
        return newDateStr;
    }

    public static String getFormattedDateForSQLLite(Date date) {
        SimpleDateFormat curFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String newDateStr = curFormater.format(date);
        return newDateStr;
    }

    public static String getFormattedDateForVisualization(Date date) {
        //"EEE, d MMM yyyy HH:mm:ss Z"
        SimpleDateFormat curFormater = new SimpleDateFormat("EEE, d MMM yyyy HH:mm");
        String newDateStr = curFormater.format(date);
        return newDateStr;
    }

    public static Date getDateFromSQLiteDate(String sDate) {
        SimpleDateFormat curFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            Date date = curFormater.parse(sDate);
            return  date;
        } catch(Exception e) {
            e.printStackTrace();
            return  null;
        }
    }

    public static void showDateTimePicker(final Context context, final OnSetDateAndTime onSetDateAndTime) {
        final Calendar date;
        final Calendar currentDate = Calendar.getInstance();
        date = Calendar.getInstance();
        new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                date.set(year, monthOfYear, dayOfMonth);
                new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        date.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        date.set(Calendar.MINUTE, minute);
                        Log.v(TAG, "The choosen one " + date.getTime());
                        onSetDateAndTime.onSetDateAndTime(date);
                    }
                }, currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), false).show();
            }
        }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE)).show();
    }



    public static Integer getNumberOfRestaurantsPerRequest(final Context context) {
        SharedPreferences sh = PreferenceManager.getDefaultSharedPreferences(context);
        String sNumber = sh.getString(context.getResources().getString(R.string.number_options_request), context.getResources().getInteger(R.integer.default_max_number_requests)+"");
        return Integer.parseInt(sNumber);
    }


    public static void showMessage(View parent, String message) {
        Snackbar sb = Snackbar.make(parent, message, Snackbar.LENGTH_LONG);
        sb.show();
        ViewCompat.setElevation(sb.getView(), 6);
    }


}
