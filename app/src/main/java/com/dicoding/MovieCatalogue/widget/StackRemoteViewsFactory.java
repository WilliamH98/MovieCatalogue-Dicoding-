package com.dicoding.MovieCatalogue.widget;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Binder;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.dicoding.MovieCatalogue.R;
import com.dicoding.MovieCatalogue.db.DatabaseContract;
import com.squareup.picasso.Picasso;

import java.io.IOException;

public class StackRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private final Context mContext;
    private Cursor mCursor;

    public StackRemoteViewsFactory(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onDataSetChanged() {
        if (mCursor != null) {
            mCursor.close();
        }
        final long identityToken = Binder.clearCallingIdentity();
        mCursor = mContext.getContentResolver().query(DatabaseContract.CONTENT_URI, null, null, null, null);
        Binder.restoreCallingIdentity(identityToken);
    }

    @Override
    public void onDestroy() {
        if (mCursor != null) {
            mCursor.close();
        }
    }

    @Override
    public int getCount() {
        return mCursor.getCount();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        mCursor.moveToPosition(position);
        final int title = mCursor.getColumnIndex(DatabaseContract.MovieColumns.MOVIE_TITLE);
        final int score = mCursor.getColumnIndex(DatabaseContract.MovieColumns.MOVIE_SCORE);
        final int poster = mCursor.getColumnIndex(DatabaseContract.MovieColumns.MOVIE_POSTER);
        String movieTitle = mCursor.getString(title);
        String movieScore = mCursor.getString(score);
        String imageURL = "https://image.tmdb.org/t/p/w342/";
        imageURL = imageURL.concat(mCursor.getString(poster));
        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.widget_item);

        try {
            Picasso.get().load(imageURL);
            Bitmap b = Picasso.get().load(imageURL).get();
            rv.setImageViewBitmap(R.id.iv_stack_movie, b);
        } catch (IOException e) {
            e.printStackTrace();
        }

        rv.setTextViewText(R.id.tv_stack_movie_score, movieScore);
        Bundle extras = new Bundle();
        extras.putString(FavoriteMovieWidget.EXTRA_ITEM, movieTitle);
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);
        rv.setOnClickFillInIntent(R.id.iv_stack_movie, fillInIntent);
        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
