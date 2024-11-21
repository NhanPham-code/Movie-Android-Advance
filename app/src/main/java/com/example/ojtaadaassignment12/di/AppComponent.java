package com.example.ojtaadaassignment12.di;

import android.app.Application;

import com.example.ojtaadaassignment12.presentation.MainActivity;
import com.example.ojtaadaassignment12.presentation.views.fragments.CommonFragment;
import com.example.ojtaadaassignment12.presentation.views.fragments.FavoriteListFragment;
import com.example.ojtaadaassignment12.presentation.views.fragments.MovieDetailFragment;
import com.example.ojtaadaassignment12.presentation.views.fragments.MovieListFragment;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;

@Singleton
@Component(modules = {MovieListModule.class, NetworkModule.class, DatabaseModule.class, ContextModule.class, CastModule.class}) // include NetworkModule to provide MovieApiService
public interface AppComponent {

    @Component.Factory
    interface Factory {
        AppComponent create(@BindsInstance Application application);
    }

    // register inject for movie list fragment
    void injectListFragment(MovieListFragment fragment);

    // register inject for favorite movie list fragment
    void injectFavoriteListFragment(FavoriteListFragment fragment);

    // register inject for main activity
    void injectMainActivity(MainActivity mainActivity);

    // register inject for common fragment
    void injectCommonFragment(CommonFragment commonFragment);

    // register inject for movie detail fragment
    void injectDetailFragment(MovieDetailFragment detailFragment);
}
