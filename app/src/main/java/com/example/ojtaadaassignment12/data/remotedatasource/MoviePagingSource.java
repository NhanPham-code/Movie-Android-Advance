package com.example.ojtaadaassignment12.data.remotedatasource;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.paging.PagingState;
import androidx.paging.rxjava3.RxPagingSource;


import com.example.ojtaadaassignment12.data.localdatasource.FavoriteMovieDao;
import com.example.ojtaadaassignment12.data.remotedatasource.api.MovieApiService;
import com.example.ojtaadaassignment12.domain.models.Movie;
import com.example.ojtaadaassignment12.domain.models.Page;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;


@Singleton
public class MoviePagingSource extends RxPagingSource<Integer, Movie> {

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
    public Integer getRefreshKey(@NonNull PagingState<Integer, Movie> pagingState) {
        return null;
    }


    @NonNull
    @Override
    public Single<LoadResult<Integer, Movie>> loadSingle(@NonNull LoadParams<Integer> loadParams) {

        //movieApiService = RetrofitClient.getInstance().getMovieApiService();

        int page = loadParams.getKey() != null ? loadParams.getKey() : 1;

        return movieApiService.getMoviesByCategory(category, page)
                .subscribeOn(Schedulers.io())
                .map(response -> toLoadResult(response, page))
                .onErrorReturn(LoadResult.Error::new);
    }


    private LoadResult<Integer, Movie> toLoadResult(Page response, int page) {
        List<Movie> movies = response.getResults();
        for(Movie m : movies) {
            m.setIsFavorite(movieDao.isFavoriteMovie(m.getId()) ? 1 : 0);
        }
        return new LoadResult.Page<>(
                movies,
                page == 1 ? null : page - 1, // page before current page
                page < response.getTotalPages() ? page + 1 : null // page after current page
        );
    }
}
