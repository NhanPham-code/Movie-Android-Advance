package com.example.ojtaadaassignment12.domain.usecase;

import androidx.lifecycle.LiveData;
import androidx.paging.PagingData;

import com.example.ojtaadaassignment12.domain.models.Movie;
import com.example.ojtaadaassignment12.domain.repository.IMovieRepository;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class FavoriteUseCase {

    private final IMovieRepository iMovieRepository;

    @Inject
    public FavoriteUseCase(IMovieRepository iMovieRepository) {
        this.iMovieRepository = iMovieRepository;
    }

    /**
     * Get favorite movie list from local database by using IMovieRepository
     *
     * @return Flowable<PagingData < Movie>> favorite movie list
     */
    public Flowable<PagingData<Movie>> getFavoriteMovies() {
        return iMovieRepository.getFavoriteMovies();
    }


    /**
     * Insert favorite movie to local database by using IMovieRepository
     *
     * @param movie: favorite movie
     */
    public void insertFavoriteMovie(Movie movie) {
        iMovieRepository.insertFavoriteMovie(movie);
    }


    /**
     * Delete favorite movie from local database by using IMovieRepository
     *
     * @param movie: favorite movie
     */
    public void deleteFavoriteMovie(Movie movie) {
        iMovieRepository.deleteFavoriteMovie(movie);
    }
}
