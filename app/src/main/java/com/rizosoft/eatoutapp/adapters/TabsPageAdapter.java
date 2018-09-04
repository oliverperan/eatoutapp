package com.rizosoft.eatoutapp.adapters;


import android.content.Context;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.rizosoft.eatoutapp.fragments.FragmenAddReservation;
import com.rizosoft.eatoutapp.fragments.FragmentMap;
import com.rizosoft.eatoutapp.fragments.FragmentRestaurantDetail;
import com.rizosoft.eatoutapp.fragments.FragmentRestaurantOverview;
import com.rizosoft.eatoutapp.fragments.FragmentRestaurantReservations;
import com.rizosoft.eatoutapp.interfaces.OnSavingReservation;
import com.rizosoft.eatoutapp.pojos.Restaurant;
import com.rizosoft.eatoutapp.services.RefresWidgetService;


public class TabsPageAdapter extends FragmentPagerAdapter implements OnSavingReservation {

    private static int NUM_ITEMS = 4;
    private Restaurant restaurant;
    private Context context;
    private FragmentRestaurantReservations fragmentRestaurantReservations;
    private FragmentRestaurantDetail parent;

    public TabsPageAdapter(FragmentManager fragmentManager, Restaurant restaurant, Context context, FragmentRestaurantDetail parent) {
            super(fragmentManager);
            this.restaurant = restaurant;
            this.context = context;
            this.parent = parent;
    }

    // Returns total number of pages
    @Override
    public int getCount() {
            return NUM_ITEMS;
            }

    // Returns the fragment to display for that page


        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            if (position == 1) {
                FragmentRestaurantReservations createdFragment = (FragmentRestaurantReservations) super.instantiateItem(container, position);
                createdFragment.setContext(context);
                fragmentRestaurantReservations = createdFragment;
                return createdFragment;
            } else if (position == 2) {
                FragmenAddReservation createdFragment = (FragmenAddReservation) super.instantiateItem(container, position);
                createdFragment.setOnSavingReservation(this);
                return createdFragment;
            }

            return super.instantiateItem(container, position);
        }

        @Override
    public Fragment getItem(int position) {
            switch (position) {
            case 0:
                return FragmentRestaurantOverview.newInstance(restaurant, context);
            case 1:
                return FragmentRestaurantReservations.newInstance(context, restaurant.getPlaceId());
            case 2:
                return FragmenAddReservation.newInstance(restaurant, context, this);

            case 3:
                double dLat=0;
                double dLong=0;
                dLat = Double.parseDouble(restaurant.getLatitude());
                dLong = Double.parseDouble(restaurant.getLongitude());
                return FragmentMap.newInstance(dLat, dLong, restaurant.getName());
            default:
                return null;
            }
    }

    public void refreshCursorReservations(Uri uri) {
        fragmentRestaurantReservations.refreshCursor(uri);
        parent.setActivePageReservations(1);
        RefresWidgetService.startActionRefreshNumberReservations(context);
    }


    @Override
    public CharSequence getPageTitle(int position) {
            switch (position) {

            case 0:{
                return "Overview";
            }
            case 1:{ return "Reservations";}
            case 2:{return "Add a reservation";}
            case 3:{return "Map";}
            default: return null;
            }
    }


    @Override
    public void onSavingReservation(Uri uri) {
        refreshCursorReservations(uri);
    }
}
