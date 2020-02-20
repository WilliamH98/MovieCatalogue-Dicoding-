package com.dicoding.consumerappstvshows;

import android.os.Parcel;
import android.os.Parcelable;

public class TVShows implements Parcelable {
    private String title, score, poster, date;

    public TVShows(String title, String score, String poster, String date) {
        this.title = title;
        this.score = score;
        this.poster = poster;
        this.date = date;
    }

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

    public TVShows() {
    }

    protected TVShows(Parcel in) {
        this.title = in.readString();
        this.score = in.readString();
        this.poster = in.readString();
        this.date = in.readString();
    }

    public static final Parcelable.Creator<TVShows> CREATOR = new Parcelable.Creator<TVShows>() {
        @Override
        public TVShows createFromParcel(Parcel source) {
            return new TVShows(source);
        }

        @Override
        public TVShows[] newArray(int size) {
            return new TVShows[size];
        }
    };
}
