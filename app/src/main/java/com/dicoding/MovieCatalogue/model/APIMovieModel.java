package com.dicoding.MovieCatalogue.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class APIMovieModel {
    @SerializedName("results")
    private ArrayList<Movies> movieList;

    public void setMovieList(ArrayList<Movies> movieList) {
        this.movieList = movieList;
    }

    public ArrayList<Movies> getMovieList() {
        return movieList;
    }
}
