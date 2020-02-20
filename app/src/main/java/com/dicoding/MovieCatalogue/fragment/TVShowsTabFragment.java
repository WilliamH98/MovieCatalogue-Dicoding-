package com.dicoding.MovieCatalogue.fragment;

import android.app.SearchManager;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dicoding.MovieCatalogue.R;
import com.dicoding.MovieCatalogue.activity.SettingsActivity;
import com.dicoding.MovieCatalogue.adapter.TVShowsAdapter;
import com.dicoding.MovieCatalogue.model.TVShows;
import com.dicoding.MovieCatalogue.model.TVShowsGenre;
import com.dicoding.MovieCatalogue.viewmodel.TVShowsGenreViewModel;
import com.dicoding.MovieCatalogue.viewmodel.TVShowsSearchViewModel;
import com.dicoding.MovieCatalogue.viewmodel.TVShowsViewModel;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

public class TVShowsTabFragment extends Fragment {
    TextView mShowsTitle, mShowsYear, mShowsImdbScore;
    Button mBtnMoreDetails, mBtnRefresh;
    ImageView mShowsPoster;
    TVShowsAdapter mShowsAdapter;
    RecyclerView mRvShows;
    ProgressBar mProgressBar;
    TVShowsViewModel mTvShowsViewModel;
    private TVShowsSearchViewModel mTVShowsSearchViewModel;
    private TVShowsGenreViewModel mTVShowsGenreViewModel;
    private LinearLayout mLLNoConnection;
    private Locale mCurrent;
    private int mConnection;
    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.6F);
    public static ArrayList<TVShowsGenre> genresList = new ArrayList<>();
    private String mSearchQuery;
    private static final String SEARCH_KEY = "Search Query";
    private static final String SEARCH_FLAG = "Search View Expand";
    private static boolean isSearch = false;
    private MenuItem searchItem, changeLanguageItem;

    public TVShowsTabFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.activity_tv_shows, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Objects.requireNonNull(getActivity()).setTitle(R.string.tv_shows);

        mShowsTitle = view.findViewById(R.id.tv_shows_title);
        mShowsYear = view.findViewById(R.id.tv_shows_year);
        mShowsImdbScore = view.findViewById(R.id.tv_shows_score);
        mBtnMoreDetails = view.findViewById(R.id.btn_shows_more_details);
        mShowsPoster = view.findViewById(R.id.iv_shows_poster);
        mRvShows = view.findViewById(R.id.rv_tv_shows);
        mProgressBar = view.findViewById(R.id.progress_bar);
        mProgressBar.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.yellow), PorterDuff.Mode.MULTIPLY);
        mLLNoConnection = view.findViewById(R.id.ll_no_connection);
        mBtnRefresh = view.findViewById(R.id.btn_refresh);

        if (savedInstanceState != null) {
            mSearchQuery = savedInstanceState.getString(SEARCH_KEY);
            isSearch = savedInstanceState.getBoolean(SEARCH_FLAG);
        }

        if (isNetworkAvailable()) {
            mConnection = 1;
        } else {
            mConnection = 0;
        }

        mBtnRefresh.setOnClickListener(v -> {
            v.startAnimation(buttonClick);

            if (isNetworkAvailable()) {
                mConnection = 1;
            } else {
                mConnection = 0;
            }

            initData();
        });

        initData();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu, menu);
        searchItem = menu.findItem(R.id.action_search);
        changeLanguageItem = menu.findItem(R.id.action_change_settings);
        SearchManager searchManager = (SearchManager) Objects.requireNonNull(getActivity()).getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) searchItem.getActionView();

        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
            searchView.setMaxWidth(Integer.MAX_VALUE);
            searchView.setQueryHint(getResources().getString(R.string.search_tv_shows));

            if (isSearch) {
                searchItem.expandActionView();
                searchView.setFocusable(true);
                changeLanguageItem.setVisible(false);
            }

            searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);

            if (mSearchQuery != null && !mSearchQuery.isEmpty()) {
                searchView.setQuery(mSearchQuery, false);

                if (mConnection == 1) {
                    mTVShowsSearchViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(TVShowsSearchViewModel.class);
                    mTVShowsSearchViewModel.getShows().observe(getActivity(), getShows);

                    if (mCurrent.toString().equals("in")) {
                        mTVShowsSearchViewModel.setShows("id", getContext(), mSearchQuery);
                    } else {
                        mTVShowsSearchViewModel.setShows(mCurrent.toString(), getContext(), mSearchQuery);
                    }

                    mShowsAdapter = new TVShowsAdapter(getContext());
                    mShowsAdapter.clear();
                    mShowsAdapter.notifyDataSetChanged();
                } else {
                    showLoading(false);
                    mLLNoConnection.setVisibility(View.VISIBLE);
                }
            }

            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    if (!s.equals("")) {
                        if (mConnection == 1) {
                            mTVShowsSearchViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(TVShowsSearchViewModel.class);
                            mTVShowsSearchViewModel.getShows().observe(getActivity(), getShows);

                            if (mCurrent.toString().equals("in")) {
                                mTVShowsSearchViewModel.setShows("id", getContext(), s);
                            } else {
                                mTVShowsSearchViewModel.setShows(mCurrent.toString(), getContext(), s);
                            }

                            mShowsAdapter = new TVShowsAdapter(getContext());
                            mShowsAdapter.clear();
                            mShowsAdapter.notifyDataSetChanged();
                            mRvShows.setHasFixedSize(true);
                            mRvShows.setLayoutManager(new LinearLayoutManager(getContext()));
                            mRvShows.setAdapter(mShowsAdapter);
                            showLoading(true);
                            mSearchQuery = s;
                        } else {
                            showLoading(false);
                            mLLNoConnection.setVisibility(View.VISIBLE);
                        }
                    } else {
                        mSearchQuery = "";
                    }
                    return true;
                }
            });

            searchItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
                @Override
                public boolean onMenuItemActionExpand(MenuItem item) {
                    changeLanguageItem.setVisible(false);
                    searchView.setFocusable(true);
                    isSearch = true;
                    return true;
                }

                @Override
                public boolean onMenuItemActionCollapse(MenuItem item) {
                    mSearchQuery = "";
                    changeLanguageItem.setVisible(true);
                    isSearch = false;
                    initData();
                    return true;
                }
            });
        }

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(SEARCH_KEY, mSearchQuery);
        outState.putBoolean(SEARCH_FLAG, isSearch);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_change_settings) {
            Intent mIntent = new Intent(getContext(), SettingsActivity.class);
            startActivity(mIntent);
        }

        return super.onOptionsItemSelected(item);
    }

    private Observer<ArrayList<TVShowsGenre>> getGenres = tvShowsGenres -> {
        if (tvShowsGenres != null) {
            genresList.clear();
            genresList.addAll(tvShowsGenres);
        }
    };

    private Observer<ArrayList<TVShows>> getShows = new Observer<ArrayList<TVShows>>() {
        @Override
        public void onChanged(@Nullable ArrayList<TVShows> tvShows) {
            if (tvShows != null) {
                mShowsAdapter.setData(tvShows);
                showLoading(false);
            }
        }
    };

    private void initData() {
        if (mConnection == 1) {
            mLLNoConnection.setVisibility(View.GONE);
            mCurrent = getResources().getConfiguration().locale;

            mTvShowsViewModel = ViewModelProviders.of(this).get(TVShowsViewModel.class);
            mTvShowsViewModel.getShows().observe(this, getShows);
            mTVShowsGenreViewModel = ViewModelProviders.of(this).get(TVShowsGenreViewModel.class);
            mTVShowsGenreViewModel.getGenres().observe(this, getGenres);

            if (mCurrent.toString().equals("in")) {
                mTvShowsViewModel.setShows("id", getContext());
                mTVShowsGenreViewModel.setGenre("id", getContext());
            } else {
                mTvShowsViewModel.setShows(mCurrent.toString(), getContext());
                mTVShowsGenreViewModel.setGenre(mCurrent.toString(), getContext());
            }

            mShowsAdapter = new TVShowsAdapter(getContext());
            mShowsAdapter.clear();
            mShowsAdapter.notifyDataSetChanged();
            mRvShows.setHasFixedSize(true);
            mRvShows.setLayoutManager(new LinearLayoutManager(getContext()));
            mRvShows.setAdapter(mShowsAdapter);
            showLoading(true);
        } else {
            showLoading(false);
            mLLNoConnection.setVisibility(View.VISIBLE);
        }
    }

    private void showLoading(Boolean state) {
        if (state) {
            mProgressBar.setVisibility(View.VISIBLE);
        } else {
            mProgressBar.setVisibility(View.GONE);
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) Objects.requireNonNull(getActivity()).getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        searchItem.collapseActionView();
    }
}
