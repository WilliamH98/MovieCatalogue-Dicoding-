package com.dicoding.MovieCatalogue.model;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.dicoding.MovieCatalogue.db.DatabaseContractTV;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

import static com.dicoding.MovieCatalogue.db.DatabaseContractTV.getColumnInt;
import static com.dicoding.MovieCatalogue.db.DatabaseContractTV.getColumnString;

public class TVShowsFavorite extends RealmObject implements Parcelable {
    @PrimaryKey
    private int id;
    private String tvShowsTitle;
    private String tvShowsYear;
    private String tvShowsImdbScore;
    private String tvShowsDescription;
    private String tvShowsVoteCount;
    private String tvShowsPoster;
    private String tvShowsOriginalLanguage;
    private String genre;

    public TVShowsFavorite(Cursor cursor) {
        this.id = getColumnInt(cursor, DatabaseContractTV.TVShowsColumns._ID);
        this.tvShowsTitle = getColumnString(cursor, DatabaseContractTV.TVShowsColumns.TV_SHOWS_TITLE);
        this.tvShowsYear = getColumnString(cursor, DatabaseContractTV.TVShowsColumns.TV_SHOWS_YEAR);
        this.tvShowsImdbScore = getColumnString(cursor, DatabaseContractTV.TVShowsColumns.TV_SHOWS_SCORE);
        this.tvShowsDescription = getColumnString(cursor, DatabaseContractTV.TVShowsColumns.TV_SHOWS_DESCRIPTION);
        this.tvShowsVoteCount = getColumnString(cursor, DatabaseContractTV.TVShowsColumns.TV_SHOWS_VOTE_COUNT);
        this.tvShowsPoster = getColumnString(cursor, DatabaseContractTV.TVShowsColumns.TV_SHOWS_POSTER);
        this.tvShowsOriginalLanguage = getColumnString(cursor, DatabaseContractTV.TVShowsColumns.TV_SHOWS_LANGUAGE);
        this.genre = getColumnString(cursor, DatabaseContractTV.TVShowsColumns.TV_SHOWS_GENRE);
    }

    public TVShowsFavorite() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
        dest.writeString(this.tvShowsTitle);
        dest.writeString(this.tvShowsYear);
        dest.writeString(this.tvShowsImdbScore);
        dest.writeString(this.tvShowsDescription);
        dest.writeString(this.tvShowsVoteCount);
        dest.writeString(this.tvShowsPoster);
        dest.writeString(this.tvShowsOriginalLanguage);
        dest.writeString(this.genre);
    }

    protected TVShowsFavorite(Parcel in) {
        this.id = in.readInt();
        this.tvShowsTitle = in.readString();
        this.tvShowsYear = in.readString();
        this.tvShowsImdbScore = in.readString();
        this.tvShowsDescription = in.readString();
        this.tvShowsVoteCount = in.readString();
        this.tvShowsPoster = in.readString();
        this.tvShowsOriginalLanguage = in.readString();
        this.genre = in.readString();
    }

    public static final Creator<TVShowsFavorite> CREATOR = new Creator<TVShowsFavorite>() {
        @Override
        public TVShowsFavorite createFromParcel(Parcel source) {
            return new TVShowsFavorite(source);
        }

        @Override
        public TVShowsFavorite[] newArray(int size) {
            return new TVShowsFavorite[size];
        }
    };
}
