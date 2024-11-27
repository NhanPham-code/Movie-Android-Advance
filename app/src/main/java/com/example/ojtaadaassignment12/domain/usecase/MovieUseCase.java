package com.example.ojtaadaassignment12.domain.usecase;

import androidx.paging.PagingData;

import com.example.ojtaadaassignment12.domain.models.Movie;
import com.example.ojtaadaassignment12.domain.repository.IMovieRepository;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

public class MovieUseCase {

    IMovieRepository iMovieRepository;

    @Inject
    public MovieUseCase(IMovieRepository movieRepository) {
        this.iMovieRepository = movieRepository;
    }

    /**
     * Fetch movies from the repository
     * @param category: the category of the movies to fetch
     * @return a Flowable of PagingData<Movie>
     */
    public Flowable<PagingData<Movie>> getMovieListFromApi(String category, String rating, String releaseYear, String sortBy) {
        return iMovieRepository.getMovies(category, rating, releaseYear, sortBy);
    }
}
