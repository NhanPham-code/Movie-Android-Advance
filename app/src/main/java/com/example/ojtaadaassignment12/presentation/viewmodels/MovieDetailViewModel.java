package com.example.ojtaadaassignment12.presentation.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.ojtaadaassignment12.domain.models.Cast;
import com.example.ojtaadaassignment12.domain.models.Movie;
import com.example.ojtaadaassignment12.domain.usecase.GetCastAndCrewUseCase;
import com.example.ojtaadaassignment12.domain.usecase.GetMovieDetailByIdUseCase;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

@Singleton
public class MovieDetailViewModel extends ViewModel {

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private final MutableLiveData<Movie> movieDetailMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<Cast>> castListMutableLiveData = new MutableLiveData<>();


    // use cases
    private final GetCastAndCrewUseCase getCastAndCrewUseCase;
    private final GetMovieDetailByIdUseCase getMovieDetailByIdUseCase;

    @Inject
    public MovieDetailViewModel(GetCastAndCrewUseCase getCastAndCrewUseCase, GetMovieDetailByIdUseCase getMovieDetailByIdUseCase) {
        this.getCastAndCrewUseCase = getCastAndCrewUseCase;
        this.getMovieDetailByIdUseCase = getMovieDetailByIdUseCase;
    }

    /**
     * Get movie live data
     */
    public MutableLiveData<Movie> getMovieDetailMutableLiveData() {
        return movieDetailMutableLiveData;
    }

    /**
     * Set movie live data
     */
    public void setMovieDetailMutableLiveData(Movie movie) {
        this.movieDetailMutableLiveData.setValue(movie);
    }

    /**
     * Get cast live data
     */
    public MutableLiveData<List<Cast>> getCastListMutableLiveData() {
        return castListMutableLiveData;
    }


    /**
     * Get cast and crew of a movie from API
     *
     * @param movieId: id of the movie
     */
    public void getCastAndCrewFromApi(long movieId) {
        Single<List<Cast>> castList = getCastAndCrewUseCase.getCastAndCrew(movieId);

        Disposable disposable = castList
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        castListMutableLiveData::setValue,
                        throwable -> {
                            // handle error
                        }
                );

        compositeDisposable.add(disposable);
    }

    /**
     * Get movie detail from API
     *
     * @param movieId: id of the movie
     */
    public void getMovieDetailFromApi(long movieId, int isFavoriteOfMovie) {
        Single<Movie> movieDetail = getMovieDetailByIdUseCase.getMovieDetailById(movieId);

        Disposable disposable = movieDetail
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        // set to movie detail live data, so that fragment can observe this live data
                        // move to movie detail fragment
                        movie -> {
                            // set is favorite for movie detail call from API (sync favorite status)
                            movie.setIsFavorite(isFavoriteOfMovie);
                            // set movie detail to live data to observe in common fragment to move to detail fragment
                            setMovieDetailMutableLiveData(movie);
                        },
                        throwable -> {
                            // handle error
                        }
                );

        compositeDisposable.add(disposable);
    }
}
