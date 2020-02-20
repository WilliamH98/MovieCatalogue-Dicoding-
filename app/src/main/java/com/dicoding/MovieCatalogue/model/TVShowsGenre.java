package com.dicoding.MovieCatalogue.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class TVShowsGenre implements Parcelable {
    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
    }

    protected TVShowsGenre(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
    }

    public static final Parcelable.Creator<TVShowsGenre> CREATOR = new Parcelable.Creator<TVShowsGenre>() {
        @Override
        public TVShowsGenre createFromParcel(Parcel source) {
            return new TVShowsGenre(source);
        }

        @Override
        public TVShowsGenre[] newArray(int size) {
            return new TVShowsGenre[size];
        }
    };
}
