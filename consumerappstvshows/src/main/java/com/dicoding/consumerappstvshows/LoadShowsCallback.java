package com.dicoding.consumerappstvshows;

import android.database.Cursor;

public interface LoadShowsCallback {
    void postExecute(Cursor tvShows);
}
