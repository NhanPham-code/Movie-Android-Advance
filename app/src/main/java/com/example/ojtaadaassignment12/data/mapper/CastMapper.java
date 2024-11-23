package com.example.ojtaadaassignment12.data.mapper;

import com.example.ojtaadaassignment12.data.entities.CastEntity;
import com.example.ojtaadaassignment12.domain.models.Cast;

public class CastMapper {

    // Convert CastEntity to Cast
    public static Cast toDomain(CastEntity castEntity) {
        return new Cast(
                castEntity.getId(),
                castEntity.getName(),
                castEntity.getProfilePath()
        );
    }

    // Convert Cast to CastEntity
    public static CastEntity toEntity(Cast cast) {
        return new CastEntity(
                cast.getId(),
                cast.getName(),
                cast.getProfilePath()
        );
    }
}
