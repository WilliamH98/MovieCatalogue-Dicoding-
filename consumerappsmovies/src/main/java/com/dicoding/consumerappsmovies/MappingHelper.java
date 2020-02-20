package com.dicoding.consumerappsmovies;

import android.database.Cursor;

import java.util.ArrayList;

import static com.dicoding.consumerappsmovies.DatabaseContract.MovieColumns.DATE;
import static com.dicoding.consumerappsmovies.DatabaseContract.MovieColumns.POSTER;
import static com.dicoding.consumerappsmovies.DatabaseContract.MovieColumns.SCORE;
import static com.dicoding.consumerappsmovies.DatabaseContract.MovieColumns.TITLE;

public class MappingHelper {
    public static ArrayList<Movies> mapCursorToArrayList(Cursor moviesCursor) {
        ArrayList<Movies> movieList = new ArrayList<>();
        while (moviesCursor.moveToNext()) {
            String title = moviesCursor.getString(moviesCursor.getColumnIndexOrThrow(TITLE));
            String score = moviesCursor.getString(moviesCursor.getColumnIndexOrThrow(SCORE));
            String poster = moviesCursor.getString(moviesCursor.getColumnIndexOrThrow(POSTER));
            String date = moviesCursor.getString(moviesCursor.getColumnIndexOrThrow(DATE));
            movieList.add(new Movies(title, score, poster, date));
        }

        return movieList;
    }
}
