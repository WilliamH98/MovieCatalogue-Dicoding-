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
import com.dicoding.MovieCatalogue.activity.MovieDetailsActivity;
import com.dicoding.MovieCatalogue.model.Movies;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesViewHolder> {
    private Context context;
    private ArrayList<Movies> moviesList = new ArrayList<>();
    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.6F);

    public MoviesAdapter(Context context) {
        this.context = context;
    }

    public void setData(ArrayList<Movies> movies) {
        moviesList.clear();
        moviesList.addAll(movies);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MoviesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.movies_list_item, viewGroup, false);
        return new MoviesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MoviesViewHolder moviesViewHolder, int i) {
        final Movies currMovies = moviesList.get(i);
        String imageURL = "https://image.tmdb.org/t/p/w342/";

        if (currMovies.getMoviePoster() == null) {
            Picasso.get().load(R.drawable.no_image).fit().centerCrop().into(moviesViewHolder.mMoviePoster);
        } else {
            imageURL = imageURL.concat(currMovies.getMoviePoster());
            Picasso.get().load(imageURL).into(moviesViewHolder.mMoviePoster);
        }

        moviesViewHolder.mMovieTitle.setText(currMovies.getMovieTitle());
        moviesViewHolder.mMovieYear.setText(currMovies.getMovieYear());
        moviesViewHolder.mMovieVoteCount.setText(String.format("(%s Votes)", currMovies.getMovieVoteCount()));
        moviesViewHolder.mMovieImdbScore.setText(currMovies.getMovieImdbScore());

        moviesViewHolder.mBtnMoreDetails.setOnClickListener(v -> {
            v.startAnimation(buttonClick);
            Intent intent = new Intent(context, MovieDetailsActivity.class);
            intent.putExtra(MovieDetailsActivity.EXTRA_MOVIES, currMovies);
            context.startActivity(intent);
        });
    }

    public void clear() {
        int size = moviesList.size();
        moviesList.clear();
        notifyItemRangeRemoved(0, size);
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }

    public class MoviesViewHolder extends RecyclerView.ViewHolder {
        private TextView mMovieTitle, mMovieYear, mMovieVoteCount, mMovieImdbScore;
        private Button mBtnMoreDetails;
        private ImageView mMoviePoster;

        public MoviesViewHolder(@NonNull View itemView) {
            super(itemView);

            mMovieTitle = itemView.findViewById(R.id.tv_title);
            mMovieYear = itemView.findViewById(R.id.tv_movie_year);
            mMovieVoteCount = itemView.findViewById(R.id.tv_movie_vote);
            mMovieImdbScore = itemView.findViewById(R.id.tv_movie_score);

            mBtnMoreDetails = itemView.findViewById(R.id.btn_more_details);
            mMoviePoster = itemView.findViewById(R.id.iv_movie_poster);
        }
    }
}
