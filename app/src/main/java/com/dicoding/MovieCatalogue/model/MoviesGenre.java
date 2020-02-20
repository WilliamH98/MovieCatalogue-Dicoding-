package com.dicoding.MovieCatalogue.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class MoviesGenre implements Parcelable {
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

    protected MoviesGenre(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
    }

    public static final Creator<MoviesGenre> CREATOR = new Creator<MoviesGenre>() {
        @Override
        public MoviesGenre createFromParcel(Parcel source) {
            return new MoviesGenre(source);
        }

        @Override
        public MoviesGenre[] newArray(int size) {
            return new MoviesGenre[size];
        }
    };
}
