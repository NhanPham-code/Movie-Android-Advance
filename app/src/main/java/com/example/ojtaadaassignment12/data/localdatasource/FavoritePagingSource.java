package com.example.ojtaadaassignment12.data.localdatasource;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.paging.PagingState;
import androidx.paging.rxjava3.RxPagingSource;

import com.example.ojtaadaassignment12.domain.models.Movie;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class FavoritePagingSource extends RxPagingSource<Integer, Movie> {

    private final FavoriteMovieDao favoriteMovieDao;

    @Inject
    public FavoritePagingSource(FavoriteMovieDao favoriteMovieDao) {
        this.favoriteMovieDao = favoriteMovieDao;
    }

    @Nullable
    @Override
    public Integer getRefreshKey(@NonNull PagingState<Integer, Movie> pagingState) {
        return null;
    }

    @NonNull
    @Override
    public Single<LoadResult<Integer, Movie>> loadSingle(@NonNull LoadParams<Integer> loadParams) {


        int page = loadParams.getKey() != null ? loadParams.getKey() : 1;
        int pageSize = loadParams.getLoadSize();

        return favoriteMovieDao.getFavoriteMovies()
                .subscribeOn(Schedulers.io())
                .map(favoriteMovieList -> toLoadResult(favoriteMovieList, page, pageSize))
                .onErrorReturn(LoadResult.Error::new);
    }


    private LoadResult<Integer, Movie> toLoadResult(List<Movie> favoriteMovieList, int page, int pageSize) {
        int fromIndex = (page - 1) * pageSize;
        int toIndex = Math.min(fromIndex + pageSize, favoriteMovieList.size());

        List<Movie> sublist = favoriteMovieList.subList(fromIndex, toIndex);

        Integer nextPage = toIndex < favoriteMovieList.size() ? page + 1 : null;

        return new LoadResult.Page<>(
                sublist,
                page == 1 ? null : page - 1,
                nextPage
        );
    }
}
