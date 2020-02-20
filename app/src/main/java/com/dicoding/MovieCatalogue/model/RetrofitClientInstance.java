package com.dicoding.MovieCatalogue.model;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClientInstance {
    private static final String BASE_URL = "https://api.themoviedb.org/3/discover/";
    private static final String BASE_URL_SEARCH = "https://api.themoviedb.org/3/search/";
    private static final String BASE_URL_GENRE = "https://api.themoviedb.org/3/genre/";

    public static Retrofit getRetrofitClientInstance(){
        return new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
    }

    public static Retrofit getRetrofitClientInstanceSearch(){
        return new Retrofit.Builder().baseUrl(BASE_URL_SEARCH).addConverterFactory(GsonConverterFactory.create()).build();
    }

    public static Retrofit getRetrofitClientInstanceGenre() {
        return new Retrofit.Builder().baseUrl(BASE_URL_GENRE).addConverterFactory(GsonConverterFactory.create()).build();
    }
}
