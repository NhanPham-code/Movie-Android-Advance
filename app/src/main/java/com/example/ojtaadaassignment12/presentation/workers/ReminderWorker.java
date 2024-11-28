package com.example.ojtaadaassignment12.presentation.workers;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.ojtaadaassignment12.R;
import com.example.ojtaadaassignment12.di.MyApplication;
import com.example.ojtaadaassignment12.domain.models.Reminder;
import com.example.ojtaadaassignment12.presentation.MainActivity;
import com.example.ojtaadaassignment12.presentation.viewmodels.ReminderViewModel;

import java.util.List;

import javax.inject.Inject;

public class ReminderWorker extends Worker {

    private static final String CHANNEL_ID = "1001";
    private static final long TIME_DIFFERENCE = 1000;

    @Inject
    ReminderViewModel reminderViewModel; // use to get reminders from database

    public ReminderWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);

        // inject ReminderViewModel
        ((MyApplication) context.getApplicationContext()).appComponent.injectReminderWorker(this);
    }

    @NonNull
    @Override
    public Result doWork() {
        // get current time
        long currentTime = System.currentTimeMillis();

        // Get reminders from database with current time (time set to notification from user)
        List<Reminder> reminderList = reminderViewModel.getRemindersByTimeRange(currentTime - TIME_DIFFERENCE, currentTime + TIME_DIFFERENCE);

        for(Reminder reminder : reminderList) {
            pushNotification(reminder);

            // remove reminder from database
            reminderViewModel.removeReminderFromDB(reminder);
        }

        return Result.success();
    }


    private void pushNotification(Reminder reminder) {
        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID, "Reminders", NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }

        // Intent mở MainActivity khi nhấn vào thông báo
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                .setContentTitle(reminder.getTitleMovie())
                .setContentText("Year: " + reminder.getReleaseDateMovie() + ", Rate: " + String.format("%.1f/10", reminder.getVoteAverageMovie()))
                .setSmallIcon(R.drawable.ic_notification)
                .setLargeIcon(BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.app_img))
                .setContentIntent(pendingIntent)
                .build();

        notificationManager.notify(reminder.getId(), notification);
    }
}
