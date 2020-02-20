package com.dicoding.MovieCatalogue.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.dicoding.MovieCatalogue.BuildConfig;
import com.dicoding.MovieCatalogue.model.APIMovieGenreModel;
import com.dicoding.MovieCatalogue.model.MoviesGenre;
import com.dicoding.MovieCatalogue.model.RetrofitClientInstance;

import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Url;

public class MovieGenreViewModel extends ViewModel {
    private MutableLiveData<ArrayList<MoviesGenre>> genreList = new MutableLiveData<>();

    public void setGenre(String locale, final Context context) {
        MovieGenreViewModel.GetGenreServices genreServices = RetrofitClientInstance.getRetrofitClientInstanceGenre().create(MovieGenreViewModel.GetGenreServices.class);
        Call<APIMovieGenreModel> callList = genreServices.getGenreAPIData(String.format("movie/list?api_key=%s&language=%s", BuildConfig.API_KEY, locale));

        callList.enqueue(new Callback<APIMovieGenreModel>() {
            @Override
            public void onResponse(@NonNull Call<APIMovieGenreModel> call, @NonNull Response<APIMovieGenreModel> response) {
                APIMovieGenreModel apiModel = response.body();

                if (response.isSuccessful()) {
                    assert apiModel != null;
                    genreList.postValue(apiModel.getGenreList());
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
            public void onFailure(@NonNull Call<APIMovieGenreModel> call, @NonNull Throwable t) {
                Log.d("Failed to Load", t.getMessage());
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    interface GetGenreServices {
        @GET
        Call<APIMovieGenreModel> getGenreAPIData(@Url String url);
    }

    public LiveData<ArrayList<MoviesGenre>> getGenres() {
        return genreList;
    }
}
