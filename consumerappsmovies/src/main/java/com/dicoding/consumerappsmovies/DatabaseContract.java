package com.dicoding.consumerappsmovies;

import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

public class DatabaseContract {
    private static final String AUTHORITY = "com.dicoding.MovieCatalogue.Movies";
    private static final String SCHEME = "content";

    public static final class MovieColumns implements BaseColumns {
        private static final String TABLE_NAME = "movies";
        public static final String TITLE = "movieTitle";
        public static final String POSTER = "moviePoster";
        public static final String DATE = "movieYear";
        public static final String SCORE = "movieImdbScore";
        public static final Uri CONTENT_URI = new Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_NAME)
                .build();
    }

    public static String getColumnString(Cursor cursor, String columnName) {
        return cursor.getString(cursor.getColumnIndex(columnName));
    }

    public static int getColumnInt(Cursor cursor, String columnName) {
        return cursor.getInt(cursor.getColumnIndex(columnName));
    }

    public static long getColumnLong(Cursor cursor, String columnName) {
        return cursor.getLong(cursor.getColumnIndex(columnName));
    }
}
