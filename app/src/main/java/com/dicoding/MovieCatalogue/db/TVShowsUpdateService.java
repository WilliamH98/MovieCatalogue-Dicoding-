package com.dicoding.MovieCatalogue.db;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

public class TVShowsUpdateService extends IntentService {
    private static final String TAG = TVShowsUpdateService.class.getSimpleName();
    public static final String ACTION_INSERT = TAG + ".INSERT";
    public static final String ACTION_DELETE = TAG + ".DELETE";

    public static final String EXTRA_VALUES = TAG + ".ContentValues";

    public static void insertNewTVShowsFav(Context context, ContentValues values) {
        Intent intent = new Intent(context, TVShowsUpdateService.class);
        intent.setAction(ACTION_INSERT);
        intent.putExtra(EXTRA_VALUES, values);
        context.startService(intent);
    }

    public static void deleteTVShowsFav(Context context, Uri uri) {
        Intent intent = new Intent(context, TVShowsUpdateService.class);
        intent.setAction(ACTION_DELETE);
        intent.setData(uri);
        context.startService(intent);
    }

    public TVShowsUpdateService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (ACTION_INSERT.equals(intent.getAction())) {
            ContentValues values = intent.getParcelableExtra(EXTRA_VALUES);
            performInsert(values);
        } else if (ACTION_DELETE.equals(intent.getAction())) {
            performDelete(intent.getData());
        }
    }

    private void performInsert(ContentValues values) {
        if (getContentResolver().insert(DatabaseContractTV.CONTENT_URI, values) != null) {
            Log.d("Insert Success", "Success insert to favorite");
        } else {
            Log.d("Insert Failed", "Failed insert to favorite");
        }
    }

    private void performDelete(Uri uri) {
        int count = getContentResolver().delete(uri, null, null);
        Log.d("Delete Success", "Deleted from favorite " + count);
    }
}
