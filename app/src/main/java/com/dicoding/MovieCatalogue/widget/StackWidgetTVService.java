package com.dicoding.MovieCatalogue.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

public class StackWidgetTVService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new StackRemoteViewsTVFactory(this.getApplicationContext());
    }
}
