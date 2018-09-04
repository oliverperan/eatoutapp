package com.rizosoft.eatoutapp.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.rizosoft.eatoutapp.R;
import com.rizosoft.eatoutapp.interfaces.OnCancelReservation;
import com.rizosoft.eatoutapp.pojos.Reservation;
import com.rizosoft.eatoutapp.providers.ReservationContract;
import com.rizosoft.eatoutapp.utilities.NetworkUtilities;

import java.util.Date;
import java.util.List;

/**
 * Created by oliver on 21/04/2018.
 */

public class RestaurantReservationAdapter extends RecyclerView.Adapter<RestaurantReservationAdapter.RestaurantReservationViewHolder> {

    private List<Reservation> restaurantReservationList;
    private Context context;
    private OnCancelReservation onCancelReservation;
    
    public RestaurantReservationAdapter(List<Reservation> restaurantReservationList, Context context, OnCancelReservation onCancelReservation) {
        this.restaurantReservationList = restaurantReservationList;
        this.context = context;
        this.onCancelReservation = onCancelReservation;
    }

    @Override
    public RestaurantReservationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.restaurant_reservation;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutIdForListItem, parent, false);
        RestaurantReservationAdapter.RestaurantReservationViewHolder restaurantReservationViewHolder = new RestaurantReservationViewHolder(view);
        return restaurantReservationViewHolder;
    }

    @Override
    public void onBindViewHolder(RestaurantReservationViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return restaurantReservationList.size();
    }

    public class RestaurantReservationViewHolder extends RecyclerView.ViewHolder {
        private TextView tvReservationDate;
        private TextView tvObservations;
        private Button btCancelReservation;


        public RestaurantReservationViewHolder(View itemView) {
            super(itemView);
            tvReservationDate = itemView.findViewById(R.id.tvReservationDate);
            tvObservations = itemView.findViewById(R.id.tvObservations);
            btCancelReservation = itemView.findViewById(R.id.btCancelReservation);
        }


        public void bind(final int listIndex) {
            Reservation reservation = restaurantReservationList.get(listIndex);
            Date reservationDate = NetworkUtilities.getDateFromSQLiteDate(reservation.getDateReservation());
            tvReservationDate.setText(NetworkUtilities.getFormattedDateForVisualization(reservationDate));
            tvObservations.setText(reservation.getObservations());
            btCancelReservation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    eliminarReserva(listIndex);
                }
            });

        }
    }


    private void eliminarReserva(int listIndex) {
        Reservation reservation = restaurantReservationList.get(listIndex);
        final Uri uri = ReservationContract.BASE_CONTENT_URI.buildUpon()
                                    .appendEncodedPath(ReservationContract.PATH_RESERVATIONS+"/"+reservation.getId()).build();


        new AlertDialog.Builder(context)
                .setTitle(context.getResources().getString(R.string.confirmation))
                .setMessage(context.getResources().getString(R.string.cancelReservationConfirmation))
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        onCancelReservation.onCancelReservation(uri);
                    }})
                .setNegativeButton(android.R.string.no, null).show();
    }

}


