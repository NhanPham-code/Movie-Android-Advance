package com.example.ojtaadaassignment12.presentation.views.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.view.MenuHost;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.ojtaadaassignment12.R;
import com.example.ojtaadaassignment12.databinding.FragmentMainBinding;
import com.example.ojtaadaassignment12.di.MyApplication;
import com.example.ojtaadaassignment12.presentation.viewmodels.MovieDetailViewModel;
import com.example.ojtaadaassignment12.presentation.viewmodels.MovieListViewModel;
import com.example.ojtaadaassignment12.presentation.views.adapters.ViewPagerAdapter;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class MainFragment extends Fragment {

    private FragmentMainBinding binding;

    private boolean isGridLayout = false;

    private ArrayList<String> tabNameList;
    private ArrayList<Integer> tabIconList;

    // view pager, tab layout
    ViewPagerAdapter viewPagerAdapter;

    // drawer layout
    ActionBarDrawerToggle actionBarDrawerToggle;

    // fragments
    List<Fragment> fragmentList;
    CommonFragment commonFragment;
    FavoriteListFragment favoriteListFragment;
    SettingsFragment settingsFragment;
    AboutFragment aboutFragment;

    // use check visible options menu
    private boolean isOptionsMenuEnabled = true;

    // View models
    @Inject
    MovieListViewModel movieListViewModel;

    @Inject
    MovieDetailViewModel movieDetailViewModel;

    public MainFragment() {
        // Required empty public constructor
    }


    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((MyApplication) requireActivity().getApplication()).appComponent.injectMainFragment(this);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentMainBinding.inflate(inflater, container, false);

        // Set up toolbar
        setUpToolbar();

        // create fragments and set up view pager, tab layout
        if (savedInstanceState != null) {
            // restore fragments when activity is recreated
            commonFragment = (CommonFragment) requireActivity().getSupportFragmentManager().getFragment(savedInstanceState, "commonFragment");

            favoriteListFragment = (FavoriteListFragment) requireActivity().getSupportFragmentManager().getFragment(savedInstanceState, "favoriteListFragment");

            settingsFragment = (SettingsFragment) requireActivity().getSupportFragmentManager().getFragment(savedInstanceState, "settingsFragment");

            aboutFragment = (AboutFragment) requireActivity().getSupportFragmentManager().getFragment(savedInstanceState, "aboutFragment");

            // set up view pager and tab layout
            setUpTabLayoutAndViewPager();

        } else {
            // create fragments for first time running
            commonFragment = CommonFragment.newInstance();
            commonFragment.setMainFragment(this);

            favoriteListFragment = FavoriteListFragment.newInstance();

            settingsFragment = new SettingsFragment();

            aboutFragment = AboutFragment.newInstance();

            // set up view pager and tab layout
            setUpTabLayoutAndViewPager();
        }

        // set up change layout movie list button
        setBtnChangeLayout();

        // set up options menu
        setUpOptionMenu();

        // Set up tab change listener to change toolbar title
        setUpTabChangeListener();

        // Set favorite tab tag
        setFavoriteTabTag();

        // set up search button
        setUpSearchButton();

        // edit profile click
        setClickEditProfileButton();

        // show all reminder click
        setClickShowAllReminderButton();

        return binding.getRoot();
    }

    /**
     * Set up listener for show all reminder button
     */
    private void setClickShowAllReminderButton() {
        View headerView = binding.navigationView.getHeaderView(0);
        AppCompatButton showAllReminderButton = headerView.findViewById(R.id.btnShowAll);
        showAllReminderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle show all reminder button click
                Toast.makeText(getContext(), "Show All Reminder Clicked", Toast.LENGTH_SHORT).show();
                // call back to main activity to navigate to show all reminder screen
            }
        });
    }


    /**
     * Set up listener for edit profile button
     */
    private void setClickEditProfileButton() {
        View headerView = binding.navigationView.getHeaderView(0);
        AppCompatButton editProfileButton = headerView.findViewById(R.id.btnEdit);
        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle edit profile button click
                Toast.makeText(getContext(), "Edit Profile Clicked", Toast.LENGTH_SHORT).show();
                // call back to main activity to navigate to edit profile screen
            }
        });
    }

    /**
     * Save the state fragment when activity is recreated
     *
     * @param outState: bundle
     */
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        requireActivity().getSupportFragmentManager().putFragment(outState, "commonFragment", commonFragment);
        requireActivity().getSupportFragmentManager().putFragment(outState, "favoriteListFragment", favoriteListFragment);
        requireActivity().getSupportFragmentManager().putFragment(outState, "settingsFragment", settingsFragment);
        requireActivity().getSupportFragmentManager().putFragment(outState, "aboutFragment", aboutFragment);
    }


    /**
     * Set up listener search button
     */
    private void setUpSearchButton() {
        binding.searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchFavoriteMovie();
            }
        });
    }

    /**
     * Search favorite movie by name keyword
     */
    private void searchFavoriteMovie() {
        String search = binding.searchEditText.getText().toString();
        if (search.isEmpty()) {
            // show all favorite movies
            favoriteListFragment.searchFavoriteMovie(search);
            Toast.makeText(getContext(), "Please enter keyword to search", Toast.LENGTH_SHORT).show();
        } else {
            // search favorite movies by keyword
            favoriteListFragment.searchFavoriteMovie(search);
        }
    }


    /**
     * Set tag for favorite tab
     */
    private void setFavoriteTabTag() {
        // observe favorite movies count (size) to set the favorite tag
        // size will get in favorite list fragment
        movieListViewModel.getFavoriteMoviesCount().observe(getViewLifecycleOwner(), favoriteListSize -> {
            TabLayout.Tab tab = binding.tabLayout.getTabAt(1);
            if (tab != null) {
                BadgeDrawable badge = tab.getOrCreateBadge();
                badge.setVisible(true);
                badge.setNumber(favoriteListSize);
            }
        });
    }

    /**
     * Set up options menu
     */
    private void setUpOptionMenu() {
        MenuHost menuHost = (MenuHost) requireActivity();
        menuHost.addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.options_menu, menu);
            }

            @Override
            public void onPrepareMenu(@NonNull Menu menu) {
                MenuProvider.super.onPrepareMenu(menu);
                menu.setGroupVisible(R.id.menu_group, isOptionsMenuEnabled);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.menu_movie_popular) {
                    movieListViewModel.setCategory("popular");
                    return true;
                } else if (itemId == R.id.menu_movie_top_rated) {
                    movieListViewModel.setCategory("top_rated");
                    return true;
                } else if (itemId == R.id.menu_movie_upcoming) {
                    movieListViewModel.setCategory("upcoming");
                    return true;
                } else if (itemId == R.id.menu_movie_now_playing) {
                    movieListViewModel.setCategory("now_playing");
                    return true;
                } else {
                    return false;
                }
            }
        });
    }


    /**
     * Set up tab change listener to change toolbar title...
     */
    private void setUpTabChangeListener() {
        // Set up tab change listener
        binding.viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position) {
                    case 0: // Movie List
                        binding.toolbar.setTitle("Movies");
                        binding.changeLayoutButton.setVisibility(View.VISIBLE);
                        binding.searchEditText.setVisibility(View.GONE);
                        binding.searchButton.setVisibility(View.GONE);
                        isOptionsMenuEnabled = true;
                        break;
                    case 1: // Favorite List
                        binding.toolbar.setTitle("");
                        binding.changeLayoutButton.setVisibility(View.GONE);
                        binding.searchEditText.setVisibility(View.VISIBLE);
                        binding.searchEditText.setText("Favorite");
                        binding.searchButton.setVisibility(View.VISIBLE);
                        isOptionsMenuEnabled = false;
                        break;
                    case 2: // Setting
                        binding.toolbar.setTitle("Setting");
                        binding.changeLayoutButton.setVisibility(View.GONE);
                        binding.searchEditText.setVisibility(View.GONE);
                        binding.searchButton.setVisibility(View.GONE);
                        isOptionsMenuEnabled = false;
                        break;
                    case 3: // About
                        binding.toolbar.setTitle("About");
                        binding.changeLayoutButton.setVisibility(View.GONE);
                        binding.searchEditText.setVisibility(View.GONE);
                        binding.searchButton.setVisibility(View.GONE);
                        isOptionsMenuEnabled = false;
                        break;
                }
                requireActivity().invalidateOptionsMenu(); // call onPrepareOptionsMenu to refresh options menu
            }
        });
    }


    /**
     * Set up button change layout of movie list (list or grid layout)
     */
    private void setBtnChangeLayout() {
        binding.changeLayoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isGridLayout = !isGridLayout;
                if (isGridLayout) {
                    binding.changeLayoutButton.setImageResource(R.drawable.ic_list);

                    // change layout to grid by view model
                    // observe to change layout in movie list fragment
                    movieListViewModel.setGrid(isGridLayout);
                } else {
                    binding.changeLayoutButton.setImageResource(R.drawable.ic_grid);

                    // change layout to grid by view model
                    // observe to change layout in movie list fragment
                    movieListViewModel.setGrid(isGridLayout);
                }
            }
        });
    }


    /**
     * Set up tab layout and view pager
     */
    private void setUpTabLayoutAndViewPager() {
        fragmentList = new ArrayList<>();
        fragmentList.add(commonFragment);
        fragmentList.add(favoriteListFragment);
        fragmentList.add(settingsFragment);
        fragmentList.add(aboutFragment);


        // set view pager with fragments
        viewPagerAdapter = new ViewPagerAdapter(requireActivity(), fragmentList);
        binding.viewPager.setAdapter(viewPagerAdapter);
        binding.viewPager.setOffscreenPageLimit(4); // create two 4 tabs to avoid null

        // set tab layout with view pager
        createTabName();
        createTabIcon();
        new TabLayoutMediator(binding.tabLayout, binding.viewPager, (tab, position) -> {
            tab.setText(tabNameList.get(position));
            tab.setIcon(tabIconList.get(position));
        }).attach();
    }


    /**
     * Create tab name list
     */
    private void createTabName() {
        tabNameList = new ArrayList<>();
        tabNameList.add("Movie List");
        tabNameList.add("Favorite List");
        tabNameList.add("Setting");
        tabNameList.add("About");
    }

    /**
     * Create tab icon list
     */
    private void createTabIcon() {
        tabIconList = new ArrayList<>();
        tabIconList.add(R.drawable.ic_home);
        tabIconList.add(R.drawable.ic_favorite);
        tabIconList.add(R.drawable.ic_setting);
        tabIconList.add(R.drawable.ic_about);
    }


    /**
     * Set up toolbar
     */
    private void setUpToolbar() {
        // set up toolbar
        AppCompatActivity activity = (AppCompatActivity) requireActivity();
        activity.setSupportActionBar(binding.toolbar);
        actionBarDrawerToggle = new ActionBarDrawerToggle(requireActivity(), binding.drawerLayout, binding.toolbar, R.string.open_drawer, R.string.close_drawer);
        binding.drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

    /**
     * Handle back press event
     */
    public void showBackButton() {
        // display back button
        actionBarDrawerToggle.setHomeAsUpIndicator(R.drawable.ic_back);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(false);
        binding.toolbar.setNavigationOnClickListener(v -> requireActivity().getOnBackPressedDispatcher().onBackPressed());

        // observe detail movie to update toolbar title by movie title when navigate to movie detail screen
        movieDetailViewModel.getMovieDetailMutableLiveData().observe(this, movie -> {
            if (movie != null) {
                binding.toolbar.setTitle(movie.getTitle());
            }
        });

        // option menu is disabled
        isOptionsMenuEnabled = false;
        requireActivity().invalidateOptionsMenu(); // call onPrepareOptionsMenu to refresh options menu

        // hide change layout button
        binding.changeLayoutButton.setVisibility(View.GONE);
    }

    /**
     * Show drawer toggle
     */
    public void showDrawerToggle() {
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        setUpToolbar();

        // set toolbar title
        binding.toolbar.setTitle("Movies");

        // option menu is enabled
        isOptionsMenuEnabled = true;
        requireActivity().invalidateOptionsMenu(); // call onPrepareOptionsMenu to refresh options menu

        // show change layout button
        binding.changeLayoutButton.setVisibility(View.VISIBLE);
    }

}