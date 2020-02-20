package com.dicoding.MovieCatalogue.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.dicoding.MovieCatalogue.R;
import com.dicoding.MovieCatalogue.activity.SettingsActivity;
import com.dicoding.MovieCatalogue.ui.main.SectionsPagerAdapter;

import java.util.Objects;

public class FavoriteTabFragment extends Fragment {

    public FavoriteTabFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.activity_favorite_tab_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Objects.requireNonNull(getActivity()).setTitle(R.string.favorite);

        ViewPager viewPager = view.findViewById(R.id.view_pager);

        setupViewPager(viewPager);
        TabLayout tabs = view.findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getChildFragmentManager());

        adapter.addFragment(new MovieFavFragment(), getResources().getString(R.string.movies));
        adapter.addFragment(new TVShowsFavFragment(), getResources().getString(R.string.tv_shows));

        viewPager.setAdapter(adapter);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchItem.setVisible(false);
        super.onCreateOptionsMenu(menu, inflater);
    }

//    @Override
//    public void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        mSearchQuery = searchView.getQuery().toString();
//        outState.putString(SEARCH_KEY, mSearchQuery);
//        outState.putParcelableArrayList(STATE_ADAPTER, ().getList());
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_change_settings) {
            Intent mIntent = new Intent(getContext(), SettingsActivity.class);
            startActivity(mIntent);
        }

        return super.onOptionsItemSelected(item);
    }
}