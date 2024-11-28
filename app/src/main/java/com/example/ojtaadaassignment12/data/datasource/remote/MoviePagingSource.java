package com.example.ojtaadaassignment12.data.datasource.remote;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.paging.PagingState;
import androidx.paging.rxjava3.RxPagingSource;


import com.example.ojtaadaassignment12.data.datasource.local.favoritedb.FavoriteMovieDao;
import com.example.ojtaadaassignment12.data.datasource.remote.api.MovieApiService;
import com.example.ojtaadaassignment12.data.entities.MovieEntity;
import com.example.ojtaadaassignment12.data.entities.PageEntity;

import java.util.ArrayList;
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
    private String rating;
    private String releaseYear;
    private String sortBy;

    @Inject
    public MoviePagingSource(MovieApiService movieApiService, FavoriteMovieDao movieDao) {
        this.movieApiService = movieApiService;
        this.movieDao = movieDao;
    }

    public void setCategory(String category) {
        this.category = category;
    }
    public void setRating(String rating) {
        this.rating = rating;
    }
    public void setReleaseYear(String releaseYear) {
        this.releaseYear = releaseYear;
    }
    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
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
            // movie list from API
            List<MovieEntity> movies = response.getResults();

            // filter movie list by rating, release year
            List<MovieEntity> filteredMovies = new ArrayList<>();

            for (MovieEntity movie : movies) {
                if (movie.getVoteAverage() >= Double.parseDouble(rating) &&
                        Integer.parseInt(movie.getReleaseDate().substring(0, 4)) >= Integer.parseInt(releaseYear)) {
                    filteredMovies.add(movie);
                }
            }

            // sort movie list
            if (sortBy.equals("Release Year")) {
                filteredMovies.sort((o1, o2) -> o2.getReleaseDate().compareTo(o1.getReleaseDate()));
            } else {
                filteredMovies.sort((o1, o2) -> Double.compare(o2.getVoteAverage(), o1.getVoteAverage()));
            }


            // sync favorite movies with movies
            for (MovieEntity movie : filteredMovies) {
                for (MovieEntity favoriteMovie : favoriteMovies) {
                    if (movie.getId() == favoriteMovie.getId()) {
                        movie.setIsFavorite(1);
                        break;
                    }
                }
            }


            return new LoadResult.Page<>(
                    filteredMovies,
                    page == 1 ? null : page - 1, // page before current page
                    page < response.getTotalPages() ? page + 1 : null // page after current page
            );
        });
    }

    public Single<MovieEntity> getMovieDetailById(long movieId) {
        return movieApiService.getMovieDetail(movieId)
                .subscribeOn(Schedulers.io());
    }
}
