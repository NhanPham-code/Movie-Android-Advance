package com.example.ojtaadaassignment12.data.datasource.local.reminderdb;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.ojtaadaassignment12.data.entities.ReminderEntity;

@Database(entities = {ReminderEntity.class}, version = 2)
public abstract class ReminderDatabase extends RoomDatabase {

    public abstract ReminderDao reminderDao();
}
