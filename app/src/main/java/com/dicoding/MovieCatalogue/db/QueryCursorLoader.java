package com.dicoding.MovieCatalogue.db;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.content.AsyncTaskLoader;

public class QueryCursorLoader extends AsyncTaskLoader<Cursor> {
    private Cursor mCursor = null;
    @SuppressLint("StaticFieldLeak")
    private Context mContext;
    private Uri mUri;
    private String mSort;

    public QueryCursorLoader(Context context, Uri uri, String sortType) {
        super(context);
        mContext = context;
        mUri = uri;
        mSort = sortType;
    }

    @Override
    public Cursor loadInBackground() {
        return mContext.getContentResolver().query(mUri, null, null, null, mSort);
    }

    @Override
    protected void onStartLoading() {
        if (mCursor != null) {
            deliverResult(mCursor);
        } else {
            forceLoad();
        }
    }

    public void deliverResult(Cursor data) {
        mCursor = data;
        super.deliverResult(data);
    }
}
