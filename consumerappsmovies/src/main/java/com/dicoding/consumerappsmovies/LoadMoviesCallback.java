package com.dicoding.consumerappsmovies;

import android.database.Cursor;

public interface LoadMoviesCallback {
    void postExecute(Cursor movies);
}
