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
import com.rizosoft.eatoutapp.adapters.RestuarantSearchResultAdapter;
import com.rizosoft.eatoutapp.interfaces.OnClickOnItemRestaurantSearch;
import com.rizosoft.eatoutapp.pojos.Restaurant;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by oliver on 01/04/2018.
 */

public class FragmentResultSearch extends Fragment  {


    private RecyclerView rcv;
    private Context context;
    private List<Restaurant> restaurants;
    private OnClickOnItemRestaurantSearch clickOnItemRestaurantSearch;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragmentresultsearch, container, false);


        rcv = rootView.findViewById(R.id.rcvRestaurantsSearch);

        return rootView;
    }

    public void setRestaurants(List<Restaurant> restaurants) {
        this.restaurants = restaurants;
    }



    public static FragmentResultSearch newInstance(List<Restaurant> restaurants, OnClickOnItemRestaurantSearch clickOnItemRestaurantSearch) {
        FragmentResultSearch fragmentFirst = new FragmentResultSearch();
        fragmentFirst.setRestaurants(restaurants);
        fragmentFirst.clickOnItemRestaurantSearch = clickOnItemRestaurantSearch;
        return fragmentFirst;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if ((restaurants != null) && (restaurants.size() > 0)) {
            outState.putParcelableArrayList(getString(R.string.searchList), (ArrayList<Restaurant>) restaurants);
        }


    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        context = getActivity();

        if ((restaurants == null) && (savedInstanceState != null)) {
            restaurants = savedInstanceState.getParcelableArrayList(getString(R.string.searchList));
        }

        if ((restaurants != null) && (restaurants.size() >0)) {
            RestuarantSearchResultAdapter adapter = new RestuarantSearchResultAdapter(context, restaurants, (OnClickOnItemRestaurantSearch) getActivity());

            GridLayoutManager manager;

            manager = new GridLayoutManager(getActivity(), 1, GridLayoutManager.VERTICAL, false);

            rcv.setLayoutManager(manager);
            //rcv.addItemDecoration(new SimpleDividerItemDecoration(context));
            rcv.setItemAnimator(new DefaultItemAnimator());
            rcv.setAdapter(adapter);
        }

    }
}
