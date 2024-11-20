package com.example.ojtaadaassignment12.domain.repository;

import androidx.lifecycle.LiveData;
import androidx.paging.PagingData;

import com.example.ojtaadaassignment12.domain.models.Movie;

import java.util.List;

import io.reactivex.rxjava3.core.Flowable;

public interface IMovieRepository {

    // get movie list by category from API
    Flowable<PagingData<Movie>> getMovies(String category);

    // get favorite movie list from local database
    Flowable<PagingData<Movie>> getFavoriteMovies();

    // insert favorite movie to local database
    void insertFavoriteMovie(Movie movie);

    // delete favorite movie from local database
    void deleteFavoriteMovie(Movie movie);
}
