package com.example.ojtaadaassignment12.data.datasource.local.favoritedb;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.ojtaadaassignment12.data.entities.MovieEntity;

@Database(entities = {MovieEntity.class}, version = 1)
public abstract class FavoriteMovieDatabase extends RoomDatabase {

    public abstract FavoriteMovieDao favoriteMovieDao();


}
