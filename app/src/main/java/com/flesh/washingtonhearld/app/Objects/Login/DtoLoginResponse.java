package com.flesh.washingtonhearld.app.Objects.Login;

import android.os.Parcel;
import android.os.Parcelable;

import com.flesh.washingtonhearld.app.Objects.User;

/**
 * Created by aaronfleshner on 5/16/14.
 */
public class DtoLoginResponse implements Parcelable {

    private String accessToken;
    private User user;

    public DtoLoginResponse(String accessToken, User user) {
        this.accessToken = accessToken;
        this.user = user;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.accessToken);
        dest.writeParcelable(this.user, 0);
    }

    private DtoLoginResponse(Parcel in) {
        this.accessToken = in.readString();
        this.user = in.readParcelable(((Object) user).getClass().getClassLoader());
    }

    public static Parcelable.Creator<DtoLoginResponse> CREATOR = new Parcelable.Creator<DtoLoginResponse>() {
        public DtoLoginResponse createFromParcel(Parcel source) {
            return new DtoLoginResponse(source);
        }

        public DtoLoginResponse[] newArray(int size) {
            return new DtoLoginResponse[size];
        }
    };
}
