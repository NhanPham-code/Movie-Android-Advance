package com.example.ojtaadaassignment12.domain.usecase;

import com.example.ojtaadaassignment12.domain.models.Reminder;
import com.example.ojtaadaassignment12.domain.repository.IReminderRepository;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

public class ReminderUseCase {

    private final IReminderRepository iReminderRepository;

    @Inject
    public ReminderUseCase(IReminderRepository iReminderRepository) {
        this.iReminderRepository = iReminderRepository;
    }

    public void addReminder(Reminder reminder) {
        iReminderRepository.addReminder(reminder);
    }

    public void removeReminder(Reminder reminder) {
        iReminderRepository.removeReminder(reminder);
    }

    public Flowable<List<Reminder>> getAllReminders() {
        return iReminderRepository.getAllReminders();
    }

    public List<Reminder> getRemindersByTimeRange(long startTime, long endTime) {
        return iReminderRepository.getRemindersByTimeRange(startTime, endTime);
    }
}
