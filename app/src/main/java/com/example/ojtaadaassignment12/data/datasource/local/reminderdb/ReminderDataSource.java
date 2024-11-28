package com.example.ojtaadaassignment12.data.datasource.local.reminderdb;

import androidx.lifecycle.LiveData;

import com.example.ojtaadaassignment12.data.entities.ReminderEntity;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

public class ReminderDataSource {
    private final ReminderDao reminderDao;

    @Inject
    public ReminderDataSource(ReminderDao reminderDao) {
        this.reminderDao = reminderDao;
    }

    public void insertReminder(ReminderEntity reminder) {
        reminderDao.insertReminder(reminder);
    }

    public void deleteReminder(ReminderEntity reminder) {
        reminderDao.deleteReminder(reminder);
    }

    public void updateReminder(ReminderEntity reminderEntity) {
        reminderDao.updateReminderTime(reminderEntity.getId(), reminderEntity.getTime());
    }

    public Flowable<List<ReminderEntity>> getAllReminders() {
        return reminderDao.getAllReminders();
    }

    public List<ReminderEntity> getRemindersByTimeRange(long startTime, long endTime) {
        return reminderDao.getRemindersByTimeRange(startTime, endTime);
    }
}
