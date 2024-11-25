package com.example.ojtaadaassignment12.domain.usecase;

import androidx.lifecycle.MutableLiveData;

import com.example.ojtaadaassignment12.domain.models.UserProfile;
import com.example.ojtaadaassignment12.domain.repository.IUserProfileRepository;

import javax.inject.Inject;

public class UserProfileUseCase {

    IUserProfileRepository userProfileRepository;

    @Inject
    public UserProfileUseCase(IUserProfileRepository userProfileRepository) {
        this.userProfileRepository = userProfileRepository;
    }

    public MutableLiveData<UserProfile> getUserProfile() {
        return userProfileRepository.getUserProfile();
    }

    public void setUserProfile(UserProfile userProfile) {
        userProfileRepository.setUserProfile(userProfile);
    }
}
