package com.example.ojtaadaassignment12.presentation.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.ojtaadaassignment12.domain.models.UserProfile;
import com.example.ojtaadaassignment12.domain.usecase.UserProfileUseCase;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class UserProfileViewModel extends ViewModel {
    private MutableLiveData<UserProfile> userProfileMutableLiveData = new MutableLiveData<>();

    UserProfileUseCase userProfileUseCase;

    @Inject
    public UserProfileViewModel(UserProfileUseCase userProfileUseCase) {
        this.userProfileUseCase = userProfileUseCase;
    }

    public void getUserProfileFromFirebase() {
        userProfileMutableLiveData = userProfileUseCase.getUserProfile();
    }

    public void setUserProfileToFirebase(UserProfile userProfile) {
        userProfileUseCase.setUserProfile(userProfile);
    }

    public MutableLiveData<UserProfile> getUserProfileMutableLiveData() {
        return userProfileMutableLiveData;
    }

    public void setUserProfile(UserProfile userProfile) {
        userProfileMutableLiveData.setValue(userProfile);
    }
}
