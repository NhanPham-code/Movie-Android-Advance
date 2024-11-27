package com.example.ojtaadaassignment12.presentation.views.fragments;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.ojtaadaassignment12.R;
import com.example.ojtaadaassignment12.databinding.FragmentMovieDetailBinding;
import com.example.ojtaadaassignment12.di.MyApplication;
import com.example.ojtaadaassignment12.domain.models.Movie;
import com.example.ojtaadaassignment12.domain.models.Reminder;
import com.example.ojtaadaassignment12.presentation.viewmodels.MovieDetailViewModel;
import com.example.ojtaadaassignment12.presentation.viewmodels.MovieListViewModel;
import com.example.ojtaadaassignment12.presentation.viewmodels.ReminderViewModel;
import com.example.ojtaadaassignment12.presentation.views.adapters.CastAdapter;
import com.example.ojtaadaassignment12.presentation.workers.ReminderWorker;
import com.example.ojtaadaassignment12.util.Constant;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;


public class MovieDetailFragment extends Fragment {
    private static final int POST_NOTIFICATIONS_PERMISSION_CODE = 100;

    FragmentMovieDetailBinding binding;

    Picasso picasso;

    @Inject
    MovieDetailViewModel movieDetailViewModel; // display movie detail information

    @Inject
    MovieListViewModel movieListViewModel; // use to update favorite and unfavorite movie

    @Inject
    ReminderViewModel reminderViewModel; // use to add reminder to database

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
            binding.tvReleaseDate.setText(movie.getReleaseDate());
            binding.tvRating.setText(String.format("%.1f/10", movie.getVoteAverage()));
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
            // observe cast list to update UI when get data from API
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

                    // add favorite movie to database (favorite list fragment observer to update)
                    movieListViewModel.insertFavoriteMovie(movie);
                } else {
                    movie.setIsFavorite(0);
                    binding.ivFavorite.setImageResource(R.drawable.ic_star);

                    // remove favorite movie from database (favorite list fragment observer to update UI)
                    movieListViewModel.deleteFavoriteMovie(movie);
                }
            });

            // movie is set reminder or not
            reminderViewModel.getReminderList().observe(getViewLifecycleOwner(), reminders -> {

                // check movie is set in reminder or not
                Reminder reminderCheck = null;
                for (Reminder reminder : reminders) {
                    if (reminder.getMovieId() == movie.getId()) { // movie have set reminder
                        reminderCheck = reminder;
                        break;
                    }
                }

                if(reminderCheck != null) { //movie is set reminder
                    // show reminder information and disable reminder button
                    binding.btnReminder.setVisibility(View.INVISIBLE);
                    binding.reminderInfo.setVisibility(View.VISIBLE);
                    binding.reminderInfo.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
                                                        .format(new Date(reminderCheck.getTime())));
                } else {
                    // show reminder button and disable reminder information
                    binding.btnReminder.setVisibility(View.VISIBLE);
                    binding.reminderInfo.setVisibility(View.GONE);
                }
            });

            // set reminder button click listener
            binding.btnReminder.setOnClickListener(v -> {
                showDateTimePicker(movie);
            });
        }

        return binding.getRoot();
    }


    /**
     * Show date time picker
     */
    private void showDateTimePicker(Movie movie) {

        // check permission to show notification
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            checkPermission(Manifest.permission.POST_NOTIFICATIONS, POST_NOTIFICATIONS_PERMISSION_CODE);
        }

        // Get current date and time
        Calendar calendar = Calendar.getInstance();

        // Date Picker
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), android.R.style.Theme_Holo_Light_DialogWhenLarge_NoActionBar,
                (view, year, month, dayOfMonth) -> {
                    calendar.setTimeZone(TimeZone.getDefault());
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, month);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                    // Time Picker
                    TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), android.R.style.Theme_Holo_Light_DialogWhenLarge_NoActionBar,
                            (timeView, hourOfDay, minute) -> {
                                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                calendar.set(Calendar.MINUTE, minute);
                                calendar.set(Calendar.SECOND, 0);
                                calendar.set(Calendar.MILLISECOND, 0);

                                // get selected time
                                long selectedTimeInMillis = calendar.getTimeInMillis();

                                // check if selected time is in the future
                                if (selectedTimeInMillis <= System.currentTimeMillis()) {
                                    Toast.makeText(getActivity(), "Please choose a future time", Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                Reminder newReminder = new Reminder();
                                newReminder.setTime(selectedTimeInMillis);
                                newReminder.setMovieId(movie.getId());
                                newReminder.setPosterPathMovie(movie.getPosterPath());
                                newReminder.setTitleMovie(movie.getTitle());
                                newReminder.setReleaseDateMovie(movie.getReleaseDate());
                                newReminder.setVoteAverageMovie(movie.getVoteAverage());
                                newReminder.setIsFavoriteOfMovie(movie.getIsFavorite()); // use to sync favorite status of movie with reminder

                                // add reminder to database
                                reminderViewModel.addReminderToDB(newReminder);

                                // add reminder to reminder list of view model to update UI
                                //reminderViewModel.addReminderToList(newReminder);

                                // set up a work to show a notification at the specified time
                                scheduleWork(selectedTimeInMillis);

                                Toast.makeText(getActivity(), "Reminder set successfully", Toast.LENGTH_SHORT).show();

                            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
                    timePickerDialog.show();
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    /**
     * Function to check and request permission.
     * @param permission: permission to check
     * @param requestCode: request code
     */
    public void checkPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(requireActivity(), permission) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{permission}, requestCode);
        }
    }

    /**
     * Schedule a work to show a notification at the specified time
     * @param reminderTime: the time to show the notification
     */
    private void scheduleWork(long reminderTime) {
        long delay = reminderTime - System.currentTimeMillis();

        OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(ReminderWorker.class)
                .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                .build();

        WorkManager.getInstance(requireContext()).enqueue(workRequest);
    }
}