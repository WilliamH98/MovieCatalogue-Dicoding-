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
import com.dicoding.MovieCatalogue.adapter.MoviesAdapter;
import com.dicoding.MovieCatalogue.model.Movies;
import com.dicoding.MovieCatalogue.model.MoviesGenre;
import com.dicoding.MovieCatalogue.viewmodel.MovieGenreViewModel;
import com.dicoding.MovieCatalogue.viewmodel.MovieSearchViewModel;
import com.dicoding.MovieCatalogue.viewmodel.MovieViewModel;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

public class MovieTabFragment extends Fragment {
    TextView mMoviesTitle, mMoviesYear, mMoviesImdbScore;
    Button mBtnMoreDetails, mBtnRefresh;
    ImageView mMoviesPoster;
    private MoviesAdapter mMoviesAdapter;
    private RecyclerView mRvMovies;
    private ProgressBar mProgressBar;
    private MovieViewModel mMovieViewModel;
    private MovieSearchViewModel mMovieSearchViewModel;
    private MovieGenreViewModel mMovieGenreViewModel;
    private LinearLayout mLLNoConnection;
    private Locale mCurrent;
    private int mConnection;
    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.6F);
    public static ArrayList<MoviesGenre> genresList = new ArrayList<>();
    private String mSearchQuery;
    private static final String SEARCH_KEY = "Search Query";
    private static final String SEARCH_FLAG = "Search View Expand";
    private static boolean isSearch = false;
    private MenuItem searchItem, changeLanguageItem;

    public MovieTabFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.activity_movie, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Objects.requireNonNull(getActivity()).setTitle(R.string.app_name);

        mMoviesTitle = view.findViewById(R.id.tv_shows_title);
        mMoviesYear = view.findViewById(R.id.tv_shows_year);
        mMoviesImdbScore = view.findViewById(R.id.tv_shows_score);
        mBtnMoreDetails = view.findViewById(R.id.btn_shows_more_details);
        mMoviesPoster = view.findViewById(R.id.iv_shows_poster);
        mRvMovies = view.findViewById(R.id.rv_movies);
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
            searchView.setQueryHint(getResources().getString(R.string.search_movies));

            if (isSearch) {
                searchItem.expandActionView();
                searchView.setFocusable(true);
                changeLanguageItem.setVisible(false);
            }

            searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);

            if (mSearchQuery != null && !mSearchQuery.isEmpty()) {
                searchView.setQuery(mSearchQuery, false);

                if (mConnection == 1) {
                    mMovieSearchViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(MovieSearchViewModel.class);
                    mMovieSearchViewModel.getMovies().observe(getActivity(), getMovies);

                    if (mCurrent.toString().equals("in")) {
                        mMovieSearchViewModel.setMovies("id", getContext(), mSearchQuery);
                    } else {
                        mMovieSearchViewModel.setMovies(mCurrent.toString(), getContext(), mSearchQuery);
                    }

                    mMoviesAdapter = new MoviesAdapter(getContext());
                    mMoviesAdapter.clear();
                    mMoviesAdapter.notifyDataSetChanged();
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
                            mMovieSearchViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(MovieSearchViewModel.class);
                            mMovieSearchViewModel.getMovies().observe(getActivity(), getMovies);

                            if (mCurrent.toString().equals("in")) {
                                mMovieSearchViewModel.setMovies("id", getContext(), s);
                            } else {
                                mMovieSearchViewModel.setMovies(mCurrent.toString(), getContext(), s);
                            }

                            mMoviesAdapter = new MoviesAdapter(getContext());
                            mMoviesAdapter.clear();
                            mMoviesAdapter.notifyDataSetChanged();
                            mRvMovies.setHasFixedSize(true);
                            mRvMovies.setLayoutManager(new LinearLayoutManager(getContext()));
                            mRvMovies.setAdapter(mMoviesAdapter);
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

    private Observer<ArrayList<Movies>> getMovies = new Observer<ArrayList<Movies>>() {
        @Override
        public void onChanged(@Nullable ArrayList<Movies> movies) {
            if (movies != null) {
                mMoviesAdapter.setData(movies);
                showLoading(false);
            }
        }
    };

    private Observer<ArrayList<MoviesGenre>> getGenres = moviesGenres -> {
        if (moviesGenres != null) {
            genresList.clear();
            genresList.addAll(moviesGenres);
        }
    };

    private void initData() {
        if (mConnection == 1) {
            mLLNoConnection.setVisibility(View.GONE);
            mCurrent = getResources().getConfiguration().locale;

            mMovieViewModel = ViewModelProviders.of(this).get(MovieViewModel.class);
            mMovieViewModel.getMovies().observe(this, getMovies);
            mMovieGenreViewModel = ViewModelProviders.of(this).get(MovieGenreViewModel.class);
            mMovieGenreViewModel.getGenres().observe(this, getGenres);

            if (mCurrent.toString().equals("in")) {
                mMovieViewModel.setMovies("id", getContext());
                mMovieGenreViewModel.setGenre("id", getContext());
            } else {
                mMovieViewModel.setMovies(mCurrent.toString(), getContext());
                mMovieGenreViewModel.setGenre(mCurrent.toString(), getContext());
            }

            mMoviesAdapter = new MoviesAdapter(getContext());
            mMoviesAdapter.clear();
            mMoviesAdapter.notifyDataSetChanged();
            mRvMovies.setHasFixedSize(true);
            mRvMovies.setLayoutManager(new LinearLayoutManager(getContext()));
            mRvMovies.setAdapter(mMoviesAdapter);
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
