package com.example.ojtaadaassignment12.di;

import com.example.ojtaadaassignment12.data.datasource.remote.UserProfileDataSource;
import com.example.ojtaadaassignment12.data.repository.UserProfileRepositoryImpl;
import com.example.ojtaadaassignment12.domain.repository.IUserProfileRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class UserProfileModule {

    @Provides
    @Singleton
    public IUserProfileRepository provideUserProfileRepository(UserProfileDataSource userProfileDataSource) {
        return new UserProfileRepositoryImpl(userProfileDataSource);
    }
}
