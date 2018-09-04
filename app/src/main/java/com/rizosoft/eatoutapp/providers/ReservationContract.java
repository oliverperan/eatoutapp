package com.rizosoft.eatoutapp.providers;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by oliver on 15/04/2018.
 */

public class ReservationContract {

    public static final String CONTENT_AUTHORITY = "com.rizosoft.eatoutapp";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_RESERVATIONS = "reservations";


    public static class ReservationEntry implements BaseColumns {

        public static final String TABLE_NAME_RESERVATION = "reservation";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_PLACE_ID = "placeId";
        public static final String COLUMN_LAT_LONG="latLong";
        public static final String COLUMN_RESTAURANT_NAME = "restaurantName";
        public static final String COLUMN_DATE_RESERVATION = "reservationDate";
        public static final String COLUMN_OBSERVATIONS = "observations";

        public static final String COLUMN_STREET_ADDRESS = "streetAddress";
        public static final String COLUMN_PHONE_NUMBER = "phoneNumber";
    }


}
