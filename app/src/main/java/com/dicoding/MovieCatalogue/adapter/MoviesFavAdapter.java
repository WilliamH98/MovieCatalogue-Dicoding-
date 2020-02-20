package com.dicoding.MovieCatalogue.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.dicoding.MovieCatalogue.R;
import com.dicoding.MovieCatalogue.model.MoviesFavorite;
import com.squareup.picasso.Picasso;

public class MoviesFavAdapter extends RecyclerView.Adapter<MoviesFavAdapter.ViewHolder> {
    private Context context;
    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.6F);
    private OnItemClickListener mOnItemClickListener;
    private Cursor mCursor;

    public MoviesFavAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public MoviesFavAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.movies_fav_list_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MoviesFavAdapter.ViewHolder viewHolder, int i) {
        mCursor.moveToPosition(i);
        MoviesFavorite currMovies = new MoviesFavorite(mCursor);
        String imageURL = "https://image.tmdb.org/t/p/w342/";

        if (currMovies.getMoviePoster() == null) {
            Picasso.get().load(R.drawable.no_image).fit().centerCrop().into(viewHolder.mMoviePoster);
        } else {
            imageURL = imageURL.concat(currMovies.getMoviePoster());
            Picasso.get().load(imageURL).into(viewHolder.mMoviePoster);
        }

        viewHolder.mMovieTitle.setText(currMovies.getMovieTitle());
        viewHolder.mMovieYear.setText(currMovies.getMovieYear());
        viewHolder.mMovieVoteCount.setText(String.format("(%s Votes)", currMovies.getMovieVoteCount()));
        viewHolder.mMovieImdbScore.setText(currMovies.getMovieImdbScore());
    }

    public interface OnItemClickListener {
        void onItemClick(View v, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    private void postItemClick(ViewHolder holder) {
        mOnItemClickListener.onItemClick(holder.itemView, holder.getAdapterPosition());
    }

    public MoviesFavorite getItem(int position) {
        if (!mCursor.moveToPosition(position)) {
            throw new IllegalStateException("Invalid item position requested");
        }

        return new MoviesFavorite(mCursor);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getId();
    }

    public void swapCursor(Cursor c) {
        if (mCursor != null) {
            mCursor.close();
        }
        mCursor = c;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return (mCursor != null) ? mCursor.getCount() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mMovieTitle, mMovieYear, mMovieVoteCount, mMovieImdbScore;
        private ImageView mMoviePoster;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mMovieTitle = itemView.findViewById(R.id.tv_title);
            mMovieYear = itemView.findViewById(R.id.tv_movie_year);
            mMovieVoteCount = itemView.findViewById(R.id.tv_movie_vote);
            mMovieImdbScore = itemView.findViewById(R.id.tv_movie_score);
            mMoviePoster = itemView.findViewById(R.id.iv_movie_poster);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            v.startAnimation(buttonClick);
            postItemClick(this);
        }
    }
}
