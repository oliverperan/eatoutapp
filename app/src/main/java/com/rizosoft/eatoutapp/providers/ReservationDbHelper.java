package com.rizosoft.eatoutapp.providers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by oliver on 15/04/2018.
 */

public class ReservationDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "reservations.db";
    private static final int DATABASE_VERSION = 1;

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_RESERVATION_TABLE =
        "CREATE TABLE " + ReservationContract.ReservationEntry.TABLE_NAME_RESERVATION + " (" +
                        ReservationContract.ReservationEntry.COLUMN_ID + " INTEGER PRIMARY KEY, "+
                        ReservationContract.ReservationEntry.COLUMN_PLACE_ID+" TEXT,"+
                        ReservationContract.ReservationEntry.COLUMN_RESTAURANT_NAME + " TEXT, "+
                        ReservationContract.ReservationEntry.COLUMN_OBSERVATIONS + " TEXT, " +
                        ReservationContract.ReservationEntry.COLUMN_DATE_RESERVATION+" TEXT,"+
                        ReservationContract.ReservationEntry.COLUMN_STREET_ADDRESS+" TEXT," +
                        ReservationContract.ReservationEntry.COLUMN_PHONE_NUMBER+" TEXT," +
                        ReservationContract.ReservationEntry.COLUMN_LAT_LONG+" TEXT)";

        sqLiteDatabase.execSQL(SQL_CREATE_RESERVATION_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ReservationContract.ReservationEntry.TABLE_NAME_RESERVATION);
        onCreate(sqLiteDatabase);
    }


    public ReservationDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

}
