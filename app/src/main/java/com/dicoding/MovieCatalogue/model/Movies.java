package com.dicoding.MovieCatalogue.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Movies implements Parcelable {
    private Integer id;

    @SerializedName("original_title")
    private String movieTitle;

    @SerializedName("release_date")
    private String movieYear;

    @SerializedName("vote_average")
    private String movieImdbScore;

    @SerializedName("overview")
    private String movieDescription;

    @SerializedName("vote_count")
    private String movieVoteCount;

    @SerializedName("poster_path")
    private String moviePoster;

    @SerializedName("original_language")
    private String movieOriginalLanguage;

    @SerializedName("genre_ids")
    private int[] genre;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int[] getGenre() {
        return genre;
    }

    public void setGenre(int[] genre) {
        this.genre = genre;
    }

    public String getMoviePoster() {
        return moviePoster;
    }

    public void setMoviePoster(String moviePoster) {
        this.moviePoster = moviePoster;
    }

    public String getMovieVoteCount() {
        return movieVoteCount;
    }

    public void setMovieVoteCount(String movieVoteAverage) {
        this.movieVoteCount = movieVoteAverage;
    }

    public String getMovieOriginalLanguage() {
        return movieOriginalLanguage;
    }

    public void setMovieOriginalLanguage(String movieOriginalLanguage) {
        this.movieOriginalLanguage = movieOriginalLanguage;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public String getMovieYear() {
        return movieYear;
    }

    public void setMovieYear(String movieYear) {
        this.movieYear = movieYear;
    }

    public String getMovieImdbScore() {
        return movieImdbScore;
    }

    public void setMovieImdbScore(String movieImdbScore) {
        this.movieImdbScore = movieImdbScore;
    }

    public String getMovieDescription() {
        return movieDescription;
    }

    public void setMovieDescription(String movieDescription) {
        this.movieDescription = movieDescription;
    }

    public Movies(int id, String movieTitle, String movieYear, String movieImdbScore, String movieDescription, String movieVoteCount, String moviePoster, String movieOriginalLanguage) {
        setId(id);
        setMovieTitle(movieTitle);
        setMovieYear(movieYear);
        setMovieImdbScore(movieImdbScore);
        setMovieDescription(movieDescription);
        setMovieVoteCount(movieVoteCount);
        setMoviePoster(moviePoster);
        setMovieOriginalLanguage(movieOriginalLanguage);
    }

    public Movies() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.movieTitle);
        dest.writeString(this.movieYear);
        dest.writeString(this.movieImdbScore);
        dest.writeString(this.movieDescription);
        dest.writeString(this.movieVoteCount);
        dest.writeString(this.moviePoster);
        dest.writeString(this.movieOriginalLanguage);
        dest.writeIntArray(this.genre);
    }

    protected Movies(Parcel in) {
        this.id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.movieTitle = in.readString();
        this.movieYear = in.readString();
        this.movieImdbScore = in.readString();
        this.movieDescription = in.readString();
        this.movieVoteCount = in.readString();
        this.moviePoster = in.readString();
        this.movieOriginalLanguage = in.readString();
        this.genre = in.createIntArray();
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
