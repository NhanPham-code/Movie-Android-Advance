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

        // Initialize RecyclerView
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        movieListAdapter = new MovieListAdapter();
        //set view model for movie list adapter
        movieListAdapter.setMovieListViewModel(movieListViewModel);
        // set view model for movie list adapter
        movieListAdapter.setMovieDetailViewModel(movieDetailViewModel);
        // add load state footer to show loading state in the footer
        binding.recyclerView.setAdapter(movieListAdapter.withLoadStateFooter(new LoadingStateAdapter()));


        // Fetch movie list from API
        movieListViewModel.getMovieListFromApi(category);
        // Observe movie list to update UI after get data from API
        movieListViewModel.getMovieList().observe(getViewLifecycleOwner(), movies -> {
            movieListAdapter.submitData(getLifecycle(), movies);
            binding.idPBLoading.setVisibility(View.GONE);
        });


        // observe category to load movie list by category in option menu in MainActivity
        movieListViewModel.getCategory().observe(getViewLifecycleOwner(), category -> {
            this.category = category;
            movieListViewModel.getMovieListFromApi(category);
        });


        // Observe favorite movie to update UI (icon) when favorite movie is updated
        movieListViewModel.getUpdateFavoriteMovie().observe(getViewLifecycleOwner(), movie -> {
            movieListAdapter.updateItem(movie);
        });


        // pull to refresh
        binding.swipeRefreshLayout.setOnRefreshListener(() -> {
            movieListViewModel.getMovieListFromApi(category);
            binding.swipeRefreshLayout.setRefreshing(false);
        });

        // observe to change layout of movie list
        movieListViewModel.getGrid().observe(getViewLifecycleOwner(), this::changeLayout);

        return binding.getRoot();
    }

    /**
     * Load movie list by category (popular, top_rated, upcoming)
     *
     * @param category: category of movie list
     */
    public void loadMovieListByCategory(String category) {
        this.category = category;
        movieListViewModel.getMovieListFromApi(category);
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