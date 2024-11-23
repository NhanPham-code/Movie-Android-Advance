package com.example.ojtaadaassignment12.presentation.views.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;


import com.example.ojtaadaassignment12.di.MyApplication;
import com.example.ojtaadaassignment12.databinding.FragmentMovieListBinding;
import com.example.ojtaadaassignment12.presentation.viewmodels.MovieDetailViewModel;
import com.example.ojtaadaassignment12.presentation.viewmodels.MovieListViewModel;
import com.example.ojtaadaassignment12.presentation.views.adapters.LoadingStateAdapter;
import com.example.ojtaadaassignment12.presentation.views.adapters.MovieListAdapter;

import javax.inject.Inject;


public class MovieListFragment extends Fragment {

    FragmentMovieListBinding binding;

    MovieListAdapter movieListAdapter;

    String category = "popular"; // default category for first time

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
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment (view binding)
        binding = FragmentMovieListBinding.inflate(inflater, container, false);

        // Restore category if state exists
        if (savedInstanceState != null) {
            category = savedInstanceState.getString("category", "popular");
        }

        // Initialize RecyclerView
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        movieListAdapter = new MovieListAdapter();
        // Set ViewModel for the movie list adapter
        movieListAdapter.setMovieListViewModel(movieListViewModel);
        movieListAdapter.setMovieDetailViewModel(movieDetailViewModel);
        // Add load state footer to show loading state in the footer
        binding.recyclerView.setAdapter(movieListAdapter.withLoadStateFooter(new LoadingStateAdapter()));

        // Check if data is already loaded; fetch from API only if not
        if (savedInstanceState == null && movieListViewModel.getMovieList().getValue() == null) {
            movieListViewModel.getMovieListFromApi(category);
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
                movieListViewModel.getMovieListFromApi(newCategory);
                binding.idPBLoading.setVisibility(View.VISIBLE);
            }
        });

        // Observe favorite movie to update UI (icon) when the favorite movie is updated
        movieListViewModel.getUpdateFavoriteMovie().observe(getViewLifecycleOwner(), movie -> {
            movieListAdapter.updateItem(movie);
        });

        // Pull to refresh
        binding.swipeRefreshLayout.setOnRefreshListener(() -> {
            movieListViewModel.getMovieListFromApi(category);
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
}