package com.dicoding.MovieCatalogue.db;

import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

public class DatabaseContract {
    public static final String TABLE_MOVIES = "movies";

    public static final class MovieColumns implements BaseColumns {
        public static final String _ID = "id";
        public static final String MOVIE_TITLE = "movieTitle";
        public static final String MOVIE_YEAR = "movieYear";
        public static final String MOVIE_SCORE = "movieImdbScore";
        public static final String MOVIE_DESCRIPTION = "movieDescription";
        public static final String MOVIE_VOTE_COUNT = "movieVoteCount";
        public static final String MOVIE_POSTER = "moviePoster";
        public static final String MOVIE_LANGUAGE = "movieOriginalLanguage";
        public static final String MOVIE_GENRE = "genre";
    }

    public static final String CONTENT_AUTHORITY = "com.dicoding.MovieCatalogue.Movies";

    public static final String DEFAULT_SORT = String.format("%s ASC, %s DESC, %s ASC",
            MovieColumns._ID,
            MovieColumns.MOVIE_TITLE,
            MovieColumns.MOVIE_YEAR);

    public static final Uri CONTENT_URI = new Uri.Builder().scheme("content")
            .authority(CONTENT_AUTHORITY)
            .appendPath(TABLE_MOVIES)
            .build();

    public static String getColumnString(Cursor cursor, String columnName) {
        return cursor.getString(cursor.getColumnIndex(columnName));
    }

    public static int getColumnInt(Cursor cursor, String columnName) {
        return cursor.getInt(cursor.getColumnIndex(columnName));
    }
}
