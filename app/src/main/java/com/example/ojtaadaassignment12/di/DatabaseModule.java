package com.example.ojtaadaassignment12.di;

import android.content.Context;

import androidx.room.Room;

import com.example.ojtaadaassignment12.data.datasource.local.FavoriteMovieDao;
import com.example.ojtaadaassignment12.data.datasource.local.FavoriteMovieDatabase;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class DatabaseModule {

    @Provides
    @Singleton
    public FavoriteMovieDatabase provideDatabase(Context context) {
        return Room.databaseBuilder(context, FavoriteMovieDatabase.class, "favorite_movie_database")
                .fallbackToDestructiveMigration()
                .build();
    }

    @Provides
    @Singleton
    public FavoriteMovieDao provideFavoriteMovieDao(FavoriteMovieDatabase database) {
        return database.favoriteMovieDao();
    }
}
