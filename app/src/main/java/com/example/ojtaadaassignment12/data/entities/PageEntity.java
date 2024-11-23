package com.example.ojtaadaassignment12.data.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class PageEntity implements Parcelable {
    @SerializedName("page")
    private int page;
    @SerializedName("results")
    List<MovieEntity> results;
    @SerializedName("total_pages")
    private int totalPages;
    @SerializedName("total_results")
    private int totalResults;

    public PageEntity() {
    }

    public PageEntity(int page, int totalResults, int totalPages, List<MovieEntity> results) {
        this.page = page;
        this.totalResults = totalResults;
        this.totalPages = totalPages;
        this.results = results;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<MovieEntity> getResults() {
        return results;
    }

    public void setResults(List<MovieEntity> results) {
        this.results = results;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    // Parcelable implementation
    protected PageEntity(Parcel in) {
        page = in.readInt();
        totalResults = in.readInt();
        totalPages = in.readInt();
        results = new ArrayList<>();
        in.readList(results, MovieEntity.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(page);
        dest.writeInt(totalResults);
        dest.writeInt(totalPages);
        dest.writeList(results);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PageEntity> CREATOR = new Creator<PageEntity>() {
        @Override
        public PageEntity createFromParcel(Parcel in) {
            return new PageEntity(in);
        }

        @Override
        public PageEntity[] newArray(int size) {
            return new PageEntity[size];
        }
    };
}
