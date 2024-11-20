package com.example.ojtaadaassignment12.data.localdatasource;

import androidx.paging.PagingData;
import androidx.paging.PagingSource;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.ojtaadaassignment12.domain.models.Movie;

import io.reactivex.rxjava3.core.Flowable;

@Dao
public interface FavoriteMovieDao {

    @Insert
    void insertFavoriteMovie(Movie movie);

    @Query("DELETE FROM favorite_movie_table WHERE id = :id")
    void deleteFavoriteMovieById(long id);

    @Query("SELECT * FROM favorite_movie_table")
    PagingSource<Integer, Movie> getFavoriteMovies();

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_movie_table WHERE id = :id)")
    boolean isFavoriteMovie(long id);

}
