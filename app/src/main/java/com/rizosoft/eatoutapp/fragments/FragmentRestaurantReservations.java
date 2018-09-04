package com.rizosoft.eatoutapp.fragments;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.rizosoft.eatoutapp.R;
import com.rizosoft.eatoutapp.services.RefresWidgetService;
import com.rizosoft.eatoutapp.adapters.RestaurantReservationAdapter;
import com.rizosoft.eatoutapp.interfaces.OnCancelReservation;
import com.rizosoft.eatoutapp.pojos.Reservation;
import com.rizosoft.eatoutapp.providers.ReservationContract;
import com.rizosoft.eatoutapp.utilities.NetworkUtilities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by oliver on 20/04/2018.
 */

public class FragmentRestaurantReservations extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, OnCancelReservation {
    private List<Reservation> reservations;
    private String placeId;
    private RecyclerView rcv;
    private Context context;
    private Cursor cursor;
    private LinearLayout lNoReservations;

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }
    public void setContext(Context context)
    {
        this.context = context;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }


    public static FragmentRestaurantReservations newInstance(Context context, String placeId) {
        FragmentRestaurantReservations fragmentFirst = new FragmentRestaurantReservations();
        fragmentFirst.setContext(context);
        fragmentFirst.setPlaceId(placeId);
        return fragmentFirst;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_restaurant_reservations, container, false);
        rcv = view.findViewById(R.id.rcvRestaurantReservations);
        lNoReservations = view.findViewById(R.id.lNoRestaurantReservations);
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            reservations = savedInstanceState.getParcelableArrayList(getString(R.string.restaurantReservations));
            placeId = savedInstanceState.getString(getString(R.string.placeId));
        }

        setControls();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(getString(R.string.restaurantReservations), (ArrayList)reservations);
        outState.putString(getString(R.string.placeId), placeId);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void setControls() {
        getActivity().getSupportLoaderManager().restartLoader(0, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri uri = ReservationContract.BASE_CONTENT_URI.buildUpon().appendPath(ReservationContract.PATH_RESERVATIONS).build();
        return new CursorLoader(getActivity(),
                uri,
                null,
                ReservationContract.ReservationEntry.COLUMN_PLACE_ID+" = ? and "+
                         ReservationContract.ReservationEntry.COLUMN_DATE_RESERVATION+" >= ?",
                new String[] {placeId, NetworkUtilities.getFormattedDateForSQLLite(new Date())},
                ReservationContract.ReservationEntry.COLUMN_DATE_RESERVATION);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        try {
            reservations = new ArrayList<Reservation>();
            cursor = data;
            if (data.getCount() > 0) {
                rcv.setVisibility(View.VISIBLE);
                lNoReservations.setVisibility(View.GONE);
                Reservation newReservation = null;
                while (data.moveToNext()) {
                    newReservation = new Reservation();
                    newReservation.setId(data.getInt((data.getColumnIndex(ReservationContract.ReservationEntry.COLUMN_ID))));
                    newReservation.setPlaceId(data.getString(data.getColumnIndex(ReservationContract.ReservationEntry.COLUMN_PLACE_ID)));
                    newReservation.setRestaurantName(data.getString(data.getColumnIndex(ReservationContract.ReservationEntry.COLUMN_RESTAURANT_NAME)));
                    newReservation.setObservations(data.getString(data.getColumnIndex(ReservationContract.ReservationEntry.COLUMN_OBSERVATIONS)));
                    newReservation.setDateReservation(data.getString(data.getColumnIndex(ReservationContract.ReservationEntry.COLUMN_DATE_RESERVATION)));
                    newReservation.setStreetAddress(data.getString(data.getColumnIndex(ReservationContract.ReservationEntry.COLUMN_STREET_ADDRESS)));
                    newReservation.setPhoneNumber(data.getString(data.getColumnIndex(ReservationContract.ReservationEntry.COLUMN_PHONE_NUMBER)));
                    newReservation.setLatLong(data.getString(data.getColumnIndex(ReservationContract.ReservationEntry.COLUMN_LAT_LONG)));

                    reservations.add(newReservation);
                }

                setRcv();
            } else {
                rcv.setVisibility(View.GONE);
                lNoReservations.setVisibility(View.VISIBLE);
            }



        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void setRcv() {
        RestaurantReservationAdapter adapter = new RestaurantReservationAdapter(reservations, context, this);
        GridLayoutManager manager;

        manager = new GridLayoutManager(getActivity(), 1, GridLayoutManager.VERTICAL, false);

        rcv.setLayoutManager(manager);
        //rcv.addItemDecoration(new SimpleDividerItemDecoration(context));
        rcv.setItemAnimator(new DefaultItemAnimator());
        rcv.setAdapter(adapter);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    public void refreshCursor(Uri uri) {
        cursor.setNotificationUri(context.getContentResolver(), uri);
        ((AppCompatActivity)context).getSupportLoaderManager().restartLoader(0, null, this);
    }


    private void restartLoader() {
        ((AppCompatActivity)context).getSupportLoaderManager().restartLoader(0, null, this);
    }
    @Override
    public void onCancelReservation(Uri uri) {
        ContentResolver mContentResolver = context.getContentResolver();
        AsyncQueryHandler deleteReservationHandler = new AsyncQueryHandler(mContentResolver) {

            @Override
            protected void onDeleteComplete(int token, Object cookie, int result) {
                super.onDeleteComplete(token, cookie, result);
                restartLoader();
                RefresWidgetService.startActionRefreshNumberReservations(context);
            }
        };

        deleteReservationHandler.startDelete(0,null, uri,null, null);
    }
}
