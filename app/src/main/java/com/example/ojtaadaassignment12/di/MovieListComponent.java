package com.example.ojtaadaassignment12.di;

import android.app.Application;

import com.example.ojtaadaassignment12.presentation.views.fragments.FavoriteListFragment;
import com.example.ojtaadaassignment12.presentation.views.fragments.MovieListFragment;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;

@Singleton
@Component(modules = {MovieListModule.class, NetworkModule.class, DatabaseModule.class, ContextModule.class}) // include NetworkModule to provide MovieApiService
public interface MovieListComponent {

    @Component.Factory
    interface Factory {
        MovieListComponent create(@BindsInstance Application application);
    }

    // register inject for movie list fragment
    void injectListFragment(MovieListFragment fragment);

    // register inject for favorite movie list fragment
    void injectFavoriteListFragment(FavoriteListFragment fragment);
}
