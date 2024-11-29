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
import com.example.ojtaadaassignment12.domain.models.Movie;
import com.example.ojtaadaassignment12.presentation.viewmodels.MovieDetailViewModel;

import javax.inject.Inject;


public class CommonFragment extends Fragment {

    FragmentCommonBinding fragmentCommonBinding;
    NavController navController;

    MainFragment mainFragment;


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

            // restore the state of the MainFragment
            String mainFragmentTag = savedInstanceState.getString("mainFragmentTag");
            if (mainFragmentTag != null) {
                mainFragment = (MainFragment) getParentFragmentManager().findFragmentByTag(mainFragmentTag);
            }
        }

        // observe movie detail view model to navigate to movie detail screen (click movie item or click reminder item)
        movieDetailViewModel.getMovieDetailMutableLiveData().observe(getViewLifecycleOwner(), movie -> {
            if (movie != null && movie.getId() != 0) {
                if (navController.getCurrentDestination().getId() == R.id.movieListFragment) {
                    navController.navigate(R.id.action_movieListFragment_to_movieDetailFragment);

                    mainFragment.moveToTab(0);
                } else {
                    // Detail fragment is show in tab 0  -> move to tab 0
                    mainFragment.moveToTab(0);
                }
            }
        });

        // Notify Main Fragment about navigation changes to update Appbar
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            if (destination.getId() == R.id.movieDetailFragment) {
                // in detail fragment
                // set appbar in detail fragment
                if(mainFragment != null) {
                    mainFragment.setAppbarInDetailFragment();

                    mainFragment.setIsDetailFragmentShow(true);
                }
            } else {
                // back to movie list fragment
                // set detail movie in viewmodel is empty for next time click (avoid navigate to detail fragment when rotate screen)
                movieDetailViewModel.setMovieDetailMutableLiveData(new Movie());

                // set appbar in list fragment
                if(mainFragment != null) {
                    mainFragment.setAppbarInListFragment();

                    mainFragment.setIsDetailFragmentShow(false);
                }
            }
        });

        // handle back button press
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (navController.getCurrentDestination().getId() == R.id.movieDetailFragment) {
                    navController.popBackStack(R.id.movieListFragment, false);

                    mainFragment.moveToTab(0);
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
     * NavController will handle the state of the fragments
     *
     * @param outState Bundle in which to place your saved state.
     */
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (navController != null) {
            Bundle navState = navController.saveState();
            outState.putBundle("nav_state", navState);
        }

        if (mainFragment != null && mainFragment.isAdded()) {
            outState.putString("mainFragmentTag", mainFragment.getTag());
        }
    }
}