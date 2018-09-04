package com.rizosoft.eatoutapp.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.rizosoft.eatoutapp.R;
import com.rizosoft.eatoutapp.SeeObservationsActivity;
import com.rizosoft.eatoutapp.interfaces.OnCancelReservation;
import com.rizosoft.eatoutapp.interfaces.OnClickButtonMapReservation;
import com.rizosoft.eatoutapp.pojos.Reservation;
import com.rizosoft.eatoutapp.providers.ReservationContract;
import com.rizosoft.eatoutapp.utilities.NetworkUtilities;

import java.util.Date;
import java.util.List;

/**
 * Created by oliver on 22/04/2018.
 */

public class AllReservationsAdapter extends RecyclerView.Adapter<AllReservationsAdapter.ViewAllReservationsHolder> {

    private List<Reservation> reservations;
    private OnCancelReservation onCancelReservation;
    private OnClickButtonMapReservation onClickButtonMapReservation;
    private Context context;

    public AllReservationsAdapter(List<Reservation> reservations, OnCancelReservation onCancelReservation,
                                  Context context, OnClickButtonMapReservation onClickButtonMapReservation) {
        this.reservations = reservations;
        this.onCancelReservation = onCancelReservation;
        this.context = context;
        this.onClickButtonMapReservation = onClickButtonMapReservation;
    }

    @Override
    public ViewAllReservationsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.item_fragment_all_reservations;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutIdForListItem, parent, false);
        ViewAllReservationsHolder viewAllReservationsHolder = new ViewAllReservationsHolder(view);
        return viewAllReservationsHolder;
    }

    @Override
    public void onBindViewHolder(ViewAllReservationsHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return reservations.size();
    }

    public class ViewAllReservationsHolder extends RecyclerView.ViewHolder {
        private TextView tvDateTimeReservation;
        private TextView tvRestaurantName;
        private Button btSeeObservations;
        private Button btMap;
        private Button btCancelReservation;


        public ViewAllReservationsHolder(View itemView) {
            super(itemView);
            tvDateTimeReservation = itemView.findViewById(R.id.tvDateTimeReservation);
            tvRestaurantName = itemView.findViewById(R.id.tvRestaurantName);
            btSeeObservations = itemView.findViewById(R.id.btSeeObservations);
            btMap = itemView.findViewById(R.id.btLocateMap);
            btCancelReservation = itemView.findViewById(R.id.btCancelReservation2);
        }


        public void bind(final int position) {
            final Reservation reservation = reservations.get(position);
            Date date = NetworkUtilities.getDateFromSQLiteDate(reservation.getDateReservation());
            String visualizationDate = NetworkUtilities.getFormattedDateForVisualization(date);
            final String observations = reservation.getObservations();
            tvDateTimeReservation.setText(visualizationDate);
            tvRestaurantName.setText(reservation.getRestaurantName());
            btSeeObservations.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, SeeObservationsActivity.class);
                    i.putExtra(context.getResources().getString(R.string.st_Observations), observations);
                    context.startActivity(i);
                }
            });

            btMap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    double dLat=0;
                    double dLong=0;
                    String sLatLong = reservation.getLatLong();
                    String sLat="";
                    String sLong="";
                    int index = sLatLong.indexOf(",");
                    sLat = sLatLong.substring(0, index-1);
                    sLong = sLatLong.substring(index +1);
                    dLat = Double.parseDouble(sLat);
                    dLong = Double.parseDouble(sLong);
                    onClickButtonMapReservation.onClickButtonMapReservation(dLat, dLong, reservation.getRestaurantName());

                }
            });

            btCancelReservation.setOnClickListener(new View.OnClickListener() {
                                                       @Override
                                                       public void onClick(View v) {
                                                           Reservation reservation1 = reservations.get(position);
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
            );


        }
    }
}
