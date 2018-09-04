package com.rizosoft.eatoutapp.pojos;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by oliver on 15/04/2018.
 */

public class Attribution implements Parcelable {

    private String authorName;
    private String urlName;
    private String webSiteSource;
    private String nameSiteSource;
    private int drawable;

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getUrlName() {
        return urlName;
    }

    public void setUrlName(String urlName) {
        this.urlName = urlName;
    }

    public String getWebSiteSource() {
        return webSiteSource;
    }

    public void setWebSiteSource(String webSiteSource) {
        this.webSiteSource = webSiteSource;
    }

    public String getNameSiteSource() {
        return nameSiteSource;
    }

    public void setNameSiteSource(String nameSiteSource) {
        this.nameSiteSource = nameSiteSource;
    }

    public int getDrawable() {
        return drawable;
    }

    public void setDrawable(int drawable) {
        this.drawable = drawable;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(authorName);
        dest.writeString(urlName);
        dest.writeString(webSiteSource);
        dest.writeString(nameSiteSource);
        dest.writeInt(drawable);
    }

    public Attribution() {

    }


    private Attribution(Parcel in) {
        authorName = in.readString();
        urlName = in.readString();
        webSiteSource = in.readString();
        nameSiteSource = in.readString();
        drawable = in.readInt();
    }

    public static final Parcelable.Creator<Attribution> CREATOR = new Parcelable.Creator<Attribution>() {
        public Attribution createFromParcel(Parcel source) {
            return new Attribution(source);
        }

        public Attribution[] newArray(int size) {
            return new Attribution[size];
        }
    };


    public String toString() {
        String strAttribution = "Icon made by <a href='"+urlName+"'>"+authorName+"</a> from <a href='"+webSiteSource+"'>"+nameSiteSource+"</a>";
        return strAttribution;
    }
}
