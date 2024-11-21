package com.example.ojtaadaassignment12.di;

import android.app.Application;

public class MyApplication extends Application {

    public AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
       appComponent = DaggerAppComponent.factory().create(this);
    }
}
