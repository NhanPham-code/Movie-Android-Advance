package com.example.ojtaadaassignment12.data.datasource.local.favoritedb;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.ojtaadaassignment12.data.entities.MovieEntity;

import java.util.List;

import io.reactivex.rxjava3.core.Single;

@Dao
public interface FavoriteMovieDao {

    @Insert
    void insertFavoriteMovie(MovieEntity movie);

    @Query("DELETE FROM favorite_movie_table WHERE id = :id")
    void deleteFavoriteMovieById(long id);

    @Query("SELECT * FROM favorite_movie_table")
    Single<List<MovieEntity>> getFavoriteMovies();

    @Query("SELECT * FROM favorite_movie_table WHERE title LIKE :title")
    Single<List<MovieEntity>> searchFavoriteMoviesByTitle(String title);

    @Query("SELECT COUNT(*) FROM favorite_movie_table")
    Single<Integer> getFavoriteMoviesCount();

}
