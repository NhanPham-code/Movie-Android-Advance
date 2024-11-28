package com.example.ojtaadaassignment12.di;

import android.app.Application;

import com.example.ojtaadaassignment12.presentation.MainActivity;
import com.example.ojtaadaassignment12.presentation.views.fragments.AllReminderFragment;
import com.example.ojtaadaassignment12.presentation.views.fragments.CommonFragment;
import com.example.ojtaadaassignment12.presentation.views.fragments.EditProfileFragment;
import com.example.ojtaadaassignment12.presentation.views.fragments.FavoriteListFragment;
import com.example.ojtaadaassignment12.presentation.views.fragments.MainFragment;
import com.example.ojtaadaassignment12.presentation.views.fragments.MovieDetailFragment;
import com.example.ojtaadaassignment12.presentation.views.fragments.MovieListFragment;
import com.example.ojtaadaassignment12.presentation.workers.ReminderWorker;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;

@Singleton
@Component(modules = {MovieListModule.class, NetworkModule.class, DatabaseModule.class,
                        ContextModule.class, CastModule.class, UserProfileModule.class, ReminderModule.class}) // include NetworkModule to provide MovieApiService
public interface AppComponent {

    @Component.Factory
    interface Factory {
        AppComponent create(@BindsInstance Application application);
    }

    // register inject for movie list fragment
    void injectListFragment(MovieListFragment fragment);

    // register inject for favorite movie list fragment
    void injectFavoriteListFragment(FavoriteListFragment fragment);

    // register inject for main fragment
    void injectMainFragment(MainFragment fragment);

    // register inject for common fragment
    void injectCommonFragment(CommonFragment commonFragment);

    // register inject for movie detail fragment
    void injectDetailFragment(MovieDetailFragment detailFragment);

    // register inject for edit profile fragment
    void injectEditProfileFragment(EditProfileFragment editProfileFragment);

    // register inject for reminder worker
    void injectReminderWorker(ReminderWorker reminderWorker);

    // register inject for all reminder fragment
    void injectAllReminderFragment(AllReminderFragment allReminderFragment);

    // register inject for main activity
    void injectMainActivity(MainActivity mainActivity);
}
