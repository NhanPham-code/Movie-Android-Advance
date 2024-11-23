package com.example.ojtaadaassignment12.data.datasource.remote;

import com.example.ojtaadaassignment12.data.datasource.remote.api.MovieApiService;
import com.example.ojtaadaassignment12.data.entities.CastEntity;

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
    public Single<List<CastEntity>> getCastAndCrew(long movieId) {
        // get cast and crew of a movie
        return movieApiService.getMovieCast(movieId)
                .map(castResponseEntity -> castResponseEntity.getCastList());
    }
}
