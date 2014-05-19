package com.flesh.washingtonhearld.app.Objects;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by aaronfleshner on 5/16/14.
 */
public class User implements Parcelable {

    private String Id;
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String position;
    private boolean isWriter;
    private String imageUrl;

    public User(){

    }

    public User(String id, String firstName, String lastName, String username, String email, String position, boolean isWriter, String imageUrl) {
        this.Id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.email = email;
        this.position = position;
        this.isWriter = isWriter;
        this.imageUrl = imageUrl;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public boolean isWriter() {
        return isWriter;
    }

    public void setWriter(boolean isWriter) {
        this.isWriter = isWriter;
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
        dest.writeString(this.Id);
        dest.writeString(this.firstName);
        dest.writeString(this.lastName);
        dest.writeString(this.username);
        dest.writeString(this.email);
        dest.writeString(this.position);
        dest.writeByte(isWriter ? (byte) 1 : (byte) 0);
        dest.writeString(this.imageUrl);
    }

    private User(Parcel in) {
        this.Id = in.readString();
        this.firstName = in.readString();
        this.lastName = in.readString();
        this.username = in.readString();
        this.email = in.readString();
        this.position = in.readString();
        this.isWriter = in.readByte() != 0;
        this.imageUrl = in.readString();
    }

    public static Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
