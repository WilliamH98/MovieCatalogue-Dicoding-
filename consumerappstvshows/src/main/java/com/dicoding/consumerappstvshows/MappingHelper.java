package com.dicoding.consumerappstvshows;

import android.database.Cursor;

import java.util.ArrayList;

import static com.dicoding.consumerappstvshows.DatabaseContract.ShowsColumn.DATE;
import static com.dicoding.consumerappstvshows.DatabaseContract.ShowsColumn.POSTER;
import static com.dicoding.consumerappstvshows.DatabaseContract.ShowsColumn.SCORE;
import static com.dicoding.consumerappstvshows.DatabaseContract.ShowsColumn.TITLE;

public class MappingHelper {
    public static ArrayList<TVShows> mapCursorToArrayList(Cursor showsCursor) {
        ArrayList<TVShows> showsList = new ArrayList<>();
        while (showsCursor.moveToNext()) {
            String title = showsCursor.getString(showsCursor.getColumnIndexOrThrow(TITLE));
            String score = showsCursor.getString(showsCursor.getColumnIndexOrThrow(SCORE));
            String poster = showsCursor.getString(showsCursor.getColumnIndexOrThrow(POSTER));
            String date = showsCursor.getString(showsCursor.getColumnIndexOrThrow(DATE));
            showsList.add(new TVShows(title, score, poster, date));
        }

        return showsList;
    }
}
