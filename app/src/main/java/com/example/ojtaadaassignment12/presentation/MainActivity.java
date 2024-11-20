package com.example.ojtaadaassignment12.presentation;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.ojtaadaassignment12.R;
import com.example.ojtaadaassignment12.databinding.ActivityMainBinding;
import com.example.ojtaadaassignment12.di.MyApplication;
import com.example.ojtaadaassignment12.presentation.viewmodels.MovieListViewModel;
import com.example.ojtaadaassignment12.presentation.views.adapters.ViewPagerAdapter;
import com.example.ojtaadaassignment12.presentation.views.fragments.FavoriteListFragment;
import com.example.ojtaadaassignment12.presentation.views.fragments.MovieListFragment;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    private boolean isGridLayout = false;

    private ArrayList<String> tabNameList;
    private ArrayList<Integer> tabIconList;

    // view pager, tab layout
    ViewPagerAdapter viewPagerAdapter;

    // drawer layout
    ActionBarDrawerToggle actionBarDrawerToggle;

    // fragments
    List<Fragment> fragmentList;
    MovieListFragment movieListFragment;
    Fragment favoriteListFragment;

    // View models
    @Inject
    MovieListViewModel movieListViewModel;

    /**
     * Save the state fragment when activity is recreated
     * @param outState: bundle
     */
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState, "movieListFragment", movieListFragment);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Set up toolbar
        setUpToolbar();

        // create fragments and set up view pager, tab layout
        if (savedInstanceState != null) {
            // restore fragments when activity is recreated
            movieListFragment = (MovieListFragment) getSupportFragmentManager().getFragment(savedInstanceState, "movieListFragment");

            // set up view pager and tab layout
            setUpTabLayoutAndViewPager();

        } else {
            // create fragments for first time running
            movieListFragment = MovieListFragment.newInstance();

            favoriteListFragment = FavoriteListFragment.newInstance();

            // set up view pager and tab layout
            setUpTabLayoutAndViewPager();
        }

        // set up change layout movie list button
        setBtnChangeLayout();

        // Set up tab change listener to change toolbar title...
        setUpTabChangeListener();

        // Inject MovieListComponent by MyApplication to use MovieListViewModel
        ((MyApplication) getApplication()).movieListComponent.injectMainActivity(this);

        // observe favorite movies count (size) to set the favorite tag
        movieListViewModel.getFavoriteMoviesCount().observe(this, favoriteListSize -> {
            TabLayout.Tab tab = binding.tabLayout.getTabAt(1);
            if(tab != null) {
                BadgeDrawable badge = tab.getOrCreateBadge();
                badge.setVisible(true);
                badge.setNumber(favoriteListSize);
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
                        binding.toolbar.setTitle("Moives");
                        binding.changeLayoutButton.setVisibility(View.VISIBLE);
                        binding.searchEditText.setVisibility(View.GONE);
                        binding.searchButton.setVisibility(View.GONE);
                        break;
                    case 1: // Favorite List
                        binding.toolbar.setTitle("");
                        binding.changeLayoutButton.setVisibility(View.GONE);
                        binding.searchEditText.setVisibility(View.VISIBLE);
                        binding.searchEditText.setText("Favourite");
                        binding.searchButton.setVisibility(View.VISIBLE);
                        break;
                    case 2: // Setting
                        binding.toolbar.setTitle("Setting");
                        binding.changeLayoutButton.setVisibility(View.GONE);
                        binding.searchEditText.setVisibility(View.GONE);
                        binding.searchButton.setVisibility(View.GONE);
                        break;
                    case 3: // About
                        binding.toolbar.setTitle("About");
                        binding.changeLayoutButton.setVisibility(View.GONE);
                        binding.searchEditText.setVisibility(View.GONE);
                        binding.searchButton.setVisibility(View.GONE);
                        break;
                }
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
                    movieListFragment.changeLayout(isGridLayout);
                } else {
                    binding.changeLayoutButton.setImageResource(R.drawable.ic_grid);
                    movieListFragment.changeLayout(isGridLayout);
                }
            }
        });
    }

    /**
     * Set up tab layout and view pager
     */
    private void setUpTabLayoutAndViewPager() {
        fragmentList = new ArrayList<>();
        fragmentList.add(movieListFragment);
        fragmentList.add(favoriteListFragment);


        // set view pager with fragments
        viewPagerAdapter = new ViewPagerAdapter(this, fragmentList);
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
        setSupportActionBar(binding.toolbar);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, binding.drawerLayout, binding.toolbar, R.string.open_drawer, R.string.close_drawer);
        binding.drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }
}