package com.rizosoft.eatoutapp.pojos;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by oliver on 20/04/2018.
 */

public class Reservation implements Parcelable {

    private int id;
    private String placeId;
    private String restaurantName;
    private String observations;
    private String dateReservation;
    private String streetAddress;
    private String phoneNumber;
    private String latLong;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    public String getDateReservation() {
        return dateReservation;
    }

    public void setDateReservation(String dateReservation) {
        this.dateReservation = dateReservation;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getLatLong() {
        return latLong;
    }

    public void setLatLong(String latLong) {
        this.latLong = latLong;
    }


    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Reservation() {

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(placeId);
        dest.writeString(restaurantName);
        dest.writeString(observations);
        dest.writeString(dateReservation);
        dest.writeString(streetAddress);
        dest.writeString(phoneNumber);
        dest.writeString(latLong);
    }

    private Reservation(Parcel in) {
        id = in.readInt();
        placeId = in.readString();
        restaurantName = in.readString();
        observations = in.readString();
        dateReservation = in.readString();
        streetAddress = in.readString();
        phoneNumber = in.readString();
        latLong = in.readString();
    }

    public static final Parcelable.Creator<Reservation> CREATOR = new Parcelable.Creator<Reservation>() {
        public Reservation createFromParcel(Parcel source) {
            return new Reservation(source);
        }

        public Reservation[] newArray(int size) { return new Reservation[size];
        }
    };
}
