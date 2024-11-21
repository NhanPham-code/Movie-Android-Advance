package com.example.ojtaadaassignment12.presentation.views.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ojtaadaassignment12.R;
import com.example.ojtaadaassignment12.databinding.FragmentMovieDetailBinding;
import com.example.ojtaadaassignment12.di.MyApplication;
import com.example.ojtaadaassignment12.domain.models.Movie;
import com.example.ojtaadaassignment12.presentation.viewmodels.MovieDetailViewModel;
import com.example.ojtaadaassignment12.presentation.viewmodels.MovieListViewModel;
import com.example.ojtaadaassignment12.presentation.views.adapters.CastAdapter;
import com.example.ojtaadaassignment12.util.Constant;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;


public class MovieDetailFragment extends Fragment {

    FragmentMovieDetailBinding binding;

    Picasso picasso;

    @Inject
    MovieDetailViewModel movieDetailViewModel;

    @Inject
    MovieListViewModel movieListViewModel; // use to update favorite and unfavorite movie

    public MovieDetailFragment() {
        // Required empty public constructor
    }


    public static MovieDetailFragment newInstance() {
        MovieDetailFragment fragment = new MovieDetailFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // inject MovieListComponent by MyApplication
        ((MyApplication) requireActivity().getApplication()).appComponent.injectDetailFragment(this);
        picasso = Picasso.get();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentMovieDetailBinding.inflate(inflater, container, false);

        // get movie detail from view model
        Movie movie = movieDetailViewModel.getMovieDetailMutableLiveData().getValue();

        if (movie != null) {
            // fill data to view
            binding.tvReleaseDate.setText(movie.getTitle());
            binding.tvRating.setText(String.valueOf(movie.getVoteAverage()));
            binding.tvOverview.setText(movie.getOverview());
            picasso.load(Constant.IMAGE_BASE_URL + movie.getPosterPath())
                    .placeholder(R.drawable.baseline_image_24)
                    .error(R.drawable.baseline_image_not_supported_24)
                    .into(binding.ivMoviePoster);
            if (movie.getIsFavorite() == 0) {
                binding.ivFavorite.setImageResource(R.drawable.ic_star);
            } else {
                binding.ivFavorite.setImageResource(R.drawable.ic_star_favorite);
            }

            // get cast and crew of a movie
            movieDetailViewModel.getCastAndCrewFromApi(movie.getId());
            movieDetailViewModel.getCastListMutableLiveData().observe(getViewLifecycleOwner(), casts -> {
                // set cast and crew to recycler view
                CastAdapter castAdapter = new CastAdapter(casts);
                binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
                binding.recyclerView.setAdapter(castAdapter);
            });

            // set favorite button click listener
            binding.ivFavorite.setOnClickListener(v -> {
                if (movie.getIsFavorite() == 0) {
                    movie.setIsFavorite(1);
                    binding.ivFavorite.setImageResource(R.drawable.ic_star_favorite);

                    // add favorite movie to database
                    movieListViewModel.insertFavoriteMovie(movie);
                } else {
                    movie.setIsFavorite(0);
                    binding.ivFavorite.setImageResource(R.drawable.ic_star);

                    // remove favorite movie from database
                    movieListViewModel.deleteFavoriteMovie(movie);
                }
            });
        }

        return binding.getRoot();
    }
}