package com.flesh.washingtonhearld.app.Objects;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by aaronfleshner on 5/17/14.
 */
public class Category implements Parcelable {

    private String categoryId;
    private String name;
    private String description;

    public Category() {
    }

    public Category(String categoryId, String name, String description) {
        this.categoryId = categoryId;
        this.name = name;
        this.description = description;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.categoryId);
        dest.writeString(this.name);
        dest.writeString(this.description);
    }

    private Category(Parcel in) {
        this.categoryId = in.readString();
        this.name = in.readString();
        this.description = in.readString();
    }

    public static Parcelable.Creator<Category> CREATOR = new Parcelable.Creator<Category>() {
        public Category createFromParcel(Parcel source) {
            return new Category(source);
        }

        public Category[] newArray(int size) {
            return new Category[size];
        }
    };
}
