package com.example.ojtaadaassignment12.domain.repository;

import androidx.paging.PagingData;

import com.example.ojtaadaassignment12.domain.models.Movie;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

public interface IMovieRepository {

    // get movie list by category from API
    Flowable<PagingData<Movie>> getMovies(String category, String rating, String releaseYear, String sortBy);

    // get favorite movie list from local database
    Flowable<PagingData<Movie>> getFavoriteMovies();

    // get favorite movie list from local database by name key
    Flowable<PagingData<Movie>> getFavoriteMoviesByTitle(String title);

    // insert favorite movie to local database
    void insertFavoriteMovie(Movie movie);

    // delete favorite movie from local database
    void deleteFavoriteMovie(Movie movie);

    // get favorite movies count to set the favorite tag
    Single<Integer> getFavoriteMoviesCount();

    // get movie detail from API
    Single<Movie> getMovieDetailById(long movieId);

}
