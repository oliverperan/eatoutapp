package com.rizosoft.eatoutapp.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.rizosoft.eatoutapp.providers.ReservationContract;
import com.rizosoft.eatoutapp.utilities.NetworkUtilities;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by oliver on 08/05/2018.
 */

public class CleanReservationsService extends IntentService {

    public static String ACTION_CLEAN_RESERVATIONS = "com.rizosoft.eatoutapp.cleanReservations";

    public CleanReservationsService()
    {
        super("CleanReservationsService");
    }

    public static void startActionCleanReservations(Context context) {
        Intent intent = new Intent(context, CleanReservationsService.class);
        intent.setAction(ACTION_CLEAN_RESERVATIONS);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        if (intent.getAction().equals(ACTION_CLEAN_RESERVATIONS)) {
            Uri uri = ReservationContract.BASE_CONTENT_URI.buildUpon().appendPath(ReservationContract.PATH_RESERVATIONS).build();

            Date date = Calendar.getInstance().getTime();
            String sDate = NetworkUtilities.getFormattedDateForSQLLite(date);

            getContentResolver().delete(uri,ReservationContract.ReservationEntry.COLUMN_DATE_RESERVATION+" < ?",new String[]{sDate});
        }

    }

}
