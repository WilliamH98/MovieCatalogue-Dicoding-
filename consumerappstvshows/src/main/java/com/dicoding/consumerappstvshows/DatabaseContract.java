package com.dicoding.consumerappstvshows;

import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

public class DatabaseContract {
    private static final String AUTHORITY = "com.dicoding.MovieCatalogue.TVShows";
    private static final String SCHEME = "content";

    public static final class ShowsColumn implements BaseColumns {
        private static final String TABLE_NAME = "tvshows";
        public static final String TITLE = "tvShowsTitle";
        public static final String POSTER = "tvShowsPoster";
        public static final String DATE = "tvShowsYear";
        public static final String SCORE = "tvShowsImdbScore";
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
