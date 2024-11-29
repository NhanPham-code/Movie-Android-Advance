package com.example.ojtaadaassignment12.presentation.views.fragments;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.preference.EditTextPreference;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.example.ojtaadaassignment12.R;

import java.util.Calendar;

public class SettingsFragment extends PreferenceFragmentCompat {

    SharedPreferences sharedPreferences;
    SharedPreferences.OnSharedPreferenceChangeListener preferenceChangeListener;

    ListPreference categoryPreferences;
    Preference ratingPreferences;
    EditTextPreference releaseYearPreferences;
    ListPreference sortByPreferences;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);

        sharedPreferences = getPreferenceManager().getSharedPreferences();

        // category
        categoryPreferences = findPreference("category");
        if (categoryPreferences != null) {
            categoryPreferences.setOnPreferenceChangeListener((preference, newValue) -> {
                String category = newValue.toString();
                Toast.makeText(getContext(), "Category: " + category, Toast.LENGTH_SHORT).show();
                return true;
            });
        }

        // Listen for changes in shared preferences (selected in option menu)
        // sync the selected category with the list preference
        preferenceChangeListener = (sharedPreferences, key) -> {
            if ("category".equals(key)) {
                String category = sharedPreferences.getString(key, "popular");
                if (categoryPreferences != null) {
                    categoryPreferences.setValue(category); // Update the list preference's value
                }
            }
        };
        sharedPreferences.registerOnSharedPreferenceChangeListener(preferenceChangeListener);


        // ratting
        ratingPreferences = findPreference("rating");
        if (ratingPreferences != null) {
            // Load saved rating value and set it as the summary
            String rating = sharedPreferences.getString("rating", "1");
            ratingPreferences.setSummary(rating);

            ratingPreferences.setOnPreferenceClickListener(preference -> {
                showRatingDialog();
                return true;
            });
        }

        // release year
        releaseYearPreferences = findPreference("releaseYear");
        if (releaseYearPreferences != null) {

            releaseYearPreferences.setOnBindEditTextListener(editText -> {
                editText.setInputType(InputType.TYPE_CLASS_NUMBER);
            });

            releaseYearPreferences.setOnPreferenceChangeListener((preference, newValue) -> {
                String yearText = newValue.toString();

                if (yearText.isEmpty()) {
                    Toast.makeText(getContext(), "Invalid year format! Setting default year to 1970.", Toast.LENGTH_SHORT).show();
                    return false;
                }

                if (yearText.length() != 4) {
                    Toast.makeText(getContext(), "Invalid year format! Year must be 4 digits long.", Toast.LENGTH_SHORT).show();
                    return false;
                }

                int year = Integer.parseInt(yearText);
                int currentYear = Calendar.getInstance().get(Calendar.YEAR);

                if (year < 1970 || year > currentYear) {
                    Toast.makeText(getContext(), "Year must be between 1970 and " + currentYear + ". Setting default year to 1970.", Toast.LENGTH_SHORT).show();
                    return false;
                }

                Toast.makeText(getContext(), "Release year: " + yearText, Toast.LENGTH_SHORT).show();
                return true;
            });
        }

        // sort by
        sortByPreferences = findPreference("sortBy");
        if (sortByPreferences != null) {
            sortByPreferences.setOnPreferenceChangeListener((preference, newValue) -> {
                String sortBy = newValue.toString();
                Toast.makeText(getContext(), "Sort by: " + sortBy, Toast.LENGTH_SHORT).show();
                return true;
            });
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(preferenceChangeListener);
    }

    private void showRatingDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = getLayoutInflater().inflate(R.layout.dialog_seekbar, null);
        SeekBar seekBar = view.findViewById(R.id.seekBar);
        TextView seekBarValue = view.findViewById(R.id.seekBarValue);

        seekBar.setMax(10);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                seekBarValue.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        builder.setView(view)
                .setPositiveButton("Yes", (dialog, which) -> {
                    String rating = seekBarValue.getText().toString();
                    ratingPreferences.setSummary(rating);
                    ratingPreferences.setDefaultValue(rating);
                    sharedPreferences.edit().putString("rating", rating).apply(); // save to shared preferences
                })
                .setNegativeButton("No", null)
                .show();
    }
}