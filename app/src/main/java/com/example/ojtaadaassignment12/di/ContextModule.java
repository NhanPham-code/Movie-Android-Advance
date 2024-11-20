package com.example.ojtaadaassignment12.di;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ContextModule {

    @Provides
    @Singleton
    public Context provideContext(Application application) {
        return application.getApplicationContext();
    }
}
