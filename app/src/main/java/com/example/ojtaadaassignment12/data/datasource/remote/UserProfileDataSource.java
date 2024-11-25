package com.example.ojtaadaassignment12.data.datasource.remote;

import android.content.Context;
import android.provider.Settings;

import com.example.ojtaadaassignment12.data.entities.UserProfileEntity;
import com.google.firebase.database.DatabaseReference;

import javax.inject.Inject;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserProfileDataSource {

    private final DatabaseReference mDatabaseReference;
    Context context;

    @Inject
    public UserProfileDataSource(Context context) {
        this.context = context;
        FirebaseDatabase mFirebaseDatabase = FirebaseDatabase
                .getInstance("https://mock-project-be2a3-default-rtdb.asia-southeast1.firebasedatabase.app");
        mDatabaseReference = mFirebaseDatabase.getReference("user_profiles");
    }


    public void getUserProfile(ValueEventListener listener) {
        String deviceId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        mDatabaseReference.child(deviceId).addListenerForSingleValueEvent(listener);
    }

    public void setUserProfile(UserProfileEntity userProfile) {
        String deviceId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        mDatabaseReference.child(deviceId).setValue(userProfile);
    }
}
