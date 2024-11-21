package com.example.ojtaadaassignment12.domain.repository;

import com.example.ojtaadaassignment12.domain.models.Cast;
import com.example.ojtaadaassignment12.domain.models.CastOfMovie;

import java.util.List;

import io.reactivex.rxjava3.core.Single;

public interface ICastRepository {

    // get cast and crew of a movie
    Single<List<Cast>> getCastAndCrew(long movieId);
}
