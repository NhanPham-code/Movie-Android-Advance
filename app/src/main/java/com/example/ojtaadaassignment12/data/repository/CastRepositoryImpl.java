package com.example.ojtaadaassignment12.data.repository;

import com.example.ojtaadaassignment12.data.datasource.remote.CastDataSource;
import com.example.ojtaadaassignment12.data.mapper.CastMapper;
import com.example.ojtaadaassignment12.domain.models.Cast;
import com.example.ojtaadaassignment12.domain.repository.ICastRepository;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Single;

public class CastRepositoryImpl implements ICastRepository {

    private final CastDataSource castPagingSource;

    @Inject
    public CastRepositoryImpl(CastDataSource castPagingSource) {
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
        // map the list of cast entities to list of cast domain models
        return castPagingSource.getCastAndCrew(movieId)
                .map(castEntities -> castEntities.stream()
                        .map(CastMapper::toDomain)
                        .collect(Collectors.toList()));
    }
}
