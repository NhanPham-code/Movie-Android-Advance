package com.example.ojtaadaassignment12.data.repository;

import com.example.ojtaadaassignment12.data.remotedatasource.CastPagingSource;
import com.example.ojtaadaassignment12.domain.models.Cast;
import com.example.ojtaadaassignment12.domain.models.CastOfMovie;
import com.example.ojtaadaassignment12.domain.repository.ICastRepository;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Single;

public class CastRepositoryImpl implements ICastRepository {

    private final CastPagingSource castPagingSource;

    @Inject
    public CastRepositoryImpl(CastPagingSource castPagingSource) {
        this.castPagingSource = castPagingSource;
    }

    /**
     * Get cast and crew of a movie
     * @param movieId: id of the movie
     * @return list of cast and crew
     */
    @Override
    public Single<List<Cast>> getCastAndCrew(long movieId) {
        // get cast and crew of a movie
        return castPagingSource.getCastAndCrew(movieId);
    }
}
