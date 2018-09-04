package com.rizosoft.eatoutapp.services;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.rizosoft.eatoutapp.ReservationsWidget;
import com.rizosoft.eatoutapp.providers.ReservationContract;
import com.rizosoft.eatoutapp.utilities.NetworkUtilities;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by oliver on 05/05/2018.
 */

public class RefresWidgetService extends IntentService {

    public static String ACTION_REFRESH_NUMBER_RESERVATIONS = "com.rizosoft.eatoutapp.numberReservations";

    public RefresWidgetService()
    {
        super("RefresWidgetService");
    }

    public static void startActionRefreshNumberReservations(Context context) {
        Intent intent = new Intent(context, RefresWidgetService.class);
        intent.setAction(ACTION_REFRESH_NUMBER_RESERVATIONS);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        if (intent.getAction().equals(ACTION_REFRESH_NUMBER_RESERVATIONS)) {
            final String[] countProjection = new String[] {
                    "count(" + ReservationContract.ReservationEntry.COLUMN_ID + ")",
            };

            Uri uri = ReservationContract.BASE_CONTENT_URI.buildUpon().appendPath(ReservationContract.PATH_RESERVATIONS).build();

            Date date = Calendar.getInstance().getTime();
            String sDate = NetworkUtilities.getFormattedDateForSQLLite(date);

            Cursor countCursor = getContentResolver().query(uri,
                    countProjection,
                    ReservationContract.ReservationEntry.COLUMN_DATE_RESERVATION + " >=? ",
                    new String[] {sDate},
                    null);

            countCursor.moveToFirst();
            int numberOfReservations = countCursor.getInt(0);
            countCursor.close();

            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
            int[] appWidgetsIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, ReservationsWidget.class));

            ReservationsWidget.updateAppWidgets(this, appWidgetManager, numberOfReservations, appWidgetsIds);

        }

    }


}
