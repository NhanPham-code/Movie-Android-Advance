package com.example.ojtaadaassignment12.presentation.views.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;


import com.example.ojtaadaassignment12.di.MyApplication;
import com.example.ojtaadaassignment12.databinding.FragmentMovieListBinding;
import com.example.ojtaadaassignment12.presentation.viewmodels.MovieDetailViewModel;
import com.example.ojtaadaassignment12.presentation.viewmodels.MovieListViewModel;
import com.example.ojtaadaassignment12.presentation.views.adapters.LoadingStateAdapter;
import com.example.ojtaadaassignment12.presentation.views.adapters.MovieListAdapter;

import javax.inject.Inject;


public class MovieListFragment extends Fragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    FragmentMovieListBinding binding;

    MovieListAdapter movieListAdapter;

    SharedPreferences sharedPreferences;
    private String category;
    private String rating;
    private String releaseYear;
    private String sortBy;

    @Inject
    MovieListViewModel movieListViewModel;

    @Inject
    MovieDetailViewModel movieDetailViewModel;


    public MovieListFragment() {
        // Required empty public constructor
    }


    public static MovieListFragment newInstance() {
        MovieListFragment fragment = new MovieListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // inject MovieListComponent by MyApplication
        ((MyApplication) requireActivity().getApplication()).appComponent.injectListFragment(this);

        // get shared preferences
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment (view binding)
        binding = FragmentMovieListBinding.inflate(inflater, container, false);

        // Get data settings from shared preferences
        getDataSettings();

        // Initialize RecyclerView
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        movieListAdapter = new MovieListAdapter();
        // Set ViewModel for the movie list adapter
        movieListAdapter.setMovieListViewModel(movieListViewModel);
        movieListAdapter.setMovieDetailViewModel(movieDetailViewModel);
        // Add load state footer to show loading state in the footer
        binding.recyclerView.setAdapter(movieListAdapter.withLoadStateFooter(new LoadingStateAdapter()));

        // Check if data is already loaded; if not, get data from the API (first time)
        if (savedInstanceState == null && movieListViewModel.getMovieList().getValue() == null) {
            // Get movie list by category and sort, filter
            movieListViewModel.getMovieListFromApi(category, rating, releaseYear, sortBy);
        }

        // Observe movie list to update UI after getting data from the API
        movieListViewModel.getMovieList().observe(getViewLifecycleOwner(), movies -> {
            movieListAdapter.submitData(getLifecycle(), movies);
            binding.idPBLoading.setVisibility(View.GONE);
        });

        // Observe category to load movie list by category in the option menu in MainActivity
        movieListViewModel.getCategory().observe(getViewLifecycleOwner(), newCategory -> {
            if (!newCategory.equals(this.category)) {
                this.category = newCategory;
                // Update movie list by category and sort, filter
                movieListViewModel.getMovieListFromApi(newCategory, rating, releaseYear, sortBy);
                binding.idPBLoading.setVisibility(View.VISIBLE);
            }
        });

        // Observe favorite movie to update UI (icon) when the favorite movie is updated
        movieListViewModel.getUpdateFavoriteMovie().observe(getViewLifecycleOwner(), movie -> {
            movieListAdapter.updateItem(movie);
        });

        //swipe to refresh
        binding.swipeRefreshLayout.setOnRefreshListener(() -> {
            movieListViewModel.getMovieListFromApi(category, rating, releaseYear, sortBy);
            binding.swipeRefreshLayout.setRefreshing(false);
        });

        // Observe to change layout of the movie list
        movieListViewModel.getGrid().observe(getViewLifecycleOwner(), this::changeLayout);

        return binding.getRoot();
    }



    /**
     * Change layout of movie list
     *
     * @param isGridLayout: true if grid layout, false if linear layout
     */
    public void changeLayout(boolean isGridLayout) {
        if (isGridLayout) {
            binding.recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        } else {
            binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        }
        movieListAdapter.setGridLayout(isGridLayout);
        binding.recyclerView.setAdapter(movieListAdapter);
    }

    /**
     * Get data settings from shared preferences
     */
    private void getDataSettings() {
        category = sharedPreferences.getString("category", "popular");
        rating = sharedPreferences.getString("rating", "1");
        releaseYear = sharedPreferences.getString("releaseYear", "1970");
        sortBy = sharedPreferences.getString("sortBy", "rating");
    }

    /**
     * Update movie list by category and sort, filter after changing settings
     * @param sharedPreferences shared preferences
     * @param s key of the changed setting
     */
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, @Nullable String s) {
        // Get data settings from shared preferences
        getDataSettings();

        // Update movie list by category and sort, filter
        movieListViewModel.getMovieListFromApi(category, rating, releaseYear, sortBy);
    }
}