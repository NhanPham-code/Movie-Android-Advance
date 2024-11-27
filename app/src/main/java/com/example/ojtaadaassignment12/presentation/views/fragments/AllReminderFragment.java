package com.example.ojtaadaassignment12.presentation.views.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ojtaadaassignment12.R;
import com.example.ojtaadaassignment12.databinding.FragmentAllReminderBinding;
import com.example.ojtaadaassignment12.di.MyApplication;
import com.example.ojtaadaassignment12.presentation.viewmodels.MovieDetailViewModel;
import com.example.ojtaadaassignment12.presentation.viewmodels.ReminderViewModel;
import com.example.ojtaadaassignment12.presentation.views.adapters.AllReminderAdapter;

import javax.inject.Inject;


public class AllReminderFragment extends Fragment {

    FragmentAllReminderBinding binding;

    // use this view model to get all reminders and update reminder list in UI after add or remove a reminder
    @Inject
    ReminderViewModel reminderViewModel;

    @Inject
    MovieDetailViewModel movieDetailViewModel;

    public AllReminderFragment() {
        // Required empty public constructor
    }


    public static AllReminderFragment newInstance() {
        AllReminderFragment fragment = new AllReminderFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // inject the view model
        ((MyApplication) requireActivity().getApplication()).appComponent.injectAllReminderFragment(this);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAllReminderBinding.inflate(inflater, container, false);

        // get nav controller
        NavController navController = Navigation.findNavController(requireActivity(), R.id.fragment_container);

        // set up recycler view by observer reminder list from view model
        reminderViewModel.getReminderList().observe(getViewLifecycleOwner(), reminders -> {

            AllReminderAdapter allReminderAdapter = new AllReminderAdapter(reminders, reminderViewModel,
                    movieDetailViewModel, navController, getViewLifecycleOwner());
            binding.rvAllReminder.setLayoutManager(new LinearLayoutManager(requireContext()));
            binding.rvAllReminder.setAdapter(allReminderAdapter);
        });

        // handle back press
        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.popBackStack();
            }
        });

        return binding.getRoot();
    }
}