package com.example.ojtaadaassignment12.data.mapper;

import com.example.ojtaadaassignment12.data.entities.ReminderEntity;
import com.example.ojtaadaassignment12.domain.models.Reminder;

public class ReminderMapper {
    public static ReminderEntity toEntity(Reminder reminder) {
        if (reminder == null) {
            return null;
        }
        ReminderEntity entity = new ReminderEntity();
        entity.setId(reminder.getId());
        entity.setTime(reminder.getTime());
        entity.setMovieId(reminder.getMovieId());
        entity.setPosterPathMovie(reminder.getPosterPathMovie());
        entity.setTitleMovie(reminder.getTitleMovie());
        entity.setReleaseDateMovie(reminder.getReleaseDateMovie());
        entity.setVoteAverageMovie(reminder.getVoteAverageMovie());
        entity.setIsFavoriteOfMovie(reminder.getIsFavoriteOfMovie());
        return entity;
    }

    public static Reminder toDomain(ReminderEntity entity) {
        if (entity == null) {
            return null;
        }
        Reminder reminder = new Reminder();
        reminder.setId(entity.getId());
        reminder.setTime(entity.getTime());
        reminder.setMovieId(entity.getMovieId());
        reminder.setPosterPathMovie(entity.getPosterPathMovie());
        reminder.setTitleMovie(entity.getTitleMovie());
        reminder.setReleaseDateMovie(entity.getReleaseDateMovie());
        reminder.setVoteAverageMovie(entity.getVoteAverageMovie());
        reminder.setIsFavoriteOfMovie(entity.getIsFavoriteOfMovie());
        return reminder;
    }
}
