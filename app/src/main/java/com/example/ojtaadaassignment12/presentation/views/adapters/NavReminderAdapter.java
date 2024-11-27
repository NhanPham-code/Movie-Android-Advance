package com.example.ojtaadaassignment12.presentation.views.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ojtaadaassignment12.R;
import com.example.ojtaadaassignment12.domain.models.Movie;
import com.example.ojtaadaassignment12.domain.models.Reminder;
import com.example.ojtaadaassignment12.presentation.viewmodels.MovieDetailViewModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class NavReminderAdapter extends RecyclerView.Adapter<NavReminderAdapter.ReminderViewHolder> {

    private final List<Reminder> reminderList;


    public NavReminderAdapter(List<Reminder> reminderList) {
        this.reminderList = reminderList;
    }

    @NonNull
    @Override
    public ReminderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_nav_reminder, parent, false);
        return new ReminderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReminderViewHolder holder, int position) {
        // Get reminder in list
        Reminder reminder = reminderList.get(position);

        holder.tvMovieInfo.setText(reminder.getTitleMovie() + " - " + reminder.getReleaseDateMovie().substring(0, 4) + " - " + String.format("%.1f", reminder.getVoteAverageMovie()));
        holder.tvReminderInfo.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(new Date(reminder.getTime())));
    }

    @Override
    public int getItemCount() {
        return reminderList.size();
    }

    public static class ReminderViewHolder extends RecyclerView.ViewHolder {
        TextView tvMovieInfo;
        TextView tvReminderInfo;

        public ReminderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMovieInfo = itemView.findViewById(R.id.tv_movie_info);
            tvReminderInfo = itemView.findViewById(R.id.tv_reminder_info);
        }
    }
}
