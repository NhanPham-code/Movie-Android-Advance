package com.example.ojtaadaassignment12.presentation.views.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.ojtaadaassignment12.R;
import com.example.ojtaadaassignment12.databinding.FragmentCommonBinding;
import com.example.ojtaadaassignment12.di.MyApplication;
import com.example.ojtaadaassignment12.presentation.MainActivity;
import com.example.ojtaadaassignment12.presentation.viewmodels.MovieDetailViewModel;

import javax.inject.Inject;


public class CommonFragment extends Fragment {

    FragmentCommonBinding fragmentCommonBinding;
    NavController navController;

    MainFragment mainFragment;
    MovieListFragment movieListFragment;
    MovieDetailFragment movieDetailFragment;

    public void setMainFragment(MainFragment mainFragment) {
        this.mainFragment = mainFragment;
    }

    @Inject
    MovieDetailViewModel movieDetailViewModel;

    public CommonFragment() {
        // Required empty public constructor
    }

    public static CommonFragment newInstance() {
        CommonFragment fragment = new CommonFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // inject MovieListComponent by MyApplication
        ((MyApplication) requireActivity().getApplication()).appComponent.injectCommonFragment(this);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentCommonBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_common, container, false);

        // Initialize NavController
        NavHostFragment navHostFragment = (NavHostFragment) getChildFragmentManager().findFragmentById(R.id.nav_host_fragment);
        if (navHostFragment != null) {
            Log.d("TAG", "onViewCreated: " + "navHostFragment is not null");
            navController = navHostFragment.getNavController();
        }

        // Restore the state of the NavController if it exists in the bundle
        if (savedInstanceState != null) {
            // restore the state of the NavController (rote screen)
            navController.restoreState(savedInstanceState);
        }

        // observe movie detail view model to navigate to movie detail screen
        movieDetailViewModel.getMovieDetailMutableLiveData().observe(getViewLifecycleOwner(), movie -> {
            if (movie != null) {
                navController.navigate(R.id.movieDetailFragment);
            }
        });

        // Notify MainActivity about navigation changes
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            if (destination.getId() == R.id.movieDetailFragment) {
                mainFragment.showBackButton();
            } else {
                mainFragment.showDrawerToggle();
            }
        });

        // handle back button press
        requireActivity().getOnBackPressedDispatcher().addCallback(requireActivity(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (navController.getCurrentDestination().getId() == R.id.movieDetailFragment) {
                    navController.popBackStack();
                } else {
                    requireActivity().finish();
                }
            }
        });

        return fragmentCommonBinding.getRoot();
    }


    /**
     * Save the state of the NavController in the bundle. This is necessary to restore the state of the NavController
     * User rote screen orientation change.
     *
     * @param outState Bundle in which to place your saved state.
     */
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Bundle navState = navController.saveState();
        outState.putBundle("nav_state", navState);
    }
}