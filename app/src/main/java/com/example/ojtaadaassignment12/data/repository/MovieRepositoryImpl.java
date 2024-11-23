package com.example.ojtaadaassignment12.data.repository;

import androidx.paging.Pager;
import androidx.paging.PagingConfig;
import androidx.paging.PagingData;
import androidx.paging.PagingDataTransforms;
import androidx.paging.rxjava3.PagingRx;

import com.example.ojtaadaassignment12.data.datasource.local.FavoriteMovieDao;
import com.example.ojtaadaassignment12.data.datasource.local.FavoritePagingSource;
import com.example.ojtaadaassignment12.data.datasource.remote.MoviePagingSource;
import com.example.ojtaadaassignment12.data.entities.MovieEntity;
import com.example.ojtaadaassignment12.data.mapper.MovieMapper;
import com.example.ojtaadaassignment12.domain.models.Movie;
import com.example.ojtaadaassignment12.domain.repository.IMovieRepository;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

@Singleton
public class MovieRepositoryImpl implements IMovieRepository {

    // Network data source
    MoviePagingSource moviePagingSource;

    // Local data source
    FavoritePagingSource favoritePagingSource;

    // Use to get favorite movie list from local database
    FavoriteMovieDao favoriteMovieDao;

    @Inject
    public MovieRepositoryImpl(MoviePagingSource moviePagingSource, FavoritePagingSource favoritePagingSource, FavoriteMovieDao favoriteMovieDao) {
        // use dagger to inject the network page source
        this.moviePagingSource = moviePagingSource;

        // use dagger to inject the local page source
        this.favoritePagingSource = favoritePagingSource;

        // use dagger to inject the favorite movie dao
        this.favoriteMovieDao = favoriteMovieDao;
    }

    /**
     * Get movie list by category from API
     *
     * @param category: movie category
     * @return Flowable<PagingData < Movie>> movie list
     */
    @Override
    public Flowable<PagingData<Movie>> getMovies(String category) {

        Pager<Integer, MovieEntity> pager = new Pager<>(
                new PagingConfig(
                        5,
                        1,
                        false,
                        5,
                        10
                ), () -> {
            moviePagingSource.setCategory(category);
            return moviePagingSource;
        });

        // Map the MovieEntity -> Movie using PagingDataTransforms.map
        return PagingRx.getFlowable(pager)
                .map(pagingData -> PagingDataTransforms.map(pagingData, Runnable::run, MovieMapper::toDomain));
    }


    /**
     * Get favorite movie list from local database
     *
     * @return Flowable<PagingData < Movie>> favorite movie list
     */
    @Override
    public Flowable<PagingData<Movie>> getFavoriteMovies() {

        Pager<Integer, MovieEntity> pager = new Pager<>(
                new PagingConfig(
                        5,
                        1,
                        false,
                        5,
                        10
                ), () -> favoritePagingSource
        );

        // Map the MovieEntity -> Movie using PagingDataTransforms.map
        return PagingRx.getFlowable(pager)
                .map(pagingData -> PagingDataTransforms.map(pagingData, Runnable::run, MovieMapper::toDomain));
    }

    /**
     * Get favorite movie list by title from local database
     *
     * @param title: movie title
     * @return Flowable<PagingData < Movie>> favorite movie list
     */
    @Override
    public Flowable<PagingData<Movie>> getFavoriteMoviesByTitle(String title) {

        favoritePagingSource.setTitleSearch(title);

        Pager<Integer, MovieEntity> pager = new Pager<>(
                new PagingConfig(
                        5,
                        1,
                        false,
                        5,
                        10
                ), () -> favoritePagingSource
        );

        // Map the MovieEntity -> Movie using PagingDataTransforms.map
        return PagingRx.getFlowable(pager)
                .map(pagingData -> PagingDataTransforms.map(pagingData, Runnable::run, MovieMapper::toDomain));
    }


    /**
     * Insert favorite movie to local database
     *
     * @param movie: favorite movie
     */
    @Override
    public void insertFavoriteMovie(Movie movie) {
        favoriteMovieDao.insertFavoriteMovie(MovieMapper.toEntity(movie));
    }

    /**
     * Delete favorite movie from local database
     *
     * @param movie: favorite movie
     */
    @Override
    public void deleteFavoriteMovie(Movie movie) {
        favoriteMovieDao.deleteFavoriteMovieById(movie.getId());
    }

    /**
     * Get favorite movie count from local database
     *
     * @return Single<Integer> favorite movie count
     */
    @Override
    public Single<Integer> getFavoriteMoviesCount() {
        return favoriteMovieDao.getFavoriteMoviesCount();
    }

}
