package com.bubnov.lab6base;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseUser;

public class LoginRegisterModel extends AndroidViewModel {

    private final AuthRepository authRepository;
    private final MutableLiveData<FirebaseUser> userMutableLiveData;

    public LoginRegisterModel(@NonNull Application application){
        super(application);
        authRepository = new AuthRepository(application);
        userMutableLiveData = authRepository.getUserMutableLiveData();
    }

    public void userRegistration(String firstname, String surname, String email, String password){
        authRepository.userRegistration(firstname, surname, email, password);
    }

    public void login(String email, String password){
        authRepository.logIn(email, password);
    }

    public MutableLiveData<FirebaseUser> getUserMutableLiveData(){
        return userMutableLiveData;
    }

}
