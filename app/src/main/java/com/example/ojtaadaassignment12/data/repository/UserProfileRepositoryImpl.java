package com.example.ojtaadaassignment12.data.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.ojtaadaassignment12.data.datasource.remote.UserProfileDataSource;
import com.example.ojtaadaassignment12.data.entities.UserProfileEntity;
import com.example.ojtaadaassignment12.data.mapper.UserProfileMapper;
import com.example.ojtaadaassignment12.domain.models.UserProfile;
import com.example.ojtaadaassignment12.domain.repository.IUserProfileRepository;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import javax.inject.Inject;

public class UserProfileRepositoryImpl implements IUserProfileRepository {

    UserProfileDataSource userProfileDataSource;
    MutableLiveData<UserProfile> userProfileLiveData = new MutableLiveData<>();

    @Inject
    public UserProfileRepositoryImpl(UserProfileDataSource userProfileDataSource) {
        this.userProfileDataSource = userProfileDataSource;
    }

    @Override
    public MutableLiveData<UserProfile> getUserProfile() {
        // get user profile from firebase
        // map user profile entity to model
        userProfileDataSource.getUserProfile(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserProfileEntity userProfileEntity = snapshot.getValue(UserProfileEntity.class);
                if (userProfileEntity != null) {
                    UserProfile userProfile = UserProfileMapper.toDomain(userProfileEntity);
                    userProfileLiveData.postValue(userProfile); // Update LiveData from background thread safety
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return userProfileLiveData;
    }

    @Override
    public void setUserProfile(UserProfile userProfile) {
        // map user profile to entity
        UserProfileEntity userProfileEntity = UserProfileMapper.toEntity(userProfile);
        // save user profile to firebase
        userProfileDataSource.setUserProfile(userProfileEntity);
    }
}
