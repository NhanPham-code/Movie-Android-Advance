package com.example.ojtaadaassignment12.presentation.views.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.LifecycleOwner;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.WorkManager;

import com.example.ojtaadaassignment12.R;
import com.example.ojtaadaassignment12.domain.models.Reminder;
import com.example.ojtaadaassignment12.presentation.viewmodels.MovieDetailViewModel;
import com.example.ojtaadaassignment12.presentation.viewmodels.ReminderViewModel;
import com.example.ojtaadaassignment12.util.Constant;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AllReminderAdapter extends RecyclerView.Adapter<AllReminderAdapter.AllReminderViewHolder> {

    private final List<Reminder> reminderList;

    Picasso picasso;

    // use to remove reminder when click delete button
    ReminderViewModel reminderViewModel;

    // use to set movie to detail view model when click reminder item.
    // Common fragment observe this view model to show movie detail
    MovieDetailViewModel movieDetailViewModel;

    NavController navController;

    LifecycleOwner lifecycleOwner;

    Context context;

    public AllReminderAdapter(List<Reminder> reminderList, ReminderViewModel reminderViewModel,
                              MovieDetailViewModel movieDetailViewModel, NavController navController,
                              LifecycleOwner lifecycleOwner, Context context) {
        this.reminderList = reminderList;
        this.reminderViewModel = reminderViewModel;
        this.navController = navController;
        this.movieDetailViewModel = movieDetailViewModel;
        this.lifecycleOwner = lifecycleOwner;
        this.context = context;
    }

    @NonNull
    @Override
    public AllReminderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_all_reminder, parent, false);
        return new AllReminderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AllReminderViewHolder holder, int position) {

        Reminder reminder = reminderList.get(position);

        picasso = Picasso.get();
        // set data to UI
        picasso.load(Constant.IMAGE_BASE_URL + reminder.getPosterPathMovie())
                .placeholder(R.drawable.baseline_image_24)
                .error(R.drawable.baseline_image_not_supported_24)
                .into(holder.ivMoviePoster);
        holder.tvMovieInfo.setText(reminder.getTitleMovie() + " - " + reminder.getReleaseDateMovie().substring(0, 4) + " - " + String.format("%.1f", reminder.getVoteAverageMovie()));
        holder.tvReminderInfo.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(new Date(reminder.getTime())));


        // Set delete reminder button
        holder.btnDeleteReminder.setOnClickListener(v -> {
            // Show popup menu
            PopupMenu popupMenu = new PopupMenu(holder.itemView.getContext(), holder.btnDeleteReminder);
            popupMenu.getMenuInflater().inflate(R.menu.menu_delete_reminder, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == R.id.delete_reminder) {
                    // Show confirmation dialog
                    new AlertDialog.Builder(holder.itemView.getContext())
                            .setTitle("Delete Reminder")
                            .setMessage("Are you sure you want to delete this reminder?")
                            .setPositiveButton("Delete", (dialog, which) -> {
                                // remove reminder from database
                                reminderViewModel.removeReminderFromDB(reminder);

                                // cancel worker
                                cancelWork(reminder.getMovieId());
                            })
                            .setNegativeButton("Cancel", null)
                            .show();
                    return true;
                }
                return false;
            });
            popupMenu.show();
        });


        // Set on click listener for reminder item
        holder.itemView.setOnClickListener(view -> {
            // pop back stack to go to main fragment (contain common fragment to show movie detail)
            navController.popBackStack();

            // get movie information from API
            // set new movie from API for movie detail mutable live data in movie detail view model
            // common fragment observe this live data to show movie detail
            movieDetailViewModel.getMovieDetailFromApi(reminder.getMovieId(), reminder.getIsFavoriteOfMovie());

        });

    }

    /**
     * Cancel work with the specific tag
     *
     * @param movieId: the id of the movie
     */
    private void cancelWork(long movieId) {
        WorkManager.getInstance(context)
                .cancelAllWorkByTag("Reminder_" + movieId); // Cancel work with the specific tag
    }

    @Override
    public int getItemCount() {
        return reminderList.size();
    }


    public static class AllReminderViewHolder extends RecyclerView.ViewHolder {
        private final ImageView ivMoviePoster;
        private final TextView tvMovieInfo;
        private final TextView tvReminderInfo;
        private final ImageButton btnDeleteReminder;


        public AllReminderViewHolder(@NonNull View itemView) {
            super(itemView);
            ivMoviePoster = itemView.findViewById(R.id.movie_poster);
            tvMovieInfo = itemView.findViewById(R.id.movie_info);
            tvReminderInfo = itemView.findViewById(R.id.reminder_info);
            btnDeleteReminder = itemView.findViewById(R.id.btn_remove_reminder);
        }

    }
}
