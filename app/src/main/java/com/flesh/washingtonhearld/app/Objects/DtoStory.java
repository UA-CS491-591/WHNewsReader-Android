package com.flesh.washingtonhearld.app.Objects;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by aaronfleshner on 5/17/14.
 */
public class DtoStory implements Parcelable {

    private String storyId;
    private String title;
    private String subtitle;
    private String body;
    private String datePublished;// make this a date. For joda time
    private User author;
    private double lat;
    private double lng;
    private Category category;
    private String imageUrl;

    public DtoStory() {
    }

    public DtoStory(String storyId, String title, String subtitle, String body, String datePublished, User author, double lat, double lng, Category category, String imageUrl) {
        this.storyId = storyId;
        this.title = title;
        this.subtitle = subtitle;
        this.body = body;
        this.datePublished = datePublished;
        this.author = author;
        this.lat = lat;
        this.lng = lng;
        this.category = category;
        this.imageUrl = imageUrl;
    }

    public String getStoryId() {
        return storyId;
    }

    public void setStoryId(String storyId) {
        this.storyId = storyId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getDatePublished() {
        return datePublished;
    }

    public void setDatePublished(String datePublished) {
        this.datePublished = datePublished;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.storyId);
        dest.writeString(this.title);
        dest.writeString(this.subtitle);
        dest.writeString(this.body);
        dest.writeString(this.datePublished);
        dest.writeParcelable(this.author, 0);
        dest.writeDouble(this.lat);
        dest.writeDouble(this.lng);
        dest.writeParcelable(this.category, 0);
        dest.writeString(this.imageUrl);
    }

    private DtoStory(Parcel in) {
        this.storyId = in.readString();
        this.title = in.readString();
        this.subtitle = in.readString();
        this.body = in.readString();
        this.datePublished = in.readString();
        this.author = in.readParcelable(((Object) author).getClass().getClassLoader());
        this.lat = in.readDouble();
        this.lng = in.readDouble();
        this.category = in.readParcelable(((Object) category).getClass().getClassLoader());
        this.imageUrl = in.readString();
    }

    public static Parcelable.Creator<DtoStory> CREATOR = new Parcelable.Creator<DtoStory>() {
        public DtoStory createFromParcel(Parcel source) {
            return new DtoStory(source);
        }

        public DtoStory[] newArray(int size) {
            return new DtoStory[size];
        }
    };
}
