package com.example.ojtaadaassignment12.di;

import com.example.ojtaadaassignment12.data.datasource.local.reminderdb.ReminderDataSource;
import com.example.ojtaadaassignment12.data.repository.ReminderRepositoryImpl;
import com.example.ojtaadaassignment12.domain.repository.IReminderRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ReminderModule {

    @Provides
    @Singleton
    public IReminderRepository provideReminderRepository(ReminderDataSource reminderDataSource) {
        return new ReminderRepositoryImpl(reminderDataSource);
    }
}
