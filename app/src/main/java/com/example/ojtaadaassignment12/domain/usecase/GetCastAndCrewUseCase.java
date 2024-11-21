package com.example.ojtaadaassignment12.domain.usecase;

import com.example.ojtaadaassignment12.domain.models.Cast;
import com.example.ojtaadaassignment12.domain.repository.ICastRepository;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Single;

public class GetCastAndCrewUseCase {

    private final ICastRepository castRepository;

    @Inject
    public GetCastAndCrewUseCase(ICastRepository castRepository) {
        this.castRepository = castRepository;
    }

    /**
     * Get cast and crew of a movie
     * @param movieId: id of the movie
     */
    public Single<List<Cast>> getCastAndCrew(long movieId) {
        // get cast and crew of a movie
        return castRepository.getCastAndCrew(movieId);
    }
}
