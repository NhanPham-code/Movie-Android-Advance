package com.example.ojtaadaassignment12.di;

import android.app.Application;

public class MyApplication extends Application {

    public MovieListComponent movieListComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        movieListComponent = DaggerMovieListComponent.factory().create(this);
    }
}
