package com.example.ojtaadaassignment12.data.datasource.local.reminderdb;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.ojtaadaassignment12.data.entities.ReminderEntity;

import java.util.List;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface ReminderDao {
    @Insert
    void insertReminder(ReminderEntity reminder);

    @Delete
    void deleteReminder(ReminderEntity reminder);

    @Query("SELECT * FROM reminder_table")
    Flowable<List<ReminderEntity>> getAllReminders();

    @Query("SELECT * FROM reminder_table WHERE time BETWEEN :startTime AND :endTime")
    List<ReminderEntity> getRemindersByTimeRange(long startTime, long endTime);
}
