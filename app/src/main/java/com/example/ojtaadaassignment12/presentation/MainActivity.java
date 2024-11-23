package com.example.ojtaadaassignment12.presentation;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.viewpager2.widget.ViewPager2;

import com.example.ojtaadaassignment12.R;
import com.example.ojtaadaassignment12.databinding.ActivityMainBinding;
import com.example.ojtaadaassignment12.di.MyApplication;
import com.example.ojtaadaassignment12.presentation.viewmodels.MovieDetailViewModel;
import com.example.ojtaadaassignment12.presentation.viewmodels.MovieListViewModel;
import com.example.ojtaadaassignment12.presentation.views.adapters.ViewPagerAdapter;
import com.example.ojtaadaassignment12.presentation.views.fragments.AboutFragment;
import com.example.ojtaadaassignment12.presentation.views.fragments.CommonFragment;
import com.example.ojtaadaassignment12.presentation.views.fragments.FavoriteListFragment;
import com.example.ojtaadaassignment12.presentation.views.fragments.SettingsFragment;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    NavController navController;

    /**
     * Save the state fragment when activity is recreated
     *
     * @param outState: bundle
     */
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize NavController
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if(navHostFragment != null) {
            navController = navHostFragment.getNavController();
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.fragment_container);
        return navController.navigateUp() || super.onSupportNavigateUp();
    }


}