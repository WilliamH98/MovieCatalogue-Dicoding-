package com.dicoding.consumerappsmovies;

import android.os.Parcel;
import android.os.Parcelable;

public class Movies implements Parcelable {
    private String title, score, poster, date;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Movies(String title, String score, String poster, String date) {
        this.title = title;
        this.score = score;
        this.poster = poster;
        this.date = date;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.score);
        dest.writeString(this.poster);
        dest.writeString(this.date);
    }

    protected Movies(Parcel in) {
        this.title = in.readString();
        this.score = in.readString();
        this.poster = in.readString();
        this.date = in.readString();
    }

    public static final Creator<Movies> CREATOR = new Creator<Movies>() {
        @Override
        public Movies createFromParcel(Parcel source) {
            return new Movies(source);
        }

        @Override
        public Movies[] newArray(int size) {
            return new Movies[size];
        }
    };
}
