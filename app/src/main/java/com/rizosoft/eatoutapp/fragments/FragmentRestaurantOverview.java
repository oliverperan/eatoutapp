package com.rizosoft.eatoutapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rizosoft.eatoutapp.R;
import com.rizosoft.eatoutapp.adapters.RestaurantReviewsAdapter;
import com.rizosoft.eatoutapp.pojos.Restaurant;

/**
 * Created by oliver on 03/04/2018.
 */

public class FragmentRestaurantOverview extends Fragment {

    private Restaurant restaurant;
    private RecyclerView rcv;
    private Context context;

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }
    public void setContext(Context context) {this.context = context;}
    public static FragmentRestaurantOverview newInstance(Restaurant restaurant, Context context) {
        FragmentRestaurantOverview fragmentFirst = new FragmentRestaurantOverview();
        fragmentFirst.setContext(context);
        fragmentFirst.setRestaurant(restaurant);
        return fragmentFirst;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);
        outState.putParcelable(getString(R.string.restaurant),restaurant);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_restaurant_overview, container, false);
        rcv = view.findViewById(R.id.rcvReviews);

        return view;
    }


    private void setControls() {

        RestaurantReviewsAdapter adapter = new RestaurantReviewsAdapter(context, restaurant);

        GridLayoutManager manager;

        manager = new GridLayoutManager(getActivity(), 1, GridLayoutManager.VERTICAL, false);


        rcv.setLayoutManager(manager);
        //rcv.addItemDecoration(new SimpleDividerItemDecoration(context));
        rcv.setItemAnimator(new DefaultItemAnimator());
        rcv.setAdapter(adapter);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            restaurant = savedInstanceState.getParcelable(getString(R.string.restaurant));
        }
        if (restaurant != null) {
            setControls();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

}
