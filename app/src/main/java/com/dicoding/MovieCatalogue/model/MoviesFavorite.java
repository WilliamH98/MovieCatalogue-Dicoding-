package com.dicoding.MovieCatalogue.model;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.dicoding.MovieCatalogue.db.DatabaseContract;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

import static com.dicoding.MovieCatalogue.db.DatabaseContract.getColumnInt;
import static com.dicoding.MovieCatalogue.db.DatabaseContract.getColumnString;

public class MoviesFavorite extends RealmObject implements Parcelable {
    @PrimaryKey
    private int id;
    private String movieTitle;
    private String movieYear;
    private String movieImdbScore;
    private String movieDescription;
    private String movieVoteCount;
    private String moviePoster;
    private String movieOriginalLanguage;
    private String genre;

    public MoviesFavorite(Cursor cursor) {
        this.id = getColumnInt(cursor, DatabaseContract.MovieColumns._ID);
        this.movieTitle = getColumnString(cursor, DatabaseContract.MovieColumns.MOVIE_TITLE);
        this.movieYear = getColumnString(cursor, DatabaseContract.MovieColumns.MOVIE_YEAR);
        this.movieImdbScore = getColumnString(cursor, DatabaseContract.MovieColumns.MOVIE_SCORE);
        this.movieDescription = getColumnString(cursor, DatabaseContract.MovieColumns.MOVIE_DESCRIPTION);
        this.movieVoteCount = getColumnString(cursor, DatabaseContract.MovieColumns.MOVIE_VOTE_COUNT);
        this.moviePoster = getColumnString(cursor, DatabaseContract.MovieColumns.MOVIE_POSTER);
        this.movieOriginalLanguage = getColumnString(cursor, DatabaseContract.MovieColumns.MOVIE_LANGUAGE);
        this.genre = getColumnString(cursor, DatabaseContract.MovieColumns.MOVIE_GENRE);
    }

    public MoviesFavorite() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.movieTitle);
        dest.writeString(this.movieYear);
        dest.writeString(this.movieImdbScore);
        dest.writeString(this.movieDescription);
        dest.writeString(this.movieVoteCount);
        dest.writeString(this.moviePoster);
        dest.writeString(this.movieOriginalLanguage);
        dest.writeString(this.genre);
    }

    protected MoviesFavorite(Parcel in) {
        this.id = in.readInt();
        this.movieTitle = in.readString();
        this.movieYear = in.readString();
        this.movieImdbScore = in.readString();
        this.movieDescription = in.readString();
        this.movieVoteCount = in.readString();
        this.moviePoster = in.readString();
        this.movieOriginalLanguage = in.readString();
        this.genre = in.readString();
    }

    public static final Creator<MoviesFavorite> CREATOR = new Creator<MoviesFavorite>() {
        @Override
        public MoviesFavorite createFromParcel(Parcel source) {
            return new MoviesFavorite(source);
        }

        @Override
        public MoviesFavorite[] newArray(int size) {
            return new MoviesFavorite[size];
        }
    };
}
