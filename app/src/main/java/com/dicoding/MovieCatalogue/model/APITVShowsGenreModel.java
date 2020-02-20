package com.dicoding.MovieCatalogue.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class APITVShowsGenreModel {
    @SerializedName("genres")
    private ArrayList<TVShowsGenre> genreList;

    public void setGenreList(ArrayList<TVShowsGenre> genreList) {
        this.genreList = genreList;
    }

    public ArrayList<TVShowsGenre> getGenreList() {
        return genreList;
    }
}
