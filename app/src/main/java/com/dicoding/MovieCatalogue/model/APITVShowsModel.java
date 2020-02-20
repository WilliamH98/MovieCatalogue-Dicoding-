package com.dicoding.MovieCatalogue.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class APITVShowsModel {
    @SerializedName("results")
    private ArrayList<TVShows> showsList;

    public void setShowsList(ArrayList<TVShows> movieList) {
        this.showsList = movieList;
    }

    public ArrayList<TVShows> getShowsList() {
        return showsList;
    }
}
