package com.flesh.washingtonhearld.app.Objects.Login;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by aaronfleshner on 5/16/14.
 */
public class DtoLogin implements Parcelable {

    private String username;
    private String password;

    public DtoLogin(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.username);
        dest.writeString(this.password);
    }

    private DtoLogin(Parcel in) {
        this.username = in.readString();
        this.password = in.readString();
    }

    public static Parcelable.Creator<DtoLogin> CREATOR = new Parcelable.Creator<DtoLogin>() {
        public DtoLogin createFromParcel(Parcel source) {
            return new DtoLogin(source);
        }

        public DtoLogin[] newArray(int size) {
            return new DtoLogin[size];
        }
    };
}
