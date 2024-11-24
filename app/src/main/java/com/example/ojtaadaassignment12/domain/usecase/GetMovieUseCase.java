package com.example.ojtaadaassignment12.domain.usecase;

import androidx.paging.PagingData;

import com.example.ojtaadaassignment12.domain.models.Movie;
import com.example.ojtaadaassignment12.domain.repository.IMovieRepository;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Flowable;

public class GetMovieUseCase {

    IMovieRepository iMovieRepository;

    @Inject
    public GetMovieUseCase(IMovieRepository movieRepository) {
        this.iMovieRepository = movieRepository;
    }

    /**
     * Fetch movies from the repository
     * @param category: the category of the movies to fetch
     * @return a Flowable of PagingData<Movie>
     */
    public Flowable<PagingData<Movie>> execute(String category, String rating, String releaseYear, String sortBy) {
        return iMovieRepository.getMovies(category, rating, releaseYear, sortBy);
    }
}
