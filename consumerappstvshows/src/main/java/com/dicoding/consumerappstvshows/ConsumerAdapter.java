package com.dicoding.consumerappstvshows;

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
    private final ArrayList<TVShows> listShows = new ArrayList<>();
    private final Activity activity;

    public ConsumerAdapter(Activity activity) {
        this.activity = activity;
    }

    public void setListShows(ArrayList<TVShows> listShows) {
        this.listShows.clear();
        this.listShows.addAll(listShows);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_consumer_tv, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final TVShows currShows = listShows.get(position);
        String imageURL = "https://image.tmdb.org/t/p/w342/";

        holder.tvTitle.setText(currShows.getTitle());
        holder.tvScore.setText(String.format("Score: %s", currShows.getScore()));
        imageURL = imageURL.concat(currShows.getPoster());
        Picasso.get().load(imageURL).into(holder.ivPoster);

        holder.tvDate.setText(String.format("Release Date: %s", currShows.getDate()));
    }

    @Override
    public int getItemCount() {
        return listShows.size();
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
