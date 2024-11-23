package com.example.ojtaadaassignment12.di;

import com.example.ojtaadaassignment12.data.datasource.local.FavoriteMovieDao;
import com.example.ojtaadaassignment12.data.datasource.local.FavoritePagingSource;
import com.example.ojtaadaassignment12.data.datasource.remote.MoviePagingSource;
import com.example.ojtaadaassignment12.data.repository.MovieRepositoryImpl;
import com.example.ojtaadaassignment12.domain.repository.IMovieRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class MovieListModule {

    @Provides
    @Singleton
    public IMovieRepository movieRepository(MoviePagingSource moviePagingSource, FavoritePagingSource favoritePagingSource, FavoriteMovieDao favoriteMovieDao) {
        return new MovieRepositoryImpl(moviePagingSource, favoritePagingSource, favoriteMovieDao);
    }

}
