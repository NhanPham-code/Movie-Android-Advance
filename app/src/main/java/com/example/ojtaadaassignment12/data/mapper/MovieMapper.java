package com.example.ojtaadaassignment12.data.mapper;

import com.example.ojtaadaassignment12.data.entities.MovieEntity;
import com.example.ojtaadaassignment12.domain.models.Movie;

public class MovieMapper {

    public static MovieEntity toEntity(Movie movie) {
        MovieEntity entity = new MovieEntity();
        entity.setAdult(movie.isAdult());
        entity.setBackdropPath(movie.getBackdropPath());
        entity.setId(movie.getId());
        entity.setOriginalLanguage(movie.getOriginalLanguage());
        entity.setOriginalTitle(movie.getOriginalTitle());
        entity.setOverview(movie.getOverview());
        entity.setPopularity(movie.getPopularity());
        entity.setPosterPath(movie.getPosterPath());
        entity.setReleaseDate(movie.getReleaseDate());
        entity.setTitle(movie.getTitle());
        entity.setVideo(movie.isVideo());
        entity.setVoteAverage(movie.getVoteAverage());
        entity.setVoteCount(movie.getVoteCount());
        entity.setIsFavorite(movie.getIsFavorite());
        return entity;
    }

    public static Movie toDomain(MovieEntity entity) {
        // Transform MovieEntity into Movie
        Movie movie = new Movie();
        movie.setAdult(entity.isAdult());
        movie.setBackdropPath(entity.getBackdropPath());
        movie.setId(entity.getId());
        movie.setOriginalLanguage(entity.getOriginalLanguage());
        movie.setOriginalTitle(entity.getOriginalTitle());
        movie.setOverview(entity.getOverview());
        movie.setPopularity(entity.getPopularity());
        movie.setPosterPath(entity.getPosterPath());
        movie.setReleaseDate(entity.getReleaseDate());
        movie.setTitle(entity.getTitle());
        movie.setVideo(entity.isVideo());
        movie.setVoteAverage(entity.getVoteAverage());
        movie.setVoteCount(entity.getVoteCount());
        movie.setIsFavorite(entity.getIsFavorite());
        return movie;
    }
}
