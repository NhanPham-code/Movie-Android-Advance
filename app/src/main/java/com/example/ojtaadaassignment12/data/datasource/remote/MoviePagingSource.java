package com.example.ojtaadaassignment12.data.datasource.remote;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.paging.PagingState;
import androidx.paging.rxjava3.RxPagingSource;


import com.example.ojtaadaassignment12.data.datasource.local.FavoriteMovieDao;
import com.example.ojtaadaassignment12.data.datasource.remote.api.MovieApiService;
import com.example.ojtaadaassignment12.data.entities.MovieEntity;
import com.example.ojtaadaassignment12.data.entities.PageEntity;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;


@Singleton
public class MoviePagingSource extends RxPagingSource<Integer, MovieEntity> {

    MovieApiService movieApiService;

    FavoriteMovieDao movieDao;

    private String category;

    @Inject
    public MoviePagingSource(MovieApiService movieApiService, FavoriteMovieDao movieDao) {
        this.movieApiService = movieApiService;
        this.movieDao = movieDao;
    }

    public void setCategory(String category) {
        this.category = category;
    }


    @Nullable
    @Override
    public Integer getRefreshKey(@NonNull PagingState<Integer, MovieEntity> pagingState) {
        return null;
    }


    @NonNull
    @Override
    public Single<LoadResult<Integer, MovieEntity>> loadSingle(@NonNull LoadParams<Integer> loadParams) {

        int page = loadParams.getKey() != null ? loadParams.getKey() : 1;

        Single<PageEntity> moviesSingle = movieApiService.getMoviesByCategory(category, page)
                .subscribeOn(Schedulers.io());

        Single<List<MovieEntity>> favoriteMoviesSingle = movieDao.getFavoriteMovies()
                .subscribeOn(Schedulers.io());

        // use zip operator to combine the response of movies and favorite movies
        return Single.zip(moviesSingle, favoriteMoviesSingle, (response, favoriteMovies) -> {
            List<MovieEntity> movies = response.getResults();

            for (MovieEntity movie : movies) {
                for (MovieEntity favoriteMovie : favoriteMovies) {
                    if (movie.getId() == favoriteMovie.getId()) {
                        movie.setIsFavorite(1);
                        break;
                    }
                }
            }

            return new LoadResult.Page<>(
                    movies,
                    page == 1 ? null : page - 1, // page before current page
                    page < response.getTotalPages() ? page + 1 : null // page after current page
            );
        });
    }
}
