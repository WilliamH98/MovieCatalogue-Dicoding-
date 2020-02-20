package com.dicoding.consumerappsmovies;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ConsumerAdapter extends RecyclerView.Adapter<ConsumerAdapter.ViewHolder> {
    private final ArrayList<Movies> listMovies = new ArrayList<>();
    private final Activity activity;

    public ConsumerAdapter(Activity activity) {
        this.activity = activity;
    }

    public void setListMovies(ArrayList<Movies> listMovies) {
        this.listMovies.clear();
        this.listMovies.addAll(listMovies);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_consumer_movies, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Movies currMovies = listMovies.get(position);
        String imageURL = "https://image.tmdb.org/t/p/w342/";

        holder.tvTitle.setText(currMovies.getTitle());
        holder.tvScore.setText(String.format("Score: %s", currMovies.getScore()));
        imageURL = imageURL.concat(currMovies.getPoster());
        Picasso.get().load(imageURL).into(holder.ivPoster);

        holder.tvDate.setText(String.format("Release Date: %s", currMovies.getDate()));
    }

    @Override
    public int getItemCount() {
        return listMovies.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvScore, tvDate;
        ImageView ivPoster;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tv_title);
            tvScore = itemView.findViewById(R.id.tv_score);
            tvDate = itemView.findViewById(R.id.tv_date);
            ivPoster = itemView.findViewById(R.id.iv_poster);
        }
    }
}
