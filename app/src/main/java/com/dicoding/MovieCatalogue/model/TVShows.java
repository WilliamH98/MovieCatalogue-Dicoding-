package com.dicoding.MovieCatalogue.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class TVShows implements Parcelable {
    private Integer id;


    @SerializedName("original_name")
    private String tvShowsTitle;

    @SerializedName("first_air_date")
    private String tvShowsYear;

    @SerializedName("vote_average")
    private String tvShowsImdbScore;

    @SerializedName("overview")
    private String tvShowsDescription;

    @SerializedName("vote_count")
    private String tvShowsVoteCount;

    @SerializedName("poster_path")
    private String tvShowsPoster;

    @SerializedName("original_language")
    private String tvShowsOriginalLanguage;

    @SerializedName("genre_ids")
    private int[] genre;

    public int[] getGenre() {
        return genre;
    }

    public void setGenre(int[] genre) {
        this.genre = genre;
    }

    public String getTvShowsTitle() {
        return tvShowsTitle;
    }

    public void setTvShowsTitle(String tvShowsTitle) {
        this.tvShowsTitle = tvShowsTitle;
    }

    public String getTvShowsYear() {
        return tvShowsYear;
    }

    public void setTvShowsYear(String tvShowsYear) {
        this.tvShowsYear = tvShowsYear;
    }

    public String getTvShowsImdbScore() {
        return tvShowsImdbScore;
    }

    public void setTvShowsImdbScore(String tvShowsImdbScore) {
        this.tvShowsImdbScore = tvShowsImdbScore;
    }

    public String getTvShowsDescription() {
        return tvShowsDescription;
    }

    public void setTvShowsDescription(String tvShowsDescription) {
        this.tvShowsDescription = tvShowsDescription;
    }

    public String getTvShowsVoteCount() {
        return tvShowsVoteCount;
    }

    public void setTvShowsVoteCount(String tvShowsVoteCount) {
        this.tvShowsVoteCount = tvShowsVoteCount;
    }

    public String getTvShowsPoster() {
        return tvShowsPoster;
    }

    public void setTvShowsPoster(String tvShowsPoster) {
        this.tvShowsPoster = tvShowsPoster;
    }

    public String getTvShowsOriginalLanguage() {
        return tvShowsOriginalLanguage;
    }

    public void setTvShowsOriginalLanguage(String tvShowsOriginalLanguage) {
        this.tvShowsOriginalLanguage = tvShowsOriginalLanguage;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public TVShows(int id, String tvShowsTitle, String tvShowsYear, String tvShowsImdbScore, String tvShowsDescription, String tvShowsVoteCount, String tvShowsPoster, String tvShowsOriginalLanguage) {
        setId(id);
        setTvShowsTitle(tvShowsTitle);
        setTvShowsYear(tvShowsYear);
        setTvShowsImdbScore(tvShowsImdbScore);
        setTvShowsDescription(tvShowsDescription);
        setTvShowsVoteCount(tvShowsVoteCount);
        setTvShowsPoster(tvShowsPoster);
        setTvShowsOriginalLanguage(tvShowsOriginalLanguage);
    }

    public TVShows() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.tvShowsTitle);
        dest.writeString(this.tvShowsYear);
        dest.writeString(this.tvShowsImdbScore);
        dest.writeString(this.tvShowsDescription);
        dest.writeString(this.tvShowsVoteCount);
        dest.writeString(this.tvShowsPoster);
        dest.writeString(this.tvShowsOriginalLanguage);
        dest.writeIntArray(this.genre);
    }

    protected TVShows(Parcel in) {
        this.id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.tvShowsTitle = in.readString();
        this.tvShowsYear = in.readString();
        this.tvShowsImdbScore = in.readString();
        this.tvShowsDescription = in.readString();
        this.tvShowsVoteCount = in.readString();
        this.tvShowsPoster = in.readString();
        this.tvShowsOriginalLanguage = in.readString();
        this.genre = in.createIntArray();
    }

    public static final Creator<TVShows> CREATOR = new Creator<TVShows>() {
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
