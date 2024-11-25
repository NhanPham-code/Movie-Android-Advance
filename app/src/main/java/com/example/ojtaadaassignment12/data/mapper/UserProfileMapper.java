package com.example.ojtaadaassignment12.data.mapper;

import com.example.ojtaadaassignment12.data.entities.UserProfileEntity;
import com.example.ojtaadaassignment12.domain.models.UserProfile;

public class UserProfileMapper {
    public static UserProfileEntity toEntity(UserProfile userProfile) {
        return new UserProfileEntity(
                userProfile.getFullName(),
                userProfile.getEmail(),
                userProfile.getBirthday(),
                userProfile.getGender(),
                userProfile.getAvatarBase64()
        );
    }

    public static UserProfile toDomain(UserProfileEntity userProfileEntity) {
        return new UserProfile(
                userProfileEntity.getFullName(),
                userProfileEntity.getEmail(),
                userProfileEntity.getBirthday(),
                userProfileEntity.getGender(),
                userProfileEntity.getAvatarBase64()
        );
    }
}
