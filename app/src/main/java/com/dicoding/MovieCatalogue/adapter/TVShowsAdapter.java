package com.dicoding.MovieCatalogue.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.dicoding.MovieCatalogue.R;
import com.dicoding.MovieCatalogue.activity.TVShowsDetailsActivity;
import com.dicoding.MovieCatalogue.model.TVShows;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class TVShowsAdapter extends RecyclerView.Adapter<TVShowsAdapter.TVShowsViewHolder> {
    private Context context;
    private ArrayList<TVShows> tvShowsList = new ArrayList<>();
    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.6F);

    public TVShowsAdapter(Context context) {
        this.context = context;
    }

    public void setData(ArrayList<TVShows> tvShows) {
        tvShowsList.clear();
        tvShowsList.addAll(tvShows);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TVShowsAdapter.TVShowsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.tv_shows_list_item, viewGroup, false);
        return new TVShowsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TVShowsAdapter.TVShowsViewHolder tvShowsViewHolder, int i) {
        final TVShows currShows = tvShowsList.get(i);
        String imageURL = "https://image.tmdb.org/t/p/w342/";

        if (currShows.getTvShowsPoster() == null) {
            Picasso.get().load(R.drawable.no_image).fit().centerCrop().into(tvShowsViewHolder.mShowsPoster);
        } else {
            imageURL = imageURL.concat(currShows.getTvShowsPoster());
            Picasso.get().load(imageURL).into(tvShowsViewHolder.mShowsPoster);
        }

        tvShowsViewHolder.mShowsTitle.setText(currShows.getTvShowsTitle());
        tvShowsViewHolder.mShowsYear.setText(currShows.getTvShowsYear());
        tvShowsViewHolder.mShowsVoteCount.setText(String.format("(%s Votes)", currShows.getTvShowsVoteCount()));
        tvShowsViewHolder.mShowsImdbScore.setText(currShows.getTvShowsImdbScore());

        tvShowsViewHolder.mBtnMoreDetails.setOnClickListener(v -> {
            v.startAnimation(buttonClick);
            Intent intent = new Intent(context, TVShowsDetailsActivity.class);
            intent.putExtra(TVShowsDetailsActivity.EXTRA_TV_SHOWS, currShows);
            context.startActivity(intent);
        });
    }

    public void clear() {
        int size = tvShowsList.size();
        tvShowsList.clear();
        notifyItemRangeRemoved(0, size);
    }

    @Override
    public int getItemCount() {
        return tvShowsList.size();
    }

    public class TVShowsViewHolder extends RecyclerView.ViewHolder {
        private TextView mShowsTitle, mShowsYear, mShowsVoteCount, mShowsImdbScore;
        private Button mBtnMoreDetails;
        private ImageView mShowsPoster;

        public TVShowsViewHolder(@NonNull View itemView) {
            super(itemView);

            mShowsTitle = itemView.findViewById(R.id.tv_shows_title);
            mShowsYear = itemView.findViewById(R.id.tv_shows_year);
            mShowsVoteCount = itemView.findViewById(R.id.tv_shows_vote);
            mShowsImdbScore = itemView.findViewById(R.id.tv_shows_score);

            mBtnMoreDetails = itemView.findViewById(R.id.btn_shows_more_details);
            mShowsPoster = itemView.findViewById(R.id.iv_shows_poster);
        }
    }
}
