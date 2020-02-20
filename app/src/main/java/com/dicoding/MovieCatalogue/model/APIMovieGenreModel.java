package com.dicoding.MovieCatalogue.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class APIMovieGenreModel {
    @SerializedName("genres")
    private ArrayList<MoviesGenre> genreList;

    public void setGenreList(ArrayList<MoviesGenre> genreList) {
        this.genreList = genreList;
    }

    public ArrayList<MoviesGenre> getGenreList() {
        return genreList;
    }
}
