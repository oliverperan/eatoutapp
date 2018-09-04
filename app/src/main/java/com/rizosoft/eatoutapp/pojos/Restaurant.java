package com.rizosoft.eatoutapp.pojos;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by oliver on 01/04/2018.
 */

public class Restaurant implements Parcelable {

    private String latitude;
    private String longitude;
    private String name;
    private Boolean openNow;
    private String photoReference;
    private String placeId;
    private float rating;
    private String streetAddress;
    private Boolean detailsLoaded;
    private String reference;
    private String vicinity;
    private String phoneNumber;
    private List<Review> reviews;

    public String getPictureAttibutions() {
        return pictureAttibutions;
    }

    public void setPictureAttibutions(String pictureAttibutions) {
        this.pictureAttibutions = pictureAttibutions;
    }

    private String pictureAttibutions;

    public Boolean getDetailsLoaded() {
        return detailsLoaded;
    }

    public void setDetailsLoaded(Boolean detailsLoaded) {
        this.detailsLoaded = detailsLoaded;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getOpenNow() {
        return openNow;
    }

    public void setOpenNow(Boolean openNow) {
        this.openNow = openNow;
    }

    public String getPhotoReference() {
        return photoReference;
    }

    public void setPhotoReference(String photoReference) {
        this.photoReference = photoReference;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getVicinity() {
        return vicinity;
    }

    public void setVicinity(String vicinity) {
        this.vicinity = vicinity;
    }


    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }


    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }


    public void sortByTimeDesc() {
        int n = reviews.size();
        Review temp = null;
        for(int i=0; i < n; i++){
            for(int j=1; j < (n-i); j++){
                if (reviews.get(j).getLongTime() > reviews.get(j-1).getLongTime()) {
                    temp = reviews.get(j-1);
                    reviews.set(j-1, reviews.get(j));
                    reviews.set(j,temp);
                }
            }
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(latitude);
        dest.writeString(longitude);
        dest.writeString(name);
        dest.writeInt(openNow ? 1:0);
        dest.writeString(photoReference);
        dest.writeString(placeId);
        dest.writeFloat(rating);
        dest.writeInt(detailsLoaded ? 1:0);
        dest.writeString(streetAddress);
        dest.writeString(pictureAttibutions);
        dest.writeString(phoneNumber);
        dest.writeTypedList(reviews);
    }

    public Restaurant() {

    }


    private Restaurant(Parcel in) {
        latitude = in.readString();
        longitude = in.readString();
        name = in.readString();
        openNow = (in.readInt() != 0);
        photoReference = in.readString();
        placeId = in.readString();
        rating = in.readFloat();
        detailsLoaded = (in.readInt() != 0);
        streetAddress = in.readString();
        pictureAttibutions = in.readString();
        phoneNumber = in.readString();
        reviews = new ArrayList<Review>();
        in.readTypedList(reviews, Review.CREATOR);

    }

    public static final Parcelable.Creator<Restaurant> CREATOR = new Parcelable.Creator<Restaurant>() {
        public Restaurant createFromParcel(Parcel source) {
            return new Restaurant(source);
        }

        public Restaurant[] newArray(int size) {
            return new Restaurant[size];
        }
    };
}
