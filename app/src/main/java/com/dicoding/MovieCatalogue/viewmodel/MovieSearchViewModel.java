package com.dicoding.MovieCatalogue.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.dicoding.MovieCatalogue.BuildConfig;
import com.dicoding.MovieCatalogue.model.APIMovieModel;
import com.dicoding.MovieCatalogue.model.Movies;
import com.dicoding.MovieCatalogue.model.RetrofitClientInstance;

import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Url;

public class MovieSearchViewModel extends ViewModel {
    private MutableLiveData<ArrayList<Movies>> moviesList = new MutableLiveData<>();

    public void setMovies(String locale, final Context context, String querySearch) {
        MovieSearchViewModel.GetMoviesServices moviesService = RetrofitClientInstance.getRetrofitClientInstanceSearch().create(MovieSearchViewModel.GetMoviesServices.class);
        Call<APIMovieModel> callList = moviesService.getMovieAPIData(String.format("movie?api_key=%s&language=%s&query=%s", BuildConfig.API_KEY, locale, querySearch));

        callList.enqueue(new Callback<APIMovieModel>() {
            @Override
            public void onResponse(@NonNull Call<APIMovieModel> call, @NonNull Response<APIMovieModel> response) {
                APIMovieModel apiModel = response.body();

                if (response.isSuccessful()) {
                    assert apiModel != null;
                    moviesList.postValue(apiModel.getMovieList());
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
            public void onFailure(@NonNull Call<APIMovieModel> call, @NonNull Throwable t) {
                Log.d("Failed to Load", t.getMessage());
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    interface GetMoviesServices {
        @GET
        Call<APIMovieModel> getMovieAPIData(@Url String url);
    }

    public LiveData<ArrayList<Movies>> getMovies() {
        return moviesList;
    }
}
