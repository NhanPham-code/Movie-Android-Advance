package com.example.ojtaadaassignment12.presentation.views.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.paging.PagingData;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ojtaadaassignment12.databinding.FragmentMovieListBinding;
import com.example.ojtaadaassignment12.di.MyApplication;
import com.example.ojtaadaassignment12.domain.models.Movie;
import com.example.ojtaadaassignment12.presentation.viewmodels.MovieListViewModel;
import com.example.ojtaadaassignment12.presentation.views.adapters.LoadingStateAdapter;
import com.example.ojtaadaassignment12.presentation.views.adapters.MovieListAdapter;

import javax.inject.Inject;

public class FavoriteListFragment extends Fragment {

    FragmentMovieListBinding binding;

    MovieListAdapter movieListAdapter;

    @Inject
    MovieListViewModel movieListViewModel;

    public FavoriteListFragment() {
        // Required empty public constructor
    }

    public static FavoriteListFragment newInstance() {
        FavoriteListFragment fragment = new FavoriteListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // inject MovieListComponent by MyApplication
        ((MyApplication) requireActivity().getApplication()).appComponent.injectFavoriteListFragment(this);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentMovieListBinding.inflate(inflater, container, false);

        // Initialize RecyclerView
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        movieListAdapter = new MovieListAdapter();
        movieListAdapter.setMovieListViewModel(movieListViewModel);
        // add load state footer to show loading state in the footer
        binding.recyclerView.setAdapter(movieListAdapter.withLoadStateFooter(new LoadingStateAdapter()));

        // get data from local database by using FavoriteListViewModel
        movieListViewModel.getFavoriteListFromDb();
        movieListViewModel.getFavoriteList().observe(getViewLifecycleOwner(), movies -> {
            movieListAdapter.submitData(getLifecycle(), movies);

            binding.idPBLoading.setVisibility(View.GONE);
        });

        // get favorite movies count to set the favorite tag
        movieListViewModel.getFavoriteMovieListSizeFromDb();

        // pull to refresh
        binding.swipeRefreshLayout.setOnRefreshListener(() -> {
            movieListViewModel.getFavoriteListFromDb();
            binding.swipeRefreshLayout.setRefreshing(false);
        });


        return binding.getRoot();
    }

    /**
     * Search favorite movie by title
     * @param title: movie title
     */
    public void searchFavoriteMovie(String title) {
        movieListViewModel.searchFavoriteMovie(title);
    }
}