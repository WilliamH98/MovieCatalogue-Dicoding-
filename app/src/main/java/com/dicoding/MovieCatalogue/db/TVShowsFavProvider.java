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

import com.dicoding.MovieCatalogue.model.TVShowsFavorite;

import java.util.Objects;

import io.realm.DynamicRealm;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmMigration;
import io.realm.RealmResults;
import io.realm.RealmSchema;

public class TVShowsFavProvider extends ContentProvider {
    private static final int TV_SHOWS = 200;
    private static final int TV_SHOWS_WITH_ID = 201;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(DatabaseContractTV.CONTENT_AUTHORITY,
                DatabaseContractTV.TABLE_TVSHOWS,
                TV_SHOWS);

        sUriMatcher.addURI(DatabaseContractTV.CONTENT_AUTHORITY,
                DatabaseContractTV.TABLE_TVSHOWS + "/#",
                TV_SHOWS_WITH_ID);
    }

    @Override
    public boolean onCreate() {
        Realm.init(Objects.requireNonNull(getContext()));
        RealmConfiguration config = new RealmConfiguration.Builder()
                .schemaVersion(1)
                .migration(new MyRealmMigrationTVShows())
                .build();
        Realm.setDefaultConfiguration(config);

        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        int match = sUriMatcher.match(uri);

        Realm realm = Realm.getDefaultInstance();
        MatrixCursor myCursor = new MatrixCursor(new String[]{DatabaseContractTV.TVShowsColumns._ID,
                DatabaseContractTV.TVShowsColumns.TV_SHOWS_TITLE,
                DatabaseContractTV.TVShowsColumns.TV_SHOWS_YEAR,
                DatabaseContractTV.TVShowsColumns.TV_SHOWS_SCORE,
                DatabaseContractTV.TVShowsColumns.TV_SHOWS_DESCRIPTION,
                DatabaseContractTV.TVShowsColumns.TV_SHOWS_VOTE_COUNT,
                DatabaseContractTV.TVShowsColumns.TV_SHOWS_POSTER,
                DatabaseContractTV.TVShowsColumns.TV_SHOWS_LANGUAGE,
                DatabaseContractTV.TVShowsColumns.TV_SHOWS_GENRE
        });

        try {
            switch (match) {
                case TV_SHOWS:
                    RealmResults<TVShowsFavorite> tvShowsFavoritesRealm = realm.where(TVShowsFavorite.class).findAll();
                    for (TVShowsFavorite tvShowsFavorite : tvShowsFavoritesRealm) {
                        Object[] rowData = new Object[]{tvShowsFavorite.getId(),
                                tvShowsFavorite.getTvShowsTitle(),
                                tvShowsFavorite.getTvShowsYear(),
                                tvShowsFavorite.getTvShowsImdbScore(),
                                tvShowsFavorite.getTvShowsDescription(),
                                tvShowsFavorite.getTvShowsVoteCount(),
                                tvShowsFavorite.getTvShowsPoster(),
                                tvShowsFavorite.getTvShowsOriginalLanguage(),
                                tvShowsFavorite.getGenre()
                        };
                        myCursor.addRow(rowData);
                        Log.v("RealmDB", tvShowsFavorite.toString());
                    }
                    break;

                case TV_SHOWS_WITH_ID:
                    Integer id = Integer.parseInt(uri.getPathSegments().get(1));
                    TVShowsFavorite tvShowsFavoriteRealm = realm.where(TVShowsFavorite.class).equalTo("id", id).findFirst();
                    assert tvShowsFavoriteRealm != null;
                    myCursor.addRow(new Object[]{tvShowsFavoriteRealm.getId(),
                            tvShowsFavoriteRealm.getTvShowsTitle(),
                            tvShowsFavoriteRealm.getTvShowsYear(),
                            tvShowsFavoriteRealm.getTvShowsImdbScore(),
                            tvShowsFavoriteRealm.getTvShowsDescription(),
                            tvShowsFavoriteRealm.getTvShowsVoteCount(),
                            tvShowsFavoriteRealm.getTvShowsPoster(),
                            tvShowsFavoriteRealm.getTvShowsOriginalLanguage(),
                            tvShowsFavoriteRealm.getGenre()
                    });
                    Log.v("RealmDB", tvShowsFavoriteRealm.toString());
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
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        int match = sUriMatcher.match(uri);
        Uri returnUri;

        try (Realm realm = Realm.getDefaultInstance()) {
            if (match == TV_SHOWS) {
                realm.executeTransaction(realm1 -> {
                    Number currId = realm1.where(TVShowsFavorite.class).max(DatabaseContractTV.TVShowsColumns._ID);
                    Integer nextId = (currId == null) ? 1 : currId.intValue() + 1;

                    TVShowsFavorite tvShowsFavoriteNewRealm = realm1.createObject(TVShowsFavorite.class, nextId);
                    assert values != null;
                    tvShowsFavoriteNewRealm.setTvShowsTitle(values.get(DatabaseContractTV.TVShowsColumns.TV_SHOWS_TITLE).toString());
                    tvShowsFavoriteNewRealm.setTvShowsYear(values.get(DatabaseContractTV.TVShowsColumns.TV_SHOWS_YEAR).toString());
                    tvShowsFavoriteNewRealm.setTvShowsImdbScore(values.get(DatabaseContractTV.TVShowsColumns.TV_SHOWS_SCORE).toString());
                    tvShowsFavoriteNewRealm.setTvShowsDescription(values.get(DatabaseContractTV.TVShowsColumns.TV_SHOWS_DESCRIPTION).toString());
                    tvShowsFavoriteNewRealm.setTvShowsVoteCount(values.get(DatabaseContractTV.TVShowsColumns.TV_SHOWS_VOTE_COUNT).toString());
                    tvShowsFavoriteNewRealm.setTvShowsPoster(values.get(DatabaseContractTV.TVShowsColumns.TV_SHOWS_POSTER).toString());
                    tvShowsFavoriteNewRealm.setTvShowsOriginalLanguage(values.get(DatabaseContractTV.TVShowsColumns.TV_SHOWS_LANGUAGE).toString());
                    tvShowsFavoriteNewRealm.setGenre(values.get(DatabaseContractTV.TVShowsColumns.TV_SHOWS_GENRE).toString());
                });
                returnUri = ContentUris.withAppendedId(DatabaseContractTV.CONTENT_URI, '1');
            } else {
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }

            Objects.requireNonNull(getContext()).getContentResolver().notifyChange(uri, null);
        }
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int count = 0;
        try (Realm realm = Realm.getDefaultInstance()) {
            switch (sUriMatcher.match(uri)) {
                case TV_SHOWS:
                    selection = (selection == null) ? "1" : selection;
                    assert selectionArgs != null;
                    RealmResults<TVShowsFavorite> tvShowsFavoritesRealm = realm.where(TVShowsFavorite.class).equalTo(selection, Integer.parseInt(selectionArgs[0])).findAll();
                    realm.beginTransaction();
                    tvShowsFavoritesRealm.deleteAllFromRealm();
                    count++;
                    realm.commitTransaction();
                    break;
                case TV_SHOWS_WITH_ID:
                    Integer id = Integer.parseInt(String.valueOf(ContentUris.parseId(uri)));
                    TVShowsFavorite tvShowsFavoriteRealmId = realm.where(TVShowsFavorite.class).equalTo("id", id).findFirst();
                    realm.beginTransaction();
                    assert tvShowsFavoriteRealmId != null;
                    tvShowsFavoriteRealmId.deleteFromRealm();
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

class MyRealmMigrationTVShows implements RealmMigration {
    @Override
    public void migrate(@NonNull DynamicRealm realm, long oldVersion, long newVersion) {

        RealmSchema schema = realm.getSchema();

        if (oldVersion != 0) {
            schema.create(DatabaseContractTV.TABLE_TVSHOWS)
                    .addField(DatabaseContractTV.TVShowsColumns._ID, Integer.class)
                    .addField(DatabaseContractTV.TVShowsColumns.TV_SHOWS_TITLE, String.class)
                    .addField(DatabaseContractTV.TVShowsColumns.TV_SHOWS_YEAR, String.class)
                    .addField(DatabaseContractTV.TVShowsColumns.TV_SHOWS_SCORE, String.class)
                    .addField(DatabaseContractTV.TVShowsColumns.TV_SHOWS_DESCRIPTION, String.class)
                    .addField(DatabaseContractTV.TVShowsColumns.TV_SHOWS_VOTE_COUNT, String.class)
                    .addField(DatabaseContractTV.TVShowsColumns.TV_SHOWS_POSTER, String.class)
                    .addField(DatabaseContractTV.TVShowsColumns.TV_SHOWS_LANGUAGE, String.class)
                    .addField(DatabaseContractTV.TVShowsColumns.TV_SHOWS_GENRE, String.class);
            oldVersion += 1;
        }

    }
}
