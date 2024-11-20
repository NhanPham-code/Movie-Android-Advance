package com.example.ojtaadaassignment12.data.localdatasource;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.ojtaadaassignment12.domain.models.Movie;

@Database(entities = {Movie.class}, version = 1)
public abstract class FavoriteMovieDatabase extends RoomDatabase {

    public abstract FavoriteMovieDao favoriteMovieDao();


}
