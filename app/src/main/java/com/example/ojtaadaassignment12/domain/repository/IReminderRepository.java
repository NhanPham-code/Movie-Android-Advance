package com.example.ojtaadaassignment12.domain.repository;

import com.example.ojtaadaassignment12.domain.models.Reminder;

import java.util.List;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

public interface IReminderRepository {
    void addReminder(Reminder reminder);

    void removeReminder(Reminder reminder);

    void updateReminder(Reminder reminder);

    Flowable<List<Reminder>> getAllReminders();

    // worker use to get reminders by time range from the database
    // worker is background thread so don't need to use rxjava
    List<Reminder> getRemindersByTimeRange(long startTime, long endTime);

}
