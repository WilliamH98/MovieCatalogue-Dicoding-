package com.dicoding.MovieCatalogue.fragment;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dicoding.MovieCatalogue.R;
import com.dicoding.MovieCatalogue.activity.MovieDetailsActivity;
import com.dicoding.MovieCatalogue.adapter.MoviesFavAdapter;
import com.dicoding.MovieCatalogue.db.DatabaseContract;
import com.dicoding.MovieCatalogue.db.QueryCursorLoader;
import com.dicoding.MovieCatalogue.model.Movies;
import com.dicoding.MovieCatalogue.model.MoviesFavorite;

public class MovieFavFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, MoviesFavAdapter.OnItemClickListener {
    private MoviesFavAdapter mMoviesFavAdapter;
    private static final int ID_LOADER = 30;
    private LoaderManager loaderManager;
    private Loader<Cursor> asyncTaskLoader;

    private String URI_EXTRA;
    private String SORT_EXTRA;
    private String sortOrder;

    public MovieFavFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_movie_fav, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView mRVMovieFav = view.findViewById(R.id.rv_movies_fav);
        URI_EXTRA = "Details Intent";
        SORT_EXTRA = "Sort Extra";
        sortOrder = "default";

        loaderManager = LoaderManager.getInstance(this);
        loaderManager.initLoader(0x01, null, this);
        mMoviesFavAdapter = new MoviesFavAdapter(getContext());
        mMoviesFavAdapter.setOnItemClickListener(this);
        mMoviesFavAdapter.notifyDataSetChanged();
        mRVMovieFav.setHasFixedSize(true);
        mRVMovieFav.setLayoutManager(new LinearLayoutManager(getContext()));
        mRVMovieFav.setAdapter(mMoviesFavAdapter);

        Bundle bundle = new Bundle();
        bundle.putString(SORT_EXTRA, sortOrder);

        asyncTaskLoader = loaderManager.getLoader(ID_LOADER);
        if (asyncTaskLoader == null) {
            mMoviesFavAdapter.swapCursor(null);
            loaderManager.initLoader(ID_LOADER, bundle, this);
        } else {
            mMoviesFavAdapter.swapCursor(null);
            loaderManager.restartLoader(ID_LOADER, bundle, this);
        }
    }

    @Override
    public void onItemClick(View v, int position) {
        MoviesFavorite moviesFavorite = mMoviesFavAdapter.getItem(position);
        Movies movies = new Movies(
                moviesFavorite.getId(),
                moviesFavorite.getMovieTitle(),
                moviesFavorite.getMovieYear(),
                moviesFavorite.getMovieImdbScore(),
                moviesFavorite.getMovieDescription(),
                moviesFavorite.getMovieVoteCount(),
                moviesFavorite.getMoviePoster(),
                moviesFavorite.getMovieOriginalLanguage()
        );
        Intent intent = new Intent(getContext(), MovieDetailsActivity.class);
        intent.putExtra(URI_EXTRA, movies);
        startActivity(intent);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new QueryCursorLoader(getActivity(), DatabaseContract.CONTENT_URI, DatabaseContract.DEFAULT_SORT);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        mMoviesFavAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        mMoviesFavAdapter.swapCursor(null);
    }

    public void onResume() {
        super.onResume();
        loaderManager.restartLoader(0x01, null, this);
    }
}
