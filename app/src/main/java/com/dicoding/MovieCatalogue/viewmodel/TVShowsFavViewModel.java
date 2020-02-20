package com.dicoding.MovieCatalogue.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.dicoding.MovieCatalogue.model.TVShowsFavorite;

import java.util.List;

public class TVShowsFavViewModel extends AndroidViewModel {
    private LiveData<List<TVShowsFavorite>> tvShowsList;

    public TVShowsFavViewModel(@NonNull Application application) {
        super(application);
//        TVShowsRepository tvShowsRepository = new TVShowsRepository(application);
//        tvShowsList = tvShowsRepository.getTVShows();
    }

    public LiveData<List<TVShowsFavorite>> getTVShows() {
        return tvShowsList;
    }
}
