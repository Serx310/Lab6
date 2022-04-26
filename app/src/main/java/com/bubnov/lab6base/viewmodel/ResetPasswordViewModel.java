package com.bubnov.lab6base.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.bubnov.lab6base.AuthRepository;
import com.google.firebase.auth.FirebaseUser;

public class ResetPasswordViewModel extends AndroidViewModel {
    private final AuthRepository authRepository;
    private final MutableLiveData<FirebaseUser> userMutableLiveData;
    public ResetPasswordViewModel(@NonNull Application application){
        super(application);
        authRepository = new AuthRepository(application);
        userMutableLiveData = authRepository.getUserMutableLiveData();
    }
    public void resetPassword(String email) {authRepository.sendPasswordResetEmail(email);}
    public MutableLiveData<FirebaseUser> getUserMutableLiveData() {return userMutableLiveData;}
}
