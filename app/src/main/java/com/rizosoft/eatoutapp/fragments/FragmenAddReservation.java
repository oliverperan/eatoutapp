package com.rizosoft.eatoutapp.fragments;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rizosoft.eatoutapp.R;
import com.rizosoft.eatoutapp.interfaces.OnSavingReservation;
import com.rizosoft.eatoutapp.interfaces.OnSetDateAndTime;
import com.rizosoft.eatoutapp.pojos.Restaurant;
import com.rizosoft.eatoutapp.providers.ReservationContract;
import com.rizosoft.eatoutapp.utilities.NetworkUtilities;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by oliver on 15/04/2018.
 */

public class FragmenAddReservation extends Fragment implements OnSetDateAndTime {
    private Restaurant restaurant;
    private Context context;
    private ImageView buttonSelectDateAndTime;
    private ImageView buttonSave;
    private FragmenAddReservation fragment;
    private TextView tvSelectedDate;
    private Date selectedDateReservation;
    private LinearLayout lDateSelected;
    private EditText editObservations;
    private OnSavingReservation onSavingReservation;
    private ConstraintLayout clAddReservation;


    private static final int MESSAGE_RESERVATION_INSERTED=1;

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {


        if (context == null) {
            context = getActivity();
        }
        View view = inflater.inflate(R.layout.fragment_add_reservation, container, false);
        clAddReservation = view.findViewById(R.id.clAddReservation);
        editObservations = view.findViewById(R.id.edObservations);
        buttonSelectDateAndTime = view.findViewById(R.id.btSelectDateAndTime);
        buttonSelectDateAndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NetworkUtilities.showDateTimePicker(context, fragment);
            }
        });



        buttonSave = view.findViewById(R.id.btSave);
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()){
                    Handler mHandler = new Handler() {
                        @Override
                        public void handleMessage(Message msg) {
                            if (msg.what == MESSAGE_RESERVATION_INSERTED) {
                                Uri uri = (Uri) msg.obj;
                                onSavingReservation.onSavingReservation(uri);
                            }
                        }
                    };

                    String observation = editObservations.getText().toString();
                    insertData(selectedDateReservation, observation, mHandler);
                }

            }
        });
        tvSelectedDate = view.findViewById(R.id.tvDateTimeSelected);
        lDateSelected = view.findViewById(R.id.lDateTimeSelected);
        fragment = this;
        return view;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setOnSavingReservation(OnSavingReservation onSavingReservation) {
        this.onSavingReservation = onSavingReservation;
    }

    public static FragmenAddReservation newInstance(Restaurant restaurant, Context context, OnSavingReservation onSavingReservation) {
        FragmenAddReservation fragmentFirst = new FragmenAddReservation();
        fragmentFirst.setContext(context);
        fragmentFirst.setRestaurant(restaurant);
        fragmentFirst.setOnSavingReservation(onSavingReservation);

        return fragmentFirst;
    }

    @Override
    public void onSetDateAndTime(Calendar dateAndTimeSelected) {
            selectedDateReservation = dateAndTimeSelected.getTime();
            tvSelectedDate.setText(NetworkUtilities.getFormattedDateForVisualization(selectedDateReservation));
            lDateSelected.setVisibility(View.VISIBLE);
    }


    private final void insertData(Date date, String observations, final Handler handler) {

        ContentResolver mContentResolver = context.getContentResolver();
        ContentValues values = new ContentValues();

        values.put(ReservationContract.ReservationEntry.COLUMN_PLACE_ID, restaurant.getPlaceId());
        values.put(ReservationContract.ReservationEntry.COLUMN_DATE_RESERVATION,NetworkUtilities.getFormattedDateForSQLLite(date));
        values.put(ReservationContract.ReservationEntry.COLUMN_LAT_LONG, restaurant.getLatitude()+","+restaurant.getLongitude());
        values.put(ReservationContract.ReservationEntry.COLUMN_RESTAURANT_NAME, restaurant.getName());
        values.put(ReservationContract.ReservationEntry.COLUMN_OBSERVATIONS, observations);

        AsyncQueryHandler insertReservationHandler = new AsyncQueryHandler(mContentResolver) {
            @Override
            protected void onInsertComplete(int token, Object cookie, Uri uri) {
                if (handler != null) {
                    Message message = Message.obtain();
                    message.what = MESSAGE_RESERVATION_INSERTED;
                    message.obj = uri;
                    handler.sendMessage(message);
                    selectedDateReservation = null;
                    lDateSelected.setVisibility(View.GONE);
                    editObservations.setText("");
                }
            }
        };

        //Uri uri = MovieContract.BASE_CONTENT_URI.buildUpon().appendEncodedPath(MovieContract.PATH_MOVIES).build();
        Uri uri = ReservationContract.BASE_CONTENT_URI.buildUpon().appendEncodedPath(ReservationContract.PATH_RESERVATIONS).build();

        insertReservationHandler.startInsert(0, null, uri, values);

    }


    private Boolean validate() {

        if (tvSelectedDate.getText().equals("")) {
            NetworkUtilities.showMessage(clAddReservation, getResources().getString(R.string.data_date_reservation_compulsory));
            return false;
        } else if (new Date().after(selectedDateReservation)) {
            NetworkUtilities.showMessage(clAddReservation, getResources().getString(R.string.current_date_after_date_selected));
            return false;
        }

        return true;
    }


    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        if (selectedDateReservation != null) {
            outState.putString(getString(R.string.select_date_time), NetworkUtilities.getFormattedDateForSQLLite(selectedDateReservation));
        }

        outState.putParcelable(getString(R.string.restaurant), restaurant);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null){
            restaurant = savedInstanceState.getParcelable(getString(R.string.restaurant));
            String sDate = savedInstanceState.getString(getString(R.string.select_date_time),"");
            if (!sDate.equals("")) {
                selectedDateReservation = NetworkUtilities.getDateFromSQLiteDate(sDate);
                tvSelectedDate.setText(NetworkUtilities.getFormattedDateForVisualization(selectedDateReservation));
                lDateSelected.setVisibility(View.VISIBLE);
            }
        }

    }
}
