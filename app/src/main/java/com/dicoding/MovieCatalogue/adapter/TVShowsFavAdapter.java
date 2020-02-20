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
import com.dicoding.MovieCatalogue.model.TVShowsFavorite;
import com.squareup.picasso.Picasso;

public class TVShowsFavAdapter extends RecyclerView.Adapter<TVShowsFavAdapter.ViewHolder> {
    private Context context;
    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.6F);
    private TVShowsFavAdapter.OnItemClickListener mOnItemClickListener;
    private Cursor mCursor;

    public TVShowsFavAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.tv_shows_fav_list_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        mCursor.moveToPosition(i);
        TVShowsFavorite currTVShows = new TVShowsFavorite(mCursor);
        String imageURL = "https://image.tmdb.org/t/p/w342/";

        if (currTVShows.getTvShowsPoster() == null) {
            Picasso.get().load(R.drawable.no_image).fit().centerCrop().into(viewHolder.mShowsPoster);
        } else {
            imageURL = imageURL.concat(currTVShows.getTvShowsPoster());
            Picasso.get().load(imageURL).into(viewHolder.mShowsPoster);
        }

        viewHolder.mShowsTitle.setText(currTVShows.getTvShowsTitle());
        viewHolder.mShowsYear.setText(currTVShows.getTvShowsYear());
        viewHolder.mShowsVoteCount.setText(String.format("(%s Votes)", currTVShows.getTvShowsVoteCount()));
        viewHolder.mShowsImdbScore.setText(currTVShows.getTvShowsImdbScore());
    }

    public interface OnItemClickListener {
        void onItemClick(View v, int position);
    }

    public void setOnItemClickListener(TVShowsFavAdapter.OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    private void postItemClick(TVShowsFavAdapter.ViewHolder holder) {
        mOnItemClickListener.onItemClick(holder.itemView, holder.getAdapterPosition());
    }

    public TVShowsFavorite getItem(int position) {
        if (!mCursor.moveToPosition(position)) {
            throw new IllegalStateException("Invalid item position requested");
        }

        return new TVShowsFavorite(mCursor);
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
        private TextView mShowsTitle, mShowsYear, mShowsVoteCount, mShowsImdbScore;
        private ImageView mShowsPoster;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mShowsTitle = itemView.findViewById(R.id.tv_shows_title);
            mShowsYear = itemView.findViewById(R.id.tv_shows_year);
            mShowsVoteCount = itemView.findViewById(R.id.tv_shows_vote);
            mShowsImdbScore = itemView.findViewById(R.id.tv_shows_score);
            mShowsPoster = itemView.findViewById(R.id.iv_shows_poster);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            v.startAnimation(buttonClick);
            postItemClick(this);
        }
    }
}
