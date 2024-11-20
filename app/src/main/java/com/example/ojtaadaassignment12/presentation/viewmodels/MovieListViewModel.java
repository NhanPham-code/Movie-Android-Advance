package com.example.ojtaadaassignment12.presentation.viewmodels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelKt;
import androidx.paging.PagingData;
import androidx.paging.rxjava3.PagingRx;

import com.example.ojtaadaassignment12.domain.models.Movie;
import com.example.ojtaadaassignment12.domain.usecase.FavoriteUseCase;
import com.example.ojtaadaassignment12.domain.usecase.GetMovieUseCase;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;


@Singleton
public class MovieListViewModel extends ViewModel {

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private final MutableLiveData<PagingData<Movie>> movieList = new MutableLiveData<>();
    private final MutableLiveData<PagingData<Movie>> favoriteList = new MutableLiveData<>();
    private Flowable<PagingData<Movie>> cachedMovies;

    // use to notify movie list adapter to update the favorite icon
    private final MutableLiveData<Movie> updateFavoriteMovie = new MutableLiveData<>();

    // use to notify main activity to update the favorite tag size
    private final MutableLiveData<Integer> favoriteMoviesCount = new MutableLiveData<>();

    GetMovieUseCase getMovieUseCase;

    FavoriteUseCase favoriteUseCase;


    String category = "popular"; // default category

    @Inject
    public MovieListViewModel(GetMovieUseCase getMovieUseCase, FavoriteUseCase favoriteUseCase) {
        // old code (don't need to use)
        //this.movieRepository = new MovieRepository()

        // use dagger to inject the movie use case
        this.getMovieUseCase = getMovieUseCase;

        this.favoriteUseCase = favoriteUseCase;
    }

    /**
     * get movie list from api
     *
     * @return movie list
     */
    public LiveData<PagingData<Movie>> getMovieList() {
        return movieList;
    }

    /**
     * Get favorite movie list from database
     *
     * @return favorite movie list
     */
    public MutableLiveData<PagingData<Movie>> getFavoriteList() {
        return favoriteList;
    }

    /**
     * Get the updated favorite movie
     *
     * @return updated favorite movie
     */
    public MutableLiveData<Movie> getUpdateFavoriteMovie() {
        return updateFavoriteMovie;
    }

    /**
     * Get the favorite movies count
     *
     * @return favorite movies count
     */
    public MutableLiveData<Integer> getFavoriteMoviesCount() {
        return favoriteMoviesCount;
    }

    /**
     * Fetch movies from the repository and set the value of movieList
     */
    public void getMovieListFromApi() {
        if (cachedMovies == null) {
            // Fetch movies from the MovieService
            Flowable<PagingData<Movie>> movies = getMovieUseCase.execute(category);

            // Cache the movies to avoid fetching them again when the activity is recreated
            cachedMovies = PagingRx.cachedIn(movies, ViewModelKt.getViewModelScope(this));

            Disposable disposable = cachedMovies
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            movieList::setValue, // set the value of movieList
                            throwable -> Log.d("check", "fetchMovies: " + throwable)
                    );
            compositeDisposable.add(disposable);
        }
    }

    /**
     * get favorite movie list from database
     */
    public void getFavoriteListFromDb() {
        if (cachedMovies != null) {
            Flowable<PagingData<Movie>> movies = favoriteUseCase.getFavoriteMovies();

            cachedMovies = PagingRx.cachedIn(movies, ViewModelKt.getViewModelScope(this));

            Disposable disposable = cachedMovies
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            favoriteList::setValue,
                            throwable -> Log.d("check", "fetchFavoriteMovies: " + throwable)
                    );
            compositeDisposable.add(disposable);
        }
    }

    /**
     * get favorite movies count from database after insert or delete favorite movie
     */
    public void getFavoriteMovieListSizeFromDb() {
        Disposable disposable = favoriteUseCase.getFavoriteMoviesCount()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        favoriteMoviesCount::setValue,
                        throwable -> Log.d("check", "getFavoriteMoviesCount: " + throwable)
                );
        compositeDisposable.add(disposable);
    }

    /**
     * insert favorite movie to database
     *
     * @param movie: favorite movie
     */
    public void insertFavoriteMovie(Movie movie) {
        Disposable disposable = Completable.fromAction(() -> favoriteUseCase.insertFavoriteMovie(movie))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> {
                            Log.d("check", "insertFavoriteMovie: success");
                            // get favorite movie list from database after insert favorite movie
                            getFavoriteListFromDb();
                            // get favorite movies count from database after insert favorite movie
                            getFavoriteMovieListSizeFromDb();
                        },

                        throwable -> Log.d("check", "insertFavoriteMovie: " + throwable)
                );
        compositeDisposable.add(disposable);

        updateFavoriteMovie.setValue(movie);
    }

    /**
     * delete favorite movie from database
     *
     * @param movie: favorite movie
     */
    public void deleteFavoriteMovie(Movie movie) {
        Disposable disposable = Completable.fromAction(() -> favoriteUseCase.deleteFavoriteMovie(movie))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> {
                            Log.d("check", "insertFavoriteMovie: success");
                            // get favorite movie list from database after insert favorite movie
                            getFavoriteListFromDb();
                            // get favorite movies count from database after insert favorite movie
                            getFavoriteMovieListSizeFromDb();
                        },

                        throwable -> Log.d("check", "insertFavoriteMovie: " + throwable)
                );
        compositeDisposable.add(disposable);

        updateFavoriteMovie.setValue(movie);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.clear();
    }
}
