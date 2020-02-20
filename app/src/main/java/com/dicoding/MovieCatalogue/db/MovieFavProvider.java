package com.dicoding.MovieCatalogue.db;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.dicoding.MovieCatalogue.model.MoviesFavorite;

import java.util.Objects;

import io.realm.DynamicRealm;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmMigration;
import io.realm.RealmResults;
import io.realm.RealmSchema;

public class MovieFavProvider extends ContentProvider {
    private static final int MOVIES = 100;
    private static final int MOVIES_WITH_ID = 101;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(DatabaseContract.CONTENT_AUTHORITY,
                DatabaseContract.TABLE_MOVIES,
                MOVIES);

        sUriMatcher.addURI(DatabaseContract.CONTENT_AUTHORITY,
                DatabaseContract.TABLE_MOVIES + "/#",
                MOVIES_WITH_ID);
    }

    @Override
    public boolean onCreate() {
        Realm.init(Objects.requireNonNull(getContext()));
        RealmConfiguration config = new RealmConfiguration.Builder()
                .schemaVersion(1)
                .migration(new MyRealmMigrationMovies())
                .build();
        Realm.setDefaultConfiguration(config);

        return true;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {

        int match = sUriMatcher.match(uri);

        Realm realm = Realm.getDefaultInstance();
        MatrixCursor myCursor = new MatrixCursor(new String[]{DatabaseContract.MovieColumns._ID,
                DatabaseContract.MovieColumns.MOVIE_TITLE,
                DatabaseContract.MovieColumns.MOVIE_YEAR,
                DatabaseContract.MovieColumns.MOVIE_SCORE,
                DatabaseContract.MovieColumns.MOVIE_DESCRIPTION,
                DatabaseContract.MovieColumns.MOVIE_VOTE_COUNT,
                DatabaseContract.MovieColumns.MOVIE_POSTER,
                DatabaseContract.MovieColumns.MOVIE_LANGUAGE,
                DatabaseContract.MovieColumns.MOVIE_GENRE
        });

        try {
            switch (match) {
                case MOVIES:
                    RealmResults<MoviesFavorite> moviesFavoritesRealm = realm.where(MoviesFavorite.class).findAll();
                    for (MoviesFavorite moviesFavorite : moviesFavoritesRealm) {
                        Object[] rowData = new Object[]{moviesFavorite.getId(),
                                moviesFavorite.getMovieTitle(),
                                moviesFavorite.getMovieYear(),
                                moviesFavorite.getMovieImdbScore(),
                                moviesFavorite.getMovieDescription(),
                                moviesFavorite.getMovieVoteCount(),
                                moviesFavorite.getMoviePoster(),
                                moviesFavorite.getMovieOriginalLanguage(),
                                moviesFavorite.getGenre()
                        };
                        myCursor.addRow(rowData);
                        Log.v("RealmDB", moviesFavorite.toString());
                    }
                    break;

                case MOVIES_WITH_ID:
                    Integer id = Integer.parseInt(uri.getPathSegments().get(1));
                    MoviesFavorite moviesFavoriteRealm = realm.where(MoviesFavorite.class).equalTo("id", id).findFirst();
                    assert moviesFavoriteRealm != null;
                    myCursor.addRow(new Object[]{moviesFavoriteRealm.getId(),
                            moviesFavoriteRealm.getMovieTitle(),
                            moviesFavoriteRealm.getMovieYear(),
                            moviesFavoriteRealm.getMovieImdbScore(),
                            moviesFavoriteRealm.getMovieDescription(),
                            moviesFavoriteRealm.getMovieVoteCount(),
                            moviesFavoriteRealm.getMoviePoster(),
                            moviesFavoriteRealm.getMovieOriginalLanguage(),
                            moviesFavoriteRealm.getGenre()
                    });
                    Log.v("RealmDB", moviesFavoriteRealm.toString());
                    break;

                default:
                    throw new UnsupportedOperationException("Unknown uri: " + uri);
            }

            myCursor.setNotificationUri(Objects.requireNonNull(getContext()).getContentResolver(), uri);
        } finally {
            realm.close();
        }
        return myCursor;

    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, final ContentValues contentValues) {
        int match = sUriMatcher.match(uri);
        Uri returnUri;

        try (Realm realm = Realm.getDefaultInstance()) {
            if (match == MOVIES) {
                realm.executeTransaction(realm1 -> {
                    Number currId = realm1.where(MoviesFavorite.class).max(DatabaseContract.MovieColumns._ID);
                    Integer nextId = (currId == null) ? 1 : currId.intValue() + 1;

                    MoviesFavorite moviesFavoriteNewRealm = realm1.createObject(MoviesFavorite.class, nextId);
                    moviesFavoriteNewRealm.setMovieTitle(contentValues.get(DatabaseContract.MovieColumns.MOVIE_TITLE).toString());
                    moviesFavoriteNewRealm.setMovieYear(contentValues.get(DatabaseContract.MovieColumns.MOVIE_YEAR).toString());
                    moviesFavoriteNewRealm.setMovieImdbScore(contentValues.get(DatabaseContract.MovieColumns.MOVIE_SCORE).toString());
                    moviesFavoriteNewRealm.setMovieDescription(contentValues.get(DatabaseContract.MovieColumns.MOVIE_DESCRIPTION).toString());
                    moviesFavoriteNewRealm.setMovieVoteCount(contentValues.get(DatabaseContract.MovieColumns.MOVIE_VOTE_COUNT).toString());
                    moviesFavoriteNewRealm.setMoviePoster(contentValues.get(DatabaseContract.MovieColumns.MOVIE_POSTER).toString());
                    moviesFavoriteNewRealm.setMovieOriginalLanguage(contentValues.get(DatabaseContract.MovieColumns.MOVIE_LANGUAGE).toString());
                    moviesFavoriteNewRealm.setGenre(contentValues.get(DatabaseContract.MovieColumns.MOVIE_GENRE).toString());
                });
                returnUri = ContentUris.withAppendedId(DatabaseContract.CONTENT_URI, '1');
            } else {
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }

            Objects.requireNonNull(getContext()).getContentResolver().notifyChange(uri, null);
        }
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        int count = 0;
        try (Realm realm = Realm.getDefaultInstance()) {
            switch (sUriMatcher.match(uri)) {
                case MOVIES:
                    selection = (selection == null) ? "1" : selection;
                    RealmResults<MoviesFavorite> moviesFavoritesRealm = realm.where(MoviesFavorite.class).equalTo(selection, Integer.parseInt(selectionArgs[0])).findAll();
                    realm.beginTransaction();
                    moviesFavoritesRealm.deleteAllFromRealm();
                    count++;
                    realm.commitTransaction();
                    break;
                case MOVIES_WITH_ID:
                    Integer id = Integer.parseInt(String.valueOf(ContentUris.parseId(uri)));
                    MoviesFavorite moviesFavoritesRealmId = realm.where(MoviesFavorite.class).equalTo("id", id).findFirst();
                    realm.beginTransaction();
                    assert moviesFavoritesRealmId != null;
                    moviesFavoritesRealmId.deleteFromRealm();
                    count++;
                    realm.commitTransaction();
                    break;
                default:
                    throw new IllegalArgumentException("Illegal delete URI");
            }
        }
        if (count > 0) {
            Objects.requireNonNull(getContext()).getContentResolver().notifyChange(uri, null);
        }

        return count;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}

class MyRealmMigrationMovies implements RealmMigration {
    @Override
    public void migrate(@NonNull DynamicRealm realm, long oldVersion, long newVersion) {

        RealmSchema schema = realm.getSchema();

        if (oldVersion != 0) {
            schema.create(DatabaseContract.TABLE_MOVIES)
                    .addField(DatabaseContract.MovieColumns._ID, Integer.class)
                    .addField(DatabaseContract.MovieColumns.MOVIE_TITLE, String.class)
                    .addField(DatabaseContract.MovieColumns.MOVIE_YEAR, String.class)
                    .addField(DatabaseContract.MovieColumns.MOVIE_SCORE, String.class)
                    .addField(DatabaseContract.MovieColumns.MOVIE_DESCRIPTION, String.class)
                    .addField(DatabaseContract.MovieColumns.MOVIE_VOTE_COUNT, String.class)
                    .addField(DatabaseContract.MovieColumns.MOVIE_POSTER, String.class)
                    .addField(DatabaseContract.MovieColumns.MOVIE_LANGUAGE, String.class)
                    .addField(DatabaseContract.MovieColumns.MOVIE_GENRE, String.class);
            oldVersion += 1;
        }

    }
}
