package com.example.ojtaadaassignment12.domain.usecase;

import com.example.ojtaadaassignment12.domain.models.Movie;
import com.example.ojtaadaassignment12.domain.repository.IMovieRepository;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Single;

public class GetMovieDetailByIdUseCase {

    IMovieRepository movieRepository;

    @Inject
    public  GetMovieDetailByIdUseCase(IMovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public Single<Movie> getMovieDetailById(long movieId) {
        return movieRepository.getMovieDetailById(movieId);
    }
}
