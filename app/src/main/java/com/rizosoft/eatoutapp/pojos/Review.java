package com.rizosoft.eatoutapp.pojos;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by oliver on 10/04/2018.
 */

public class Review implements Parcelable {

    private String address;
    private String phoneNumber;
    private String authorName;
    private String authorUrl;
    private String language;
    private float rating;
    private String text;
    private String time;


    private String relativeTimeDescription;

    public String getRelativeTimeDescription() {
        return relativeTimeDescription;
    }

    public void setRelativeTimeDescription(String relativeTimeDescription) {
        this.relativeTimeDescription = relativeTimeDescription;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getAuthorUrl() {
        return authorUrl;
    }

    public void setAuthorUrl(String authorUrl) {
        this.authorUrl = authorUrl;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }



    public Review() {

    }

    @Override
    public int describeContents() {
        return 0;
    }




    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(address);
        dest.writeString(phoneNumber);
        dest.writeString(authorName);
        dest.writeString(authorUrl);
        dest.writeString(language);
        dest.writeFloat(rating);
        dest.writeString(text);
        dest.writeString(time);
        dest.writeString(relativeTimeDescription);
    }


    public Review(Parcel in) {
        address = in.readString();
        phoneNumber = in.readString();
        authorName = in.readString();
        authorUrl = in.readString();
        language = in.readString();
        rating = in.readFloat();
        text = in.readString();
        time = in.readString();
        relativeTimeDescription = in.readString();
    }

    public static final Parcelable.Creator<Review> CREATOR = new Parcelable.Creator<Review>() {
        public Review createFromParcel(Parcel source) {
            return new Review(source);
        }

        public Review[] newArray(int size) {
            return new Review[size];
        }
    };


    public long getLongTime() {
        long l = Long.parseLong(time);
        return l;
    }

}
