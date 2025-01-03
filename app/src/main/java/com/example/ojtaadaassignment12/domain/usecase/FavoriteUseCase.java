package com.example.ojtaadaassignment12.domain.usecase;

import androidx.paging.PagingData;

import com.example.ojtaadaassignment12.domain.models.Movie;
import com.example.ojtaadaassignment12.domain.repository.IMovieRepository;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

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
     * get favorite movie list from local database by name key by using IMovieRepository
     */
    public Flowable<PagingData<Movie>> getFavoriteMoviesByTitle(String title) {
        return iMovieRepository.getFavoriteMoviesByTitle(title);
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

    /**
     * Get favorite movies count to set the favorite tag by using IMovieRepository
     *
     * @return Single<Integer> favorite movies count
     */
    public Single<Integer> getFavoriteMoviesCount() {
        return iMovieRepository.getFavoriteMoviesCount();
    }
}
