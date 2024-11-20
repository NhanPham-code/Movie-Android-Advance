package com.example.ojtaadaassignment12.data.remotedatasource.api;

import com.example.ojtaadaassignment12.domain.models.Page;

import io.reactivex.rxjava3.core.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieApiService {
    // https://api.themoviedb.org/3/movie/popular?api_key=e7631ffcb8e766993e5ec0c1f4245f93&page=1

    // detail: api.themoviedb.org/3/movie/{movieId}?api_key=e7631ffcb8e766993e5ec0c1f4245f93

    // cast: api.themoviedb.org/3/movie/{movieId}/credits?api_key=e7631ffcb8e766993e5ec0c1f4245f93

    @GET("movie/{category}?api_key=e7631ffcb8e766993e5ec0c1f4245f93")
    Single<Page> getMoviesByCategory(@Path("category") String category, @Query("page") int page);

}
