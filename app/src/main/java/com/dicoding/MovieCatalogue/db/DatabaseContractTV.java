package com.dicoding.MovieCatalogue.db;

import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

public class DatabaseContractTV {
    public static final String TABLE_TVSHOWS = "tvshows";

    public static final class TVShowsColumns implements BaseColumns {
        public static final String _ID = "id";
        public static final String TV_SHOWS_TITLE = "tvShowsTitle";
        public static final String TV_SHOWS_YEAR = "tvShowsYear";
        public static final String TV_SHOWS_SCORE = "tvShowsImdbScore";
        public static final String TV_SHOWS_DESCRIPTION = "tvShowsDescription";
        public static final String TV_SHOWS_VOTE_COUNT = "tvShowsVoteCount";
        public static final String TV_SHOWS_POSTER = "tvShowsPoster";
        public static final String TV_SHOWS_LANGUAGE = "tvShowsOriginalLanguage";
        public static final String TV_SHOWS_GENRE = "genre";
    }

    public static final String CONTENT_AUTHORITY = "com.dicoding.MovieCatalogue.TVShows";

    public static final String DEFAULT_SORT_TV = String.format("%s ASC, %s DESC, %s ASC",
            TVShowsColumns._ID,
            TVShowsColumns.TV_SHOWS_TITLE,
            TVShowsColumns.TV_SHOWS_YEAR);

    public static final Uri CONTENT_URI = new Uri.Builder().scheme("content")
            .authority(CONTENT_AUTHORITY)
            .appendPath(TABLE_TVSHOWS)
            .build();

    public static String getColumnString(Cursor cursor, String columnName) {
        return cursor.getString(cursor.getColumnIndex(columnName));
    }

    public static int getColumnInt(Cursor cursor, String columnName) {
        return cursor.getInt(cursor.getColumnIndex(columnName));
    }
}
