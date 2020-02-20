package com.dicoding.MovieCatalogue.viewmodel;


import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.dicoding.MovieCatalogue.BuildConfig;
import com.dicoding.MovieCatalogue.model.APITVShowsModel;
import com.dicoding.MovieCatalogue.model.RetrofitClientInstance;
import com.dicoding.MovieCatalogue.model.TVShows;

import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Url;

public class TVShowsViewModel extends ViewModel {
    private MutableLiveData<ArrayList<TVShows>> showsList = new MutableLiveData<>();

    public void setShows(String locale, final Context context) {
        GetShowsServices showsService = RetrofitClientInstance.getRetrofitClientInstance().create(GetShowsServices.class);
        Call<APITVShowsModel> callList = showsService.getShowsAPIData(String.format("tv?api_key=%s&language=%s", BuildConfig.API_KEY, locale));

        callList.enqueue(new Callback<APITVShowsModel>() {
            @Override
            public void onResponse(@NonNull Call<APITVShowsModel> call, @NonNull Response<APITVShowsModel> response) {
                APITVShowsModel apiModel = response.body();

                if (response.isSuccessful()) {
                    assert apiModel != null;
                    showsList.postValue(apiModel.getShowsList());
                } else {
                    try {
                        assert response.errorBody() != null;
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        Toast.makeText(context, jObjError.getJSONObject("error").getString("message"), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<APITVShowsModel> call, @NonNull Throwable t) {
                Log.d("Failed to Load", t.getMessage());
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    interface GetShowsServices {
        @GET
        Call<APITVShowsModel> getShowsAPIData(@Url String url);
    }

    public LiveData<ArrayList<TVShows>> getShows() {
        return showsList;
    }
}
