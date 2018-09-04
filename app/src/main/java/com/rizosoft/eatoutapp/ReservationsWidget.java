package com.rizosoft.eatoutapp;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.rizosoft.eatoutapp.services.RefresWidgetService;

/**
 * Implementation of App Widget functionality.
 */
public class ReservationsWidget extends AppWidgetProvider {


    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int numberOfReservations, int appWidgetId) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.reservations_widget);
        Intent intent = new Intent(context, AllReservationsActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent,0);
        views.setOnClickPendingIntent(R.id.appwidget_text, pendingIntent);
        views.setOnClickPendingIntent(R.id.imCalendar, pendingIntent);
        views.setTextViewText(R.id.appwidget_text, numberOfReservations+"");
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        RefresWidgetService.startActionRefreshNumberReservations(context);
    }



    public static void updateAppWidgets(Context context, AppWidgetManager appWidgetManager, int numberOfReservations, int[] appWidgetsIds) {
        for (int appWidgetId:appWidgetsIds) {
           updateAppWidget(context, appWidgetManager, numberOfReservations, appWidgetId);
        }
    }



    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }


}

