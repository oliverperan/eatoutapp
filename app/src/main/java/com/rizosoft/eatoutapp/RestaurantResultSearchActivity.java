package com.rizosoft.eatoutapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.transition.Slide;
import android.transition.Transition;
import android.view.Gravity;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;

import com.rizosoft.eatoutapp.fragments.FragmentRestaurantDetail;
import com.rizosoft.eatoutapp.fragments.FragmentResultSearch;
import com.rizosoft.eatoutapp.interfaces.OnClickOnItemRestaurantSearch;
import com.rizosoft.eatoutapp.pojos.Restaurant;

import java.util.ArrayList;
import java.util.List;

public class RestaurantResultSearchActivity extends AppCompatActivity implements OnClickOnItemRestaurantSearch {


    public static  final String TAG_FR_SEARCH="TG_FR_SEARCH";
    public static final String TAG_FR_DETAIL="TG_FR_DETAIL";
    List<Restaurant> restaurants;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_restaurant_result_search);




        if (savedInstanceState!= null){
            restaurants = savedInstanceState.getParcelableArrayList(getString(R.string.searchList));

        } else {

            restaurants = getIntent().getParcelableArrayListExtra("restaurants");

            String sNamePlace = getIntent().getStringExtra("NamePlace");
            setTitle(getTitle()+" - "+sNamePlace);
            FragmentResultSearch frResultSearch = FragmentResultSearch.newInstance(restaurants, this);
            FrameLayout fr = findViewById(R.id.frameResultSearch);
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(fr.getId(),frResultSearch,TAG_FR_SEARCH);
            fragmentTransaction.commit();
        }



    }


    @SuppressWarnings("unchecked")
    @Override
    public void onClickOnItemRestaurantSearch(Restaurant restaurant) {
        if (!getResources().getBoolean(R.bool.istablet)) {


            Transition transition = new Slide(Gravity.START);
            transition.setDuration(500);
            transition.setInterpolator(new DecelerateInterpolator());

            getWindow().setExitTransition(transition);



            Intent intent = new Intent(getApplicationContext(), RestaurantDetailActivity.class);
            intent.putExtra("restaurant", restaurant);
            startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle());
        } else {
            FragmentRestaurantDetail fragmentRestaurantDetail = FragmentRestaurantDetail.newInstance(restaurant, this);
            FrameLayout frDetail = findViewById(R.id.frameDetail);
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(frDetail.getId(),fragmentRestaurantDetail,TAG_FR_DETAIL);
            fragmentTransaction.commit();
        }
    }


    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onRestart() {
        super.onRestart();

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {


        if ((restaurants != null) && (restaurants.size() > 0)) {
            outState.putParcelableArrayList(getString(R.string.searchList), (ArrayList<Restaurant>) restaurants);
            super.onSaveInstanceState(outState);
        }
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        restaurants = savedInstanceState.getParcelableArrayList(getString(R.string.searchList));

    }
}
