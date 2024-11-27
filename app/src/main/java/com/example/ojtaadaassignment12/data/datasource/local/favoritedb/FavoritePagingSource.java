package com.example.ojtaadaassignment12.data.datasource.local.favoritedb;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.paging.PagingState;
import androidx.paging.rxjava3.RxPagingSource;

import com.example.ojtaadaassignment12.data.entities.MovieEntity;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class FavoritePagingSource extends RxPagingSource<Integer, MovieEntity> {

    private final FavoriteMovieDao favoriteMovieDao;

    private String titleSearch;

    @Inject
    public FavoritePagingSource(FavoriteMovieDao favoriteMovieDao) {
        this.favoriteMovieDao = favoriteMovieDao;
    }

    /**
     * Set the name key to search favorite movie list
     *
     * @param title: movie title
     */
    public void setTitleSearch(String title) {
        this.titleSearch = title;
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

        // page size is 5 is config in the PagingConfig in MovieRepositoryImpl
        int pageSize = loadParams.getLoadSize();

        Single<List<MovieEntity>> movieListSingle;

        // check if the title search is empty or null
        if (titleSearch == null || titleSearch.isEmpty()) {
            // get all favorite movies from local database
            movieListSingle = favoriteMovieDao.getFavoriteMovies();
        } else {
            // search favorite movies by name key from local database
            movieListSingle = favoriteMovieDao.searchFavoriteMoviesByTitle("%" + titleSearch + "%");
        }

        return movieListSingle
                .subscribeOn(Schedulers.io())
                .map(favoriteMovieList -> toLoadResult(favoriteMovieList, page, pageSize))
                .onErrorReturn(LoadResult.Error::new);
    }


    private LoadResult<Integer, MovieEntity> toLoadResult(List<MovieEntity> favoriteMovieList, int page, int pageSize) {

        // page size is 5 is config in the PagingConfig in MovieRepositoryImpl
        int fromIndex = (page - 1) * pageSize;
        int toIndex = Math.min(fromIndex + pageSize, favoriteMovieList.size());

        // sublist is a part of the favorite movie list that will be displayed on the current page
        // first time: page = 1, fromIndex = 0, toIndex = 5, get items (0, 1, 2, 3, 4)
        List<MovieEntity> sublist = favoriteMovieList.subList(fromIndex, toIndex);

        Integer nextPage = toIndex < favoriteMovieList.size() ? page + 1 : null;

        return new LoadResult.Page<>(
                sublist,
                page == 1 ? null : page - 1,
                nextPage
        );
    }
}
