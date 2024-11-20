package com.example.ojtaadaassignment12.di;

import com.example.ojtaadaassignment12.data.remotedatasource.api.MovieApiService;
import com.example.ojtaadaassignment12.data.remotedatasource.api.UnsafeOkHttpClient;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class NetworkModule {
    private static final String BASE_URL = "https://api.themoviedb.org/3/";

    @Provides
    @Singleton
    public static MovieApiService provideMovieApiService() {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(UnsafeOkHttpClient.getUnsafeOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build().create(MovieApiService.class);
    }
}
