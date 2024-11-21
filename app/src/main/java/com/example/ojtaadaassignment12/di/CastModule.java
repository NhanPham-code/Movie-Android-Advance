package com.example.ojtaadaassignment12.di;

import com.example.ojtaadaassignment12.data.remotedatasource.CastPagingSource;
import com.example.ojtaadaassignment12.data.repository.CastRepositoryImpl;
import com.example.ojtaadaassignment12.domain.repository.ICastRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class CastModule {

    @Provides
    @Singleton
    public ICastRepository castRepository(CastPagingSource castPagingSource) {
        return new CastRepositoryImpl(castPagingSource);
    }
}
