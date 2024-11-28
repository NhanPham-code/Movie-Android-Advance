package com.example.ojtaadaassignment12.data.repository;

import androidx.lifecycle.LiveData;

import com.example.ojtaadaassignment12.data.datasource.local.reminderdb.ReminderDataSource;
import com.example.ojtaadaassignment12.data.entities.ReminderEntity;
import com.example.ojtaadaassignment12.data.mapper.ReminderMapper;
import com.example.ojtaadaassignment12.domain.models.Reminder;
import com.example.ojtaadaassignment12.domain.repository.IReminderRepository;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

public class ReminderRepositoryImpl implements IReminderRepository {

    ReminderDataSource reminderDataSource;

    @Inject
    public ReminderRepositoryImpl(ReminderDataSource reminderDataSource) {
        this.reminderDataSource = reminderDataSource;
    }

    @Override
    public void addReminder(Reminder reminder) {
        // mapper
        ReminderEntity reminderEntity = ReminderMapper.toEntity(reminder);
        // add to DB
        reminderDataSource.insertReminder(reminderEntity);
    }

    @Override
    public void removeReminder(Reminder reminder) {
        ReminderEntity reminderEntity = ReminderMapper.toEntity(reminder);

        reminderDataSource.deleteReminder(reminderEntity);
    }

    @Override
    public void updateReminder(Reminder reminder) {
        ReminderEntity reminderEntity = ReminderMapper.toEntity(reminder);
        reminderDataSource.updateReminder(reminderEntity);
    }

    @Override
    public Flowable<List<Reminder>> getAllReminders() {
        return reminderDataSource.getAllReminders()
                .map(reminderEntities -> reminderEntities.stream()
                        .map(ReminderMapper::toDomain)
                        .collect(Collectors.toList()));
    }

    @Override
    public List<Reminder> getRemindersByTimeRange(long startTime, long endTime) {
        return reminderDataSource.getRemindersByTimeRange(startTime, endTime).stream()
                .map(ReminderMapper::toDomain)
                .collect(Collectors.toList());
    }
}
