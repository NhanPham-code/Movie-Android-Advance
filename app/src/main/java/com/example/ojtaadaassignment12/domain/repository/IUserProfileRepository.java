package com.example.ojtaadaassignment12.domain.repository;

import android.app.Application;

import androidx.lifecycle.MutableLiveData;

import com.example.ojtaadaassignment12.domain.models.UserProfile;

public interface IUserProfileRepository {
    MutableLiveData<UserProfile> getUserProfile();
    void setUserProfile(UserProfile userProfile);
}
