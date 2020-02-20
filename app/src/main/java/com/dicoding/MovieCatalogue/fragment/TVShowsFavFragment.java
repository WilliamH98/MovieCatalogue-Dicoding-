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
import com.dicoding.MovieCatalogue.activity.TVShowsDetailsActivity;
import com.dicoding.MovieCatalogue.adapter.TVShowsFavAdapter;
import com.dicoding.MovieCatalogue.db.DatabaseContractTV;
import com.dicoding.MovieCatalogue.db.QueryCursorLoader;
import com.dicoding.MovieCatalogue.model.TVShows;
import com.dicoding.MovieCatalogue.model.TVShowsFavorite;

public class TVShowsFavFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, TVShowsFavAdapter.OnItemClickListener {
    private TVShowsFavAdapter mTVShowsFavAdapter;
    private static final int ID_LOADER = 30;
    private LoaderManager loaderManager;
    private Loader<Cursor> asyncTaskLoader;

    private String URI_EXTRA_TV;
    private String SORT_EXTRA;
    private String sortOrder;

    public TVShowsFavFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tvshows_fav, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView mRVTVShowsFav = view.findViewById(R.id.rv_tv_shows_fav);
        URI_EXTRA_TV = "Details Intent TV";
        SORT_EXTRA = "Sort Extra";
        sortOrder = "default";

        loaderManager = LoaderManager.getInstance(this);
        loaderManager.initLoader(0x01, null, this);
        mTVShowsFavAdapter = new TVShowsFavAdapter(getContext());
        mTVShowsFavAdapter.setOnItemClickListener(this);
        mTVShowsFavAdapter.notifyDataSetChanged();
        mRVTVShowsFav.setHasFixedSize(true);
        mRVTVShowsFav.setLayoutManager(new LinearLayoutManager(getContext()));
        mRVTVShowsFav.setAdapter(mTVShowsFavAdapter);

        Bundle bundle = new Bundle();
        bundle.putString(SORT_EXTRA, sortOrder);

        asyncTaskLoader = loaderManager.getLoader(ID_LOADER);
        if (asyncTaskLoader == null) {
            mTVShowsFavAdapter.swapCursor(null);
            loaderManager.initLoader(ID_LOADER, bundle, this);
        } else {
            mTVShowsFavAdapter.swapCursor(null);
            loaderManager.restartLoader(ID_LOADER, bundle, this);
        }
    }

    @Override
    public void onItemClick(View v, int position) {
        TVShowsFavorite tvShowsFavorite = mTVShowsFavAdapter.getItem(position);
        TVShows tvShows = new TVShows(
                tvShowsFavorite.getId(),
                tvShowsFavorite.getTvShowsTitle(),
                tvShowsFavorite.getTvShowsYear(),
                tvShowsFavorite.getTvShowsImdbScore(),
                tvShowsFavorite.getTvShowsDescription(),
                tvShowsFavorite.getTvShowsVoteCount(),
                tvShowsFavorite.getTvShowsPoster(),
                tvShowsFavorite.getTvShowsOriginalLanguage()
        );
        Intent intent = new Intent(getContext(), TVShowsDetailsActivity.class);
        intent.putExtra(URI_EXTRA_TV, tvShows);
        startActivity(intent);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new QueryCursorLoader(getActivity(), DatabaseContractTV.CONTENT_URI, DatabaseContractTV.DEFAULT_SORT_TV);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        mTVShowsFavAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        mTVShowsFavAdapter.swapCursor(null);
    }

    public void onResume() {
        super.onResume();
        loaderManager.restartLoader(0x01, null, this);
    }
}
