package com.example.ojtaadaassignment12.data.remotedatasource;

import com.example.ojtaadaassignment12.data.remotedatasource.api.MovieApiService;
import com.example.ojtaadaassignment12.domain.models.Cast;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Single;

public class CastPagingSource {

    private final MovieApiService movieApiService;

    @Inject
    public CastPagingSource(MovieApiService movieApiService) {
        this.movieApiService = movieApiService;
    }

    /**
     * Get cast and crew of a movie
     * @param movieId: id of the movie
     * @return list of cast and crew
     */
    public Single<List<Cast>> getCastAndCrew(long movieId) {
        // get cast and crew of a movie
        return movieApiService.getMovieCast(movieId)
                .map(castOfMovie -> castOfMovie.getCastList());
    }
}
