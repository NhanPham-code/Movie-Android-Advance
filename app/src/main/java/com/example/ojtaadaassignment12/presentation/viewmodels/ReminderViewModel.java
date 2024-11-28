package com.example.ojtaadaassignment12.presentation.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.ojtaadaassignment12.domain.models.Reminder;
import com.example.ojtaadaassignment12.domain.usecase.ReminderUseCase;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

@Singleton
public class ReminderViewModel extends ViewModel {

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private final ReminderUseCase reminderUseCase;

    private final MutableLiveData<List<Reminder>> reminderList = new MutableLiveData<>();

    @Inject
    public ReminderViewModel(ReminderUseCase reminderUseCase) {
        this.reminderUseCase = reminderUseCase;
    }

    public MutableLiveData<List<Reminder>> getReminderList() {
        return reminderList;
    }

    /**
     * Add a reminder
     *
     * @param reminder: reminder to be added
     */
    public void addReminderToDB(Reminder reminder) {
        Disposable disposable = Completable.fromCallable(() -> {
                    reminderUseCase.addReminder(reminder);
                    return true;
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();

        compositeDisposable.add(disposable);
    }

    /**
     * Remove a reminder
     *
     * @param reminder: reminder to be removed
     */
    public void removeReminderFromDB(Reminder reminder) {
        Disposable disposable = Completable.fromCallable(() -> {
                    reminderUseCase.removeReminder(reminder);
                    return true;
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();

        compositeDisposable.add(disposable);
    }

    /**
     * Update a reminder
     * @param reminder: reminder to be updated
     */
    public void updateReminderToDB(Reminder reminder) {
        Disposable disposable = Completable.fromCallable(() -> {
                    reminderUseCase.updateReminder(reminder);
                    return true;
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();

        compositeDisposable.add(disposable);
    }


    /**
     * Get all reminders
     */
    public void getAllReminders() {
        // use Flowable help to update UI whenever DB changes (add, remove reminders)
        Flowable<List<Reminder>> reminderSingle = reminderUseCase.getAllReminders();

        Disposable disposable = reminderSingle
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        reminders -> reminderList.setValue(reminders),
                        throwable -> reminderList.setValue(null)
                );

        compositeDisposable.add(disposable);
    }


    /**
     * Get reminders by time range (use in worker)
     *
     * @param startTime: start time of the range
     * @param endTime:   end time of the range
     */
    public List<Reminder> getRemindersByTimeRange(long startTime, long endTime) {
        return reminderUseCase.getRemindersByTimeRange(startTime, endTime);
    }




    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.clear();
    }
}
