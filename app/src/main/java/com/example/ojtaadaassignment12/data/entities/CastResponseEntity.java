package com.example.ojtaadaassignment12.data.entities;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CastResponseEntity {
    @SerializedName("id")
    private int id;
    @SerializedName("cast")
    private List<CastEntity> castList;

    public CastResponseEntity() {
    }

    public CastResponseEntity(int id, List<CastEntity> castList) {
        this.id = id;
        this.castList = castList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<CastEntity> getCastList() {
        return castList;
    }

    public void setCastList(List<CastEntity> castList) {
        this.castList = castList;
    }
}
