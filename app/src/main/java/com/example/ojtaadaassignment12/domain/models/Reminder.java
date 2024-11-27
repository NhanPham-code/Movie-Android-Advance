package com.example.ojtaadaassignment12.domain.models;

public class Reminder {
    private int id;
    private long time;
    private long movieId;
    private String posterPathMovie;
    private String titleMovie;
    private String releaseDateMovie;
    private double voteAverageMovie;
    private int isFavoriteOfMovie;

    public Reminder() {
    }

    public Reminder(int id, long time, long movieId, String posterPathMovie, String releaseDateMovie, String titleMovie, double voteAverageMovie, int isFavoriteOfMovie) {
        this.id = id;
        this.time = time;
        this.movieId = movieId;
        this.posterPathMovie = posterPathMovie;
        this.releaseDateMovie = releaseDateMovie;
        this.titleMovie = titleMovie;
        this.voteAverageMovie = voteAverageMovie;
        this.isFavoriteOfMovie = isFavoriteOfMovie;
    }

    public int getIsFavoriteOfMovie() {
        return isFavoriteOfMovie;
    }

    public void setIsFavoriteOfMovie(int isFavoriteOfMovie) {
        this.isFavoriteOfMovie = isFavoriteOfMovie;
    }

    public long getMovieId() {
        return movieId;
    }

    public void setMovieId(long movieId) {
        this.movieId = movieId;
    }

    public int getId() {
        return id;
    }

    public long getTime() {
        return time;
    }


    public void setId(int id) {
        this.id = id;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getTitleMovie() {
        return titleMovie;
    }

    public void setTitleMovie(String titleMovie) {
        this.titleMovie = titleMovie;
    }

    public String getReleaseDateMovie() {
        return releaseDateMovie;
    }

    public void setReleaseDateMovie(String releaseDateMovie) {
        this.releaseDateMovie = releaseDateMovie;
    }

    public String getPosterPathMovie() {
        return posterPathMovie;
    }

    public void setPosterPathMovie(String posterPathMovie) {
        this.posterPathMovie = posterPathMovie;
    }

    public double getVoteAverageMovie() {
        return voteAverageMovie;
    }

    public void setVoteAverageMovie(double voteAverageMovie) {
        this.voteAverageMovie = voteAverageMovie;
    }
}
