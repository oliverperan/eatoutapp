package com.rizosoft.eatoutapp.providers;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.rizosoft.eatoutapp.utilities.NetworkUtilities;

/**
 * Created by oliver on 15/04/2018.
 */

public class ReservationProvider extends ContentProvider {
    public final String QUERY_RESERVATIONS_AHEAD = "SELECT * FROM "+ ReservationContract.ReservationEntry.TABLE_NAME_RESERVATION
                                                   +"WHERE "+ ReservationContract.ReservationEntry.COLUMN_DATE_RESERVATION +" >= "
                                                   + "'"+NetworkUtilities.getCurrentDate()+"' ORDER BY "
                                                   + ReservationContract.ReservationEntry.COLUMN_DATE_RESERVATION+ " DESC";




    private static final int CODE_RESERVATIONS = 100;
    private static final int CODE_INDIVIDUAL_RESERVATION = 101;


    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = ReservationContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, ReservationContract.PATH_RESERVATIONS, CODE_RESERVATIONS);
        matcher.addURI(authority, ReservationContract.PATH_RESERVATIONS+"/#", CODE_INDIVIDUAL_RESERVATION);
        return matcher;
    }

    private UriMatcher sUriMatcher = buildUriMatcher();
    private ReservationDbHelper dbHelper;

    @Override
    public boolean onCreate() {
        dbHelper = new ReservationDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        Cursor cursor;

        /*
         * Here's the switch statement that, given a URI, will determine what kind of request is
         * being made and query the database accordingly.
         */
        switch (sUriMatcher.match(uri)) {
            case CODE_INDIVIDUAL_RESERVATION: {

                String sId = uri.getLastPathSegment();


                String[] selectionArguments = new String[]{sId};

                cursor = dbHelper.getReadableDatabase().query(
                        ReservationContract.ReservationEntry.TABLE_NAME_RESERVATION,

                        projection,

                        ReservationContract.ReservationEntry.COLUMN_ID + " = ? ",
                        selectionArguments,
                        null,
                        null,
                        sortOrder);

                break;
            }

            case CODE_RESERVATIONS: {
                cursor = dbHelper.getReadableDatabase().query(
                        ReservationContract.ReservationEntry.TABLE_NAME_RESERVATION,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);

                break;
            }


            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        //cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;


    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        int match = sUriMatcher.match(uri);

        switch (match)
        {
            case CODE_RESERVATIONS:
                return "vnd.android.cursor.dir/com.rizosoft.android.eatoutapp.reservation";
            case CODE_INDIVIDUAL_RESERVATION:
                return "vnd.android.cursor.item/com.rizosoft.android.eatoutapp.reservation";
            default:
                return null;
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {

        int match = sUriMatcher.match(uri);

        SQLiteDatabase dbW = dbHelper.getWritableDatabase();
        switch (match) {
            case CODE_RESERVATIONS: {
                Long id = dbW.insert(ReservationContract.ReservationEntry.TABLE_NAME_RESERVATION, null, contentValues);
                if (id > 0) {
                    //getContext().getContentResolver().notifyChange(uri, null);
                    return ContentUris.withAppendedId(uri, id);
                }
            }
            default: {
                throw new SQLException("Error inserting into table: ");
            }
        }

    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String where, @Nullable String[] whereArgs) {
        int count;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String segment = "";
        switch (sUriMatcher.match(uri)) {
            case CODE_RESERVATIONS:
                count = db.delete(ReservationContract.ReservationEntry.TABLE_NAME_RESERVATION, where, whereArgs);
                break;
            case CODE_INDIVIDUAL_RESERVATION:
                segment = uri.getLastPathSegment();
                count = db.delete(ReservationContract.ReservationEntry.TABLE_NAME_RESERVATION, ReservationContract.ReservationEntry.COLUMN_ID + "="
                        + segment
                        + (!TextUtils.isEmpty(where) ? " AND ("
                        + where + ')' : ""), whereArgs);
                break;
            default: throw new IllegalArgumentException("Unsupported URI: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;

    }


    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        int count;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        String id;

        switch (match){
            case CODE_INDIVIDUAL_RESERVATION:
                id = uri.getLastPathSegment();
                count = db.update(ReservationContract.ReservationEntry.TABLE_NAME_RESERVATION, values, ReservationContract.ReservationEntry.COLUMN_ID +
                        " = " + id + (!TextUtils.isEmpty(selection) ? " AND (" +
                        selection + ')' : ""), selectionArgs);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri+ " or no valid for update");
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

}
